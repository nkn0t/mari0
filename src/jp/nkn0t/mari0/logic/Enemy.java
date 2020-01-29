package jp.nkn0t.mari0.logic;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import jp.nkn0t.mari0.model.map.GameMap;
import jp.nkn0t.mari0.ui.MainPanel;
import jp.nkn0t.mari0.ui.Stage1;
import jp.nkn0t.mari0.util.BGManager;

public abstract class Enemy implements Runnable {
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int TS = 32;
	int life;
	public int x;
	public int y;
	int vx, vy;
	int width, height;
	protected int direction = LEFT;
	boolean isAppear;
	boolean isDie = false;
	private BufferedImage nowImage;
	private BufferedImage defeatedImage;
	private GameMap map;
	private int fallHeight = 0;
	private ArrayList<Enemy> enmList;
	private String defeatedBGMURL;

	public Enemy(GameMap map) {
		this.map = map;
		init();
	}

	private void init() {
		new Thread(this).start();
	}

	public void run() {
		while(!isAppear);
		while(!getDie()) {
			gameUpdate();
			try {
				Thread.sleep(40);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}

		while(fallHeight < 100) {
			this.vy += 1;
			fallHeight += this.vy;
			this.y += this.vy;
			try {
				Thread.sleep(30);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("die " + this);
		enmList.remove(this);
	}

	public void gameUpdate() {
		direction = (vx >= 0) ? RIGHT : LEFT;
		movingAnimation();
		this.vy += Stage1.GRAVITY;
		double newX = this.x + this.vx + this.width;
		Point tile1, tile2;
		if (this.vx > 0) {
			tile1 = map.getTileCollision(newX, this.y);
			if (tile1 == null) {
				this.x = (int)newX - this.width;
			} else {
				this.x = map.tilesToPixels(tile1.x - 1);
				this.vx = -this.vx;
			}
		} else if (this.vx < 0) {
			tile1 = map.getTileCollision(newX - this.width - 1, this.y);
			if (tile1 == null) {
				this.x = (int)newX - this.width;
			} else {
				this.x = map.tilesToPixels(tile1.x + 1);
				this.vx = -this.vx;
			}
		}

        double newY = this.y + this.vy + this.height;
        if (newY > MainPanel.HEIGHT) {
        	vx = 0;
        	return;
        }
        if (this.vy >= 0) {
        	tile1 = map.getTileCollision(this.x, newY);
        	tile2 = map.getTileCollision(this.x + TS - 1, newY);
        	if (tile1 == null && tile2 == null) {
                this.y = (int)newY - this.height;
        	} else {
        		if (tile1 == null) tile1 = tile2;
        		this.y = map.tilesToPixels(tile1.y) - this.height;
                this.vy = 0;
        	}
        } else if (this.vy < 0) {
        	tile1 = map.getTileCollision(this.x, newY - this.height);
        	tile2 = map.getTileCollision(this.x + TS - 1, newY - this.height);
        	if (tile1 == null && tile2 == null) {
                this.y = (int)newY - this.height;
        	} else {
        		if (tile1 == null) tile1 = tile2;
                this.vy = 0;
        	}
        }
	}

	public void die(ArrayList<Enemy> enmList) {
		this.setVx(0);
		this.setVy(-8);
		new BGManager(defeatedBGMURL).play();
		setDie(true);
		this.setImage(getDefeatedImage());
		this.enmList = enmList;
	}
	public void dispose(ArrayList<Enemy> enmList) {
		setDie(true);
		this.enmList = enmList;
	}


	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getVx() {
		return vx;
	}
	public void setVx(int vx) {
		this.vx = vx;
	}
	public int getVy() {
		return vy;
	}
	public void setVy(int vy) {
		this.vy = vy;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	public BufferedImage getImage() {
		return nowImage;
	}

	public void setImage(BufferedImage img) {
		this.nowImage = img;
	}
	public void draw(Graphics g) {
		g.drawImage(nowImage, x, y, null);
	}
	public boolean getAppear() {
		return isAppear;
	}
	public void setAppear(boolean b) {
		isAppear = b;
	}
	public boolean getDie() {
		return isDie;
	}
	public void setDie(boolean b) {
		isDie = b;
	}
	public BufferedImage getDefeatedImage() {
		return defeatedImage;
	}

	public void setDefeatedImage(BufferedImage defeatedImage) {
		this.defeatedImage = defeatedImage;
	}

	public String getDefeatedBGMURL() {
		return defeatedBGMURL;
	}

	public void setDefeatedBGMURL(String defeatedBGMURL) {
		this.defeatedBGMURL = defeatedBGMURL;
	}

	public abstract void movingAnimation();


}