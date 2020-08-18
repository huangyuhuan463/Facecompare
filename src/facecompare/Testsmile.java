package facecompare;

import java.awt.HeadlessException;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class Testsmile {

	public static Mat detectFace(Mat img) {
		// �������ļ�lbpcascade_frontalface.xml�д���һ������ʶ����
		CascadeClassifier faceDetector = new CascadeClassifier(
				"D:\\biancheng\\java\\ku\\opencv\\sources\\data" + "\\haarcascades\\haarcascade_frontalface_alt.xml");

		// ��ͼƬ�м������
		MatOfRect faceDections = new MatOfRect();
		faceDetector.detectMultiScale(img, faceDections);

		Rect[] facerects = faceDections.toArray();
		if (facerects != null && facerects.length >= 1) {
			for (Rect rect : facerects) {
				// ������
				Imgproc.rectangle(img, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
						new Scalar(0, 0, 255), 2);

			}
		}
		return img;
	}

	static boolean isInside(Rect rect1, Rect rect2) {
		if (rect1.x > rect2.x && rect1.x + rect1.width < rect2.x + rect2.width && rect1.y > rect2.y
				&& rect1.y + rect1.height < rect2.y + rect2.height)
			return true;
		return false;
	}

	public static void main(String[] args) {
		// ����opencv��
		try {
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

			// ����ͼƬ
			Mat image = Imgcodecs.imread("src/facecompare/4gay.jpg");

			// ����ʶ�����ͼ��
			Mat target = new Mat();
			target = detectFace(image);
			Imgcodecs.imwrite("src/facecompare/4face.jpg", target);
			System.out.println("done!");
		} catch (HeadlessException e) {
			e.printStackTrace();
		}
	}

}
