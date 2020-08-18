package Total;

import java.util.Arrays;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgproc.Imgproc;

public class FinalResult {
	private double compareHist;
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public FinalResult(Mat img1, Mat img2) {
		compareHist = compare_image(img1, img2);
	}

	private double compare_image(Mat img1, Mat img2) {
		Mat hist_1 = new Mat();
		Mat hist_2 = new Mat();

		// 颜色范围
		MatOfFloat ranges = new MatOfFloat(0f, 256f);
		// 直方图大小， 越大匹配越精确 (越慢)
		MatOfInt histSize = new MatOfInt(1000);

		Imgproc.calcHist(Arrays.asList(img1), new MatOfInt(0), new Mat(), hist_1, histSize, ranges);
		Imgproc.calcHist(Arrays.asList(img2), new MatOfInt(0), new Mat(), hist_2, histSize, ranges);

		// CORREL 相关系数
		double res = Imgproc.compareHist(hist_1, hist_2, Imgproc.CV_COMP_CORREL);
		return res;
	}
	
	public double getcompareHist() {
		return compareHist;
	}

}
