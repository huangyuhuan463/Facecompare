import java.awt.*;
import java.awt.image.*;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.*;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class MyDemo extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage mImg;

	/**
	 * ת��ͼ��
	 * 
	 * @param mat
	 * @return
	 */
	private BufferedImage mat2BI(Mat mat) {
		int dataSize = mat.cols() * mat.rows() * (int) mat.elemSize();
		byte[] data = new byte[dataSize];
		mat.get(0, 0, data);

		int type = mat.channels() == 1 ? BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_3BYTE_BGR;
		if (type == BufferedImage.TYPE_3BYTE_BGR) {
			for (int i = 0; i < dataSize; i += 3) {
				byte blue = data[i + 0];
				data[i + 0] = data[i + 2];
				data[i + 2] = blue;
			}
		}
		/*
		 * ����һ��Ԥ����ͼ�����͵�BufferedImage�� ����������ͼ��Ŀ�ȡ��߶ȡ�����
		 */
		BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
		/*
		 * �������������ؾ��ε����� ����:x���Ͻ�����λ�õ�x���ꡣy���Ͻ�����λ�õ�y���ꡣw���ؾ��εĿ�ȡ�h���ؾ��εĸ߶ȡ�
		 */
		image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
		return image;
	}

	public void paint(Graphics g) {
		if (mImg != null) {
			g.drawImage(mImg, 0, 0, mImg.getWidth(), mImg.getHeight(), this);// ����ָ����ͼ��
		}
	}

	/**
	 * opencvʵ������ʶ��
	 * 
	 * @param img
	 * @return
	 */
	public static Mat detectFace(Mat img) {
		// �������ļ�lbpcascade_frontalface.xml�д���һ������ʶ����
		CascadeClassifier faceDetector = new CascadeClassifier(
				"D:\\biancheng\\java\\ku\\opencv\\sources\\data" + "\\haarcascades\\haarcascade_frontalface_alt.xml");
		CascadeClassifier eyeDetector = new CascadeClassifier(
				"D:\\biancheng\\java\\ku\\opencv\\sources\\data" + "\\haarcascades\\haarcascade_eye_tree_eyeglasses.xml");

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

		// ʶ������
		MatOfRect eyesDetections = new MatOfRect();
		eyeDetector.detectMultiScale(img, eyesDetections);
		Rect[] eyerects = eyesDetections.toArray();
		if (eyerects != null && eyerects.length >= 1) {
			for (Rect rect1 : eyerects) {
				// ������
				Imgproc.rectangle(img, new Point(rect1.x, rect1.y),
						new Point(rect1.x + rect1.width, rect1.y + rect1.height), new Scalar(255, 0, 0), 2);
			}
		}
		return img;
	}

	public static void main(String[] args) {
		try {
			// ����opencv��
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			// ��ȡ����ͷ��Ƶ��
			VideoCapture capture = new VideoCapture(0);
			int height = (int) capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
			int width = (int) capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
			if (height == 0 || width == 0) {
				throw new Exception("camera not found!");
			}

			// ʹ��Swing����GUI
			JFrame frame = new JFrame("camera");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			MyDemo panel = new MyDemo();
			frame.setContentPane(panel);
			frame.setVisible(true);
			frame.setSize(width + frame.getInsets().left + frame.getInsets().right,
					height + frame.getInsets().top + frame.getInsets().bottom);

			Mat capImg = new Mat();
			Mat temp = new Mat();
			while (frame.isShowing()) {
				// ��ȡ��Ƶ֡
				capture.read(capImg);
				// ת��Ϊ�Ҷ�ͼ
				Imgproc.cvtColor(capImg, temp, Imgproc.COLOR_RGB2GRAY);
				// ʶ������
				Mat image = detectFace(capImg);
				// תΪͼ����ʾ
				panel.mImg = panel.mat2BI(image);
				panel.repaint();
			}
			capture.release();
			frame.dispose();
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			System.out.println(sw.toString());
		} finally {
			System.out.println("Exit!");
		}
	}

}
