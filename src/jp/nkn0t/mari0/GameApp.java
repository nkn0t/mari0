package jp.nkn0t.mari0;

import javax.swing.SwingUtilities;

import jp.nkn0t.mari0.ui.GameFrame;

public class GameApp {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new GameFrame("");
			}
		});
	}
}
