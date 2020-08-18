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
	 * 转换图像
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
		 * 构造一个预定义图像类型的BufferedImage。 参数：创建图像的宽度、高度、类型
		 */
		BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
		/*
		 * 设置数组中像素矩形的数据 参数:x左上角像素位置的x坐标。y左上角像素位置的y坐标。w像素矩形的宽度。h像素矩形的高度。
		 */
		image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
		return image;
	}

	public void paint(Graphics g) {
		if (mImg != null) {
			g.drawImage(mImg, 0, 0, mImg.getWidth(), mImg.getHeight(), this);// 绘制指定的图像
		}
	}

	/**
	 * opencv实现人眼识别
	 * 
	 * @param img
	 * @return
	 */
	public static Mat detectFace(Mat img) {
		// 从配置文件lbpcascade_frontalface.xml中创建一个人脸识别器
		CascadeClassifier faceDetector = new CascadeClassifier(
				"D:\\biancheng\\java\\ku\\opencv\\sources\\data" + "\\haarcascades\\haarcascade_frontalface_alt.xml");
		CascadeClassifier eyeDetector = new CascadeClassifier(
				"D:\\biancheng\\java\\ku\\opencv\\sources\\data" + "\\haarcascades\\haarcascade_eye_tree_eyeglasses.xml");

		// 在图片中检测人脸
		MatOfRect faceDections = new MatOfRect();
		faceDetector.detectMultiScale(img, faceDections);

		Rect[] facerects = faceDections.toArray();
		if (facerects != null && facerects.length >= 1) {
			for (Rect rect : facerects) {

				// 画矩形
				Imgproc.rectangle(img, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
						new Scalar(0, 0, 255), 2);

			}
		}

		// 识别人眼
		MatOfRect eyesDetections = new MatOfRect();
		eyeDetector.detectMultiScale(img, eyesDetections);
		Rect[] eyerects = eyesDetections.toArray();
		if (eyerects != null && eyerects.length >= 1) {
			for (Rect rect1 : eyerects) {
				// 画矩形
				Imgproc.rectangle(img, new Point(rect1.x, rect1.y),
						new Point(rect1.x + rect1.width, rect1.y + rect1.height), new Scalar(255, 0, 0), 2);
			}
		}
		return img;
	}

	public static void main(String[] args) {
		try {
			// 加载opencv库
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			// 获取摄像头视频流
			VideoCapture capture = new VideoCapture(0);
			int height = (int) capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
			int width = (int) capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
			if (height == 0 || width == 0) {
				throw new Exception("camera not found!");
			}

			// 使用Swing生成GUI
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
				// 获取视频帧
				capture.read(capImg);
				// 转换为灰度图
				Imgproc.cvtColor(capImg, temp, Imgproc.COLOR_RGB2GRAY);
				// 识别人脸
				Mat image = detectFace(capImg);
				// 转为图像显示
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
