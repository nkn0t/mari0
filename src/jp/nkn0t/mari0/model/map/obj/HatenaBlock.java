package jp.nkn0t.mari0.model.map.obj;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import jp.nkn0t.mari0.model.map.Ringo;
import jp.nkn0t.mari0.util.BGManager;

public class HatenaBlock implements MapObject {
	private boolean isPassable = false;
	private BufferedImage image;
	int i, j;
	private boolean isGot = false;
	public HatenaBlock(BufferedImage image, int i, int j) {
		this.image = image;
		this.i = i;
		this.j = j;
	}
	@Override
	public void collisonFromTop() {

	}

	@Override
	public void collisonFromSide() {

	}

	@Override
	public void collisonFromBottom() {
		if (!isGot) getItem();
	}

	public void getItem() {
		Ringo.num++;
		Graphics2D g = (Graphics2D)image.getGraphics();
		try {
			g.drawImage(ImageIO.read(new File("Game_Image/hatena_3.png")), 0, 0, 32, 32, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		new BGManager("BGM/gameon.wav").play();
		isGot = true;
	}

	@Override
	public boolean isPassable() {
		return isPassable;
	}
}
