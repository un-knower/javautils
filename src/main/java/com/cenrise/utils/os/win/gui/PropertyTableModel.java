package com.cenrise.utils.os.win.gui;


import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.cenrise.commons.utils.os.win.util.SysProperty;

public class PropertyTableModel extends AbstractTableModel
{
  private static final long serialVersionUID = 1L;
  private final String[] COL_NAMES = { 
    Messages.getString("PropertyTableModel.Name"), 
    Messages.getString("PropertyTableModel.Value") };
  private ArrayList<SysProperty> properties = null;

  public PropertyTableModel(ArrayList<SysProperty> properties)
  {
    this.properties = properties;
  }

  public int getColumnCount() {
    return this.COL_NAMES.length;
  }

  public String getColumnName(int column) {
    return this.COL_NAMES[column];
  }

  public int getRowCount() {
    return this.properties.size();
  }

  public boolean isCellEditable(int rowIndex, int columnIndex)
  {
    return false;
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    SysProperty sysProperty = (SysProperty)this.properties.get(rowIndex);
    if (columnIndex == 0)
      return sysProperty.getName();

    return sysProperty.getValue();
  }
}