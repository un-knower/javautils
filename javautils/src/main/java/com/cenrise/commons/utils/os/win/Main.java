package com.cenrise.commons.utils.os.win;

import javax.swing.SwingUtilities;

/**
 * 解压缩之后直接双击jar文件或者双击bat文件即可。<br>
 * <br>
 * 本软件可以查看java虚拟机的参数信息以及系统环境变量的信息。<br>
 * 运行环境：装有java虚拟机(jdk6)的PC即可。
 * 
 * @author Admin
 *
 */
public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFrame thisClass = new MainFrame();
				thisClass.setVisible(true);
			}
		});
	}
}
