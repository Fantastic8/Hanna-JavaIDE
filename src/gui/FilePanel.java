package gui;

import interfaces.PagePanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import tools.HannaTheme;

public class FilePanel extends PagePanel implements Runnable{
	//Pane声明
	private JTextPane editPane;
//	private SelectTextPane editPane;
	private JTextPane lineNumber;
	private JPanel scrollPanel;
	private JScrollPane editscrollPane;
	//数据声明
	private LineNumberReader linereader;
	private File sourcefile;
	private boolean isdispose;
	private boolean issaved;
	private boolean iscompiled;
	
	/**
	 * Create the panel.
	 */
	public FilePanel(File file) {
		super();
		//设置panel属性
		scrollPanel=new JPanel();
		scrollPanel.setBorder(new EmptyBorder(0,0,0,0));
		scrollPanel.setLayout(new BorderLayout(0, 0));
		this.setLayout(new BorderLayout());
		editscrollPane=new JScrollPane(scrollPanel);
		editscrollPane.getVerticalScrollBar().setUnitIncrement(20);
		this.add(editscrollPane,BorderLayout.CENTER);
		
		//显示行号标签
		lineNumber=new JTextPane();
		lineNumber.setOpaque(true);
		lineNumber.setFont(HannaTheme.FILEFONT);
		lineNumber.setBackground(HannaTheme.LINENUMBG);
		lineNumber.setForeground(HannaTheme.LINENUMCOLOR);
		lineNumber.setEditable(false);
		lineNumber.setLayout(new BorderLayout());
		scrollPanel.add(lineNumber,BorderLayout.WEST);
		lineNumber.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()>=2)
				{
					lineNumber.setVisible(false);
				}
			}
		});
		//编辑Pane
		editPane=new JTextPane();
		editPane.setBackground(HannaTheme.TEXTBG);
		editPane.setFont(HannaTheme.FILEFONT);
		scrollPanel.add(editPane,BorderLayout.CENTER);
		
		//数据初始化
		sourcefile=file;
		isdispose=false;
		issaved=true;
		iscompiled=false;
		
		//开启线程显示行数
		new Thread(FilePanel.this).start();
	}
	
	public void appendText(String str)
	{
		editPane.setText(editPane.getText()+str);
	}
	
	public void appendlnText(String str)
	{
		editPane.setText(editPane.getText()+str+"\n");
	}
	
	public void loadFile()
	{
		if(sourcefile==null)
		{
			JOptionPane.showMessageDialog(null, "File is not readable!", "ERROR",JOptionPane.ERROR_MESSAGE);
			return;
		}
		//get message
		try {
			BufferedReader buff=new BufferedReader(new FileReader(sourcefile));
			editPane.setText("");//清空面板
			String line;
			while((line=buff.readLine())!=null)
			{
				appendlnText(line);
			}
			buff.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "File "+sourcefile.getName()+" is unavaliable", "ERROR",JOptionPane.ERROR_MESSAGE);
		}
		//设置光标起始位置
		editPane.setCaretPosition(0);
		
		//为文本添加监听事件
		editPane.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void changedUpdate(DocumentEvent e) {}

			@Override
			public void insertUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				if(issaved)
				{
					issaved=false;
					iscompiled=false;
					FilePanel.this.getTitle().setText("*"+FilePanel.this.getTitle().getText());
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				if(issaved)
				{
					issaved=false;
					iscompiled=false;
					FilePanel.this.getTitle().setText("*"+FilePanel.this.getTitle().getText());
				}
			}
			
		});
	}

	//显示行数线程
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		//previousline 为之前所读取到的最大行数, currentline为当前嗦读取到的最大行数
		int previousline=1,currentline=1;
		//获得最大行数的位数
		int num=0;
		//每一行的行号
		String li="";
		//所有行号
		String lines="";
		//isdispose 是文件关闭标志，当文件被关闭时线程也会结束
		while(!isdispose)
		{
			try {
				//减缓刷新速度
				Thread.sleep(50);
				//新建行号读取类
				linereader=new LineNumberReader(new StringReader(editPane.getText()));
				//将光标移至文档最后
				linereader.skip(Long.MAX_VALUE);
				//读取最大行号并+1，因为该类返回值下标是从0开始的
				currentline=linereader.getLineNumber()+1;
				//得到最大行号的位数
				num=String.valueOf(currentline).length()-1;
				//只有当行号与之前检查时的行号不一样时才开始更新，这样可以减缓刷新频率
				if(currentline!=previousline)
				{
					//开始更新
					lines="";
					//给每一行赋值行号
					for(int i=1;i<=currentline;i++)
					{
						//得到行号字符串
						li=String.valueOf(i);
						
						//输出空格将行号对齐
						for(int j=String.valueOf(i).length();j<=num;j++)
						{
							li=" "+li;
						}
						lines=lines+li+"\n";
					}
					
					//添加行号到行号面板
					lineNumber.setText(lines);
					//更新行数变量
					previousline=currentline;
				}
			} catch (InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//打开触发事件
	@Override
	public void openEvent()
	{
		loadFile();
	}
	
	//关闭触发事件
	@Override
	public void closeEvent()
	{
		//是否保存
		if(!issaved)
		{
			//弹出窗口保存
			if(JOptionPane.showConfirmDialog(null, "File "+sourcefile.getName()+" hasn't saved yet. Do you want to save it now?", "WARNING",JOptionPane.YES_NO_OPTION)==0)//判断保存
			{
				save();
			}
		}
		isdispose=true;
		
	}
	
	public boolean save()
	{
		try {
			FileOutputStream sa=new FileOutputStream(sourcefile);
			sa.write(editPane.getText().getBytes());
			sa.flush();
			sa.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//提示保存错误
			JOptionPane.showMessageDialog(null, "File Save Failed!", "ERROR",JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if(!issaved)
		{
			issaved=true;
			String t=FilePanel.this.getTitle().getText();
			if(t.charAt(0)=='*')
			{
				FilePanel.this.getTitle().setText(t.substring(1,t.length()));
			}
		}
		return true;
	}
	
	public void saveas(String path)
	{
		File f=new File(path);
		try {
			FileOutputStream sa=new FileOutputStream(f);
			sa.write(editPane.getText().getBytes());
			sa.flush();
			sa.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//提示保存错误
			JOptionPane.showMessageDialog(null, "File Save Failed!", "ERROR",JOptionPane.ERROR_MESSAGE);
			return;
		}
		sourcefile=f;
		issaved=true;
		FilePanel.this.getTitle().setText(sourcefile.getName());
	}
	
	public void showLineNumber(boolean b)
	{
		lineNumber.setVisible(b);
	}

	//getter and setter
	public JTextPane getEditPane() {
		return editPane;
	}
	
	public File getSourcefile() {
		return sourcefile;
	}

	public boolean isshowline()
	{
		return lineNumber.isVisible();
	}
	
	public boolean issaved()
	{
		return issaved;
	}
	
	public boolean iscompiled()
	{
		return iscompiled;
	}
	
	public void setcompiled(boolean b)
	{
		iscompiled=b;
	}
}
