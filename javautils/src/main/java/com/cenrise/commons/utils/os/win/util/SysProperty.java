package com.cenrise.commons.utils.os.win.util;

public class SysProperty
{
  private String name = null;
  private String value = null;

  public SysProperty(String name, String value)
  {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String toString() {
    return this.name + "=" + this.value;
  }
}