package jp.nkn0t.mari0.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import jp.nkn0t.mari0.logic.Operator;
import jp.nkn0t.mari0.logic.Player;
import jp.nkn0t.mari0.model.map.GameMap;
import jp.nkn0t.mari0.model.map.Ringo;
import jp.nkn0t.mari0.model.map.obj.MapObject;
import jp.nkn0t.mari0.util.BGManager;

@SuppressWarnings("serial")
public class MainPanel extends JPanel implements Runnable {
	private Graphics dbg;
	private Image dbImage = null;
	public static final int TILE_SIZE = 32;
	public static final int WIDTH = 640, HEIGHT = 640;
	private Player player = new Player(0, 0);
	private GameMap nowStage;
	private int offset = 0;
	private int life;
	private boolean shibouFlag;
	private JFrame parent;

	public MainPanel(JFrame parent) {
		init();
		this.parent = parent;
	}

	private void init() {
		nowStage = new Stage1();
		shibouFlag = false;
		life =  1;
		player.x = 32;
		player.y = 500;
		(new Thread(this)).start();
	}

	public void run() {
		//BGManager bgmanager = new BGManager("BGM/montague.wav");
	//	bgmanager.play();
		long time = System.currentTimeMillis();
		int cnt = 0;
		while (!shibouFlag) {
			if (Ringo.num == 5) {
				life++;
				Ringo.num = 0;
			}
			gameUpdate();
			gameRender();
			paintScreen();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			cnt++;
			if (System.currentTimeMillis() - time >= 1000) {
				System.out.println(cnt + " fps");
				cnt = 0;
				time = System.currentTimeMillis();
			}
		}

		//bgmanager.stop();
	}

	private void gameUpdate() {
		player.vy += Stage1.GRAVITY;;
		offset = (player.isCrouching()) ? TILE_SIZE : 0;

		double newX = player.x + player.vx + player.width;
        double newY = player.y + player.vy + player.height + offset;
        if (newY < 0 || newY > MainPanel.HEIGHT) {
        	restart();
        	return;
        }
		Point tile1, tile2;

		if (player.vx > 0) {
			tile1 = nowStage.getTileCollision(newX, player.y + TILE_SIZE, MapObject.SIDE);
			tile2 = nowStage.getTileCollision(newX, player.y, MapObject.SIDE);
			if (tile1 == null && tile2 == null) {
				player.x = newX - player.width;
			} else {
				if (tile1 != null) {
					player.x = nowStage.tilesToPixels(tile1.x - 1);
					player.vx = 0;
				} else {
					player.x = nowStage.tilesToPixels(tile2.x - 1);
					player.vx = 0;
				}
			}
		} else if (player.vx < 0) {
			tile1 = nowStage.getTileCollision(newX - player.width - 1,
												player.y + TILE_SIZE, MapObject.SIDE);
			tile2 = nowStage.getTileCollision(newX - player.width - 1, player.y, MapObject.SIDE);
			if (tile1 == null && tile2 == null) {
				player.x = newX - player.width;
			} else {
				if (tile1 != null) {
					player.x = nowStage.tilesToPixels(tile1.x + 1);
					player.vx = 0;
				} else {
					player.x = nowStage.tilesToPixels(tile2.x + 1);
					player.vx = 0;
				}
			}
		}

        if (player.vy >= 0) {
        	tile1 = nowStage.getTileCollision(player.x, newY, MapObject.TOP);
        	tile2 = nowStage.getTileCollision(player.x + TILE_SIZE - 1, newY, MapObject.TOP);
        	if (tile1 == null && tile2 == null) {
                player.y = newY - player.height - offset;
                player.setOnGround(false);
        	} else {
        		if (nowStage.isStageChange() && player.isCrouching()) {
        			next(MapObject.TOP);
        		} else {
        			nowStage.setStageChange(false);
        		}
        		if (tile1 == null) tile1 = tile2;
        		player.y = nowStage.tilesToPixels(tile1.y) - player.height - offset;
                player.vy = 0;
                if (!player.isOnGround()) player.takeOn();
                player.setOnGround(true);
        	}
        } else if (player.vy < 0) {
        	tile1 = nowStage.getTileCollision(player.x, newY - player.height - offset, MapObject.BOTTOM);
        	tile2 = nowStage.getTileCollision(player.x + TILE_SIZE - 1, newY - player.height - offset, MapObject.BOTTOM);
        	if (tile1 == null && tile2 == null) {
                player.y = newY - player.height - offset;
                player.setOnGround(false);
        	} else {
        		if (tile1 == null) tile1 = tile2;
        		player.y = nowStage.tilesToPixels(tile1.y + 1) - offset;
                player.vy = 0;
                player.setOnGround(false);
        	}
        }

		if (nowStage.isExistEnemy((int)player.x, (int)player.y)) {
			if (nowStage.isEnemyDie((int)player.x , (int)(player.y - player.vy))) {
				player.vy = -15;
				player.vx *= 0.5;
			} else {
				restart();
			}
		}
	}

