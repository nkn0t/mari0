package jp.nkn0t.mari0.util;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class MapEditPanel extends JPanel {
	private int row, col;
	private int mp_r, mp_c;
	public static final int TS = 32;
	private MapChipDialog mpDialog;
    private BufferedImage[][] mapchipImages;
    
	public int[][] map = new int[][] {
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
	
	public MapEditPanel(Dimension d, MapChipDialog mpDialog) {
		this.mpDialog = mpDialog;
		mp_r = MapChipDialog.ROW;
		mp_c = MapChipDialog.COL;
		setSize(d);
		setPreferredSize(d);
        setFocusable(true);
        mapchipImages = mpDialog.getMapchipImages();
		init();
		initMap(20, 20);
	}
	
	private void init() {
		addMouseListener(new MouseAdapterExt());
		addMouseMotionListener(new MouseAdapterExt());
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
        
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                g.drawImage(mapchipImages[map[i][j]%mp_r][map[i][j]/mp_c], j*TS, i*TS, null);
            }
        }
	}
	
    public void initMap(int r, int c) {
        row = r;
        col = c;
        map = new int[row][col];

        // �p���b�g�őI������Ă���}�b�v�`�b�v�ԍ����擾
        int mapchipNo = mpDialog.getSelectedMapchipNo();
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                map[i][j] = mapchipNo;
            }
        }
    }
	
    public void fillMap() {
        // �p���b�g�őI������Ă���}�b�v�`�b�v�ԍ����擾
        int mapchipNo = mpDialog.getSelectedMapchipNo();

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                map[i][j] = mapchipNo;
            }
        }

        repaint();
    }
	
	public void loadMap(File file) {
		try {
			FileInputStream is = new FileInputStream(file);
			row = is.read();
			col = is.read();
			map = new int[row][col];
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < col; j++) {
					map[i][j] = is.read();
					System.out.println(map[i][j]);
				}
			}
			is.close();
			setSize(new Dimension(col*TS, row*TS));
			setPreferredSize(new Dimension(col*TS, row*TS));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void saveMap(File file) {
		try {
			FileOutputStream os = new FileOutputStream(file);
			int row = getHeight()/TS;
			int col = getWidth()/TS;
			os.write(row);
			os.write(col);
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < col; j++) {
					os.write(map[i][j]);
				}
			}
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void changeSize(Dimension d) {
		int tmp_r = row;
		int tmp_c = col;
		int[][] tmp = map.clone();
		row = d.height/TS;
		col = d.width/TS;
		map = new int[row][col];
		
		for (int i = 0; i < row; i++) {
			if (i == tmp_r) break;
			for (int j = 0; j < col; j++) {
				if (j == tmp_c) break;
				map[i][j] = tmp[i][j];
			}
		}
		setSize(d);
		setPreferredSize(d);
	}
	
	private class MouseAdapterExt extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
	        int x = e.getX() / TS;
	        int y = e.getY() / TS;

	        if (SwingUtilities.isLeftMouseButton(e)) { // ���N���b�N�̏ꍇ
	            // �p���b�g����擾�����ԍ����Z�b�g
	            if (x >= 0 && x < col && y >= 0 && y < row) {
	                map[y][x] =	mpDialog.getSelectedMapchipNo();
	            }
	        } else if (SwingUtilities.isRightMouseButton(e)) { // �E�N���b�N�̏ꍇ
	            // ���݈ʒu�̃}�b�v�`�b�v�ԍ����Z�b�g
	            mpDialog.setSelectedMapchipNo(map[y][x]);
	        }
	        repaint();
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			int x = e.getX()/TS;
			int y = e.getY()/TS;
			if (SwingUtilities.isLeftMouseButton(e)) { // ���N���b�N�̏ꍇ
	            // �p���b�g����擾�����ԍ����Z�b�g
	            if (x >= 0 && x < col && y >= 0 && y < row) {
	                map[y][x] = mpDialog.getSelectedMapchipNo();
	            }
	        } else if (SwingUtilities.isRightMouseButton(e)) { // �E�N���b�N�̏ꍇ
	            // ���݈ʒu�̃}�b�v�`�b�v�ԍ����Z�b�g
	            mpDialog.setSelectedMapchipNo(map[y][x]);
	        }
			repaint();
		}
	}
}
