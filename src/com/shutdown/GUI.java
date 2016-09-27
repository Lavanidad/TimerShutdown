package com.shutdown;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import java.net.URI;
import java.net.URISyntaxException;

public class GUI extends JFrame{
	private JMenuBar bar;//�˵���
	private JMenu menuB,menuA;//�˵�
	private JLabel NowTime,ShutTime,CdType,EndTime;
	private JButton ShutBt,CancelBt; 
	private JTextField ShowTime,PrintTime,CountTime;
	private JComboBox TimeType_Box,ShutType_Box;
	private boolean begin = false;
	private boolean stop = false;
	
	public GUI() throws Exception{
		this.setTitle("��ʱ�ػ�");
		this.setSize(320,280);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		/*
		 * �˵���
		 */
		bar = new JMenuBar();
		menuB = new JMenu("@DeepSpring��Ʒ");
		menuA = new JMenu("by��С");
		bar.add(menuB);
		bar.add(menuA);
		this.setJMenuBar(bar);
		/**
		 * ����
		 */
		NowTime = new JLabel("��ǰʱ�䣺");
		NowTime.setBounds(30, 20, 100, 30);
		add(NowTime);
		
		ShowTime = new JTextField();
		ShowTime.setBounds(100, 20, 175, 30);
		ShowTime.setEditable(false);
		add(ShowTime);
		
		ShutTime = new JLabel("����ʱ�䣺");
		ShutTime.setBounds(30, 70, 100, 30);
		add(ShutTime);
		
		PrintTime = new JTextField();
		PrintTime.setBounds(100, 70, 60, 30);
		add(PrintTime);
		
		TimeType_Box = new JComboBox();
		TimeType_Box.setModel(new javax.swing.DefaultComboBoxModel(
			new String[]{"��","ʱ"}	));
		TimeType_Box.setBounds(160,70,40,30);
		add(TimeType_Box);
		
		
		ShutBt = new JButton("ȷ��");
		ShutBt.setBounds(215, 70, 60, 30);
		add(ShutBt);
		ShutBt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ShutBt_Onclick(e);
			}
		});
		
		CdType = new JLabel("�ػ����ͣ�");
		CdType.setBounds(30, 120, 100, 30);
		add(CdType);
		
		ShutType_Box = new JComboBox();
		ShutType_Box.setModel(new javax.swing.DefaultComboBoxModel(
				new String[]{"�رռ����","���������","ע�������"}));
		ShutType_Box.setBounds(100,120,100,30); 
		add(ShutType_Box);
		
		CancelBt = new JButton("ȡ��");
		CancelBt.setBounds(215, 120, 60, 30);
		add(CancelBt);
		CancelBt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				CancelBt_Onclick(e);
			}
		});
		
		EndTime = new JLabel("�ػ�ʱ�䣺");
		EndTime.setBounds(30, 170, 100, 30);
		add(EndTime);
		
		CountTime = new JTextField();
		CountTime.setBounds(100, 170, 175, 30);
		CountTime.setEditable(false);
		add(CountTime);
	
		validate();	//��ʾ���
		
		Thread thread1 = new Thread(new Runnable(){
			public void run(){
				Calendar now =null;
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy��MM��dd��HHʱmm��ss��");
				while(true){
					try{
						Thread.sleep(1000);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
					now = Calendar.getInstance();
					String nowTime = sdf.format(now.getTime());
					ShowTime.setText(""+nowTime);
				}
			}
		});
		thread1.start();
	}
	/*
	 * �¼�����
	 */
	public void CancelBt_Onclick(ActionEvent e){
		if(begin==true){
			stop=true;
			CountTime.setText("��ȡ��");
			begin=false;
		}
	}
	public void ShutBt_Onclick(ActionEvent e){
		if(begin==true){
			JOptionPane.showMessageDialog(this, "�ػ��������ڽ�����");
			return;
		}
		final Calendar last = Calendar.getInstance();
		final SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy��MM��dd��HHʱmm��ss��");
		String ShutdownTime = PrintTime.getText();
		if(ShutdownTime==null || ShutdownTime.equals("")){
			JOptionPane.showMessageDialog(this, "���벻��Ϊ��");
			return;
		}
		final int time;
		try{
			time = Integer.parseInt(ShutdownTime);
		}catch(Exception ec){
			JOptionPane.showMessageDialog(this, "����ֻ��Ϊ����");
			return;
		}
		final String timeType = TimeType_Box.getSelectedItem().toString();
		final int shutType = ShutType_Box.getSelectedIndex();
		begin = true;
		new Thread(new Runnable(){
			public void run(){
				if(timeType.equals("��")){
					last.add(Calendar.MINUTE, time);
					CountTime.setText(""+sdf.format(last.getTime()));
					while(true){
						Calendar now = Calendar.getInstance();
						if(!now.before(last)){
							Runtime runtime = Runtime.getRuntime();
							try{
								switch(shutType){
									case 0:
										runtime.exec("C:\\Windows\\System32\\shutdown.exe /p");
										break;
									case 1:
										runtime.exec("C:\\Windows\\System32\\shutdown.exe /r");
										break;
									case 2:
										runtime.exec("C:\\Windows\\System32\\shutdown.exe /l");
										break;
								}//end switch
							}catch(Exception ec){
								ec.printStackTrace();
							}//end try-catch
							break;
						}//end if
						if(stop==true){
							stop=false;
							break;
						}
						try{
							Thread.sleep(100);
						}catch(InterruptedException ec){
							ec.printStackTrace();
						}
					}//end while
				}//end ��Ҫ��if
				else if(timeType.equals("ʱ")){
					last.add(Calendar.HOUR_OF_DAY, time);
					CountTime.setText(""+ sdf.format(last.getTime()));
					while (true) {
						Calendar now = Calendar.getInstance();
						if (!now.before(last)) {
							Runtime runtime = Runtime.getRuntime();
							try {
								switch (shutType) {
								case 0:
									runtime.exec("C:\\Windows\\System32\\shutdown.exe /p");
									break;
								case 1:
									runtime.exec("C:\\Windows\\System32\\shutdown.exe /r");
									break;
								case 2:
									runtime.exec("C:\\Windows\\System32\\shutdown.exe /l");
									break;
								}
							} catch (IOException ec) {
								ec.printStackTrace();
							}
							break;
						}
						if (stop == true) {
							stop = false;
							break;
						}
						try {
							Thread.sleep(100);
						} catch (InterruptedException ec) {
							ec.printStackTrace();
						}
					}
				}
			}//end run
		}).start();
	}

	/*
	 * main
	 */
	public static void main(String[] args){
		java.awt.EventQueue.invokeLater(new Runnable(){
			public void run(){
				try{
					new GUI().setVisible(true);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
}
