package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import data.OriPage;
import tools.Global;
import tools.HannaTheme;
import tools.Tool;

public class OrientationPanel extends JPanel {
//成员变量声明区域
	//Panel声明
	private JPanel switchPanel;
	private JPanel pagePanel;
	
	//Icon 声明
	private ImageIcon switcherselectedicon;
	private ImageIcon switcherunselectedicon;
	
	//layout声明
	private CardLayout cards;
	
	//数据声明
	private LinkedList<OriPage> pages;
	private OriPage selectedpage;

	/**
	 * Create the panel.
	 */
	public OrientationPanel() {
		super();
		//自我初始化
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setLayout(new BorderLayout(5, 0));
		
		//数据初始化
		pages=new LinkedList<OriPage>();
		
		//Panel初始化
		switchPanel=new JPanel();
		switchPanel.setLayout(new FlowLayout(FlowLayout.LEADING,5,0));
		this.setBorder(new EmptyBorder(0, 0, 0, 0));
		switchPanel.setBackground(HannaTheme.SWITCHERPANELBG);
		this.add(switchPanel,BorderLayout.NORTH);
		
		//icon 初始化
		switcherselectedicon=new ImageIcon(OrientationPanel.class.getResource(HannaTheme.SWITCHERSELECTED_PATH));
		switcherunselectedicon=new ImageIcon(OrientationPanel.class.getResource(HannaTheme.SWITCHERUNSELECTED_PATH));
		
		cards=new CardLayout();
		pagePanel=new JPanel();
		pagePanel.setLayout(cards);
		this.add(pagePanel,BorderLayout.CENTER);
	}
	
	//添加页面
	public void addPage(final OriPage op,int location)
	{
		//设置未被选中默认颜色
		if(selectedpage==null)
		{
			selectedpage=op;
			op.getPageLabel().setIcon(switcherselectedicon);
			op.getPageLabel().setForeground(HannaTheme.SWITCHERSELECTED_FONTC);
			selectEvent(op);
		}
		else
		{
			op.getPageLabel().setIcon(switcherunselectedicon);
			op.getPageLabel().setForeground(HannaTheme.SWITCHERUNSELECTED_FONTC);
		}
		
		//添加鼠标选中响应事件
		op.getPageLabel().addMouseListener(new MouseAdapter(){
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				//选中事件
				selectEvent(op);
			}
		});
		
		//添加关闭事件
		//添加关闭按钮
		op.getPageLabel().setLayout(new FlowLayout(FlowLayout.RIGHT));
		Tool.addButton(op.getPageLabel(), HannaTheme.CLOSEBTNSIZE,(int)(op.getPageLabel().getPreferredSize().getWidth()-25),5,"Close", HannaTheme.CLOSEBTN_NORMAL_PATH, HannaTheme.CLOSEBTN_OVER_PATH, HannaTheme.CLOSEBTN_PRESSED_PATH, new MouseAdapter(){

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				deleteEvent(op);
			}
		});
		//更新op位置
		op.setLocation(location);
		//将切换按钮添加至pages组
		pages.add(op);
		//将切换组件添加到切换panel中
		switchPanel.add(op.getPageLabel());
		//将页面组件添加到卡片布局中
		pagePanel.add(op.getPageLabel().getText(),op.getComp());
		
		//触发打开事件
		op.getComp().openEvent();
		
		//组件更新
		this.paintComponents(getGraphics());
	}
	
	public void showPage(final OriPage op,int location)
	{
		//设置未被选中默认颜色
		if(selectedpage==null)
		{
			selectedpage=op;
			op.getPageLabel().setIcon(switcherselectedicon);
			op.getPageLabel().setForeground(HannaTheme.SWITCHERSELECTED_FONTC);
			selectEvent(op);
		}
		else
		{
			op.getPageLabel().setIcon(switcherunselectedicon);
			op.getPageLabel().setForeground(HannaTheme.SWITCHERUNSELECTED_FONTC);
		}
		
		op.setLocation(location);
		//将切换按钮添加至pages组
		pages.add(op);
		//将切换组件添加到切换panel中
		switchPanel.add(op.getPageLabel());
		//将页面组件添加到卡片布局中
		pagePanel.add(op.getPageLabel().getText(),op.getComp());
		
		//触发打开事件
		op.getComp().openEvent();
		
		//组件更新
		this.paintComponents(getGraphics());
	}
	
	//删除页面事件
	public void deleteEvent(OriPage page)
	{		
		//判断是否为所选组件
		if(selectedpage==page)
		{
			//默认选中第一个
			if(pages.size()-1>0)
			{
				selectEvent(pages.get(0));
			}
			else
			{
				selectedpage=null;
			}
		}
		
		//更新数据
		page.setLocation(-1);

		//删除组件
		pages.remove(page);
		switchPanel.remove(page.getPageLabel());
		pagePanel.remove(page.getComp());
		
		//没有页面就自动删除
		if(pages.size()<=0)
		{
			Global.HANNA.getContentPane().remove(OrientationPanel.this);
			Global.HANNA.repaint();
		}
		
		//触发关闭事件
		page.getComp().closeEvent();
		
		//组件更新
		this.paintComponents(getGraphics());
	}
	
	//显示页面
	public void showPage(String title)
	{
		cards.show(pagePanel, title);
		repaint();
	}
	
	//页面选中事件
	public void selectEvent(OriPage page)
	{		
		//设置未被选中
		if(selectedpage!=null)
		{
			selectedpage.getPageLabel().setIcon(switcherunselectedicon);
			selectedpage.getPageLabel().setForeground(HannaTheme.SWITCHERUNSELECTED_FONTC);
		}
		//设置被选中
		selectedpage=page;
		page.getPageLabel().setIcon(switcherselectedicon);
		page.getPageLabel().setForeground(HannaTheme.SWITCHERSELECTED_FONTC);
		if(page.getPagetype()==Global.TYPECODE&&!((FilePanel)page.getComp()).issaved()&&page.getPageLabel().getText().indexOf('*')==0)
		{
			//去掉*
			showPage(page.getPageLabel().getText().substring(1,page.getPageLabel().getText().length()));
		}
		showPage(page.getPageLabel().getText());
	}
	
	//通过title选定页面
	public void selectPage(String title)
	{
		Iterator itr=pages.iterator();
		OriPage page;
		while(itr.hasNext())
		{
			page=(OriPage)itr.next();
			if(page.getPageLabel().getText().equals(title))
			{
				selectEvent(page);
				return;
			}
		}
	}
	
	//重写绘制组件函数
	@Override
	public void paintComponents(Graphics g)
	{
		super.paintComponents(g);
		
		Iterator itr=pages.iterator();
		OriPage page;
		switchPanel.removeAll();
		while(itr.hasNext())
		{
			page=(OriPage)itr.next();
			switchPanel.add(page.getPageLabel());
		}
	}

	//getter and setter
	public LinkedList<OriPage> getPages() {
		return pages;
	}

	public OriPage getSelectedpage() {
		return selectedpage;
	}
}
