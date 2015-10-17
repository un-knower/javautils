package com.cenrise.commons.utils.os.win.gui;


import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages
{
  private static final String BUNDLE_NAME = "com.cenrise.commons.utils.os.win.gui.messages";
  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("com.cenrise.commons.utils.os.win.gui.messages");

  public static String getString(String key)
  {
    try
    {
      return RESOURCE_BUNDLE.getString(key); } catch (MissingResourceException e) {
    }
    return '!' + key + '!';
  }
}