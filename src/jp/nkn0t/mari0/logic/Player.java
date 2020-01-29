package jp.nkn0t.mari0.logic;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import jp.nkn0t.mari0.util.BGManager;

public class Player implements Operator {
	private BufferedImage[][] image = new BufferedImage[2][7];
	private BufferedImage nowImage;
	public int width = 32, height = 64;
	public double x;
	public double y;
	public double vx = 0.0;
	public double vy = 0.0;
	long jump_time = 0;
	int boost = 1;
	int speed = 3;

	private static final int JUMP_SPEED = 8;
	private int direction = RIGHT;
	private boolean isCrouching = false;
	private boolean isBPressed = false;
	private boolean isOnGround = true;
	private boolean isTurning = false;

	public Player(int x, int y) {
		this.x = x;
		this.y = y;
		initImage();
	}

	private void initImage() {
		try {
			BufferedImage img = ImageIO.read(new File("Game_Image/mario.png"));
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 4; j++) {
					image[i][j] = img.getSubimage(j * width, i * height, width, height);
				}
			}
			image[RIGHT][TURNING] = img.getSubimage(160, 0, width, height);
			image[LEFT][TURNING] = img.getSubimage(160, 64, width, height);
			image[RIGHT][CROUCHING] = img.getSubimage(0, 128, width, height);
			image[LEFT][CROUCHING] = img.getSubimage(32, 128, width, height);
			image[RIGHT][JUMPING] = img.getSubimage(128, 0, width, height);
			image[LEFT][JUMPING] = img.getSubimage(128, 64, width, height);

			nowImage = image[0][0];
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void draw(Graphics g, int offsetX, int offsetY) {
		g.drawImage(nowImage, (int)x + offsetX, (int)y + offsetY, null);
	}


	@Override
	public void pressedLeft() {
		if (isOnGround()) {
			//�n�ʂ̏�ɂ��鎞
			if (isCrouching()) {
				//���Ⴊ��ł�����������ς���
				direction = LEFT;
				return;
			}

			if (direction == RIGHT && vx > 0) {
				nowImage = image[direction][TURNING];
				direction = LEFT;
				//�����]��
				//����������]�����܂���Flag=true
				setTurning(true);
				while (isTurning()) turning();

			} else {
				//���֑���
				direction = LEFT;
				vx = -speed * boost;
				movingAnimation();
			}
		} else {
			//�󒆂Ȃ�W�����v��ԂƂ��Ĉ����A�������������ֈړ�
			jump_time = System.currentTimeMillis() - jump_time;
			vx -= (double)speed / 10;
		}
	}


	@Override
	public void pressedRight() {
		/**
		 * pressedLeft()�̉E�o�[�W����
		 * ���삪�t�����ɂȂ�������
		 */

		if(isOnGround()) {
			if (isCrouching()) {
				direction = RIGHT;
				return;
			}

			if (direction == LEFT && vx < 0) {
				nowImage = image[direction][TURNING];
				direction = RIGHT;
				setTurning(true);
				while (isTurning()) turning();
			} else {
				direction = RIGHT;
				vx = speed * boost;
				movingAnimation();
			}
		} else {
			jump_time = System.currentTimeMillis() - jump_time;
			vx += (double)speed / 10;
		}
	}

	@Override
	public void pressedDown() {
		//�n��ɂ���Ȃ炵�Ⴊ�ݏ�Ԃɂ��āA���Ⴊ�ݓ���
		if (isOnGround()) {
			height = 32;
			setCrouching(true);
			nowImage = image[direction][CROUCHING];
			crouching();
		}
	}

	@Override
	public void pressedSpace() {
		if (jump_time == 0)	{
			//�󒆂ɂ��鎞�Ԃ�0�Ȃ�W�����v�ł���
			setOnGround(false);
			if (isCrouching()) {
				nowImage = image[direction][CROUCHING];
			} else {
				nowImage = image[direction][JUMPING];
			}
			jump_time = System.currentTimeMillis();
			//�W�����v���n�߂����̎��Ԃ�jump_time��
			vy = -JUMP_SPEED - (boost * speed);
			//Y�����̑��x��ݒ�
			if (isBPressed()) {
				new BGManager("BGM/jump_sp.wav").play();
			} else {
				new BGManager("BGM/jump_sm.wav").play();
			}
		}
		if (System.currentTimeMillis() - jump_time < 500) {
			//�W�����v���Ă���500�~���b�����ł���΃W�����v�͂��グ��
			vy--;
		}
	}

