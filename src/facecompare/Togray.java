package facecompare;

import java.awt.HeadlessException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Togray {
	
	public static void main(String[] args) {
		// ����opencv��
		try {
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

			// ����ͼƬ
			Mat image = Imgcodecs.imread("src/facecompare/4.jpg");

			// ����ʶ�����ͼ��
			Mat target = new Mat();
			Imgproc.cvtColor(image, target, Imgproc.COLOR_BGR2GRAY);
			Imgcodecs.imwrite("src/facecompare/4gay.jpg", target);
			System.out.println("done!");
		} catch (HeadlessException e) {
			e.printStackTrace();
		}
	}

}
