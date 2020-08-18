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
 * ������
 * 
 * @author ���ڻ�
 *
 */
public class Mainframe extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String imgFile1;// ѡ���ͼƬ
	private String imgFile2;

	private Mat grayimage1;// ͼƬ�ҶȻ����ͼ��
	private Mat grayimage2;

	private Mat cutimage1;// ͼƬ�ҶȻ���ͼ���ͼ��
	private Mat cutimage2;

	private double res;// �ԱȽ��
	
	private static JButton buttonopen;
	private static JButton buttongray;
	private static JButton buttonDetction;
	private static JButton buttoncut;
	private static JButton buttonhist;
	private static JButton buttonres;
	

	public static void main(String[] args) {
		Mainframe frame = new Mainframe();
		frame.setVisible(true);//���ô�������ɼ�
	}

	public Mainframe() {
		super();
		Container c = getContentPane();
		setTitle("����ƥ��ȼ��С����");// ����ı���
		setBounds(100, 100, 600, 300);// ����λ�úʹ�С
		setLayout(new BorderLayout());//�߽粼��
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// ����Ĺرյ�ʱ	
		
		JPanel panel_1 = new JPanel();
		JPanel panel_2 = new JPanel();
		JPanel panel_3 = new JPanel();
		panel_3.setLayout(new GridLayout(2,1));
		
		JLabel jl1 = new JLabel("��ӭʹ������ƥ��ȼ��С����!",JLabel.CENTER);
		jl1.setFont(new Font("����", Font.BOLD, 28));
		jl1.setForeground(Color.MAGENTA);
		
		JLabel jl2 = new JLabel("�밴�հ�ť˳�����������������",JLabel.CENTER);
		jl2.setFont(new Font("����", Font.BOLD, 16));
		jl2.setForeground(Color.BLACK);

		JTextArea area = new JTextArea();
		area.setRows(5);// �ı�������5��20��
		area.setColumns(20);

		buttonopen = new JButton("ѡ��ͼƬ");
		buttonopen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buttonEnable();
				area.setText("");
				
				JFileChooser fileChooser = new JFileChooser();
				int i = fileChooser.showOpenDialog(c);// ѡ����Ҫ��ͼƬ
				if (i == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					imgFile1 = selectedFile.getPath();// �õ��ļ���·��
				}

				int j = fileChooser.showOpenDialog(c);// ѡ����Ҫ��ͼƬ
				if (j == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					imgFile2 = selectedFile.getPath();// �õ��ļ���·��
				}
				new Chooseimage(imgFile1, imgFile2);//�����Ӵ�����ʾͼƬ
				buttongray.setEnabled(true);
			}
		});

		buttongray = new JButton("���лҶȻ�����");
		buttongray.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ToGray tg = new ToGray(imgFile1, imgFile2);
					grayimage1 = tg.getgrayimage1();
					grayimage2 = tg.getgrayimage2();
					buttonDetction.setEnabled(true);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "��û��ѡ��ͼƬ����ѡ��!");
					
				}
			}
		});

		buttonDetction = new JButton("��������ʶ��");
		buttonDetction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Facedetection fd = new Facedetection(grayimage1, grayimage2);
				cutimage1 = fd.getcutimage1();//���زü����ͼ��
				cutimage2 = fd.getcutimage2();
				buttoncut.setEnabled(true);
			}
		});

		buttoncut = new JButton("��ͼ");
		buttoncut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImageViewer imageview = new ImageViewer(cutimage1, "�ü����ͼ��1");
				imageview.imshow();//��ʾͼ��
				ImageViewer imageview2 = new ImageViewer(cutimage2, "�ü����ͼ��2");
				imageview2.imshow();
				buttonhist.setEnabled(true);
			}
		});

		buttonhist = new JButton("ͼ��ֱ��ͼ");
		buttonhist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BufferedImage bf1 = Mat2BufImg(cutimage1, ".jpg");// matתBufferimage
				BufferedImage bf2 = Mat2BufImg(cutimage2, ".jpg");
				new ImageAnalyse(bf1);//����ֱ��ͼ
				new ImageAnalyse(bf2);
				buttonres.setEnabled(true);
			}
		});

		buttonres = new JButton("�����");
		buttonres.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FinalResult fr = new FinalResult(cutimage1, cutimage2);
				res = fr.getcompareHist();
				if (res != 0) {
					area.setText("�Աȶ�Ϊ" + res + "\n");
					if (res > 0.72) {
						area.append("����ƥ��!");
					} else {
						area.append("������ƥ��!");
					}
				}
				JOptionPane.showMessageDialog(null, "��ϲ�㣡����ƥ���Ѿ����\n" +
				"��������һ�β鿴�м䲽���������ѡ��ͼƬ");
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