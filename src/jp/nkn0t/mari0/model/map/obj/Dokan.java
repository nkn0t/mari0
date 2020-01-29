package jp.nkn0t.mari0.model.map.obj;

import jp.nkn0t.mari0.model.map.GameMap;

public class Dokan implements MapObject {
	private int direction;
	private boolean isPassable = false;
	private boolean isEnterable = false;
	private GameMap stage;
	public Dokan(int direction, GameMap nowStage) {
		this.direction = direction;
		stage = nowStage;
	}
	
	@Override
	public void collisonFromTop() {
		if (direction == TOP) {
			enter();
		}
	}

	@Override
	public void collisonFromSide() {
		if (direction == SIDE) {
			enter();
		}
	}

	@Override
	public void collisonFromBottom() {
		if (direction == BOTTOM) {
			enter();
		}
	}

	@Override
	public boolean isPassable() {
		return isPassable;
	}
	
	public void setEnterable(boolean b) {
		isEnterable = b;
	}
	
	public boolean isEnterable() {
		return isEnterable;
	}
	
	public void enter() {
		System.out.println("ent");
		if (!stage.isStageChange())	stage.setStageChange(true);
	}
}
