package jp.nkn0t.mari0.model.map;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import jp.nkn0t.mari0.logic.Enemy;

public class Detteiu extends Enemy {
	BufferedImage[][] image = new BufferedImage[2][4];
	private boolean isRolling;
	public Detteiu(GameMap map, int x, int y, boolean rolling) {
		super(map);	
		setX(x);
		setY(y);
		init();
		isRolling = rolling;
	}
	
	private void init() {
		setWidth(32);
		setHeight(32);

		setLife(1);
		setVx((int)(4*Math.PI));
		setVy(1);
		
		try {
			BufferedImage img = ImageIO.read(new File("./Game_Image/detteiu.png"));
			image[0][0] = img.getSubimage(TS*0, TS*0, TS, TS);
			image[0][1] = img.getSubimage(TS*1, TS*0, TS, TS);
			image[0][2] = img.getSubimage(TS*2, TS*0, TS, TS);
			image[0][3] = img.getSubimage(TS*3, TS*0, TS, TS);
			image[1][0] = img.getSubimage(TS*0, TS*1, TS, TS);
			image[1][1] = img.getSubimage(TS*1, TS*1, TS, TS);
			image[1][2] = img.getSubimage(TS*2, TS*1, TS, TS);
			image[1][3] = img.getSubimage(TS*3, TS*1, TS, TS);
			setDefeatedImage(img.getSubimage(TS*0, TS*2, TS, TS));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		setImage(image[0][0]);
		setDefeatedBGMURL("BGM/quack.wav");
	}

	@Override
	public void movingAnimation() {
		if (isRolling) {
			if (getImage() == image[direction][0]) {
				setImage(image[direction][1]);
			} else if (getImage() == image[direction][1]) {
				setImage(image[direction][2]);
			} else if (getImage() == image[direction][2]) {
				setImage(image[direction][3]);
			} else {
				setImage(image[direction][0]);
			}
		} else {
			setImage(image[direction][0]);
		}
		try {
			Thread.sleep(50);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}
