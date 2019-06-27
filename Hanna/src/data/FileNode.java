package data;

import java.io.File;

public class FileNode {
	public File file;
	public boolean isInit = false;
	public FileNode(File f) {
		this.file = f;
	}
}
