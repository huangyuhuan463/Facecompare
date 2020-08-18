package Total;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class Facedetection {
	private static Mat sub1;
	private static Mat sub2;
	
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static Mat detectFace(Mat img,boolean flag) {
		// 从配置文件lbpcascade_frontalface.xml中创建一个人脸识别器
		CascadeClassifier faceDetector = new CascadeClassifier(
				"D:\\biancheng\\java\\ku\\opencv\\sources\\data" + "\\haarcascades\\haarcascade_frontalface_alt.xml");

		// 在图片中检测人脸
		MatOfRect faceDections = new MatOfRect();
		faceDetector.detectMultiScale(img, faceDections);

		Rect[] facerects = faceDections.toArray();
		Rect maxrect = facerects[0];
		if (facerects != null && facerects.length >= 1) {
			for (Rect rect : facerects) {
				// 画矩形
				Imgproc.rectangle(img, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
						new Scalar(0, 0, 255), 2);
				if (maxrect.area() < rect.area())
					maxrect = rect;
			}
		}
		//截图保存
		save(img,maxrect,flag);
			
		return img;
	}

	private static void save(Mat img, Rect maxrect,boolean flag) {
		if(flag == true) {
			sub1 = img.submat(maxrect);
		} else {
			sub2 = img.submat(maxrect);
		}
			
	}

	public Facedetection(Mat image1, Mat image2) {
		// 人脸识别，输出图像
		Mat target1 = new Mat();
		target1 = detectFace(image1,true);

		Mat target2 = new Mat();
		target2 = detectFace(image2,false);

		ImageViewer imageview = new ImageViewer(target1, "人脸识别后的图像1");
		imageview.imshow();
		ImageViewer imageview2 = new ImageViewer(target2, "人脸识别后的图像2");
		imageview2.imshow();
	}
	
	public Mat getcutimage1() {
		return sub1;
	}
	
	public Mat getcutimage2() {
		return sub2;
	}

}
