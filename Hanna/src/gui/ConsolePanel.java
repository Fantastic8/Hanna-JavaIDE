package gui;

import interfaces.PagePanel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import tools.HannaTheme;
import tools.Tool;

public class ConsolePanel extends PagePanel{
	//Pane声明
	private JTextPane showPane;
	private JTextField editField;
	private JPanel scrollPanel;
	private JScrollPane editscrollPane;
	//按钮声明
	private JButton stopBtn;
	//监听事件声明
	private KeyAdapter enterListener;
	//数据声明
	private LinkedList<String> buff;
	private Scanner scan;
	
	private Process process;
	private boolean isrunning;
	
	//reader
	private BufferedReader reader;
	private boolean isreader;
	public PipedOutputStream pos;
	
	public Thread readingThread;
	
	//writer
	private BufferedWriter writer;
	private boolean iswriter;
	private PipedInputStream pis;
	
	
	/**
	 * Create the panel.
	 */
	public ConsolePanel() {
		super();
		//设置EditPanel属性
		
		this.setLayout(new BorderLayout());
		
		//初始化scrollpanel
		scrollPanel=new JPanel();
		//设置布局为无边框
		scrollPanel.setLayout(new BorderLayout(0,0));
		//显示Pane
		showPane=new JTextPane();
		showPane.setEditable(false);
		showPane.addFocusListener(new FocusAdapter(){

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				//焦点转移
				editField.requestFocus();
			}
			
		});
		//设置主题
		showPane.setFont(HannaTheme.CONSOLEFONT);
		showPane.setBackground(HannaTheme.CONSOLEBG);
		showPane.setForeground(HannaTheme.CONSOLEFG);
		
		//编辑文本框初始化
		editField=new JTextField();
		editField.setFont(HannaTheme.CONSOLEFONT);
		editField.setBorder(null);
		editField.setLayout(new FlowLayout(FlowLayout.RIGHT));
		editField.setBorder(new EmptyBorder(10,10,10,10));
		editField.setBackground(HannaTheme.CONSOLEBG);
		editField.setForeground(HannaTheme.CONSOLEFG);
		