	@Override
	public void releasedLeft() {
		if (isOnGround()) {
			//�n�ʂɂ������~
			vx = 0;
		} else {
			//�󒆂ɂ���Ȃ�W�����v���Ƃ��Ĉ�������
			jump_time = System.currentTimeMillis() - jump_time;
			vx *= (double)speed / 10;
		}
	}

	@Override
	public void releasedRight() {
		//releasedLeft()�̋t�B�Ƃ��ɐ����Ȃ�
		if (isOnGround()) {
			vx = 0;
		} else {
			jump_time = System.currentTimeMillis() - jump_time;
			vx *= (double)speed / 10;
		}
	}

	@Override
	public void releasedDown() {
		//���Ⴊ�ݏ�ԉ���
		height = 64;
		vx = 0;
		setCrouching(false);
	}

	@Override
	public void releasedSpace() {
		//�X�y�[�X�������ꂽ���̓���@���܂͉����Ȃ�
		//������Ȃ��C������
	}

	@Override
	public void pressedB() {
		//������B�_�b�V���̂��߂̋@�\�@�t�@�C�A�{�[�����ǉ��\��
		boost = 2;
		setBPressed(true);
	}

	@Override
	public void releasedB() {
		boost = 1;
		setBPressed(false);
	}

	@Override
	public void releaseAll() {
		//B�ȊO�̃L�[��������Ă��Ȃ���ԂŁA�n�ʂɂ��鎞�A�摜���f�t�H��
		if (isOnGround()) nowImage = image[direction][0];
	}

	private void movingAnimation() {
		//�����Ă�悤�ɂ݂�����
		if (nowImage == image[direction][0]) {
			nowImage = image[direction][1];
		} else if (nowImage == image[direction][1]) {
			nowImage = image[direction][2];
		} else if (nowImage == image[direction][2]) {
			nowImage = image[direction][3];
		} else {
			nowImage = image[direction][0];
		}
		if (!isBPressed()) sleep(10);
	}

	public void takeOn() {
		/**
		 * �n�ʂɂ������ɂ��
		 * player�N���X���ł́A�����n��������������Ȃ��̂�
		 * player�𗘗p�N���X�Ő������f����
		 *
		 */
		jump_time = 0;
		vx = 0;
		if (!isCrouching()) nowImage = image[direction][0];
	}

	private void crouching() {
		//�u���[�L
		vx *= 0.8;

		//�摜�\���p
		sleep(30);

		if (Math.abs(vx) < 1) {
			vx = 0;
		}
	}

	private void turning() {
		//�u���[�L
		vx *= 0.5;

		//�^�[�j���O���@�摜�\���p
		sleep(30);

		if (Math.abs(vx) < 1) {
			//���x��1�����ɂȂ�����^�[�j���O�I��
			setTurning(false);
			vx = 0;
		}
	}

	private void sleep(long time) {
		//time(�~���b)�����X���[�v
		try {
			Thread.sleep(time);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void dieing() {
		try {
			nowImage = ImageIO.read(new File("Game_Image/die.png"));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	//�ȉ��������������݂����Ȃ��
	public boolean isCrouching() {
		return isCrouching;
	}

	public void setCrouching(boolean isCrouching) {
		this.isCrouching = isCrouching;
	}

	public boolean isBPressed() {
		return isBPressed;
	}

	public void setBPressed(boolean isBPressed) {
		this.isBPressed = isBPressed;
	}

	public boolean isOnGround() {
		return isOnGround;
	}

	public void setOnGround(boolean isOnGround) {
		this.isOnGround = isOnGround;
	}

	public boolean isTurning() {
		return isTurning;
	}

	public void setTurning(boolean isTurning) {
		this.isTurning = isTurning;
	}
}
