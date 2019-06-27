package interfaces;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class PagePanel extends JPanel {
	
	private JLabel Title;
	
	public PagePanel()
	{
		super();
	}
	
	/**
	 * 打开事件
	 */
	public void openEvent(){}
	
	/**
	 * 关闭事件
	 */
	public void closeEvent(){}

	/**
	 * Getter and Setter
	 * @return
	 */
	public JLabel getTitle() {
		return Title;
	}

	public void setTitle(JLabel title) {
		Title = title;
	}
}
