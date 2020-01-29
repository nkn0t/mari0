package jp.nkn0t.mari0.model.map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import jp.nkn0t.mari0.logic.Enemy;
import jp.nkn0t.mari0.model.map.obj.Apple;
import jp.nkn0t.mari0.model.map.obj.BackgroundObject;
import jp.nkn0t.mari0.model.map.obj.BrickBlock;
import jp.nkn0t.mari0.model.map.obj.Cloud;
import jp.nkn0t.mari0.model.map.obj.Dokan;
import jp.nkn0t.mari0.model.map.obj.HatenaBlock;
import jp.nkn0t.mari0.model.map.obj.MapObject;
import jp.nkn0t.mari0.model.map.obj.StoneBlock;
import jp.nkn0t.mari0.model.map.obj.Water;

public class GameMap {
	public static final int FS = 640;
	public int row;
	public int col;
	public int width;
	public int height;
	private Color bgColor = Color.lightGray.brighter();
	private BufferedImage mapchip;
	private BufferedImage[][] mapchips;
	private BufferedImage mapImage;
	private int mpc_r, mpc_c;
	public static final int TS = 32;
	private int[][] map;
	private MapObject[][] mapObjects;
	private boolean isStageChange = false;
	private GameMap nextStage;

	protected ArrayList<Enemy> enmList = new ArrayList<Enemy>();
	private Enemy enemyAttackedByPlayer;

	public void draw(Graphics g, int offsetX, int offsetY) {
		g.drawImage(mapImage.getSubimage(-offsetX, -offsetY, FS, FS),0,0,null);
		for (Enemy enm: enmList) {
			if (enm.getAppear()) {
				g.drawImage(enm.getImage(), enm.x + offsetX, enm.y + offsetY, null);
			}
		}
	}

	public Point getTileCollision(double x, double y, int direction) {
		if (mapObjects[(int)y/TS][(int)x/TS] instanceof Cloud) {
			if (((Cloud)mapObjects[(int)y/TS][(int)x/TS]).isPassable(direction)){
				return null;
			} else {
				return new Point((int)x/TS, (int)y/TS);
			}
		}
		if (mapObjects[(int)y/TS][(int)x/TS].isPassable()) {
			return null;
		} else {
			switch(direction) {
			case MapObject.TOP:
				mapObjects[(int)y/TS][(int)x/TS].collisonFromTop();
				break;

			case MapObject.SIDE:
				mapObjects[(int)y/TS][(int)x/TS].collisonFromSide();
				break;

			case MapObject.BOTTOM:
				mapObjects[(int)y/TS][(int)x/TS].collisonFromBottom();
				break;
			}

			return new Point((int)x/TS, (int)y/TS);
		}
	}
	public Point getTileCollision(double x, double y) {
		if (mapObjects[(int)y/TS][(int)x/TS].isPassable()) {
			return null;
		} else {
			return new Point((int)x/TS, (int)y/TS);
		}
	}

	public int tilesToPixels(int num) {
		return num * TS;
	}

	public int pixelsToTiles(int num) {
		return num / TS;
	}

