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


		// 创建媒体跟踪器以跟踪给定组件的图像
		tracker = new MediaTracker(this);
		tracker.addImage(rawImg, 1);// 将图像添加到图像列表中;

		// 等待10秒加载图像数据
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
		imageFrame.pack();// 使此窗口的大小适合其子组件的首选大小和布局
		imageFrame.setVisible(true);
	}


	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(rawImg, 0, 0, rawImg.getWidth(), rawImg.getHeight(), null);
		g2.drawImage(modImg, rawImg.getWidth() + 10, 0, modImg.getWidth(), modImg.getHeight(), null);
		g2.drawString("source image", 10, rawImg.getHeight() + 10);// 呈现指定字符串的文本
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
