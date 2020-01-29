package jp.nkn0t.mari0.util;

import static java.awt.event.InputEvent.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

@SuppressWarnings("serial")
public class MapBuilder extends JFrame {
	private Dimension dimension;
	private Container contentPane;
	private JMenuBar menubar;
	private MapEditPanel mapEditPanel;
	private MapChipDialog mcDialog;
	public static final int HEIGHT = 640;
	public static final int WIDTH = 640;
	public static final int TILE_SIZE = 32;

	MapBuilder() {
		super("MapBuilder");
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

		dimension = new Dimension(640, 640);

		init();
		initMenubar();

		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void init() {
		mcDialog = new MapChipDialog(this);
		mcDialog.setVisible(true);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		contentPane = getContentPane();
		contentPane.setLayout(new  BorderLayout());
		mapEditPanel = new MapEditPanel(dimension, mcDialog);

		JScrollPane scrollPane = new JScrollPane(mapEditPanel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		contentPane.add(BorderLayout.CENTER, scrollPane);
		contentPane.add(BorderLayout.SOUTH, new JLabel("(  ,  )"));
		setSize(670, 730);
	}

	private void initMenubar() {
		menubar = new JMenuBar();

		JMenu fileMenu = new JMenu("�t�@�C��(F)");
		fileMenu.setMnemonic('F');
		fileMenu.add(new FileOpen("�J��(O)", this));
		fileMenu.addSeparator();
		fileMenu.add(new FileSave("���O�����ĕۑ�(S)", this));


		JMenu edit = new JMenu("�ҏW(E)");
		edit.setMnemonic('E');
		edit.add(new JMenuItem(new SizeEdit("�}�b�v�T�C�Y�̕ύX", this, dimension)));

		JMenu view = new JMenu("(V)");
		view.setMnemonic('V');
		view.add(new JMenuItem(new ShowMapchipDialog("")));

		menubar.add(fileMenu);
		menubar.add(edit);
		menubar.add(view);
		setJMenuBar(menubar);
	}

	//�t�@�C�����J���A�N�V����
	class FileOpen extends AbstractAction {
		private JFrame parent;
		public FileOpen(String label, JFrame frame) {
			super(label);
			parent = frame;
			putValue(MNEMONIC_KEY, KeyEvent.VK_O);
		}

		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser("./map");
			fileChooser.setFileFilter(new MapFileFilter());
			fileChooser.setDialogTitle("�t�@�C�����J��");
			fileChooser.setPreferredSize(new Dimension(500, 500));
			if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				mapEditPanel.loadMap(file);
				dimension = mapEditPanel.getSize();
			}
		}
	}

