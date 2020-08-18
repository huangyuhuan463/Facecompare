package Total;
import java.awt.HeadlessException;


import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class ToGray{
	private Mat target1;
	private Mat target2;
	
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	public ToGray(String imgFile1,String imgFile2) {		
		try {					
			// 加载图片
			Mat image1 = Imgcodecs.imread(imgFile1);
			Mat image2 = Imgcodecs.imread(imgFile2);

			target1 = new Mat();
			Imgproc.cvtColor(image1, target1, Imgproc.COLOR_BGR2GRAY);
			target2 = new Mat();
			Imgproc.cvtColor(image2, target2, Imgproc.COLOR_BGR2GRAY);
			
			ImageViewer imageViewer1 = new ImageViewer(target1, "灰度化后的图片1");
			ImageViewer imageViewer2 = new ImageViewer(target2, "灰度化后的图片2");
			imageViewer1.imshow();
			imageViewer2.imshow();
		} catch (HeadlessException e1) {
			e1.printStackTrace();
		}
	}
	
	public Mat getgrayimage1() {
		return target1;
	}
	
	public Mat getgrayimage2() {
		return target2;
	}
	
}
