package jp.nkn0t.mari0.model.map.obj;

public class StoneBlock implements MapObject {

	@Override
	public void collisonFromTop() {}

	@Override
	public void collisonFromSide() {}

	@Override
	public void collisonFromBottom() {}

	@Override
	public boolean isPassable() {
		return false;
	}

}
