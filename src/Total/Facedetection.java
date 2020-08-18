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
		// �������ļ�lbpcascade_frontalface.xml�д���һ������ʶ����
		CascadeClassifier faceDetector = new CascadeClassifier(
				"D:\\biancheng\\java\\ku\\opencv\\sources\\data" + "\\haarcascades\\haarcascade_frontalface_alt.xml");

		// ��ͼƬ�м������
		MatOfRect faceDections = new MatOfRect();
		faceDetector.detectMultiScale(img, faceDections);

		Rect[] facerects = faceDections.toArray();
		Rect maxrect = facerects[0];
		if (facerects != null && facerects.length >= 1) {
			for (Rect rect : facerects) {
				// ������
				Imgproc.rectangle(img, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
						new Scalar(0, 0, 255), 2);
				if (maxrect.area() < rect.area())
					maxrect = rect;
			}
		}
		//��ͼ����
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
		// ����ʶ�����ͼ��
		Mat target1 = new Mat();
		target1 = detectFace(image1,true);

		Mat target2 = new Mat();
		target2 = detectFace(image2,false);

		ImageViewer imageview = new ImageViewer(target1, "����ʶ����ͼ��1");
		imageview.imshow();
		ImageViewer imageview2 = new ImageViewer(target2, "����ʶ����ͼ��2");
		imageview2.imshow();
	}
	
	public Mat getcutimage1() {
		return sub1;
	}
	
	public Mat getcutimage2() {
		return sub2;
	}

}
