package facecompare;
import java.util.*;


import org.opencv.core.*;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;
import org.opencv.objdetect.*;

/**
 * ����ƥ��ȼ����Ҫʵ��
 * @author ���ڻ�
 *
 */
public class Facecompare {
	// ��ʼ������̽����
    static CascadeClassifier faceDetector;
    private double compareHist;
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        faceDetector = new CascadeClassifier("D:\\biancheng\\java\\ku\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml");
    }
    
    /**
     * ���ػع�ϵ��
     * @return �ع�ϵ��
     */
    public double getcompareHist() {
    	return compareHist;
    }

    /**
     * ���캯��
     * @param index1 ͼƬ1������
     * @param index2 ͼƬ2������
     */
    public Facecompare(String index1,String index2) {
    	compareHist = compare_image(index1,index2);
	}

    /**
     * �ҶȻ�����
     * @param img ͼƬ����
     * @return
     */
    public static Mat conv_Mat(String img) {
        Mat image0 = Imgcodecs.imread(img);

        Mat image1 = new Mat();
        // �ҶȻ�
        Imgproc.cvtColor(image0, image1, Imgproc.COLOR_BGR2GRAY);
        // ̽������
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image1, faceDetections);
        // rect������ͼƬ�ķ�Χ
        for (Rect rect : faceDetections.toArray()) {
            Mat face = new Mat(image1, rect);
            return face;
        }
        return null;
    }

    /**
     * ��ֱ��ͼ��������ϵ��
     * @param img_1 ͼƬ1������
     * @param img_2 ͼƬ2������
     * @return ���ϵ��
     */
	public static double compare_image(String img_1, String img_2) {
        Mat mat_1 = conv_Mat(img_1);
        Mat mat_2 = conv_Mat(img_2);
        Mat hist_1 = new Mat();
        Mat hist_2 = new Mat();            

        //��ɫ��Χ
        MatOfFloat ranges = new MatOfFloat(0f, 256f);
        //ֱ��ͼ��С�� Խ��ƥ��Խ��ȷ (Խ��)
        MatOfInt histSize = new MatOfInt(1000);

        Imgproc.calcHist(Arrays.asList(mat_1), new MatOfInt(0), new Mat(), hist_1, histSize, ranges);
        Imgproc.calcHist(Arrays.asList(mat_2), new MatOfInt(0), new Mat(), hist_2, histSize, ranges);

        // CORREL ���ϵ��
        double res = Imgproc.compareHist(hist_1, hist_2, Imgproc.CV_COMP_CORREL);
        return res;
    }

}
