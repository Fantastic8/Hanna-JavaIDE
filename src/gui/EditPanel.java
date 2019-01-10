package gui;

import interfaces.PagePanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.SequenceInputStream;
import java.io.StringReader;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class EditPanel extends PagePanel{
	//Pane声明
	private JTextPane editPane;
	private JScrollPane editscrollPane;
	//数据声明
	
	
	/**
	 * Create the panel.
	 */
	public EditPanel() {
		super();
		//设置EditPanel属性
		editscrollPane=new JScrollPane();
		this.setLayout(new BorderLayout());
		//编辑Pane
		editPane=new JTextPane();
		editscrollPane=new JScrollPane(editPane);
		this.add(editscrollPane,BorderLayout.CENTER);
		
		//数据初始化
	}
	
	public void appendText(String str)
	{
		editPane.setText(editPane.getText()+str);
	}
	
	public void appendlnText(String str)
	{
		editPane.setText(editPane.getText()+str+"\r\n");
		//换掉换行符
		editPane.setCaretPosition(editPane.getText().replaceAll("\r","").length());
	}
	
	//getter and setter
	
	public void setEditable(boolean b)
	{
		editPane.setEditable(b);
	}
	
	public void setText(String str)
	{
		editPane.setText(str);
	}
	
	public String getText()
	{
		return editPane.getText();
	}

	public JTextPane getEditPane() {
		return editPane;
	}

	public JScrollPane getEditscrollPane() {
		return editscrollPane;
	}
}