	//�t�@�C�����Z�[�u����A�N�V����
	class FileSave extends AbstractAction {
		private JFrame parent;
		public FileSave(String label, JFrame frame) {
			super(label);
			parent = frame;
			putValue(MNEMONIC_KEY, KeyEvent.VK_S);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, CTRL_MASK));
		}
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser("./map");
			fileChooser.setDialogTitle("�t�@�C����ۑ�����");
			fileChooser.setPreferredSize(new Dimension(500, 500));
			if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				String name = file.getName();
				String ext = name.substring(name.indexOf('.') + 1, name.length());
				if (!ext.equals("map")) file = new File("./map/" + name + ".map");
				mapEditPanel.saveMap(file);
			}
		}
	}

	//SizeEdit���j���[�̃A�N�V����
	class SizeEdit extends AbstractAction {
		private JDialog dialog;
		private JPanel previewPanel;
		private JSpinner inputColumn, inputRow;

		public SizeEdit(String label, JFrame parent, Dimension d) {
			super(label);
			dialog = new JDialog(parent, "�}�b�v�T�C�Y�̕ύX", true);
			dimension = d;
			previewPanel = new PreviewPanel(d.width/TILE_SIZE, d.height/TILE_SIZE);
			initDialog();
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			inputRow.setValue(dimension.height/TILE_SIZE);
			inputColumn.setValue(dimension.width/TILE_SIZE);
			int x = dialog.getOwner().getX() + dialog.getOwner().getWidth()/2 - 150;
			int y = dialog.getOwner().getY() + dialog.getOwner().getHeight()/2 - 120;
			dialog.setBounds(x, y, 300, 240);
			dialog.setVisible(true);
		}

		private void initDialog() {
			JPanel colPanel = new JPanel(new FlowLayout());
			JPanel rowPanel = new JPanel(new FlowLayout());
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			JPanel leftPanel = new JPanel(new FlowLayout(0, 10, 30));
			JPanel centerPanel = new JPanel(new GridLayout(1, 2, 1, 1));

			dialog.setTitle("�}�b�v�T�C�Y�̕ύX");
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setLocationRelativeTo(dialog.getOwner());
			dialog.setResizable(false);
			dialog.getContentPane().setLayout(new BorderLayout());

			inputColumn = new JSpinner(new SpinnerNumberModel(20, 20, 500, 1));
			inputColumn.addChangeListener(new StateChanger());

			inputRow = new JSpinner(new SpinnerNumberModel(20, 20, 500, 1));
			inputRow.addChangeListener(new StateChanger());
			colPanel.add(new JLabel("��F"));
			colPanel.add(inputColumn);
			rowPanel.add(new JLabel("�s�F"));
			rowPanel.add(inputRow);
			leftPanel.add(colPanel);
			leftPanel.add(rowPanel);

			buttonPanel.add(new JButton(new OKAction("OK")));
			buttonPanel.add(new JButton(new CancelAction("cancel")));

			JPanel p = new JPanel(new BorderLayout(10, 10));
			p.add(BorderLayout.SOUTH, leftPanel);
			centerPanel.add(leftPanel);
			centerPanel.add(previewPanel);

			dialog.getContentPane().add(BorderLayout.CENTER, centerPanel);
			dialog.getContentPane().add(BorderLayout.SOUTH, buttonPanel);
		}

		class OKAction extends AbstractAction {
			public OKAction(String label) { super(label); }
			@Override
			public void actionPerformed(ActionEvent e) {
				int w = (Integer)inputColumn.getValue()*TILE_SIZE;
				int h = (Integer)inputRow.getValue()*TILE_SIZE;
				dimension.setSize(w, h);
				previewPanel.setSize(dimension.width/TILE_SIZE, dimension.height/TILE_SIZE);
				mapEditPanel.changeSize(dimension);
				dialog.dispose();
			}
		}

		class CancelAction extends AbstractAction {
			public CancelAction(String label) { super(label); }
			@Override
			public void actionPerformed(ActionEvent e) {
				inputRow.setValue(dimension.width/TILE_SIZE);
				inputColumn.setValue(dimension.height/TILE_SIZE);
				dialog.dispose();
			}
		}

		class PreviewPanel extends JPanel {
			private int prev_width, prev_height;
			public static final int WIDTH = 150;
			public static final int HEIGHT = 150;

			public PreviewPanel(int w, int h) {
				prev_width = w;
				prev_height = h;
				setPreferredSize(new Dimension(WIDTH, HEIGHT));
			}
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2D = (Graphics2D) g;
				g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2D.setColor(Color.red);
				g2D.fillRect(WIDTH/2 - prev_width/2, HEIGHT/2 - prev_height/2, prev_width, prev_height);
				g2D.setColor(Color.black);
				g2D.drawString("" + prev_width, 67, 20);
				g2D.drawString("" + prev_height, 2, 80);
				g2D.drawLine(WIDTH/2 - prev_width/2, 25, WIDTH/2 - prev_width/2, 125);
				g2D.drawLine(WIDTH/2 + prev_width/2, 25, WIDTH/2 + prev_width/2, 125);
				g2D.drawLine(25, HEIGHT/2 - prev_height/2, 125, HEIGHT/2 - prev_height/2);
				g2D.drawLine(25, HEIGHT/2 + prev_height/2, 125, HEIGHT/2 + prev_height/2);
				g2D.drawString("�v���r���[", 50, 145);
			}
			public void setSize(int w, int h) {
				prev_width = w;
				prev_height = h;
			}
		}

		class StateChanger implements ChangeListener {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				int r = (Integer)inputRow.getValue();
				int c = (Integer)inputColumn.getValue();
				boolean isRepaintable = true;

				if (r > 100 || r < 20) {
					isRepaintable = false;
				}

				if (c > 100 || c < 20) {
					isRepaintable = true;
				}

				if (isRepaintable) {
					previewPanel.setSize(c, r);
					Toolkit.getDefaultToolkit().sync();
					previewPanel.repaint();
				}
			}
		}
	}

	//View�̃A�N�V����
	class ShowMapchipDialog extends AbstractAction {
		ShowMapchipDialog(String label) {
			super(label);
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (!mcDialog.isVisible())	mcDialog.setVisible(true);
		}
	}


	class MapFileFilter extends FileFilter {
        public boolean accept(File file) {
            String extension = "";
            if (file.getPath().lastIndexOf('.') > 0) {
                extension = file.getPath().substring(
                        file.getPath().lastIndexOf('.') + 1).toLowerCase();
            }

            if (extension != "") {
                return extension.equals("map");
            } else {
                return file.isDirectory();
            }
        }

        public String getDescription() {
            return "Map Files (*.map)";
        }
    }

	public static void main(String[] args) {
		new MapBuilder();
	}
}
