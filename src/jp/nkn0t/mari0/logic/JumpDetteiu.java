package jp.nkn0t.mari0.logic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import jp.nkn0t.mari0.model.map.GameMap;
import jp.nkn0t.mari0.util.BGManager;

public class JumpDetteiu extends Enemy {
	BufferedImage[][] image = new BufferedImage[3][1];
	int cnt = 0;

	public JumpDetteiu(GameMap map, int x, int y) {
		super(map);
		setX(x);
		setY(y);
		init();
	}

	private void init() {
		setWidth(32);
		setHeight(32);

		setLife(1);
		setVx((int)(4*Math.PI));
		setVy(1);

		try {
			BufferedImage img = ImageIO.read(new File("./Game_Image/jump_detteiu.png"));
			image[0][0] = img.getSubimage(TS*0, TS*0, TS, TS);
			image[1][0] = img.getSubimage(TS*0, TS*1, TS, TS);
			setDefeatedImage(img.getSubimage(TS*0, TS*2, TS, TS));
		} catch(IOException e) {
			e.printStackTrace();
		}

		setImage(image[0][0]);
		setDefeatedBGMURL("BGM/quack.wav");
	}

	boolean b = false;;
	@Override
	public void movingAnimation() {
		if (vy > 0) {
			b = true;
		}

		if (b) {

			if (vy == 0) {
				new BGManager("BGM/bound.wav").play();
				vy = -15;
				if (vx > 0) {
					vx = - 5;
				} else {
					vx = 5;
				}

				b = false;
			}
		}


		if (cnt == 20) vy = -10;

		setImage(image[direction][0]);
		try {
			Thread.sleep(50);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

}
