package histogram;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class ImageAnalysisUI extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage rawImg;
	private BufferedImage modImg;
	private MediaTracker tracker;
	private Dimension mySize;

	public ImageAnalysisUI(File f) {
		try {
			rawImg = ImageIO.read(f);
			HistogramAnalysisAlg filter = new HistogramAnalysisAlg(rawImg);
			modImg = filter.getHistogram();
		} catch (IOException e) {
			e.printStackTrace();
		}

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
		imageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

	public static void main(String[] args) {
		JFileChooser chooser = new JFileChooser();
		chooser.showOpenDialog(null);
		File f = chooser.getSelectedFile();
		new ImageAnalysisUI(f);
	}

}
