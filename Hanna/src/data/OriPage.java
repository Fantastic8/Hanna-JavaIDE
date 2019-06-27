package data;


import interfaces.PagePanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import tools.HannaTheme;

public class OriPage {
	private JLabel pageLabel;
	private PagePanel comp;
	private int pagetype;
	private int location;
	
	public OriPage(String Name,PagePanel obj,int pagetype,int location)
	{
		pageLabel=new JLabel(Name){
			private Icon BGIcon;
			private String Title;
			private int height;
			private Color fontcolor=Color.BLACK;
			
			@Override
			public void setText(String text)
			{
				super.setText(text);
				Title=text;
				//动态改变label宽度
				this.setPreferredSize(new Dimension(((new JLabel()).getFontMetrics(HannaTheme.SWITCHERFONT)).stringWidth(text)+40,((new JLabel()).getFontMetrics(HannaTheme.SWITCHERFONT).getHeight()+10)));
				repaint();
			}
			
			@Override
			public void setIcon(Icon icon)
			{
				this.BGIcon=icon;
				repaint();
			}
			
			@Override
			public void setForeground(Color fg)
			{
				super.setForeground(fg);
				fontcolor=fg;
			}
			
			@Override
			public void setPreferredSize(Dimension preferredSize)
			{
				super.setPreferredSize(preferredSize);
				height=preferredSize.height;
			}
			
			@Override
			public void paintComponent(Graphics g)
			{
				
				super.paintComponent(g);
				this.BGIcon.paintIcon(this, g, 0, 0);
				g.setColor(fontcolor);
				g.setFont(HannaTheme.SWITCHERFONT);
				g.drawString(Title, 5, 28);
				g.setColor(Color.BLACK);
			}
		};
		pageLabel.setFont(HannaTheme.SWITCHERFONT);
		pageLabel.setOpaque(true);
		pageLabel.setHorizontalAlignment(0);
		comp=obj;
		comp.setTitle(pageLabel);
		this.pagetype=pagetype;
		this.location=location;
	}
	
	
	//getter and setter
	public JLabel getPageLabel() {
		return pageLabel;
	}
	public void setPageLabel(JLabel pageLabel) {
		this.pageLabel = pageLabel;
		pageLabel.setPreferredSize(new Dimension((int)(pageLabel.getPreferredSize().getWidth()+50),(int)(pageLabel.getPreferredSize().getHeight()+20)));
	}
	public PagePanel getComp() {
		return comp;
	}
	public void setComp(PagePanel comp) {
		this.comp = comp;
	}

	public int getPagetype() {
		return pagetype;
	}


	public int getLocation() {
		return location;
	}


	public void setLocation(int location) {
		this.location = location;
	}
}