	private void gameRender() {
		if (dbImage == null) {
			dbImage = createImage(WIDTH, HEIGHT);
			if (dbImage == null) {
				return;
			} else {
				dbg = dbImage.getGraphics();
				dbg.setColor(nowStage.getBgColor());
				dbg.fillRect(0, 0, WIDTH, HEIGHT);

			}
		}

        int offsetX = MainPanel.WIDTH / 2 - (int)player.x;
        offsetX = Math.min(offsetX, 0);
        offsetX = Math.max(offsetX, MainPanel.WIDTH - nowStage.getWidth());

        int offsetY = MainPanel.HEIGHT / 2- (int)player.y;
        offsetY = Math.min(offsetY, 0);
        offsetY = Math.max(offsetY, MainPanel.HEIGHT - nowStage.getHeight());

        if (-offsetY < MainPanel.HEIGHT/2) offsetY = 0;

        nowStage.draw(dbg, offsetX, offsetY);
        player.draw(dbg, offsetX, offsetY);
	}

	private void paintScreen() {
		Graphics g = getGraphics();

		if ((g != null) && (dbImage != null)) {
			g.drawImage(dbImage, 0, 0, null);
			g.setColor(Color.lightGray);
			g.setFont(new Font("Monospaced", Font.BOLD, 24));
			g.drawString("RINGO:" + Ringo.num, WIDTH-200, 30);
			g.drawString("LIFE:" + life, WIDTH-100, 30);
		}

		Toolkit.getDefaultToolkit().sync();

		if (g != null) {
			g.dispose();
		}
	}

	private int die() {
		new BGManager("BGM/yeargh.wav").play();
		while (player.y > 0) {
			player.y -= 10;
			player.setOnGround(false);
			player.dieing();
			gameRender();
			paintScreen();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		player.vy = 0;
		player.y = 500;
		return life--;
	}

	private void gameover() {
		nowStage.removeAllEnemy();
		new BGManager("BGM/youlose.wav").play();
		shibouFlag = true;
		getGraphics().dispose();
		setLayout(new BorderLayout());
		add(new GameOverPanel());
		revalidate();
		repaint();
	}


	private void restart() {
		if (die() == 0) {
			gameover();
		} else {
			nowStage.removeAllEnemy();
			nowStage = nowStage.getNewStage();
			player.x = 33;
			player.y = 500;
			player.vx = 0;
			player.takeOn();
			shibouFlag = false;
		}
	}

	private void next(int direction) {
		nowStage.removeAllEnemy();
		nowStage = nowStage.getNextStage();
		player.x = 33;
		player.y = 33;
		player.vx = 0;
		player.takeOn();
		shibouFlag = false;
	}

	public Operator getOperator() {
		return player;
	}

	class GameOverPanel extends JPanel {

		public GameOverPanel() {
			initialize();
		}

		private void initialize() {
			parent.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						init();
						parent.removeKeyListener(this);
					}
				}
			});
		}

		public void paintComponent(Graphics g) {

			Graphics2D G2D = (Graphics2D) g;
			try {
				BufferedImage img = ImageIO.read(new File("./Game_Image/gameover.gif"));
				G2D.drawImage(img, 0, 0, 640, 640, null);
				G2D.setColor(Color.white);
				g.setFont(new Font("Monospaced", Font.BOLD, 24));
				G2D.drawString("Enter�Ń��X�^�[�g", 320 - 100, 320 + 160);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