	public void loadMap(File file) {
		try {
			FileInputStream is = new FileInputStream(file);
			row = is.read();
			col = is.read();
			width = col*TS;
			height = row*TS;
			map = new int[row][col];
			mapObjects = new MapObject[row][col];
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < col; j++) {
					map[i][j] = is.read();
				}
			}
			is.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			mapchip = ImageIO.read(new File("Game_Image/maptip.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		mpc_r = mapchip.getHeight()/TS;
		mpc_c = mapchip.getWidth()/TS;
		mapchips = new BufferedImage[mpc_r][mpc_c];
		for (int i = 0; i < mpc_r; i++) {
			for (int j = 0; j < mpc_c; j++) {
				mapchips[i][j] = mapchip.getSubimage(TS * j, TS * i, TS, TS);
			}
		}

		mapImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D mg = (Graphics2D) mapImage.getGraphics();
		mg.setColor(bgColor);
		mg.fillRect(0, 0, width, height);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				int y = map[i][j]/mpc_r;
				int x = map[i][j]%mpc_c;
				mg.drawImage(mapchips[y][x], TS*j, TS*i, TS, TS, null);
			}
		}
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				setMapObjects(i, j);
			}
		}
	}

	public boolean isExistEnemy(int x, int y) {
		for (Enemy enm: enmList) {
			if (enm.getY() > y + TS*2 || enm.getY() + TS + 5< y) continue;
			if (enm.getX() > x - TS && enm.getX() < x + TS+ 12) {
				enemyAttackedByPlayer = enm;
				return true;
			}
		}
		return false;
	}

	public boolean isEnemyDie(int x, int y) {
		if (enemyAttackedByPlayer.getY() > y + 64) {
			enemyAttackedByPlayer.die(enmList);
		}
		return enemyAttackedByPlayer.getDie();
	}


	public void removeAllEnemy() {
		for (Enemy enm: enmList) {
			enm.dispose(enmList);
		}
	}

	private void setMapObjects(int i, int j) {
		switch(map[i][j]) {
		case 1 :
		case 2 :
		case 3 :
			mapObjects[i][j] = new BrickBlock(getBgColor(),
					mapImage.getSubimage(j*32, i*32, 32, 32), i, j);
			break;

		case 22:
		case 23:
		case 24:
			mapObjects[i][j] = new Cloud();
			break;

		case   9:
		case  15:
		case  25:
		case  31:
		case  41:
		case  47:
		case  57:
		case  63:
		case  73:
		case  79:
		case  89:
		case  95:
		case 105:
		case 111:
			mapObjects[i][j] = new Dokan(MapObject.SIDE, this);
			break;

		case 144:
		case 145:
		case 146:
		case 147:
		case 148:
		case 149:
		case 150:
		case 151:
			mapObjects[i][j] = new Dokan(MapObject.TOP, this);
			break;

		case 240:
		case 241:
		case 242:
		case 243:
		case 244:
		case 245:
		case 246:
		case 247:
			mapObjects[i][j] = new Dokan(MapObject.BOTTOM, this);
			break;

		case 17:
		case 18:
		case 19:
		case 33:
		case 34:
		case 35:
		case 49:
		case 50:
		case 51:
			mapObjects[i][j] = new StoneBlock();
			break;

		case 38:
		case 39:
		case 40:
			mapObjects[i][j] = new Water();
			break;

		case 16:
		case 52:
			mapObjects[i][j] = new HatenaBlock(mapImage.getSubimage(j*32, i*32, 32, 32), i, j);
			break;

		case   0:
		case   6:
		case   7:
		case   8:
		case  64:
		case  65:
		case  66:
		case  67:
		case  68:
		case  69:
		case  70:
		case  71:
		case  80:
		case  81:
		case  82:
		case  83:
		case  84:
		case  85:
		case  86:
		case  87:
		case  96:
		case  97:
		case  98:
		case  99:
		case 100:
		case 101:
		case 102:
		case 103:
		case 112:
		case 113:
		case 114:
		case 115:
		case 116:
		case 117:
		case 118:
		case 119:
		case 128:
		case 129:
		case 130:
		case 131:
		case 132:
		case 133:
		case 134:
		case 135:
		case 136:
		case 216:
		case 217:
		case 218:
		case 221:
		case 232:
		case 233:
		case 234:
		case 248:
		case 249:
		case 250:
			mapObjects[i][j] = new BackgroundObject();
			break;

		case 222:
			mapObjects[i][j] = new Apple(mapImage.getSubimage(j*32, i*32, 32, 32),bgColor,  i, j);
			break;

		default:
			mapObjects[i][j] = new StoneBlock();
			break;
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Color getBgColor() {
		return bgColor;
	}

	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}

	public void setEnemy() {
		enmList = new ArrayList<Enemy>();
	}

	public void setStageChange(boolean b) {
		isStageChange = b;
	}

	public boolean isStageChange() {
		return isStageChange;
	}

	public GameMap getNextStage() {
		return nextStage;
	}

	public void setNextStage(GameMap stage) {
		nextStage = stage;
	}

	public GameMap getNewStage() {
		return new GameMap();
	}
}
