package jp.nkn0t.mari0.ui;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


@SuppressWarnings("serial")
public class GameFrame extends JFrame {
	/*
	 * TwoD(2D)ScrollActionGame_Frame
	 */

	public static final int WIDTH = 640;
	public static final int HEIGHT = 640;

	public GameFrame (String frameTitle) {
		super(frameTitle);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		initLookAndFeel();
		initFrameSize();
		initContentPane();
	}

	private void initLookAndFeel() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private void initFrameSize() {
		int dx = this.getInsets().left + this.getInsets().right;
		int dy = this.getInsets().top + this.getInsets().bottom ;
		setSize(WIDTH + dx, HEIGHT + dy);
		setLocationRelativeTo(null);
	}

	private void initContentPane() {
		Container contentPane = getContentPane();
		MainPanel mainPanel = new MainPanel(this);
		KeyOperation kOperation = new KeyOperation(mainPanel.getOperator());
		addKeyListener(kOperation);
		contentPane.add(mainPanel);
	}
}
