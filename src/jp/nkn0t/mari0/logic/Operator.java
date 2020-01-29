package jp.nkn0t.mari0.logic;

public interface Operator {
	byte RIGHT = 0;
	byte LEFT = 1;
	byte TURNING = 4;
	byte CROUCHING = 5;
	byte JUMPING = 6;
	void pressedLeft();
	void releasedLeft();
	void pressedRight();
	void releasedRight();
	void pressedDown();
	void releasedDown();
	void pressedSpace();
	void releasedSpace();
	void pressedB();
	void releasedB();
	void releaseAll();
}