package com.cenrise.commons.utils.os.win;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import com.cenrise.commons.utils.os.win.gui.Messages;
import com.cenrise.commons.utils.os.win.gui.PropertyTableModel;
import com.cenrise.commons.utils.os.win.util.SystemInfo;

public class MainFrame extends JFrame
{
  private static final long serialVersionUID = 1L;
  private JPanel jContentPane = null;
  private JTabbedPane tabbedPane = null;
  private JTable jvmEnvTable = null;
  private JTable osEnvTable = null;

  public MainFrame()
  {
    setSize(600, 450);
    initialize();
  }

  private void initialize() {
    setTitle(Messages.getString("MainFrame.SystemUtil"));
    setSize(600, 450);
    setDefaultCloseOperation(3);
    setContentPane(getJContentPane());
    setLocationByPlatform(true);
  }

  private JPanel getJContentPane() {
    if (this.jContentPane == null) {
      this.jContentPane = new JPanel();
      this.jContentPane.setLayout(new BorderLayout());
      this.jContentPane.add(getItemTabbedPane(), "Center");
    }
    return this.jContentPane;
  }

  public JTabbedPane getItemTabbedPane() {
    if (this.tabbedPane == null) {
      this.tabbedPane = new JTabbedPane();
      this.tabbedPane.addTab(Messages.getString("MainFrame.JVMEnv"), getJVMEnvTable());
      this.tabbedPane.addTab(Messages.getString("MainFrame.OSEnv"), getOSEnvTable());
    }
    return this.tabbedPane;
  }

  private JScrollPane getJVMEnvTable() {
    if (this.jvmEnvTable == null) {
      ArrayList properties = SystemInfo.getJVMEnvProperties();
      PropertyTableModel tableModel = new PropertyTableModel(properties);
      this.jvmEnvTable = new JTable(tableModel);
    }
    return new JScrollPane(this.jvmEnvTable);
  }

  private JScrollPane getOSEnvTable() {
    if (this.osEnvTable == null) {
      ArrayList properties = SystemInfo.getOSEnvProperties();
      PropertyTableModel tableModel = new PropertyTableModel(properties);
      this.osEnvTable = new JTable(tableModel);
    }
    return new JScrollPane(this.osEnvTable);
  }
}