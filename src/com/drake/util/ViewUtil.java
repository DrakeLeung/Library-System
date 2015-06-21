package com.drake.util;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ViewUtil {
	
	public static void showErrorMsg(JPanel panel, String msg) {
		JOptionPane.showMessageDialog(panel, msg, "错误", 
				JOptionPane.ERROR_MESSAGE);
	}
	
	public static void showSuccessMsg(JPanel panel, String msg) {
		JOptionPane.showMessageDialog(panel, msg, "成功", 
				JOptionPane.INFORMATION_MESSAGE);
	}
}
