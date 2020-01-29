package jp.nkn0t.mari0.model.map.obj;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import jp.nkn0t.mari0.model.map.Ringo;
import jp.nkn0t.mari0.util.BGManager;

public class Apple implements MapObject {
	private boolean isPassable = false;
	private BufferedImage image;
	int i, j;
	private Color bgColor;

	public Apple(BufferedImage image, Color c, int i, int j) {
		this.image = image;
		this.i = i;
		this.j = j;
		this.bgColor = c;
	}

	@Override
	public void collisonFromTop() {
		get();
	}

	@Override
	public void collisonFromSide() {
		get();
	}

	@Override
	public void collisonFromBottom() {
		get();
	}

	public void get() {
		Ringo.num++;
		isPassable = true;
		Graphics2D g = (Graphics2D)image.getGraphics();
		g.setColor(bgColor);
		g.fillRect(0, 0, 32, 32);
		new BGManager("BGM/gameon.wav").play();
	}

	@Override
	public boolean isPassable() {
		return isPassable;
	}

}
