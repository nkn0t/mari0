package jp.nkn0t.mari0.ui;

import java.awt.Color;
import java.io.File;

import jp.nkn0t.mari0.logic.JumpDetteiu;
import jp.nkn0t.mari0.model.map.Detteiu;
import jp.nkn0t.mari0.model.map.GameMap;

public class Stage1 extends GameMap {
	public static final double GRAVITY = 1.0;

	Stage1() {
		setBgColor(Color.white);
		loadMap(new File("./map/st1.map"));
		setEnemy();
	}

	public void setEnemy() {
		super.setEnemy();
		enmList.add(new JumpDetteiu(this, 992, 500));
		enmList.add(new Detteiu(this, 2900, 500, false));
		enmList.add(new Detteiu(this, 2800, 500, false));
		enmList.add(new Detteiu(this, 2700, 500, false));
		enmList.get(0).setAppear(true);
		enmList.get(1).setAppear(true);
		enmList.get(2).setAppear(true);
		enmList.get(3).setAppear(true);
	}

	public GameMap getNewStage() {
		return new Stage1();
	}

	public GameMap getNextStage() {
		return new Stage2();
	}
}
