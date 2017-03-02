
package com.cenrise.utils.mailCBC;

import java.util.Properties;

/**
 * A configuration of properties,See the server
 *
 * @author dengluxiao
 * @version $Id: PropertyManager.java,v 1.4.1 10/08/2009 14:05:23 dengluxiao $
 */
public final class PropertyManager {

    //private static boolean ssl;
    //private static boolean tls;

    //private static final String PATH = "config/mail.properties";

    private static final String SMTP = "smtp";

    private static Properties prop_send;
    private static Properties prop_send_gmail;
    private static Properties prop_send_hotmail;

    private static Properties prop_receive;
    private static Properties prop_receive_gmail;
    private static Properties prop_receive_hotmail;

    //---------------------------------------------------------------------------
    // init every property
    //---------------------------------------------------------------------------

    static {
        if (prop_send == null) prop_send = new Properties();
        if (prop_send_gmail == null) prop_send_gmail = new Properties();
        if (prop_send_hotmail == null) prop_send_hotmail = new Properties();

        if (prop_receive == null) prop_receive = new Properties();
        if (prop_receive_gmail == null) prop_receive_gmail = new Properties();
        if (prop_receive_hotmail == null) prop_receive_hotmail = new Properties();

        prop_send.put("mail.smtp.port", "25");  // default smtp
        prop_send.put("mail.Transport.protocol", SMTP);

        prop_send_gmail.putAll(prop_send);  // add default smtp
        prop_send_gmail.put("mail.smtp.socketFactory.port", "465");
        prop_send_gmail.put("mail.smtp.socketFactory.fallback", "false");
        prop_send_gmail.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        prop_send_hotmail.putAll(prop_send); // add default smtp
        prop_send_hotmail.put("mail.smtp.starttls.enable", "true");

        prop_receive_gmail.put("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop_receive_gmail.put("mail.pop3.socketFactory.fallback", "false");
        prop_receive_gmail.put("mail.pop3.port", "995");
        prop_receive_gmail.put("mail.pop3.socketFactory.port", "995");

        // gmail equals hotmail when receive mail
        prop_receive_hotmail.putAll(prop_receive_gmail);
    }

    //---------------------------------------------------------------------------
    //choose read property file
    //---------------------------------------------------------------------------

    //---------------------------------------------------------------------------
    //  static {
    //  try {
    //  prop = new Properties();
    //  under two read properties method
    //  prop.load(PropertyManager.class.getClassLoader().getResourceAsStream("com/tom/inq/util/mail.properties"));
    //  FileInputStream fis = new FileInputStream("config/mail.properties");
    //  prop.load(fis);
    //	fis.close();
    //	} catch (FileNotFoundException e) {
    //	e.printStackTrace();
    //	} catch (IOException e) {
    //	e.printStackTrace();
    //	}
    //  }
    //-----------------------------------------------------------------------------

    //-----------------------------------------------------------------------------
    // get send properties by ext ( mail user ext)
    //-----------------------------------------------------------------------------

    public static Properties getSendProperty(String ext, String host) {
        Properties prop = null;
        ext = ext.toLowerCase();  // ***
        if (ext.trim().indexOf("@gmail.com") != -1) prop = prop_send_gmail;
        else if (ext.trim().indexOf("@live.cn") != -1) prop = prop_send_hotmail;
        else if (ext.trim().indexOf("@msn.com") != -1) prop = prop_send_hotmail;
        else if (ext.trim().indexOf("@live.com") != -1) prop = prop_send_hotmail;
        else if (ext.trim().indexOf("@hotmail.com") != -1) prop = prop_send_hotmail;
        else prop = prop_send;

        prop.put("mail.smtp.host", host);
        return prop;
    }

    //-----------------------------------------------------------------------------
    // get receive properties by ext ( mail user ext)
    //-----------------------------------------------------------------------------

    public static Properties getReceiveProperty(String ext, String host) {
        Properties prop = null;

        ext = ext.toLowerCase();
        if (ext.trim().indexOf("@gmail.com") != -1) prop = prop_receive_gmail;
        else if (ext.trim().indexOf("@live.cn") != -1) prop = prop_receive_hotmail;
        else if (ext.trim().indexOf("@msn.com") != -1) prop = prop_receive_hotmail;
        else if (ext.trim().indexOf("@live.com") != -1) prop = prop_receive_hotmail;
        else if (ext.trim().indexOf("@hotmail.com") != -1) prop = prop_receive_hotmail;
        else prop = prop_receive;

        return prop;
    }

    //-----------------------------------------------------------------------------
    //
    //-----------------------------------------------------------------------------

    public static void main(String[] args) {

        String host = "xxx";
        String ext = "xxx@hotmail.com";
        System.out.println(getSendProperty(ext, host).size());
        System.err.println(getReceiveProperty(ext, host).size());

    }

}
