package gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;

import javax.swing.JTextPane;

import tools.HannaTheme;

public class SelectTextPane extends JTextPane implements Runnable {
	
	private LineNumberReader linereader;
	private int height;
	
	public SelectTextPane()
	{
		super();
		new Thread(SelectTextPane.this).start();
	}
	
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		
		//画出所选行
		g.setColor(HannaTheme.LINESELECTEDCOLOR);
		g.fillRect(2, 3+height, (int)SelectTextPane.this.getPreferredSize().getWidth(), (int)getFontHeight());
		
		repaint();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			int currentline=0;
			while(true)
			{
				while(SelectTextPane.this.isShowing())
				{
					linereader=new LineNumberReader(new StringReader(SelectTextPane.this.getText()));
					linereader.skip(SelectTextPane.this.getCaretPosition());
					currentline=linereader.getLineNumber();
					height=(int) (currentline*getFontHeight());
				}
				Thread.sleep(100);
			}
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//获取字体高度
	public double getFontHeight() {  
        // 设置大字体  
        FontRenderContext context = ((Graphics2D)getGraphics()).getFontRenderContext();  
        // 获取字体的像素范围对象  
        Rectangle2D stringBounds = SelectTextPane.this.getFont().getStringBounds("W", context);  
        double fontWidth = stringBounds.getHeight();
        return fontWidth+1.61;
    }  
}
