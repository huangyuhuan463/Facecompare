package facecompare;
import java.util.*;


import org.opencv.core.*;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;
import org.opencv.objdetect.*;

/**
 * 人脸匹配度检测主要实现
 * @author 黄钰欢
 *
 */
public class Facecompare {
	// 初始化人脸探测器
    static CascadeClassifier faceDetector;
    private double compareHist;
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        faceDetector = new CascadeClassifier("D:\\biancheng\\java\\ku\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml");
    }
    
    /**
     * 返回回归系数
     * @return 回归系数
     */
    public double getcompareHist() {
    	return compareHist;
    }

    /**
     * 构造函数
     * @param index1 图片1的索引
     * @param index2 图片2的索引
     */
    public Facecompare(String index1,String index2) {
    	compareHist = compare_image(index1,index2);
	}

    /**
     * 灰度化人脸
     * @param img 图片索引
     * @return
     */
    public static Mat conv_Mat(String img) {
        Mat image0 = Imgcodecs.imread(img);

        Mat image1 = new Mat();
        // 灰度化
        Imgproc.cvtColor(image0, image1, Imgproc.COLOR_BGR2GRAY);
        // 探测人脸
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image1, faceDetections);
        // rect中人脸图片的范围
        for (Rect rect : faceDetections.toArray()) {
            Mat face = new Mat(image1, rect);
            return face;
        }
        return null;
    }

    /**
     * 画直方图，算出相关系数
     * @param img_1 图片1的索引
     * @param img_2 图片2的索引
     * @return 相关系数
     */
	public static double compare_image(String img_1, String img_2) {
        Mat mat_1 = conv_Mat(img_1);
        Mat mat_2 = conv_Mat(img_2);
        Mat hist_1 = new Mat();
        Mat hist_2 = new Mat();            

        //颜色范围
        MatOfFloat ranges = new MatOfFloat(0f, 256f);
        //直方图大小， 越大匹配越精确 (越慢)
        MatOfInt histSize = new MatOfInt(1000);

        Imgproc.calcHist(Arrays.asList(mat_1), new MatOfInt(0), new Mat(), hist_1, histSize, ranges);
        Imgproc.calcHist(Arrays.asList(mat_2), new MatOfInt(0), new Mat(), hist_2, histSize, ranges);

        // CORREL 相关系数
        double res = Imgproc.compareHist(hist_1, hist_2, Imgproc.CV_COMP_CORREL);
        return res;
    }

}
