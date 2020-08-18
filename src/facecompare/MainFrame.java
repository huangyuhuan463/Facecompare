package facecompare;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;

/**
 * ������
 * @author ���ڻ�
 *
 */
public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String imgFile1;//ѡ��ͼƬ������
	public String imgFile2;
	public double compareHist;// CORREL ���ϵ��

	/**
	 * ������
	 * @param args
	 */
	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.setVisible(true);//��ʾ����
	}

	/**
	 * ���캯��
	 */
	public MainFrame() {
		super();
		setTitle("ͼƬѡ����");//����ı���
		setBounds(100, 100, 800, 600);//����λ�úʹ�С
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//����Ĺرյ�ʱ
		Container c = getContentPane();//��ȡ����

		JButton button1 = new JButton("ѡ��ͼƬ1");//��ť1
		JButton button2 = new JButton("ѡ��ͼƬ2");//��ť2

		button1.addActionListener(new ActionListener() {//��Ӽ���
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int i = fileChooser.showOpenDialog(getContentPane());//ѡ����Ҫ��ͼƬ
				if (i == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					imgFile1 = selectedFile.getPath();//�õ��ļ���·��
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
		final JPanel panel_1 = new JPanel();//�ϲ����
		panel_1.add(button1);//�ϲ������Ӱ�ť1��2
		panel_1.add(button2);
		c.add(panel_1, BorderLayout.NORTH);
		
		c.add(new CanvasPanel(),BorderLayout.CENTER);//�в��������ͼƬ
		
		JTextArea area = new JTextArea();
		area.setRows(5);//�ı�������5��20��
		area.setColumns(20);
		JPanel panel_2 = new JPanel();
		panel_2.add(area);//�²������ı���Ϳ�ʼ��ť
		
		JButton button3 = new JButton("��ʼʶ��");
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Facecompare fc = new Facecompare(imgFile1, imgFile2);
				compareHist = fc.getcompareHist();//������ϵ��
				if(compareHist!=0) {
					area.setText("�Աȶ�Ϊ" + compareHist + "\n");
					if (compareHist > 0.72) {
						area.append("����ƥ��");
					} else {
						area.append("������ƥ��");
					}			
				}
			}
		});
		panel_2.add(button3);
		c.add(panel_2,BorderLayout.SOUTH);
		c.validate();//������֤��������
	}
	
	class CanvasPanel extends Canvas{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * ��ͼƬ
		 */
		public void paint(Graphics g) {
			if(imgFile1!=null) {
				Image img1 = new ImageIcon(imgFile1).getImage();//���ͼƬ
				g.drawImage(img1, 50, 50, 300, 300, this);//��ͼƬ
			}
			if(imgFile2!=null) {
				Image img2 = new ImageIcon(imgFile2).getImage();
				g.drawImage(img2, 450, 50, 300, 300, this);
			}
		}
	}	
}
