package jp.nkn0t.mari0.ui;

import java.awt.Color;
import java.io.File;

import jp.nkn0t.mari0.model.map.Detteiu;
import jp.nkn0t.mari0.model.map.GameMap;

public class Stage2 extends GameMap {
	public static final double GRAVITY = 1.0;

	Stage2() {
		setBgColor(Color.cyan);
		loadMap(new File("./map/st2.map"));
		setEnemy();
	}

	public void setEnemy() {
		super.setEnemy();
		enmList.add(new Detteiu(this, 992, 500, true));
		enmList.add(new Detteiu(this, 2500, 500, false));
		enmList.add(new Detteiu(this, 2500, 500, false));
		enmList.add(new Detteiu(this, 2500, 500, false));
		enmList.get(0).setAppear(true);
		enmList.get(1).setAppear(true);
		enmList.get(2).setAppear(true);
		enmList.get(3).setAppear(true);
	}

	public GameMap getNewStage() {
		return new Stage2();
	}

	public GameMap getNextStage() {
		return new Stage1();
	}
}