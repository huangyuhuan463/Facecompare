package Total;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * 主窗体
 * 
 * @author 黄钰欢
 *
 */
public class Mainframe extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String imgFile1;// 选择的图片
	private String imgFile2;

	private Mat grayimage1;// 图片灰度化后的图像
	private Mat grayimage2;

	private Mat cutimage1;// 图片灰度化截图后的图像
	private Mat cutimage2;

	private double res;// 对比结果
	
	private static JButton buttonopen;
	private static JButton buttongray;
	private static JButton buttonDetction;
	private static JButton buttoncut;
	private static JButton buttonhist;
	private static JButton buttonres;
	

	public static void main(String[] args) {
		Mainframe frame = new Mainframe();
		frame.setVisible(true);//设置窗体持续可见
	}

	public Mainframe() {
		super();
		Container c = getContentPane();
		setTitle("人脸匹配度检测小工具");// 窗体的标题
		setBounds(100, 100, 600, 300);// 窗体位置和大小
		setLayout(new BorderLayout());//边界布局
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 窗体的关闭当时	
		
		JPanel panel_1 = new JPanel();
		JPanel panel_2 = new JPanel();
		JPanel panel_3 = new JPanel();
		panel_3.setLayout(new GridLayout(2,1));
		
		JLabel jl1 = new JLabel("欢迎使用人脸匹配度检测小工具!",JLabel.CENTER);
		jl1.setFont(new Font("宋体", Font.BOLD, 28));
		jl1.setForeground(Color.MAGENTA);
		
		JLabel jl2 = new JLabel("请按照按钮顺序从左至右制作运行",JLabel.CENTER);
		jl2.setFont(new Font("宋体", Font.BOLD, 16));
		jl2.setForeground(Color.BLACK);

		JTextArea area = new JTextArea();
		area.setRows(5);// 文本框设置5行20列
		area.setColumns(20);

		buttonopen = new JButton("选择图片");
		buttonopen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonEnable();
				area.setText("");
				
				JFileChooser fileChooser = new JFileChooser();
				int i = fileChooser.showOpenDialog(c);// 选择所要的图片
				if (i == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					imgFile1 = selectedFile.getPath();// 得到文件的路径
				}

				int j = fileChooser.showOpenDialog(c);// 选择所要的图片
				if (j == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					imgFile2 = selectedFile.getPath();// 得到文件的路径
				}
				new Chooseimage(imgFile1, imgFile2);//生成子窗体显示图片
				buttongray.setEnabled(true);
			}
		});

		buttongray = new JButton("进行灰度化处理");
		buttongray.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ToGray tg = new ToGray(imgFile1, imgFile2);
					grayimage1 = tg.getgrayimage1();
					grayimage2 = tg.getgrayimage2();
					buttonDetction.setEnabled(true);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "您没有选择图片，请选择!");
					
				}
			}
		});

		buttonDetction = new JButton("进行人脸识别");
		buttonDetction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Facedetection fd = new Facedetection(grayimage1, grayimage2);
				cutimage1 = fd.getcutimage1();//返回裁剪后的图像
				cutimage2 = fd.getcutimage2();
				buttoncut.setEnabled(true);
			}
		});

		buttoncut = new JButton("截图");
		buttoncut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImageViewer imageview = new ImageViewer(cutimage1, "裁剪后的图像1");
				imageview.imshow();//显示图像
				ImageViewer imageview2 = new ImageViewer(cutimage2, "裁剪后的图像2");
				imageview2.imshow();
				buttonhist.setEnabled(true);
			}
		});

		buttonhist = new JButton("图像直方图");
		buttonhist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BufferedImage bf1 = Mat2BufImg(cutimage1, ".jpg");// mat转Bufferimage
				BufferedImage bf2 = Mat2BufImg(cutimage2, ".jpg");
				new ImageAnalyse(bf1);//生成直方图
				new ImageAnalyse(bf2);
				buttonres.setEnabled(true);
			}
		});

		buttonres = new JButton("最后结果");
		buttonres.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FinalResult fr = new FinalResult(cutimage1, cutimage2);
				res = fr.getcompareHist();
				if (res != 0) {
					area.setText("对比度为" + res + "\n");
					if (res > 0.72) {
						area.append("人脸匹配!");
					} else {
						area.append("人脸不匹配!");
					}
				}
				JOptionPane.showMessageDialog(null, "恭喜你！人脸匹配已经完成\n" +
				"您可以再一次查看中间步骤或者重新选择图片");
				c.revalidate();
			}
		});
		panel_3.add(jl1);
		panel_3.add(jl2);
		
		panel_1.add(buttonopen);
		panel_1.add(buttongray);
		panel_1.add(buttonDetction);
		panel_1.add(buttoncut);
		panel_1.add(buttonhist);
		panel_1.add(buttonres);

		panel_2.add(area);
	
		c.add(panel_3,BorderLayout.NORTH);
		c.add(panel_1, BorderLayout.CENTER);
		c.add(panel_2, BorderLayout.SOUTH);
		
		buttonEnable();	
	}

	public static BufferedImage Mat2BufImg(Mat matrix, String fileExtension) {
		// convert the matrix into a matrix of bytes appropriate for
		// this file extension
		MatOfByte mob = new MatOfByte();
		Imgcodecs.imencode(fileExtension, matrix, mob);
		// convert the "matrix of bytes" into a byte array
		byte[] byteArray = mob.toArray();
		BufferedImage bufImage = null;
		try {
			InputStream in = new ByteArrayInputStream(byteArray);
			bufImage = ImageIO.read(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bufImage;
	}
	
	public static void buttonEnable() {
		buttongray.setEnabled(false);
		buttonDetction.setEnabled(false);
		buttoncut.setEnabled(false);
		buttonhist.setEnabled(false);
		buttonres.setEnabled(false);
	}
}