		//添加关闭进程按钮
		stopBtn=Tool.addButton(editField, HannaTheme.STOPBTNSIZE, "STOP Running", HannaTheme.STOPBTN_NORMAL_PATH,HannaTheme.STOPBTN_OVER_PATH,HannaTheme.STOPBTN_PRESSED_PATH, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//关闭进程
				destroy();
			}
			
		});
		stopBtn.setVisible(false);
		
		//添加清空按钮
		Tool.addButton(editField, HannaTheme.CLEARBTNSIZE, "Clear Console", HannaTheme.CLEARBTN_NORMAL_PATH,HannaTheme.CLEARBTN_OVER_PATH,HannaTheme.CLEARBTN_PRESSED_PATH, new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//清空控制台
				clear();
			}
			
		});
		
		//添加进scrollpanel
		scrollPanel.add(showPane,BorderLayout.CENTER);
		scrollPanel.add(editField,BorderLayout.SOUTH);
		
		
		//添加进scrollpane
		editscrollPane=new JScrollPane(scrollPanel);
		editscrollPane.getVerticalScrollBar().setUnitIncrement(20);
		this.add(editscrollPane,BorderLayout.CENTER);
		
		//数据初始化
		buff=new LinkedList<String>();
		isreader=false;
		iswriter=false;
		isrunning=false;
		
		//接管控制台输出
		takeoverReaderControl();
		
		//监听事件初始化
		enterListener=new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyChar()==KeyEvent.VK_ENTER)
				{
					//添加缓存
					buff.addLast(editField.getText());
					//清除jtextfield
					editField.setText("");
				}
			}				
		};
		
		//关闭控制台输入
		setEditable(false);
		
		//清空缓冲
		buff.clear();
	}
	
	public void appendText(String str)
	{
		showPane.setText(showPane.getText()+str);
//**将光标移至最后一行
		editscrollPane.getVerticalScrollBar().setValue(editscrollPane.getVerticalScrollBar().getMaximum());
	}
	
	public void appendlnText(String str)
	{
		showPane.setText(showPane.getText()+str+"\n");
//**将光标移至最后一行
		editscrollPane.getVerticalScrollBar().setValue(editscrollPane.getVerticalScrollBar().getMaximum());
	}
	
	//获取控制台权限
	public void takeoverControl()
	{
		takeoverWriterControl();
		takeoverReaderControl();
	}
	
	//接管控制台输入
	public void takeoverWriterControl()
	{
		if(iswriter)
		{
			retakeWriterControl();
			return;
		}
		//更新变量
		iswriter=true;
		
		//设置管道输入
		pis=new PipedInputStream();

		//接管系统输入
		System.setIn(pis);
		
		try {
			writer=new BufferedWriter(new OutputStreamWriter(new PipedOutputStream(pis)));			
		} catch (IOException e) {
			// TODO Auto-generated catch block
//appendlnText("Take Over System Input Failed.");
		}
	}
	
	//放弃控制台输入权限
	public void giveupWriterControl()
	{
		iswriter=false;
		try {
			if(pis!=null)
			{
				pis.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.setIn(null);
	}
	
	//重新获取控制台输入权限
	public void retakeWriterControl()
	{
		giveupWriterControl();
		takeoverWriterControl();
	}
	
	//接管控制台输出
	public void takeoverReaderControl() {
//appendlnText("TakeOverReaderControl isreader="+isreader);
		//已经接管
		if(isreader)
		{
			retakeReaderControl();
			return;
		}
		isreader=true;
		
		pos=new PipedOutputStream();
		
		//接管系统输出
		System.setOut(new PrintStream(pos,true));

		//接管错误消息
		System.setErr(new PrintStream(pos,true));
		
		try {
			reader=new BufferedReader(new InputStreamReader(new PipedInputStream(pos)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
//appendlnText("Take Over System Output Failed.");
		}
		
		
		if(readingThread==null||!readingThread.isAlive())
		{
			readingThread=new Thread(new Runnable(){
				//控制输出
				@Override
				public void run() {
					// TODO Auto-generated method stub
					String line="";
					try {
//appendlnText("Start Running Thread isreader="+isreader);
						while(isreader&&(line=reader.readLine())!=null)
						{
							appendlnText(line);
							Thread.sleep(20);
						}
//appendlnText("Reader Close");
					} catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
//appendlnText(e.getMessage()+"-! isreader="+isreader+" line="+line+" reader="+reader);
						retakeReaderControl();
//appendlnText("Reader Failed");
					}
				}});
			readingThread.start();
		}
	}
	
	//放弃控制台输出权限
	public void giveupReaderControl()
	{
//appendlnText("GiveUpReaderControl isreader="+isreader);
		isreader=false;
		try {
			if(pos!=null)
			{
				pos.flush();
				pos.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.setOut(null);
		System.setErr(null);
	}
	
	//重新获取控制台输出权限
	public void retakeReaderControl()
	{
//appendlnText("ReTakeOverReaderControl isreader="+isreader);
		giveupReaderControl();
		takeoverReaderControl();
	}
	
	//强制关闭进程
	public void destroy()
	{
		if(process==null)
		{
			isrunning=false;
			//关闭控制台输入
			setEditable(false);
			
			//隐藏按钮
			stopBtn.setVisible(false);
			return;
		}
		process.destroy();
	}
	
	//清空控制台
	public void clear()
	{
		showPane.setText("");
		editField.setText("");
		buff.clear();
	}
	
	//getter and setter
	public String getNextLine()
	{
		return buff.pollFirst();
	}
	
	public void setEditable(boolean b)
	{
		editField.setEnabled(b);
		buff.clear();
		if(b)
		{
			//不重复添加监听事件
			KeyListener[] listeners=editField.getKeyListeners();
			for(int i=0;i<listeners.length;i++)
			{
				if(listeners[i].equals(enterListener))
				{
					return;
				}
			}
			editField.addKeyListener(enterListener);
		}
		else
		{
			editField.removeKeyListener(enterListener);
		}
		
	}
	
	public String getText()
	{
		return showPane.getText();
	}
	
	public String getInputText()
	{
		return editField.getText();
	}
	
	public boolean isReading()
	{
		return isreader;
	}
	
	public boolean isRunning()
	{
		return isrunning;
	}
	
	public Process getProcess()
	{
		return process;
	}
	
	public void setProcess(Process process)
	{
		this.process=process;
		isrunning=true;
		
		//控制台输出
		setEditable(true);
		
		//显示按钮
		stopBtn.setVisible(true);
//appendlnText("Process Recieved");
		//等待线程结束并设置标志
		new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					ConsolePanel.this.process.waitFor();
//appendlnText("Process D");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "Programm Running Failed", "ERROR",JOptionPane.ERROR_MESSAGE);
				}
				isrunning=false;
				ConsolePanel.this.process=null;
				
				buff.addLast("\r\n");//添加结束符
				
				//关闭控制台输入并清空
				editField.setText("");
				setEditable(false);
				
				//隐藏按钮
				stopBtn.setVisible(false);
			}
			
		}).start();
	}
	
}
