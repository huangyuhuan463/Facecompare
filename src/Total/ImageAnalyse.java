package Total;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JFrame;

import histogram.HistogramAnalysisAlg;

public class ImageAnalyse extends JComponent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage rawImg;
	private BufferedImage modImg;
	private MediaTracker tracker;
	private Dimension mySize;
	public ImageAnalyse(BufferedImage rawImg) {
		this.rawImg = rawImg;
		HistogramAnalysisAlg filter = new HistogramAnalysisAlg(rawImg);
		modImg = filter.getHistogram();


		// ����ý��������Ը��ٸ��������ͼ��
		tracker = new MediaTracker(this);
		tracker.addImage(rawImg, 1);// ��ͼ����ӵ�ͼ���б���;

		// �ȴ�10�����ͼ������
		try {
			if (!tracker.waitForID(1, 10000)) {
				System.out.println("Load error.");
				System.exit(1);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}

		mySize = new Dimension(2 * rawImg.getWidth(), rawImg.getHeight() * 2);
		JFrame imageFrame = new JFrame("Image Analysis");
		imageFrame.getContentPane().add(this);
		imageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		imageFrame.pack();// ʹ�˴��ڵĴ�С�ʺ������������ѡ��С�Ͳ���
		imageFrame.setVisible(true);
	}


	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(rawImg, 0, 0, rawImg.getWidth(), rawImg.getHeight(), null);
		g2.drawImage(modImg, rawImg.getWidth() + 10, 0, modImg.getWidth(), modImg.getHeight(), null);
		g2.drawString("source image", 10, rawImg.getHeight() + 10);// ����ָ���ַ������ı�
	}

	public Dimension getPreferredSize() {
		return mySize;
	}

	public Dimension getMinimumSize() {
		return mySize;
	}

	public Dimension getMaximumSize() {
		return mySize;
	}	
}
