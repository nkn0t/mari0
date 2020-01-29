package jp.nkn0t.mari0.model.map.obj;

public class Cloud implements MapObject {
	boolean isPassable = true;
	@Override
	public void collisonFromTop() {
		isPassable = false;
	}

	@Override
	public void collisonFromSide() {
		isPassable = false;
	}

	@Override
	public void collisonFromBottom() {
		isPassable = true;
	}

	@Override
	public boolean isPassable() {
		return isPassable;
	}
	
	public boolean isPassable(int direction) {
		if (direction == TOP)	return false;
		return true;
	}
}
