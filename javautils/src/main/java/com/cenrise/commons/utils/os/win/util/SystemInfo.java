package com.cenrise.commons.utils.os.win.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

public class SystemInfo
{
  private static ArrayList<SysProperty> jvmEnvProperties = null;
  private static ArrayList<SysProperty> osEnvProperties = null;

  public static ArrayList<SysProperty> getJVMEnvProperties()
  {
    if (jvmEnvProperties == null)
      initJVMEnvProperties();

    return jvmEnvProperties;
  }

  public static ArrayList<SysProperty> getOSEnvProperties() {
    if (osEnvProperties == null)
      initOSEnvProperties();

    return osEnvProperties;
  }

  private static void initJVMEnvProperties() {
    jvmEnvProperties = new ArrayList();

    Properties properties = System.getProperties();

    Enumeration nameEnum = properties.propertyNames();
    while (nameEnum.hasMoreElements()) {
      String name = (String)nameEnum.nextElement();
      String value = properties.getProperty(name);
      jvmEnvProperties.add(new SysProperty(name, value));
    }
  }

  private static void initOSEnvProperties() {
    osEnvProperties = new ArrayList();
    try
    {
      String osName = System.getProperty("os.name").toLowerCase();
      if (osName.indexOf("windows") == -1) {
        return;
      }

      Process process = Runtime.getRuntime().exec("cmd /c set");
      InputStream input = process.getInputStream();
      InputStreamReader inputReader = new InputStreamReader(input);
      BufferedReader buuferReader = new BufferedReader(inputReader);
      String line = buuferReader.readLine();
      while (line != null) {
        int index = line.indexOf("=");
        if (index == -1) {
          osEnvProperties.add(new SysProperty(line, ""));
        } else {
          String name = line.substring(0, index);
          String value = line.substring(index + 1);
          osEnvProperties.add(new SysProperty(name, value));
        }

        line = buuferReader.readLine();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    osEnvProperties = getOSEnvProperties();
    for (Iterator localIterator = osEnvProperties.iterator(); localIterator.hasNext(); ) { SysProperty sysProperty = (SysProperty)localIterator.next();
      System.out.println(sysProperty);
    }
  }
}