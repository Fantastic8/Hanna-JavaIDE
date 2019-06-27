package tools;

import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

public class Tool {
	//normal+over+pressed
	public static JButton addButton(JComponent com,int size,int x,int y,String tips,String normalpath,String overpath,String pressedpath,MouseAdapter ma)
	{
		JButton btn=new JButton();
		btn.addMouseListener(ma);
		//设置正常按钮图标
		btn.setIcon(new ImageIcon((new ImageIcon(Tool.class.getResource(normalpath))).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH)));
		//设置划过按钮图标
		btn.setRolloverIcon(new ImageIcon((new ImageIcon(Tool.class.getResource(overpath))).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH)));
		//设置按下按钮图标
		btn.setPressedIcon(new ImageIcon((new ImageIcon(Tool.class.getResource(pressedpath))).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH)));
		//设置提示
		btn.setToolTipText(tips);
		btn.setOpaque(false);
		btn.setContentAreaFilled(false);
		btn.setBorder(null);
		btn.setBounds(x,y, size, size);
		com.add(btn);
		return btn;
	}
	
	//normal+pressed
	public static JButton addButton(JComponent com,int size,int x,int y,String tips,String normalpath,String pressedpath,MouseAdapter ma)
	{
		JButton btn=new JButton();
		btn.addMouseListener(ma);
		//设置正常按钮图标
		btn.setIcon(new ImageIcon((new ImageIcon(Tool.class.getResource(normalpath))).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH)));
		//设置按下按钮图标
		btn.setPressedIcon(new ImageIcon((new ImageIcon(Tool.class.getResource(pressedpath))).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH)));
		//设置提示
		btn.setToolTipText(tips);
		btn.setOpaque(false);
		btn.setContentAreaFilled(false);
		btn.setBorder(null);
		btn.setBounds(x,y, size, size);
		com.add(btn);
		return btn;
	}
	
	//normal
	public static JButton addButton(JComponent com,int size,int x,int y,String tips,String normalpath,MouseAdapter ma)
	{
		JButton btn=new JButton();
		btn.addMouseListener(ma);
		//设置正常按钮图标
		btn.setIcon(new ImageIcon((new ImageIcon(Tool.class.getResource(normalpath))).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH)));
		//设置提示
		btn.setToolTipText(tips);
		btn.setOpaque(false);
		btn.setContentAreaFilled(false);
		btn.setBorder(null);
		btn.setBounds(x,y, size, size);
		com.add(btn);
		return btn;
	}
	
	//no size
	public static JButton addButton(JComponent com,int x,int y,String tips,String normalpath,String overpath,String pressedpath,MouseAdapter ma)
	{
		JButton btn=new JButton();
		btn.addMouseListener(ma);
		//设置正常按钮图标
		btn.setIcon(new ImageIcon(Tool.class.getResource(normalpath)));
		//设置划过按钮图标
		btn.setRolloverIcon(new ImageIcon(Tool.class.getResource(overpath)));
		//设置按下按钮图标
		btn.setPressedIcon(new ImageIcon(Tool.class.getResource(pressedpath)));
		//设置提示
		btn.setToolTipText(tips);
		btn.setOpaque(false);
		btn.setContentAreaFilled(false);
		btn.setBorder(null);
		btn.setLocation(x, y);
		com.add(btn);
		return btn;
	}
	
	//default location
	public static JButton addButton(JComponent com,int size,String tips,String normalpath,String overpath,String pressedpath,MouseAdapter ma)
	{
		JButton btn=new JButton();
		btn.addMouseListener(ma);
		//设置正常按钮图标
		btn.setIcon(new ImageIcon((new ImageIcon(Tool.class.getResource(normalpath))).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH)));
		//设置划过按钮图标
		btn.setRolloverIcon(new ImageIcon((new ImageIcon(Tool.class.getResource(overpath))).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH)));
		//设置按下按钮图标
		btn.setPressedIcon(new ImageIcon((new ImageIcon(Tool.class.getResource(pressedpath))).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH)));
		//设置提示
		btn.setToolTipText(tips);
		btn.setOpaque(false);
		btn.setContentAreaFilled(false);
		btn.setBorder(null);
		btn.setSize(size,size);
		com.add(btn);
		return btn;
	}
	
	//default location
	public static JButton addButton(JComponent com,int size,String tips,String normalpath,String overpath,String pressedpath,ActionListener al)
	{
		JButton btn=new JButton();
		btn.addActionListener(al);
		//设置正常按钮图标
		btn.setIcon(new ImageIcon((new ImageIcon(Tool.class.getResource(normalpath))).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH)));
		//设置划过按钮图标
		btn.setRolloverIcon(new ImageIcon((new ImageIcon(Tool.class.getResource(overpath))).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH)));
		//设置按下按钮图标
		btn.setPressedIcon(new ImageIcon((new ImageIcon(Tool.class.getResource(pressedpath))).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH)));
		//设置提示
		btn.setToolTipText(tips);
		btn.setOpaque(false);
		btn.setContentAreaFilled(false);
		btn.setBorder(null);
		btn.setSize(size,size);
		com.add(btn);
		return btn;
	}
}
