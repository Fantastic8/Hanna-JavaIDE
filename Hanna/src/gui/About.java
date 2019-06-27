package gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import tools.HannaTheme;

public class About extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public About() {
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTextPane textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setFont(HannaTheme.FILEFONT);
		JScrollPane scrollPane = new JScrollPane(textPane);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		textPane.setText("Hanna JavaIDE 1.0是基于MyEclipse2014来设计开发的。Hanna的定位是轻量级的IDE，是一款独立的软件，可脱机使用，主要功能是完成程序的编写、调试、编译及运行，除此之外，还有一些基本的文档编辑功能。Hanna的界面简洁，操作简单，没有复杂的步骤，易于上手。而且作为一款开源的自定义IDE软件，使用人可以根据自己的需求，在其基础上进行修改，方便快捷。\r\nCopyright  2017, HANNA, All Rights Reserve");
	}

}
