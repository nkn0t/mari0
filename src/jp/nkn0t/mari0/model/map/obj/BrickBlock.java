package jp.nkn0t.mari0.model.map.obj;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import jp.nkn0t.mari0.util.BGManager;

public class BrickBlock implements MapObject {
	private boolean isPassable = false;
	private BufferedImage image;
	int i, j;
	private Color bgColor;

	public BrickBlock(Color bgColor, BufferedImage image, int i, int j) {
		this.image = image;
		this.bgColor = bgColor;
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
		destroyed();
	}

	public void destroyed() {
		isPassable = true;
		Graphics2D g = (Graphics2D)image.getGraphics();
		g.setColor(bgColor);
		g.fillRect(0, 0, 32, 32);
		new BGManager("BGM/breakblock.wav").play();
	}

	@Override
	public boolean isPassable() {
		return isPassable;
	}

}
