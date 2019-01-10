package gui;

import interfaces.FileEvents;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.*;

import data.FileNode;
import gui.TreeRenderer;
/**
 * 自定义的JTree控件，主要是用来建立文件树
 */
public class FileTree extends JTree {
	JTree jtree;
	DefaultMutableTreeNode root;
	private DefaultTreeModel dt;
	
	private FileEvents FE;
	/**
	 * FileTree的容器
	 */
	public JScrollPane spanel;
	/**
	 * FileTree的构造函数
	 * @param dir String，根目录的路径
	 * @param panel FileTree的容器
	 * @param isRootShow 是否需要显示根节点
	 */
	public FileTree(String dir,final JScrollPane panel,final boolean isRootShow,FileEvents fe){
		FE=fe;
		//创建该路径的File对象
		File file = new File(dir);
		root=new DefaultMutableTreeNode(new FileNode(file));
		//保存容器信息，方便别的控件进行相应操作
		spanel = panel;
		//创建以输入文件夹为根节点的文件树
		createTree(root,file);
		//创建JTree
		dt=new DefaultTreeModel(root);
		jtree=new JTree(dt);
		//Sets the TreeCellRenderer that will be used to draw each cell. 
		jtree.setCellRenderer(new TreeRenderer());
		//如果是不需要显示根节点的就隐藏文件树的根节点
		if(!isRootShow){
			jtree.setRootVisible(false);
		}
		//添加到容器中
		panel.setViewportView(jtree);
		
		//添加文件树的鼠标监听事件
		jtree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//给鼠标双击添加动作
				if(e.getClickCount()==2){
					//获取鼠标点击的位置的TreePath
					TreePath path = jtree.getPathForLocation(e.getX(), e.getY()); // 关键是这个方法的使用
				 	//path是null说明点击的位置不是在文件树上
					if (path == null) {
				 		return;
				 	}
					//获得最后被选择（点击）的节点DefaultMutableTreeNode
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) jtree.getLastSelectedPathComponent();
					//获得该节点的文件File
					FileNode f = (FileNode)node.getUserObject();
					//如果是一个文件，就把他显示到文本显示区（没有进行具体文件类型的判断）
					if(f.file.isFile()){
						//打开文件事件
						FE.FileOpen(f.file);
					}else{
						//如果是文件夹，并且是不需要显示根节点的文件树
						if(!isRootShow){
							//就改变该文件树的根节点，变成只显示该目录下的文件的形式
							dt.setRoot(node);
						}
					}
				}
				super.mouseClicked(e);
			}
			//重载鼠标按下事件
			@Override
			public void mousePressed(MouseEvent e) {
				//获取鼠标点击的位置的TreePath
				final TreePath path = jtree.getPathForLocation(e.getX(), e.getY()); // 关键是这个方法的使用
				if(path==null){
					if (e.getButton() == 3) {
				 		//新建右键弹出的菜单对象
				 		JPopupMenu popupMenu = new JPopupMenu();
				 		//新建刷新菜单项
						JMenuItem menuItem1 = new JMenuItem("Refresh");
						//设置刷新菜单项的触发动作
						menuItem1.addActionListener(new ActionListener() {
							//刷新操作，刷新鼠标点击文件夹下面的文件信息
							@Override
							public void actionPerformed(ActionEvent e) {
						 		//重建该节点下的文件树
								root.removeAllChildren();
								createTree(root, ((FileNode)root.getUserObject()).file);
								((FileNode)root.getUserObject()).isInit = false;
								// 通知模型节点发生变化
								DefaultTreeModel treeModel1 = (DefaultTreeModel) getModel();
								treeModel1.nodeStructureChanged(root);
								//使用DefaultTreeModel的reload方法进行刷新
								dt.reload();
							}
						});
						//把建好的菜单项添加到弹出菜单中
						popupMenu.add(menuItem1);
						//在右击的位置显示该弹出菜单
						popupMenu.show(jtree, e.getX(), e.getY());
					}
				}else{
					//设置该节点的路径为选中状态，理论上可是去掉（未测试过）
				 	jtree.setSelectionPath(path);
				 	//获得最后被选择（点击）的节点DefaultMutableTreeNode
				 	final DefaultMutableTreeNode node = (DefaultMutableTreeNode) jtree.getLastSelectedPathComponent();
				 	//获得该节点的文件File
				 	final FileNode f = (FileNode)node.getUserObject();
					//设置鼠标右击的动作
				 	if (e.getButton() == 3) {
				 		//新建右键弹出的菜单对象
				 		JPopupMenu popupMenu = new JPopupMenu();
				 		//新建刷新菜单项
						JMenuItem menuItem1 = new JMenuItem("Refresh");
						//设置刷新菜单项的触发动作
						menuItem1.addActionListener(new ActionListener() {
							//刷新操作，刷新鼠标点击文件夹下面的文件信息
							@Override
							public void actionPerformed(ActionEvent e) {
						 		//重建该节点下的文件树
								node.removeAllChildren();
								createTree(node, f.file);
								f.isInit = false;
								//使用DefaultTreeModel的reload方法进行刷新
								dt.reload(node);
							}
						});
						//新建删除菜单项（还没有设置删除前的提醒操作）
						JMenuItem menuItem2 = new JMenuItem("Delete");
						//删除菜单触发事件
						menuItem2.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								int res = JOptionPane.showConfirmDialog(null,
										"Are you sure you want to delete this file?", "Warning",
										JOptionPane.YES_NO_OPTION);
								if (res == JOptionPane.YES_OPTION) {
									//如果该节点是文件就进行删除操作
									if(f.file.isFile()){
										f.file.delete();
										node.removeFromParent();
										// 通知模型节点发生变化
										DefaultTreeModel treeModel1 = (DefaultTreeModel) getModel();
										treeModel1.nodeStructureChanged(node);
										dt.reload(node);
									}else{
										deleteDir(f.file);
									}
								} else {
									return;
								}
								//TODO 进行文件夹的删除操作
							}
						});
						JMenuItem menuItem3 = new JMenuItem("New Package");
						menuItem3.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								if(f.file.isDirectory()){
									String inputValue = JOptionPane.showInputDialog("Package Name:");
									if(inputValue!=null){
										inputValue.trim();
										if(!inputValue.isEmpty()){
										String dir = f.file.getAbsolutePath();
										inputValue += '.';
											for(String str:inputValue.split("\\.")){
												if(!dir.endsWith(File.separator)){
													dir += File.separator;
												}
												dir += str;
												File newFile = new File(dir);
												if(!newFile.exists()){
													if(!newFile.mkdirs()){
														JOptionPane.showMessageDialog(panel, "Fail,please check the package name.");;
													}
												}
											}
										}
									}
								}
							}
						});
						//把建好的菜单项添加到弹出菜单中
						popupMenu.add(menuItem1);
						popupMenu.add(menuItem2);
						if(f.file.isDirectory()){
							popupMenu.add(menuItem3);
						}
						//在右击的位置显示该弹出菜单
						popupMenu.show(jtree, e.getX(), e.getY());
				 	}
				}
				super.mousePressed(e);
			}
		});
		jtree.addTreeWillExpandListener(new TreeWillExpandListener() {
			
			@Override
			public void treeWillExpand(TreeExpansionEvent event)
					throws ExpandVetoException {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) event
						.getPath().getLastPathComponent();
				FileNode fileNode = (FileNode) node.getUserObject();
				if(!fileNode.isInit){
					node.removeAllChildren();
					createTree(node,fileNode.file);
					// 通知模型节点发生变化
					DefaultTreeModel treeModel1 = (DefaultTreeModel) getModel();
					treeModel1.nodeStructureChanged(node);
				}
				// 更改标识，避免重复加载
				fileNode.isInit = true;
			}

			@Override
			public void treeWillCollapse(TreeExpansionEvent event)
					throws ExpandVetoException {
				
			}
		});
		
	}
	
	/*
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
	
	/*
	 * 创建文件树，输入根节点和根节点的File变量，就可以创建以该节点为根节点的文件树
	 */
	private void createTree(DefaultMutableTreeNode root,File file){
		File[] ff=file.listFiles();
		//判断是否为空
		if(ff==null)
		{
			return;
		}
		for(File f:ff){
			DefaultMutableTreeNode newNode = createNode(root, f);
			if(f.isDirectory()){
				//过滤掉以'.'开头的文件夹
				if(!f.getName().startsWith(".")){
					//递归查找文件夹下的所有文件夹和文件，同时完善文件树
					File[] fl = f.listFiles();
					if(fl!=null)
					for(File fle:fl){
						createNode(newNode, fle);
					}
				}
			}
		}
	}
	
	/*
	 * 给根节点添加一个新的节点
	 * file 下个节点的文件
	 */
	private DefaultMutableTreeNode createNode(DefaultMutableTreeNode root,File file){
		//遍历根文件夹下的所有文件
		if(file==null)
		{
			return null;
		}
		DefaultMutableTreeNode newNode=null;
		//过滤掉以'.'开头的文件夹和文件
//					if(!f.getName().startsWith(".")){
//						//文件符合添加的要求就创建一个新的节点
//						newNode = new DefaultMutableTreeNode(f);
//					}
		if(file.isDirectory()){
			//过滤掉以'.'开头的文件夹
			if(!file.getName().startsWith(".")){
				//文件符合添加的要求就创建一个新的节点
				newNode = new DefaultMutableTreeNode(new FileNode(file));
			}
		} else if (file.isFile() && file.getName().endsWith(".java")) {// 调试用，向控制台打印所有的文件路径
			if (!file.getName().startsWith(".")) {
				// 文件符合添加的要求就创建一个新的节点
				newNode = new DefaultMutableTreeNode(new FileNode(file));
			}
		}
		// 如果newNode不为null，就把该节点添加到它的根节点中去
		if (newNode != null) {
			root.add(newNode);
		}
		return newNode;
	}
		  
	/**
	 * 获得文件树的DefaultTreeModel
	 * @return 该文件树的DefaultTreeModel
	 */
	public DefaultTreeModel getDefTreeModel(){
		return dt;
	}
	
	/**
	 * 设置从属关系，用于两个文件树之间进行互动的情况，父文件树点击某个文件夹，子文件树显示该文件夹下的所有文件
	 * @param tree 需要关联的子文件树
	 */
	public void setChild(final FileTree tree){
		//添加JTree 的鼠标单击事件，每次单击后子文件树显示被点击的文件夹下的所有文件
		jtree.addMouseListener(new MouseAdapter() { 
			//重载鼠标点击事件函数
			@Override
			public void mouseClicked(MouseEvent e) {
				//判断是否是单击
				if(e.getClickCount()==1){
					//获取鼠标点击的位置的TreePath
					TreePath path = jtree.getPathForLocation(e.getX(), e.getY()); // 关键是这个方法的使用
				 	//path是null说明点击的位置不是在文件树上
					if (path == null) {
				 		return;
				 	}
					//获得最后被选择（点击）的节点DefaultMutableTreeNode
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) jtree.getLastSelectedPathComponent();
					//获得该节点的文件File
					FileNode f = (FileNode)node.getUserObject();
					//如果该文件是一个目录
					if(f.file.isDirectory()){
						//就改变子文件树的根节点，让子文件树只显示该文件夹下的文件
						tree.getDefTreeModel().setRoot(node);
					}
				}
				super.mouseClicked(e);
			}
		});
	}
	
	/**
	 * 改变文件树的根节点
	 * @param dir 新的文件路径
	 */
	public void setRoot(String dir){
		//创建该路径的File对象
		File file = new File(dir);
		DefaultMutableTreeNode newroot=new DefaultMutableTreeNode(new FileNode(file));
		//创建以输入文件夹为根节点的文件树
		createTree(newroot,file);
		//改变文件树的根节点
		dt.setRoot(newroot);
		dt.reload();
	}
	
}
