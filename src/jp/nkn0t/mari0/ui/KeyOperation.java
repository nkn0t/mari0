package jp.nkn0t.mari0.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.Timer;

import jp.nkn0t.mari0.logic.Operator;

public class KeyOperation extends KeyAdapter implements ActionListener {
	private Operator operator;
	private boolean isLeftPressed = false;
	private boolean isRightPressed = false;
	private boolean isSpacePressed = false;
	private boolean isDownPressed = false;
	private boolean isBPressed = false;

	KeyOperation(Operator operator) {
		this.operator = operator;
		init();
	}

	private void init() {
		Timer t = new Timer(30, this);
		t.start();
	}

	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT : {
				isLeftPressed = true;
				isRightPressed = false;
				break;
			}

			case KeyEvent.VK_RIGHT : {
				isRightPressed = true;
				isLeftPressed = false;
				break;
			}

			case KeyEvent.VK_DOWN : {
				isRightPressed = false;
				isLeftPressed = false;
				isDownPressed = true;
				break;
			}

			case KeyEvent.VK_SPACE : {
				isSpacePressed = true;
				break;
			}

			case KeyEvent.VK_B : {
				isBPressed = true;
				break;
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT : {
				isLeftPressed = false;
				operator.releasedLeft();
				break;
			}

			case KeyEvent.VK_RIGHT : {
				isRightPressed = false;
				operator.releasedRight();
				break;
			}

			case KeyEvent.VK_DOWN : {
				isDownPressed = false;
				operator.releasedDown();
				break;
			}

			case KeyEvent.VK_SPACE : {
				isSpacePressed = false;
				operator.releasedSpace();
				break;
			}

			case KeyEvent.VK_B : {
				isBPressed = false;
				operator.releasedB();
				break;
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (isBPressed) operator.pressedB();

		if (!(isLeftPressed | isRightPressed |
				isDownPressed | isSpacePressed)) {

			operator.releaseAll();
			return;

		} else {

			if (isRightPressed) {
				operator.pressedRight();
			} else if (isLeftPressed) {
				operator.pressedLeft();
			}

			if (isDownPressed) operator.pressedDown();
			if (isSpacePressed) operator.pressedSpace();

		}

		try {
			Thread.sleep((isBPressed) ? 40 : 50);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
}
