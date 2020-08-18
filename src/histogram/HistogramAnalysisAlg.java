package histogram;

import java.awt.*;
import java.awt.image.BufferedImage;

public class HistogramAnalysisAlg {
	private BufferedImage srcImage;
	private BufferedImage histogramImage;
	private int size = 280;

	public HistogramAnalysisAlg(BufferedImage srcImage) {
		// TYPE_4BYTE_ABGR
		// 表示具有8位RGBA颜色组件的图像，颜色值、绿色和红色存储在alpha的3字节和1字节中。
		histogramImage = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
		this.srcImage = srcImage;
	}

	public BufferedImage getHistogram() {
		int[] inPixels = new int[srcImage.getWidth() * srcImage.getHeight()];
		int[] intensity = new int[256];
		for (int i = 0; i < intensity.length; i++) {
			intensity[i] = 0;
		}
		getRGB(srcImage, 0, 0, srcImage.getWidth(), srcImage.getHeight(), inPixels);
		int index = 0;
		for (int row = 0; row < srcImage.getHeight(); row++) {
			int  tr = 0, tg = 0, tb = 0;
			for (int col = 0; col < srcImage.getWidth(); col++) {
				index = row * srcImage.getWidth() + col;
				tr = (inPixels[index] >> 16) & 0xff;
				tg = (inPixels[index] >> 8) & 0xff;
				tb = inPixels[index] & 0xff;
				int gray = (int) (0.299 * (double) tr + 0.587 * (double) tg + 0.114 * (double) tb);
				intensity[gray]++;
			}
		}
		
		//画XY坐标
		Graphics2D g2d = histogramImage.createGraphics();
		g2d.setPaint(Color.BLACK);
		g2d.fillRect(0, 0, size, size);
		g2d.setPaint(Color.WHITE);
		g2d.drawLine(5, 250, 265, 250);
		g2d.drawLine(5, 250, 5, 5);
		
		//坐标到200
		g2d.setPaint(Color.GREEN);
		int max = findMaxValue(intensity);
		float rate = 200.f/((float)max);
		int offset = 2;
		for(int i=0;i<intensity.length;i++) {
			int frequency = (int)(intensity[i] * rate);
			g2d.drawLine(5+offset+i, 250, 5+offset+i, 250-frequency);
		}
		
		//X坐标灰度强度
		g2d.setPaint(Color.RED);
		g2d.drawString("Gray Intensity", 100, 270);
		return histogramImage;
	}

	private int findMaxValue(int[] intensity) {
		int max = -1;
		for(int i=0;i<intensity.length;i++) {
			if(max<intensity[i]) {
				max = intensity[i];
			}
		}
		return max;
	}

	//从图像中获取ARGB像素的一种方便方法。
	public int[] getRGB(BufferedImage image, int x, int y, int width, int height, int[] Pixels) {
		int type = image.getType();
		if(type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB)
			return (int[])image.getRaster().getDataElements(x, y, width, height, Pixels);
		return image.getRGB(x, y, width, height, Pixels, 0, width);
	}

}
