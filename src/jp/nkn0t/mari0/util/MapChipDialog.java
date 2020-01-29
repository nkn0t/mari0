package jp.nkn0t.mari0.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MapChipDialog extends JDialog {
	private int selectedMapChipNo = 0;
	public static final int TS = 32;
    public static final int ROW = 16;
    public static final int COL = 16;
	
    private BufferedImage mapchipImage;
    private BufferedImage[][] mapchipImages;
	
	public MapChipDialog(JFrame parent) {
        super(parent, "MapChip", false);
        
        setVisible(true);
        initSize();
        MapChipPanel palettePanel = new MapChipPanel();
        getContentPane().add(palettePanel);
        loadImage();
        setVisible(true);
	}

	private void initSize() {
		int dx = this.getInsets().left + this.getInsets().right;
		int dy = this.getInsets().top + this.getInsets().bottom ;
		setSize(COL*TS + dx, ROW*TS + dy);
		setVisible(false);
	}


    public int getSelectedMapchipNo() {
        return selectedMapChipNo;
    }

    /**
     * �}�b�v�`�b�v�ԍ����Z�b�g
     * 
     * @param no �}�b�v�`�b�v�ԍ�
     */
    public void setSelectedMapchipNo(int no) {
        selectedMapChipNo = no;
        repaint();
    }

    /**
     * �������ꂽ�}�b�v�`�b�v�C���[�W��Ԃ�
     * 
     * @return �������ꂽ�}�b�v�`�b�v�C���[�W
     */
    public BufferedImage[][] getMapchipImages() {
        return mapchipImages;
    }

    // �}�b�v�`�b�v�C���[�W�����[�h
    private void loadImage() {
    	try {
    		mapchipImage = ImageIO.read(new File("Game_Image/maptip.png"));
    		mapchipImages = new BufferedImage[ROW][COL];
    		for (int i = 0; i < ROW; i++) {
    			for (int j = 0; j < COL; j++) {
    				mapchipImages[i][j] = mapchipImage.getSubimage(i*TS, j*TS, TS, TS);
    			}
    		}
    	} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	class MapChipPanel extends JPanel implements MouseListener {
		
		MapChipPanel() {
			init();
		}
		
		private void init() {
			addMouseListener(this);
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
            g.setColor(new Color(128, 0, 0));
            g.fillRect(0, 0, MapChipDialog.WIDTH, MapChipDialog.HEIGHT);

            g.drawImage(mapchipImage, 0, 0, this);

            int x = selectedMapChipNo % ROW;
            int y = selectedMapChipNo / COL;
            g.setColor(Color.YELLOW);
            g.drawRect(x * TS, y * TS, TS, TS);
		}
		
        public void mouseClicked(MouseEvent e) {}

        public void mouseEntered(MouseEvent e) {}

        public void mouseExited(MouseEvent e) {}

        public void mousePressed(MouseEvent e) {
            int x = e.getX() / TS;
            int y = e.getY() / TS;
            selectedMapChipNo = y * ROW + x;
            repaint();
        }

        public void mouseReleased(MouseEvent e) {}
	}
	
	
	
}
