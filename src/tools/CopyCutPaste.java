package tools;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import javax.swing.JTextPane;

public class CopyCutPaste {
	
	private static JTextPane textPane;
	private Clipboard clipboard;//获取系统剪贴板
	
	public CopyCutPaste(JTextPane textPane1) {
		this.textPane=textPane1;
		clipboard = textPane.getToolkit().getSystemClipboard();	//获取系统剪贴板
	}
	
	public CopyCutPaste() {
		this.textPane=null;
		clipboard = null;
	}
	
	public void setTextPane(JTextPane textPane1)
	{
		this.textPane=textPane1;
		clipboard = textPane.getToolkit().getSystemClipboard();	//获取系统剪贴板
	}
	
	public void Copy(){
		String tempText = textPane.getSelectedText();  //拖动鼠标选取文本
		   
		StringSelection editText =new StringSelection(tempText);	//将鼠标选取的文本内容，赋值给editText
		   
		clipboard.setContents(editText,null);			//将editText 赋值给系统剪贴板
	}
	
	public void Cut(){
		String tempText = textPane.getSelectedText();  //拖动鼠标选取文本
		   
		StringSelection editText =new StringSelection(tempText);	//将鼠标选取的文本内容，赋值给editText
		   
		clipboard.setContents(editText,null);			//将editText 赋值给系统剪贴板
		 
		textPane.replaceSelection("");					//将鼠标选取的文本内容清除
	}
	
	public void Paste(){
		Transferable contents = clipboard.getContents(this);	//获取系统剪贴板的内容
	    DataFlavor  flavor= DataFlavor.stringFlavor;			//设置请求的数据 flavor
	    if( contents.isDataFlavorSupported(flavor))				//判断是否为flavor 类型
	    {
		    try{
		    	
			    String str;
			    str = (String)contents.getTransferData(flavor);	//转换系统剪贴板的内容 为String
			    textPane.replaceSelection(str);					//修改文本内容，粘贴
		     }catch(Exception ex){
		    	 ex.printStackTrace();
		     }
	     }
	}

}
