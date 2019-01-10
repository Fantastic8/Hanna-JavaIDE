package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FindReplace extends JFrame {

	private JPanel contentPane;
	private JTextField Replace;
	private JTextField Find;
	private JButton btnReplace;
	
	private static JTextPane textPane;
	
	//Direction 变量
	private static int direction;	//direction 为1表示向上（backward）查找，为2表示向下（forward）查找

	/**
	 * Create the frame.
	 */
	public FindReplace(JTextPane textPane1) {
		
		this.textPane=textPane1;
		
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 384);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblFind = new JLabel("Find:");
		lblFind.setBounds(70, 37, 72, 18);
		contentPane.add(lblFind);
		
		JLabel lblReplaceWith = new JLabel("Replace with:");
		lblReplaceWith.setBounds(70, 68, 104, 18);
		contentPane.add(lblReplaceWith);
		
		Replace = new JTextField();
		Replace.setBounds(188, 65, 152, 24);
		contentPane.add(Replace);
		Replace.setColumns(10);
		
		Find = new JTextField();
		Find.setBounds(188, 34, 152, 24);
		contentPane.add(Find);
		Find.setColumns(10);
		
		
		
		
		
		
		//Replace
		btnReplace = new JButton("Replace");
		btnReplace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Replace
				btnReplace.setEnabled(false);			//设置Replace按钮不可点击
				
				String find_text=Find.getText();		//获取查找内容
				String replace_text=Replace.getText();	//获取替换内容
				
				textPane.replaceSelection(replace_text);//替换
			}
		});
		btnReplace.setBounds(219, 193, 121, 27);
		contentPane.add(btnReplace);
		
		//Replace All
		final JButton btnReplaceAll = new JButton("Replace All");
		btnReplaceAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Replace All
				btnReplaceAll.setEnabled(false);			//设置ReplaceAll按钮不可点击
				btnReplace.setEnabled(false);				//设置Replace按钮不可点击
				
				String find_text=Find.getText();			//获取查找内容
				int find_text_length=find_text.length();	//获取查找内容的长度
				String replace_text=Replace.getText();		//获取替换内容
				
				String text=new String();				
				text=textPane.getText();					//获取文本内容
				
				int next=0;									//从什么位置找,为0表示从文本开头开始找
				int start=0;								//从找到的第一个字符的位置，为0表示从文本开头开始找
				
				
				while(text.indexOf(find_text,next)!=-1){
					start=text.indexOf(find_text, next);			//查找从光标位置开始 向下查询第一个符合的内容
					textPane.setSelectionStart(start);				//选中开始位置到结束位置中间的数据
					textPane.setSelectionEnd(start+find_text_length);
					textPane.replaceSelection(replace_text);		//替换
					text=textPane.getText();						//重新获取文本内容
					next=start+find_text_length;;					//修改位置坐标
				}
				JOptionPane.showMessageDialog
                (null, "Replace all successfully", "Message", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnReplaceAll.setBounds(70, 243, 121, 27);
		contentPane.add(btnReplaceAll);
		
		btnReplace.setEnabled(false);			//设置Replace按钮不可点击
		btnReplaceAll.setEnabled(false);		//设置ReplaceAll按钮不可点击
		
		//Find
		JButton btnFind = new JButton("Find");
		btnFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Find
				String find_text=Find.getText();			//获取查找内容
				int find_text_length=find_text.length();	//获取查找内容的长度
				if(find_text.equals("")){					//判断查找框是否为空
					JOptionPane.showMessageDialog
                    (null, "Please enter the find content！", "Sorry", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				String text=new String();				
				text=textPane.getText();								//获取文本内容
				
				int next=textPane.getSelectionEnd();					//从什么位置找
				int start=textPane.getSelectionStart();					//从找到的第一个字符的位置
				
				if(direction==2){										//向下查找
					if(text.indexOf(find_text, next)!=-1){				//返回指定字符在此字符串中第一次出现处的索引,没有则会返回-1
						start=text.indexOf(find_text, next);			//查找从光标位置开始 向下查询第一个符合的内容
						textPane.setSelectionStart(start);				//选中开始位置到结束位置中间的数据
						textPane.setSelectionEnd(start+find_text_length);
						next=start+find_text_length;					//修改位置坐标
						btnReplace.setEnabled(true);					//设置Replace按钮可点击
						btnReplaceAll.setEnabled(true);					//设置ReplaceAll按钮可点击
					}else{ 												//没找到查找内容，提示错误对话框
	                     JOptionPane.showMessageDialog
	                      (null, "Very sorry! we cann't find it!", "Sorry", JOptionPane.ERROR_MESSAGE);     
	                     }
				}else{													//向上查找
					if(text.lastIndexOf(find_text,start-1)!=-1){	
		                int n = text.lastIndexOf(find_text,start-1);	//查找从光标位置开始 向上查询第一个符合的内容
		                textPane.setSelectionStart(n);					//选中开始位置到结束位置中间的数据
		                textPane.setSelectionEnd(n+find_text_length);
		                start = n;										//修改位置坐标
		                next = n + find_text_length;					//修改位置坐标
		                btnReplace.setEnabled(true);					//设置Replace按钮可点击
		                btnReplaceAll.setEnabled(true);					//设置ReplaceAll按钮可点击
		             }else{ 
		            	 JOptionPane.showMessageDialog
		                      (null, "Very sorry! we cann't find it!", "Sorry", JOptionPane.ERROR_MESSAGE);     
		            	 } 
				}
			}
		});
		
		btnFind.setBounds(70, 193, 121, 27);
		contentPane.add(btnFind);
		
		JLabel lblDirection = new JLabel("Direction");
		lblDirection.setBounds(140, 99, 72, 18);
		contentPane.add(lblDirection);
		
		
		
		
		
		//direction forward
		JRadioButton rdbtnForward = new JRadioButton("Backward");
		rdbtnForward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//direction forward
				direction=1;								//设置direction为1，即向上(backward)查找
			}
		});
		rdbtnForward.setBounds(126, 126, 157, 27);
		contentPane.add(rdbtnForward);
		
		//direction backward
		JRadioButton rdbtnBackward = new JRadioButton("Forward");
		rdbtnBackward.setSelected(true);					//默认选择rdbtnBackward
		direction=2;										//设置direction为2，即向下（forward）查找
		rdbtnBackward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//direction forward
				direction=2;								//设置direction为2，即向下（forward）查找
			}
		});
											
		rdbtnBackward.setBounds(126, 158, 157, 27);
		contentPane.add(rdbtnBackward);
		
		//创建Direction的ButtonGroup
		ButtonGroup direc_bg=new ButtonGroup();
		direc_bg.add(rdbtnForward);			//添加Backward单项选择按钮
		direc_bg.add(rdbtnBackward);		//添加Forward单项选择按钮
		
		
		//Close
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Close
				FindReplace.this.dispose();
			}
		});
		btnClose.setBounds(219, 243, 121, 27);
		contentPane.add(btnClose);
		
		
		FindReplace.this.setVisible(true);
		FindReplace.this.setResizable(false);
		
		
	}
}
