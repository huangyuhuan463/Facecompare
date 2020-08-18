package facecompare;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;

/**
 * 主窗体
 * @author 黄钰欢
 *
 */
public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String imgFile1;//选择图片的索引
	public String imgFile2;
	public double compareHist;// CORREL 相关系数

	/**
	 * 主函数
	 * @param args
	 */
	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.setVisible(true);//显示窗体
	}

	/**
	 * 构造函数
	 */
	public MainFrame() {
		super();
		setTitle("图片选择器");//窗体的标题
		setBounds(100, 100, 800, 600);//窗体位置和大小
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//窗体的关闭当时
		Container c = getContentPane();//获取容器

		JButton button1 = new JButton("选择图片1");//按钮1
		JButton button2 = new JButton("选择图片2");//按钮2

		button1.addActionListener(new ActionListener() {//添加监听
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int i = fileChooser.showOpenDialog(getContentPane());//选择所要的图片
				if (i == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					imgFile1 = selectedFile.getPath();//得到文件的路径
				}

			}
		});

		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int i = fileChooser.showOpenDialog(getContentPane());
				if (i == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					imgFile2 = selectedFile.getPath();
				}

			}
		});
		final JPanel panel_1 = new JPanel();//上层面板
		panel_1.add(button1);//上层面板添加按钮1，2
		panel_1.add(button2);
		c.add(panel_1, BorderLayout.NORTH);
		
		c.add(new CanvasPanel(),BorderLayout.CENTER);//中层容器存放图片
		
		JTextArea area = new JTextArea();
		area.setRows(5);//文本框设置5行20列
		area.setColumns(20);
		JPanel panel_2 = new JPanel();
		panel_2.add(area);//下层面板放文本域和开始按钮
		
		JButton button3 = new JButton("开始识别");
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Facecompare fc = new Facecompare(imgFile1, imgFile2);
				compareHist = fc.getcompareHist();//获得相关系数
				if(compareHist!=0) {
					area.setText("对比度为" + compareHist + "\n");
					if (compareHist > 0.72) {
						area.append("人脸匹配");
					} else {
						area.append("人脸不匹配");
					}			
				}
			}
		});
		panel_2.add(button3);
		c.add(panel_2,BorderLayout.SOUTH);
		c.validate();//重新验证所有容器
	}
	
	class CanvasPanel extends Canvas{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 画图片
		 */
		public void paint(Graphics g) {
			if(imgFile1!=null) {
				Image img1 = new ImageIcon(imgFile1).getImage();//获得图片
				g.drawImage(img1, 50, 50, 300, 300, this);//画图片
			}
			if(imgFile2!=null) {
				Image img2 = new ImageIcon(imgFile2).getImage();
				g.drawImage(img2, 450, 50, 300, 300, this);
			}
		}
	}	
}
