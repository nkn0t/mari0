package jp.nkn0t.mari0.model.map.obj;

import java.awt.Image;

public interface MapObject {
	int TOP = 0;
	int SIDE = 1;
	int BOTTOM = 2;
	void collisonFromTop();
	void collisonFromSide();
	void collisonFromBottom();
	boolean isPassable();

}
