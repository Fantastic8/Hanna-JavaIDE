package gui;

import java.awt.Color;
import java.awt.Component;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import data.FileNode;

/**
 * 重写DefaultTreeCellRenderer
 * 重绘每个节点元素的显示样式，令显示的图标变成系统显示的图标
 * 
 */
public class TreeRenderer extends DefaultTreeCellRenderer {
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		FileSystemView fileSystemView = FileSystemView.getFileSystemView();
		FileNode filenode = (FileNode) node.getUserObject();
		if(filenode.file.isDirectory()){
			leaf = false;
		}
		//去掉节点的虚框
		hasFocus = false;
//		setForeground(Color.RED);
//	    setTextSelectionColor(Color.black);
		//设置选择时的背景颜色
	    setBackgroundSelectionColor(new Color(205, 232, 255));
	    setBackgroundNonSelectionColor(Color.white);
        // 执行父类原型操作
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        //设置显示的图标为系统显示的图标
		setIcon(fileSystemView.getSystemIcon(filenode.file));
		//设置显示的名字
		setText(filenode.file.getName());
		
		return this;
	}
	
}
