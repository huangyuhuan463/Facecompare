package Total;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.*;

public class Chooseimage extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public Chooseimage(String imgFile1,String imgFile2) {		
		setTitle("ͼƬѡ����");
		setBounds(100, 100, 800, 500);//����λ�úʹ�С
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//����Ĺرյ�ʱ
		getContentPane().add(new CanvasPanel(imgFile1,imgFile2));
		setVisible(true);
	}
	
	class CanvasPanel extends Canvas{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String imgFile1;
		private String imgFile2;
		
		public CanvasPanel(String imgFile1,String imgFile2) {
			this.imgFile1 = imgFile1;
			this.imgFile2 = imgFile2;
		}

		/**
		 * ��ͼƬ
		 */
		public void paint(Graphics g) {
			if(imgFile1!=null) {
				Image img1 = new ImageIcon(imgFile1).getImage();//���ͼƬ
				g.drawImage(img1, 50, 50, 300, 300, this);//��ͼƬ
			}
			if(imgFile2!=null) {
				Image img2 = new ImageIcon(imgFile2).getImage();
				g.drawImage(img2, 450, 50, 300, 300, this);
			}
		}
	}	

}
