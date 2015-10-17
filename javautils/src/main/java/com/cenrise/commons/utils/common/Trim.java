package com.cenrise.commons.utils.common;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * 复制代码时用于去掉行号，减少麻烦。
 * 
 * @author jiadongpo
 *
 */
public class Trim extends JFrame {

	private JTextArea textArea;

	private JButton rebtn;

	private JScrollPane jspane;

	private JPanel southPanel;

	public Trim() {
		add(new JLabel("粘贴目标文件"), BorderLayout.NORTH);
		jspane = new JScrollPane();
		add(jspane, BorderLayout.CENTER);
		textArea = new JTextArea();
		jspane.setViewportView(textArea);
		textArea.setBackground(new Color(210, 244, 204));
		textArea.setFont(new Font("黑体", Font.BOLD, 15));
		southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		add(southPanel, BorderLayout.SOUTH);
		rebtn = new JButton();
		southPanel.add(rebtn);
		rebtn.setText("去行标");
		rebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = "";
				Pattern pattern = Pattern.compile("[0-9]*");
				String[] lineStr = textArea.getText().split("\n");
				for (String line : lineStr) {
					line = line.trim();
					if (line != "" && line != null) {
						int p = line.length();
						int t = 0;
						for (int j = 0; j < p; j++) {
							String st = line.substring(j, j + 1);
							Matcher isNum = pattern.matcher(st);
							if (st == " " || isNum.matches()) {
								t++;
								System.out.println(st);
							} else {
								str += line.substring(t).trim() + "\n";
								break;
							}
						}
						int o = line.length();
					}
				}
				textArea.setText(str);
			}
		});
		this.setSize(945, 352);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setTitle("行标去除器");
	}

	public static void main(String[] args) {
		new Trim();
	}

}
