
package com.cenrise.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;

import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.*;
import java.sql.Timestamp;
import java.text.*;
import java.time.Instant;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Const {
    private static Class<?> PKG = Const.class; // for i18n purposes, needed by Translator2!!


    /**
     * Copyright year
     */
    public static final String COPYRIGHT_YEAR = "2015";


    /**
     * Sleep time waiting when buffer is empty (the default)
     */
    public static final int TIMEOUT_GET_MILLIS = 50;

    /**
     * Sleep time waiting when buffer is full (the default)
     */
    public static final int TIMEOUT_PUT_MILLIS = 50;

    /**
     * print update every ... lines
     */
    public static final int ROWS_UPDATE = 50000;

    /**
     * Size of rowset: bigger = faster for large amounts of data
     */
    public static final int ROWS_IN_ROWSET = 10000;

    /**
     * Fetch size in rows when querying a database
     */
    public static final int FETCH_SIZE = 10000;

    /**
     * Sort size: how many rows do we sort in memory at once?
     */
    public static final int SORT_SIZE = 5000;

    /**
     * job/trans heartbeat scheduled executor periodic interval ( in seconds )
     */
    public static final int HEARTBEAT_PERIODIC_INTERVAL_IN_SECS = 10;

    /**
     * What's the file systems file separator on this operating system?
     */
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    /**
     * What's the path separator on this operating system?
     */
    public static final String PATH_SEPARATOR = System.getProperty("path.separator");

    /**
     * CR: operating systems specific Carriage Return
     */
    public static final String CR = System.getProperty("line.separator");

    /**
     * DOSCR: MS-DOS specific Carriage Return
     */
    public static final String DOSCR = "\n\r";

    /**
     * An empty ("") String.
     */
    public static final String EMPTY_STRING = "";

    /**
     * The Java runtime version
     */
    public static final String JAVA_VERSION = System.getProperty("java.vm.version");


    /**
     * Default minimum date range...
     */
    public static final Date MIN_DATE = new Date(-2208992400000L); // 1900/01/01 00:00:00.000

    /**
     * Default maximum date range...
     */
    public static final Date MAX_DATE = new Date(7258114799468L); // 2199/12/31 23:59:59.999

    /**
     * The default minimum year in a dimension date range
     */
    public static final int MIN_YEAR = 1900;

    /**
     * The default maximum year in a dimension date range
     */
    public static final int MAX_YEAR = 2199;

    /**
     * Specifies the number of pixels to the right we have to go in dialog boxes.
     */
    public static final int RIGHT = 400;

    /**
     * Specifies the length (width) of fields in a number of pixels in dialog boxes.
     */
    public static final int LENGTH = 350;

    /**
     * The margin between the different dialog components & widgets
     */
    public static final int MARGIN = 4;

    /**
     * The default percentage of the width of screen where we consider the middle of a dialog.
     */
    public static final int MIDDLE_PCT = 35;

    /**
     * The default width of an arrow in the Graphical Views
     */
    public static final int ARROW_WIDTH = 1;

    /**
     * The horizontal and vertical margin of a dialog box.
     */
    public static final int FORM_MARGIN = 5;

    /**
     * The default shadow size on the graphical view.
     */
    public static final int SHADOW_SIZE = 0;

    /**
     * The size of relationship symbols
     */
    public static final int SYMBOLSIZE = 10;

    /**
     * Max nr. of files to remember
     */
    public static final int MAX_FILE_HIST = 9; // Having more than 9 files in the file history is not compatible with pre
    // 5.0 versions

    /**
     * The default locale for the kettle environment (system defined)
     */
    public static final Locale DEFAULT_LOCALE = Locale.getDefault(); // new Locale("nl", "BE");

    /**
     * The default decimal separator . or ,
     */
    public static final char DEFAULT_DECIMAL_SEPARATOR = (new DecimalFormatSymbols(DEFAULT_LOCALE))
            .getDecimalSeparator();

    /**
     * The default grouping separator , or .
     */
    public static final char DEFAULT_GROUPING_SEPARATOR = (new DecimalFormatSymbols(DEFAULT_LOCALE))
            .getGroupingSeparator();

    /**
     * The default currency symbol
     */
    public static final String DEFAULT_CURRENCY_SYMBOL = (new DecimalFormatSymbols(DEFAULT_LOCALE))
            .getCurrencySymbol();

    /**
     * The default number format
     */
    public static final String DEFAULT_NUMBER_FORMAT = ((DecimalFormat) (NumberFormat.getInstance()))
            .toPattern();

    /**
     * Default string representing Null String values (empty)
     */
    public static final String NULL_STRING = "";

    /**
     * Default string representing Null Number values (empty)
     */
    public static final String NULL_NUMBER = "";

    /**
     * Default string representing Null Date values (empty)
     */
    public static final String NULL_DATE = "";

    /**
     * Default string representing Null BigNumber values (empty)
     */
    public static final String NULL_BIGNUMBER = "";

    /**
     * Default string representing Null Boolean values (empty)
     */
    public static final String NULL_BOOLEAN = "";

    /**
     * Default string representing Null Integer values (empty)
     */
    public static final String NULL_INTEGER = "";

    /**
     * Default string representing Null Binary values (empty)
     */
    public static final String NULL_BINARY = "";

    /**
     * Default string representing Null Undefined values (empty)
     */
    public static final String NULL_NONE = "";

    /**
     * Rounding mode, not implemented in {@code BigDecimal}. Method java.lang.Math.round(double) processes this way. <br/>
     * Rounding mode to round towards {@literal "nearest neighbor"} unless both neighbors are equidistant, in which case
     * round ceiling. <br/>
     * Behaves as for {@code ROUND_CEILING} if the discarded fraction is &ge; 0.5; otherwise, behaves as for
     * {@code ROUND_FLOOR}. Note that this is the most common arithmetical rounding mode.
     */
    public static final int ROUND_HALF_CEILING = -1;

    /**
     * The base name of the Chef logfile
     */
    public static final String CHEF_LOG_FILE = "chef";

    /**
     * The base name of the Spoon logfile
     */
    public static final String SPOON_LOG_FILE = "spoon";

    /**
     * The base name of the Menu logfile
     */
    public static final String MENU_LOG_FILE = "menu";

    /**
     * An array of date conversion formats
     */
    private static String[] dateFormats;

    /**
     * An array of number conversion formats
     */
    private static String[] numberFormats;

    /**
     * Generalized date/time format: Wherever dates are used, date and time values are organized from the most to the
     * least significant. see also method StringUtil.getFormattedDateTime()
     */
    public static final String GENERALIZED_DATE_TIME_FORMAT = "yyyyddMM_hhmmss";
    public static final String GENERALIZED_DATE_TIME_FORMAT_MILLIS = "yyyyddMM_hhmmssSSS";

    /**
     * Default we store our information in Unicode UTF-8 character set.
     */
    public static final String XML_ENCODING = "UTF-8";

    /**
     * The possible extensions a transformation XML file can have.
     */
    public static final String[] STRING_TRANS_AND_JOB_FILTER_EXT = new String[]{
            "*.ktr;*.kjb;*.xml", "*.ktr;*.xml", "*.kjb;*.xml", "*.xml", "*.*"};

    /**
     * The descriptions of the possible extensions a transformation XML file can have.
     */
    private static String[] STRING_TRANS_AND_JOB_FILTER_NAMES;

    /**
     * The possible extensions a transformation XML file can have.
     */
    public static final String[] STRING_TRANS_FILTER_EXT = new String[]{"*.ktr;*.xml", "*.xml", "*.*"};

    /**
     * The descriptions of the possible extensions a transformation XML file can have.
     */
    private static String[] STRING_TRANS_FILTER_NAMES;

    /**
     * The possible extensions a job XML file can have.
     */
    public static final String[] STRING_JOB_FILTER_EXT = new String[]{"*.kjb;*.xml", "*.xml", "*.*"};

    /**
     * The descriptions of the possible extensions a job XML file can have.
     */
    private static String[] STRING_JOB_FILTER_NAMES;


    /**
     * The default maximum for the nr of lines in the GUI logs
     */
    public static final int MAX_NR_LOG_LINES = 5000;

    /**
     * The default maximum for the nr of lines in the history views
     */
    public static final int MAX_NR_HISTORY_LINES = 50;

    /**
     * The default fetch size for lines of history.
     */
    public static final int HISTORY_LINES_FETCH_SIZE = 10;

    /**
     * The default log line timeout in minutes : 12 hours
     */
    public static final int MAX_LOG_LINE_TIMEOUT_MINUTES = 12 * 60;

    /**
     * UI-agnostic flag for warnings
     */
    public static final int WARNING = 1;

    /**
     * UI-agnostic flag for warnings
     */
    public static final int ERROR = 2;

    /**
     * UI-agnostic flag for warnings
     */
    public static final int INFO = 3;

    /**
     * The margin between the text of a note and its border.
     */
    public static final int NOTE_MARGIN = 5;


    /**
     * A general initial version comment
     */
    public static final String VERSION_COMMENT_INITIAL_VERSION = "Creation of initial version";

    /**
     * A general edit version comment
     */
    public static final String VERSION_COMMENT_EDIT_VERSION = "Modification by user";


    private static String[] emptyPaddedSpacesStrings;


    /**
     * The system environment variable indicating where the alternative location for the Pentaho metastore folder is
     * located.
     */
    public static final String PENTAHO_METASTORE_FOLDER = "PENTAHO_METASTORE_FOLDER";

    /**
     * The name of the local client MetaStore
     */
    public static final String PENTAHO_METASTORE_NAME = "Pentaho Local Client Metastore";


    /**
     * A variable to configure VFS USER_DIR_IS_ROOT option: should be "true" or "false"
     * {@linkplain org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder#USER_DIR_IS_ROOT}
     */
    public static final String VFS_USER_DIR_IS_ROOT = "vfs.sftp.userDirIsRoot";

    /**
     * rounds double f to any number of places after decimal point Does arithmetic using BigDecimal class to avoid integer
     * overflow while rounding
     *
     * @param f      The value to round
     * @param places The number of decimal places
     * @return The rounded floating point value
     */

    public static double round(double f, int places) {
        return round(f, places, java.math.BigDecimal.ROUND_HALF_EVEN);
    }

    /**
     * rounds double f to any number of places after decimal point Does arithmetic using BigDecimal class to avoid integer
     * overflow while rounding
     *
     * @param f            The value to round
     * @param places       The number of decimal places
     * @param roundingMode The mode for rounding, e.g. java.math.BigDecimal.ROUND_HALF_EVEN
     * @return The rounded floating point value
     */
    public static double round(double f, int places, int roundingMode) {
        // We can't round non-numbers or infinite values
        //
        if (Double.isNaN(f) || f == Double.NEGATIVE_INFINITY || f == Double.POSITIVE_INFINITY) {
            return f;
        }

        // Do the rounding...
        //
        java.math.BigDecimal bdtemp = round(java.math.BigDecimal.valueOf(f), places, roundingMode);
        return bdtemp.doubleValue();
    }

    /**
     * rounds BigDecimal f to any number of places after decimal point Does arithmetic using BigDecimal class to avoid
     * integer overflow while rounding
     *
     * @param f            The value to round
     * @param places       The number of decimal places
     * @param roundingMode The mode for rounding, e.g. java.math.BigDecimal.ROUND_HALF_EVEN
     * @return The rounded floating point value
     */
    public static BigDecimal round(BigDecimal f, int places, int roundingMode) {
        if (roundingMode == ROUND_HALF_CEILING) {
            if (f.signum() >= 0) {
                return round(f, places, BigDecimal.ROUND_HALF_UP);
            } else {
                return round(f, places, BigDecimal.ROUND_HALF_DOWN);
            }
        } else {
            return f.setScale(places, roundingMode);
        }
    }

    /**
     * rounds long f to any number of places after decimal point Does arithmetic using BigDecimal class to avoid integer
     * overflow while rounding
     *
     * @param f            The value to round
     * @param places       The number of decimal places
     * @param roundingMode The mode for rounding, e.g. java.math.BigDecimal.ROUND_HALF_EVEN
     * @return The rounded floating point value
     */
    public static long round(long f, int places, int roundingMode) {
        if (places >= 0) {
            return f;
        }
        BigDecimal bdtemp = round(BigDecimal.valueOf(f), places, roundingMode);
        return bdtemp.longValue();
    }

  /*
   * OLD code: caused a lot of problems with very small and very large numbers. It's a miracle it worked at all. Go
   * ahead, have a laugh... public static float round(double f, int places) { float temp = (float) (f *
   * (Math.pow(10, places)));
   *
   * temp = (Math.round(temp));
   *
   * temp = temp / (int) (Math.pow(10, places));
   *
   * return temp;
   *
   * }
   */

    /**
     * Convert a String into an integer. If the conversion fails, assign a default value.
     *
     * @param str The String to convert to an integer
     * @param def The default value
     * @return The converted value or the default.
     */
    public static int toInt(String str, int def) {
        int retval;
        try {
            retval = Integer.parseInt(str);
        } catch (Exception e) {
            retval = def;
        }
        return retval;
    }

    /**
     * Convert a String into a long integer. If the conversion fails, assign a default value.
     *
     * @param str The String to convert to a long integer
     * @param def The default value
     * @return The converted value or the default.
     */
    public static long toLong(String str, long def) {
        long retval;
        try {
            retval = Long.parseLong(str);
        } catch (Exception e) {
            retval = def;
        }
        return retval;
    }

    /**
     * Convert a String into a double. If the conversion fails, assign a default value.
     *
     * @param str The String to convert to a double
     * @param def The default value
     * @return The converted value or the default.
     */
    public static double toDouble(String str, double def) {
        double retval;
        try {
            retval = Double.parseDouble(str);
        } catch (Exception e) {
            retval = def;
        }
        return retval;
    }

    /**
     * Convert a String into a date. The date format is <code>yyyy/MM/dd HH:mm:ss.SSS</code>. If the conversion fails,
     * assign a default value.
     *
     * @param str The String to convert into a Date
     * @param def The default value
     * @return The converted value or the default.
     */
    public static Date toDate(String str, Date def) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS", Locale.US);
        try {
            return df.parse(str);
        } catch (ParseException e) {
            return def;
        }
    }

    /**
     * Determines whether or not a character is considered a space. A character is considered a space in Kettle if it is a
     * space, a tab, a newline or a cariage return.
     *
     * @param c The character to verify if it is a space.
     * @return true if the character is a space. false otherwise.
     */
    public static boolean isSpace(char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n' || Character.isWhitespace(c);
    }

    /**
     * Left trim: remove spaces to the left of a String.
     *
     * @param source The String to left trim
     * @return The left trimmed String
     */
    public static String ltrim(String source) {
        if (source == null) {
            return null;
        }
        int from = 0;
        while (from < source.length() && isSpace(source.charAt(from))) {
            from++;
        }

        return source.substring(from);
    }

    /**
     * Right trim: remove spaces to the right of a string
     *
     * @param source The string to right trim
     * @return The trimmed string.
     */
    public static String rtrim(String source) {
        if (source == null) {
            return null;
        }

        int max = source.length();
        while (max > 0 && isSpace(source.charAt(max - 1))) {
            max--;
        }

        return source.substring(0, max);
    }

    /**
     * Trims a string: removes the leading and trailing spaces of a String.
     *
     * @param str The string to trim
     * @return The trimmed string.
     */
    public static String trim(String str) {
        if (str == null) {
            return null;
        }

        int max = str.length() - 1;
        int min = 0;

        while (min <= max && isSpace(str.charAt(min))) {
            min++;
        }
        while (max >= 0 && isSpace(str.charAt(max))) {
            max--;
        }

        if (max < min) {
            return "";
        }

        return str.substring(min, max + 1);
    }

    /**
     * Right pad a string: adds spaces to a string until a certain length. If the length is smaller then the limit
     * specified, the String is truncated.
     *
     * @param ret   The string to pad
     * @param limit The desired length of the padded string.
     * @return The padded String.
     */
    public static String rightPad(String ret, int limit) {
        if (ret == null) {
            return rightPad(new StringBuilder(), limit);
        } else {
            return rightPad(new StringBuilder(ret), limit);
        }
    }

    /**
     * Right pad a StringBuffer: adds spaces to a string until a certain length. If the length is smaller then the limit
     * specified, the String is truncated.
     * <p>
     * MB - New version is nearly 25% faster
     *
     * @param ret   The StringBuffer to pad
     * @param limit The desired length of the padded string.
     * @return The padded String.
     */
    public static String rightPad(StringBuffer ret, int limit) {
        if (ret != null) {
            while (ret.length() < limit) {
                ret.append("                    ");
            }
            ret.setLength(limit);
            return ret.toString();
        } else {
            return null;
        }
    }

    /**
     * Right pad a StringBuilder: adds spaces to a string until a certain length. If the length is smaller then the limit
     * specified, the String is truncated.
     * <p>
     * MB - New version is nearly 25% faster
     *
     * @param ret   The StringBuilder to pad
     * @param limit The desired length of the padded string.
     * @return The padded String.
     */
    public static String rightPad(StringBuilder ret, int limit) {
        if (ret != null) {
            while (ret.length() < limit) {
                ret.append("                    ");
            }
            ret.setLength(limit);
            return ret.toString();
        } else {
            return null;
        }
    }

    /**
     * Replace values in a String with another.
     * <p>
     * 33% Faster using replaceAll this way than original method
     *
     * @param string The original String.
     * @param repl   The text to replace
     * @param with   The new text bit
     * @return The resulting string with the text pieces replaced.
     */
    public static String replace(String string, String repl, String with) {
        if (string != null && repl != null && with != null) {
            return string.replaceAll(Pattern.quote(repl), Matcher.quoteReplacement(with));
        } else {
            return null;
        }
    }

    /**
     * Alternate faster version of string replace using a stringbuffer as input.
     * <p>
     * 33% Faster using replaceAll this way than original method
     *
     * @param str  The string where we want to replace in
     * @param code The code to search for
     * @param repl The replacement string for code
     */
    public static void repl(StringBuffer str, String code, String repl) {
        if ((code == null) || (repl == null) || (code.length() == 0) || (repl.length() == 0) || (str == null) || (str.length() == 0)) {
            return; // do nothing
        }
        String aString = str.toString();
        str.setLength(0);
        str.append(aString.replaceAll(Pattern.quote(code), Matcher.quoteReplacement(repl)));
    }

    /**
     * Alternate faster version of string replace using a stringbuilder as input (non-synchronized).
     * <p>
     * 33% Faster using replaceAll this way than original method
     *
     * @param str  The string where we want to replace in
     * @param code The code to search for
     * @param repl The replacement string for code
     */
    public static void repl(StringBuilder str, String code, String repl) {
        if ((code == null) || (repl == null) || (str == null)) {
            return; // do nothing
        }
        String aString = str.toString();
        str.setLength(0);
        str.append(aString.replaceAll(Pattern.quote(code), Matcher.quoteReplacement(repl)));
    }

    /**
     * Count the number of spaces to the left of a text. (leading)
     *
     * @param field The text to examine
     * @return The number of leading spaces found.
     */
    public static int nrSpacesBefore(String field) {
        int nr = 0;
        int len = field.length();
        while (nr < len && field.charAt(nr) == ' ') {
            nr++;
        }
        return nr;
    }

    /**
     * Count the number of spaces to the right of a text. (trailing)
     *
     * @param field The text to examine
     * @return The number of trailing spaces found.
     */
    public static int nrSpacesAfter(String field) {
        int nr = 0;
        int len = field.length();
        while (nr < len && field.charAt(field.length() - 1 - nr) == ' ') {
            nr++;
        }
        return nr;
    }

    /**
     * Checks whether or not a String consists only of spaces.
     *
     * @param str The string to check
     * @return true if the string has nothing but spaces.
     */
    public static boolean onlySpaces(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!isSpace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * determine the OS name
     *
     * @return The name of the OS
     */
    public static String getOS() {
        return System.getProperty("os.name");
    }

    /**
     * Determine the quoting character depending on the OS. Often used for shell calls, gives back " for Windows systems
     * otherwise '
     *
     * @return quoting character
     */
    public static String getQuoteCharByOS() {
        if (isWindows()) {
            return "\"";
        } else {
            return "'";
        }
    }

    /**
     * Quote a string depending on the OS. Often used for shell calls.
     *
     * @return quoted string
     */
    public static String optionallyQuoteStringByOS(String string) {
        String quote = getQuoteCharByOS();
        if (isEmpty(string)) {
            return quote;
        }

        // If the field already contains quotes, we don't touch it anymore, just
        // return the same string...
        // also return it if no spaces are found
        if (string.indexOf(quote) >= 0 || (string.indexOf(' ') < 0 && string.indexOf('=') < 0)) {
            return string;
        } else {
            return quote + string + quote;
        }
    }

    /**
     * @return True if the OS is a Windows derivate.
     */
    public static boolean isWindows() {
        return getOS().startsWith("Windows");
    }

    /**
     * @return True if the OS is a Linux derivate.
     */
    public static boolean isLinux() {
        return getOS().startsWith("Linux");
    }

    /**
     * @return True if the OS is an OSX derivate.
     */
    public static boolean isOSX() {
        return getOS().toUpperCase().contains("OS X");
    }

    /**
     * @return True if KDE is in use.
     */
    public static boolean isKDE() {
        return StringUtils.isNotBlank(System.getenv("KDE_SESSION_VERSION"));
    }

    private static String cachedHostname;

    /**
     * This method is written especially for weird JVM's like
     *
     * @param key The key, the name of the environment variable to return
     * @param def The default value to return in case the key can't be found
     * @return The value of a System environment variable in the java virtual machine. If the key is not present, the
     * variable is not defined and the default value is returned.
     */
    public static final String getSystemProperty(String key, String def) {
        String value = System.getProperty(key, def);
        return value;
    }


    /**
     * Determins the IP address of the machine Kettle is running on.
     *
     * @return The IP address
     */
    public static String getIPAddress() throws Exception {
        Enumeration<NetworkInterface> enumInterfaces = NetworkInterface.getNetworkInterfaces();
        while (enumInterfaces.hasMoreElements()) {
            NetworkInterface nwi = enumInterfaces.nextElement();
            Enumeration<InetAddress> ip = nwi.getInetAddresses();
            while (ip.hasMoreElements()) {
                InetAddress in = ip.nextElement();
                if (!in.isLoopbackAddress() && in.toString().indexOf(":") < 0) {
                    return in.getHostAddress();
                }
            }
        }
        return "127.0.0.1";
    }

    /**
     * Get the primary IP address tied to a network interface (excluding loop-back etc)
     *
     * @param networkInterfaceName the name of the network interface to interrogate
     * @return null if the network interface or address wasn't found.
     * @throws SocketException in case of a security or network error
     */
    public static String getIPAddress(String networkInterfaceName) throws SocketException {
        NetworkInterface networkInterface = NetworkInterface.getByName(networkInterfaceName);
        Enumeration<InetAddress> ipAddresses = networkInterface.getInetAddresses();
        while (ipAddresses.hasMoreElements()) {
            InetAddress inetAddress = ipAddresses.nextElement();
            if (!inetAddress.isLoopbackAddress() && inetAddress.toString().indexOf(":") < 0) {
                String hostname = inetAddress.getHostAddress();
                return hostname;
            }
        }
        return null;
    }

    /**
     * Tries to determine the MAC address of the machine Kettle is running on.
     *
     * @return The MAC address.
     */
    public static String getMACAddress() throws Exception {
        String ip = getIPAddress();
        String mac = "none";
        String os = getOS();
        String s = "";
        @SuppressWarnings("unused")
        Boolean errorOccured = false;
        // System.out.println("os = "+os+", ip="+ip);

        if (os.equalsIgnoreCase("Windows NT")
                || os.equalsIgnoreCase("Windows 2000") || os.equalsIgnoreCase("Windows XP")
                || os.equalsIgnoreCase("Windows 95") || os.equalsIgnoreCase("Windows 98")
                || os.equalsIgnoreCase("Windows Me") || os.startsWith("Windows")) {
            try {
                // System.out.println("EXEC> nbtstat -a "+ip);

                Process p = Runtime.getRuntime().exec("nbtstat -a " + ip);

                // read the standard output of the command
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

                while (!procDone(p)) {
                    while ((s = stdInput.readLine()) != null) {
                        // System.out.println("NBTSTAT> "+s);
                        if (s.indexOf("MAC") >= 0) {
                            int idx = s.indexOf('=');
                            mac = s.substring(idx + 2);
                        }
                    }
                }
                stdInput.close();
            } catch (Exception e) {
                errorOccured = true;
            }
        } else if (os.equalsIgnoreCase("Linux")) {
            try {
                Process p = Runtime.getRuntime().exec("/sbin/ifconfig -a");

                // read the standard output of the command
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

                while (!procDone(p)) {
                    while ((s = stdInput.readLine()) != null) {
                        int idx = s.indexOf("HWaddr");
                        if (idx >= 0) {
                            mac = s.substring(idx + 7);
                        }
                    }
                }
                stdInput.close();
            } catch (Exception e) {
                errorOccured = true;

            }
        } else if (os.equalsIgnoreCase("Solaris")) {
            try {
                Process p = Runtime.getRuntime().exec("/usr/sbin/ifconfig -a");

                // read the standard output of the command
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

                while (!procDone(p)) {
                    while ((s = stdInput.readLine()) != null) {
                        int idx = s.indexOf("ether");
                        if (idx >= 0) {
                            mac = s.substring(idx + 6);
                        }
                    }
                }
                stdInput.close();
            } catch (Exception e) {
                errorOccured = true;

            }
        } else if (os.equalsIgnoreCase("HP-UX")) {
            try {
                Process p = Runtime.getRuntime().exec("/usr/sbin/lanscan -a");

                // read the standard output of the command
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

                while (!procDone(p)) {
                    while ((s = stdInput.readLine()) != null) {
                        if (s.indexOf("MAC") >= 0) {
                            int idx = s.indexOf("0x");
                            mac = s.substring(idx + 2);
                        }
                    }
                }
                stdInput.close();
            } catch (Exception e) {
                errorOccured = true;

            }
        }
        // should do something if we got an error processing!
        return Const.trim(mac);
    }

    private static final boolean procDone(Process p) {
        try {
            p.exitValue();
            return true;
        } catch (IllegalThreadStateException e) {
            return false;
        }
    }


    /**
     * Retrieves the content of an environment variable
     *
     * @param variable The name of the environment variable
     * @param deflt    The default value in case no value was found
     * @return The value of the environment variable or the value of deflt in case no variable was defined.
     */
    public static String getEnvironmentVariable(String variable, String deflt) {
        return System.getProperty(variable, deflt);
    }

    /**
     * Replaces environment variables in a string. For example if you set KETTLE_HOME as an environment variable, you can
     * use %%KETTLE_HOME%% in dialogs etc. to refer to this value. This procedures looks for %%...%% pairs and replaces
     * them including the name of the environment variable with the actual value. In case the variable was not set,
     * nothing is replaced!
     *
     * @param string The source string where text is going to be replaced.
     * @return The expanded string.
     * @deprecated use StringUtil.environmentSubstitute(): handles both Windows and unix conventions
     */
    @Deprecated
    public static String replEnv(String string) {
        if (string == null) {
            return null;
        }
        StringBuilder str = new StringBuilder(string);

        int idx = str.indexOf("%%");
        while (idx >= 0) {
            // OK, so we found a marker, look for the next one...
            int to = str.indexOf("%%", idx + 2);
            if (to >= 0) {
                // OK, we found the other marker also...
                String marker = str.substring(idx, to + 2);
                String var = str.substring(idx + 2, to);

                if (var != null && var.length() > 0) {
                    // Get the environment variable
                    String newval = getEnvironmentVariable(var, null);

                    if (newval != null) {
                        // Replace the whole bunch
                        str.replace(idx, to + 2, newval);
                        // System.out.println("Replaced ["+marker+"] with ["+newval+"]");

                        // The last position has changed...
                        to += newval.length() - marker.length();
                    }
                }

            } else {
                // We found the start, but NOT the ending %% without closing %%
                to = idx;
            }

            // Look for the next variable to replace...
            idx = str.indexOf("%%", to + 1);
        }

        return str.toString();
    }

    /**
     * Replaces environment variables in an array of strings.
     * <p>
     * See also: replEnv(String string)
     *
     * @param string The array of strings that wants its variables to be replaced.
     * @return the array with the environment variables replaced.
     * @deprecated please use StringUtil.environmentSubstitute now.
     */
    @Deprecated
    public static String[] replEnv(String[] string) {
        String[] retval = new String[string.length];
        for (int i = 0; i < string.length; i++) {
            retval[i] = Const.replEnv(string[i]);
        }
        return retval;
    }

    /**
     * Implements Oracle style NVL function
     *
     * @param source The source argument
     * @param def    The default value in case source is null or the length of the string is 0
     * @return source if source is not null, otherwise return def
     */
    public static String NVL(String source, String def) {
        if (source == null || source.length() == 0) {
            return def;
        }
        return source;
    }

    /**
     * Return empty string "" in case the given parameter is null, otherwise return the same value.
     *
     * @param source The source value to check for null.
     * @return empty string if source is null, otherwise simply return the source value.
     */
    public static String nullToEmpty(String source) {
        if (source == null) {
            return "";
        }
        return source;
    }

    /**
     * Search for a string in an array of strings and return the index.
     *
     * @param lookup The string to search for
     * @param array  The array of strings to look in
     * @return The index of a search string in an array of strings. -1 if not found.
     */
    public static int indexOfString(String lookup, String[] array) {
        if (array == null) {
            return -1;
        }
        if (lookup == null) {
            return -1;
        }

        for (int i = 0; i < array.length; i++) {
            if (lookup.equalsIgnoreCase(array[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Search for strings in an array of strings and return the indexes.
     *
     * @param lookup The strings to search for
     * @param array  The array of strings to look in
     * @return The indexes of strings in an array of strings. -1 if not found.
     */
    public static int[] indexsOfStrings(String[] lookup, String[] array) {
        int[] indexes = new int[lookup.length];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = indexOfString(lookup[i], array);
        }
        return indexes;
    }

    /**
     * Search for strings in an array of strings and return the indexes. If a string is not found, the index is not
     * returned.
     *
     * @param lookup The strings to search for
     * @param array  The array of strings to look in
     * @return The indexes of strings in an array of strings. Only existing indexes are returned (no -1)
     */
    public static int[] indexsOfFoundStrings(String[] lookup, String[] array) {
        List<Integer> indexesList = new ArrayList<Integer>();
        for (int i = 0; i < lookup.length; i++) {
            int idx = indexOfString(lookup[i], array);
            if (idx >= 0) {
                indexesList.add(Integer.valueOf(idx));
            }
        }
        int[] indexes = new int[indexesList.size()];
        for (int i = 0; i < indexesList.size(); i++) {
            indexes[i] = (indexesList.get(i)).intValue();
        }
        return indexes;
    }

    /**
     * Search for a string in a list of strings and return the index.
     *
     * @param lookup The string to search for
     * @param list   The ArrayList of strings to look in
     * @return The index of a search string in an array of strings. -1 if not found.
     */
    public static int indexOfString(String lookup, List<String> list) {
        if (list == null) {
            return -1;
        }

        for (int i = 0; i < list.size(); i++) {
            String compare = list.get(i);
            if (lookup.equalsIgnoreCase(compare)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Sort the strings of an array in alphabetical order.
     *
     * @param input The array of strings to sort.
     * @return The sorted array of strings.
     */
    public static String[] sortStrings(String[] input) {
        Arrays.sort(input);
        return input;
    }

    /**
     * Convert strings separated by a string into an array of strings.
     * <p>
     * <code>
     * Example: a;b;c;d    ==>    new String[] { a, b, c, d }
     * </code>
     * <p>
     * <p>
     * <b>NOTE: this differs from String.split() in a way that the built-in method uses regular expressions and this one
     * does not.</b>
     *
     * @param string    The string to split
     * @param separator The separator used.
     * @return the string split into an array of strings
     */
    public static String[] splitString(String string, String separator) {
    /*
     * 0123456 Example a;b;c;d --> new String[] { a, b, c, d }
     */
        // System.out.println("splitString ["+path+"] using ["+separator+"]");
        List<String> list = new ArrayList<String>();

        if (string == null || string.length() == 0) {
            return new String[]{};
        }

        int sepLen = separator.length();
        int from = 0;
        int end = string.length() - sepLen + 1;

        for (int i = from; i < end; i += sepLen) {
            if (string.substring(i, i + sepLen).equalsIgnoreCase(separator)) {
                // OK, we found a separator, the string to add to the list
                // is [from, i[
                list.add(NVL(string.substring(from, i), ""));
                from = i + sepLen;
            }
        }

        // Wait, if the string didn't end with a separator, we still have information at the end of the string...
        // In our example that would be "d"...
        if (from + sepLen <= string.length()) {
            list.add(NVL(string.substring(from, string.length()), ""));
        }

        return list.toArray(new String[list.size()]);
    }

    /**
     * Convert strings separated by a character into an array of strings.
     * <p>
     * <code>
     * Example: a;b;c;d    ==  new String[] { a, b, c, d }
     * </code>
     *
     * @param string    The string to split
     * @param separator The separator used.
     * @return the string split into an array of strings
     */
    public static String[] splitString(String string, char separator) {
        return splitString(string, separator, false);
    }

    /**
     * Convert strings separated by a character into an array of strings.
     * <p>
     * <code>
     * Example: a;b;c;d    ==  new String[] { a, b, c, d }
     * </code>
     *
     * @param string    The string to split
     * @param separator The separator used.
     * @param escape    in case the separator can be escaped (\;) The escape characters are NOT removed!
     * @return the string split into an array of strings
     */
    public static String[] splitString(String string, char separator, boolean escape) {
    /*
     * 0123456 Example a;b;c;d --> new String[] { a, b, c, d }
     */
        // System.out.println("splitString ["+path+"] using ["+separator+"]");
        List<String> list = new ArrayList<String>();

        if (string == null || string.length() == 0) {
            return new String[]{};
        }

        int from = 0;
        int end = string.length();

        for (int i = from; i < end; i += 1) {
            boolean found = string.charAt(i) == separator;
            if (found && escape && i > 0) {
                found &= string.charAt(i - 1) != '\\';
            }
            if (found) {
                // OK, we found a separator, the string to add to the list
                // is [from, i[
                list.add(NVL(string.substring(from, i), ""));
                from = i + 1;
            }
        }

        // Wait, if the string didn't end with a separator, we still have information at the end of the string...
        // In our example that would be "d"...
        if (from + 1 <= string.length()) {
            list.add(NVL(string.substring(from, string.length()), ""));
        }

        return list.toArray(new String[list.size()]);
    }

    /**
     * Convert strings separated by a string into an array of strings.
     * <p>
     * <code>
     * Example /a/b/c --> new String[] { a, b, c }
     * </code>
     *
     * @param path      The string to split
     * @param separator The separator used.
     * @return the string split into an array of strings
     */
    public static String[] splitPath(String path, String separator) {
        //
        // Example /a/b/c --> new String[] { a, b, c }
        //
        // Make sure training slashes are removed
        //
        // Example /a/b/c/ --> new String[] { a, b, c }
        //

        // Check for empty paths...
        //
        if (path == null || path.length() == 0 || path.equals(separator)) {
            return new String[]{};
        }

        // lose trailing separators
        //
        while (path.endsWith(separator)) {
            path = path.substring(0, path.length() - 1);
        }

        int sepLen = separator.length();
        int nr_separators = 1;
        int from = path.startsWith(separator) ? sepLen : 0;

        for (int i = from; i < path.length(); i += sepLen) {
            if (path.substring(i, i + sepLen).equalsIgnoreCase(separator)) {
                nr_separators++;
            }
        }

        String[] spath = new String[nr_separators];
        int nr = 0;
        for (int i = from; i < path.length(); i += sepLen) {
            if (path.substring(i, i + sepLen).equalsIgnoreCase(separator)) {
                spath[nr] = path.substring(from, i);
                nr++;

                from = i + sepLen;
            }
        }
        if (nr < spath.length) {
            spath[nr] = path.substring(from);
        }

        //
        // a --> { a }
        //
        if (spath.length == 0 && path.length() > 0) {
            spath = new String[]{path};
        }

        return spath;
    }

    /**
     * Split the given string using the given delimiter and enclosure strings.
     * <p>
     * The delimiter and enclosures are not regular expressions (regexes); rather they are literal strings that will be
     * quoted so as not to be treated like regexes.
     * <p>
     * This method expects that the data contains an even number of enclosure strings in the input; otherwise the results
     * are undefined
     *
     * @param stringToSplit the String to split
     * @param delimiter     the delimiter string
     * @param enclosure     the enclosure string
     * @return an array of strings split on the delimiter (ignoring those in enclosures), or null if the string to split
     * is null.
     */
    public static String[] splitString(String stringToSplit, String delimiter, String enclosure) {
        return splitString(stringToSplit, delimiter, enclosure, false);
    }

    /**
     * Split the given string using the given delimiter and enclosure strings.
     * <p>
     * The delimiter and enclosures are not regular expressions (regexes); rather they are literal strings that will be
     * quoted so as not to be treated like regexes.
     * <p>
     * This method expects that the data contains an even number of enclosure strings in the input; otherwise the results
     * are undefined
     *
     * @param stringToSplit   the String to split
     * @param delimiter       the delimiter string
     * @param enclosure       the enclosure string
     * @param removeEnclosure removes enclosure from split result
     * @return an array of strings split on the delimiter (ignoring those in enclosures), or null if the string to split
     * is null.
     */
    public static String[] splitString(String stringToSplit, String delimiter, String enclosure, boolean removeEnclosure) {

        ArrayList<String> splitList = null;

        // Handle "bad input" cases
        if (stringToSplit == null) {
            return null;
        }
        if (delimiter == null) {
            return (new String[]{stringToSplit});
        }

        // Split the string on the delimiter, we'll build the "real" results from the partial results
        String[] delimiterSplit = stringToSplit.split(Pattern.quote(delimiter));

        // At this point, if the enclosure is null or empty, we will return the delimiter split
        if (isEmpty(enclosure)) {
            return delimiterSplit;
        }

        // Keep track of partial splits and concatenate them into a legit split
        StringBuilder concatSplit = null;

        if (delimiterSplit != null && delimiterSplit.length > 0) {

            // We'll have at least one result so create the result list object
            splitList = new ArrayList<String>();

            // Proceed through the partial splits, concatenating if the splits are within the enclosure
            for (String currentSplit : delimiterSplit) {
                if (!currentSplit.contains(enclosure)) {

                    // If we are currently concatenating a split, we are inside an enclosure. Since this
                    // split doesn't contain an enclosure, we can concatenate it (with a delimiter in front).
                    // If we're not concatenating, the split is fine so add it to the result list.
                    if (concatSplit != null) {
                        concatSplit.append(delimiter);
                        concatSplit.append(currentSplit);
                    } else {
                        splitList.add(currentSplit);
                    }
                } else {
                    // Find number of enclosures in the split, and whether that number is odd or even.
                    int numEnclosures = StringUtils.countMatches(currentSplit, enclosure);
                    boolean oddNumberOfEnclosures = (numEnclosures % 2 != 0);
                    boolean addSplit = false;

                    // This split contains an enclosure, so either start or finish concatenating
                    if (concatSplit == null) {
                        concatSplit = new StringBuilder(currentSplit); // start concatenation
                        addSplit = !oddNumberOfEnclosures;
                    } else {
                        // Check to make sure a new enclosure hasn't started within this split. This method expects
                        // that there are no non-delimiter characters between a delimiter and a starting enclosure.

                        // At this point in the code, the split shouldn't start with the enclosure, so add a delimiter
                        concatSplit.append(delimiter);

                        // Add the current split to the concatenated split
                        concatSplit.append(currentSplit);

                        // If the number of enclosures is odd, the enclosure is closed so add the split to the list
                        // and reset the "concatSplit" buffer. Otherwise continue
                        addSplit = oddNumberOfEnclosures;
                    }
                    if (addSplit) {
                        String splitResult = concatSplit.toString();
                        //remove enclosure from resulting split
                        if (removeEnclosure) {
                            splitResult = removeEnclosure(splitResult, enclosure);
                        }

                        splitList.add(splitResult);
                        concatSplit = null;
                        addSplit = false;
                    }
                }
            }
        }

        // Return list as array
        return splitList.toArray(new String[splitList.size()]);
    }

    private static String removeEnclosure(String stringToSplit, String enclosure) {

        int firstIndex = stringToSplit.indexOf(enclosure);
        int lastIndex = stringToSplit.lastIndexOf(enclosure);
        if (firstIndex == lastIndex) {
            return stringToSplit;
        }
        StrBuilder strBuilder = new StrBuilder(stringToSplit);
        strBuilder.replace(firstIndex, enclosure.length() + firstIndex, "");
        strBuilder.replace(lastIndex - enclosure.length(), lastIndex, "");

        return strBuilder.toString();
    }

    /**
     * Sorts the array of Strings, determines the uniquely occurring strings.
     *
     * @param strings the array that you want to do a distinct on
     * @return a sorted array of uniquely occurring strings
     */
    public static String[] getDistinctStrings(String[] strings) {
        if (strings == null) {
            return null;
        }
        if (strings.length == 0) {
            return new String[]{};
        }

        String[] sorted = sortStrings(strings);
        List<String> result = new ArrayList<String>();
        String previous = "";
        for (int i = 0; i < sorted.length; i++) {
            if (!sorted[i].equalsIgnoreCase(previous)) {
                result.add(sorted[i]);
            }
            previous = sorted[i];
        }

        return result.toArray(new String[result.size()]);
    }

    /**
     * 返回指定异常的堆栈信息
     */
    public static String getStackTracker(Throwable e) {
        return getClassicStackTrace(e);
    }

    public static String getClassicStackTrace(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String string = stringWriter.toString();
        try {
            stringWriter.close();
        } catch (IOException ioe) {
            // is this really required?
        }
        return string;
    }

    /**
     * 获取当前异常的堆栈
     *
     * @param aThrowable
     * @return
     */
    public static String getCustomStackTrace(Throwable aThrowable) {
        final StringBuilder result = new StringBuilder();
        String errorMessage = aThrowable.toString();
        result.append(errorMessage);
        if (!errorMessage.contains(Const.CR)) {
            result.append(CR);
        }

        // add each element of the stack trace
        //
        for (StackTraceElement element : aThrowable.getStackTrace()) {
            result.append(element);
            result.append(CR);
        }
        return result.toString();
    }

    /**
     * Check if the CharSequence (String, StringBuffer, StringBuilder) supplied is empty.
     * A CharSequence is empty when it is null or when the length is 0
     *
     * @param string The string to check
     * @return true if the string supplied is empty
     */
    public static boolean isEmpty(CharSequence string) {
        return string == null || string.length() == 0;
    }

    /**
     * Check if the string array supplied is empty. A String array is empty when it is null or when the number of elements
     * is 0
     *
     * @param strings The string array to check
     * @return true if the string array supplied is empty
     */
    public static boolean isEmpty(CharSequence[] strings) {
        return strings == null || strings.length == 0;
    }

    /**
     * Check if the array supplied is empty. An array is empty when it is null or when the length is 0
     *
     * @param array The array to check
     * @return true if the array supplied is empty
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Check if the list supplied is empty. An array is empty when it is null or when the length is 0
     *
     * @param list the list to check
     * @return true if the supplied list is empty
     */
    public static boolean isEmpty(List<?> list) {
        return list == null || list.size() == 0;
    }

    /**
     * @return a new ClassLoader
     */
    public static ClassLoader createNewClassLoader() throws Exception {
        try {
            // Nothing really in URL, everything is in scope.
            URL[] urls = new URL[]{};
            URLClassLoader ucl = new URLClassLoader(urls);

            return ucl;
        } catch (Exception e) {
            throw new Exception("Unexpected error during classloader creation", e);
        }
    }

    /**
     * Utility class for use in JavaScript to create a new byte array. This is surprisingly difficult to do in JavaScript.
     *
     * @return a new java byte array
     */
    public static byte[] createByteArray(int size) {
        return new byte[size];
    }

    /**
     * Sets the first character of each word in upper-case.
     *
     * @param string The strings to convert to initcap
     * @return the input string but with the first character of each word converted to upper-case.
     */
    public static String initCap(String string) {
        StringBuilder change = new StringBuilder(string);
        boolean new_word;
        int i;
        char lower, upper, ch;

        new_word = true;
        for (i = 0; i < string.length(); i++) {
            lower = change.substring(i, i + 1).toLowerCase().charAt(0); // Lowercase is default.
            upper = change.substring(i, i + 1).toUpperCase().charAt(0); // Uppercase for new words.
            ch = upper;

            if (new_word) {
                change.setCharAt(i, upper);
            } else {
                change.setCharAt(i, lower);
            }

            new_word = false;

            // Cast to (int) is required for extended characters (SB)
            if (!Character.isLetterOrDigit((int) ch) && ch != '_') {
                new_word = true;
            }
        }

        return change.toString();
    }

    /**
     * Create a valid filename using a name We remove all special characters, spaces, etc.
     *
     * @param name The name to use as a base for the filename
     * @return a valid filename
     */
    public static String createFilename(String name) {
        StringBuilder filename = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isUnicodeIdentifierPart(c)) {
                filename.append(c);
            } else if (Character.isWhitespace(c)) {
                filename.append('_');
            }
        }
        return filename.toString().toLowerCase();
    }

    public static String createFilename(String directory, String name, String extension) {
        if (directory.endsWith(Const.FILE_SEPARATOR)) {
            return directory + createFilename(name) + extension;
        } else {
            return directory + Const.FILE_SEPARATOR + createFilename(name) + extension;
        }
    }

    public static String createName(String filename) {
        if (Const.isEmpty(filename)) {
            return filename;
        }

        String pureFilename = filenameOnly(filename);
        if (pureFilename.endsWith(".ktr") || pureFilename.endsWith(".kjb") || pureFilename.endsWith(".xml")) {
            pureFilename = pureFilename.substring(0, pureFilename.length() - 4);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pureFilename.length(); i++) {
            char c = pureFilename.charAt(i);
            if (Character.isUnicodeIdentifierPart(c)) {
                sb.append(c);
            } else if (Character.isWhitespace(c)) {
                sb.append(' ');
            } else if (c == '-') {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * <p>
     * Returns the pure filename of a filename with full path. E.g. if passed parameter is
     * <code>/opt/tomcat/logs/catalina.out</code> this method returns <code>catalina.out</code>. The method works with the
     * Environment variable <i>System.getProperty("file.separator")</i>, so on linux/Unix it will check for the last
     * occurrence of a frontslash, on windows for the last occurrence of a backslash.
     * </p>
     * <p>
     * <p>
     * To make this OS independent, the method could check for the last occurrence of a frontslash and backslash and use
     * the higher value of both. Should work, since these characters aren't allowed in filenames on neither OS types (or
     * said differently: Neither linux nor windows can carry frontslashes OR backslashes in filenames). Just a suggestion
     * of an improvement ...
     * </p>
     *
     * @param sFullPath
     * @return
     */
    public static String filenameOnly(String sFullPath) {
        if (Const.isEmpty(sFullPath)) {
            return sFullPath;
        }

        int idx = sFullPath.lastIndexOf(FILE_SEPARATOR);
        if (idx != -1) {
            return sFullPath.substring(idx + 1);
        } else {
            idx = sFullPath.lastIndexOf('/'); // URL, VFS/**/
            if (idx != -1) {
                return sFullPath.substring(idx + 1);
            } else {
                return sFullPath;
            }
        }

    }


    /**
     * Return the current time as nano-seconds.
     *
     * @return time as nano-seconds.
     */
    public static long nanoTime() {
        return new Date().getTime() * 1000;
    }


    /**
     * implemented to help prevent errors in matching up pluggable LAF directories and paths/files eliminating malformed
     * URLs - duplicate file separators or missing file separators.
     *
     * @param dir
     * @param file
     * @return concatenated string representing a file url
     */
    public static String safeAppendDirectory(String dir, String file) {
        boolean dirHasSeparator = ((dir.lastIndexOf(FILE_SEPARATOR)) == dir.length() - 1);
        boolean fileHasSeparator = (file.indexOf(FILE_SEPARATOR) == 0);
        if ((dirHasSeparator && !fileHasSeparator) || (!dirHasSeparator && fileHasSeparator)) {
            return dir + file;
        }
        if (dirHasSeparator && fileHasSeparator) {
            return dir + file.substring(1);
        }
        return dir + FILE_SEPARATOR + file;
    }

    /**
     * Create an array of Strings consisting of spaces. The index of a String in the array determines the number of spaces
     * in that string.
     *
     * @return array of 'space' Strings.
     */
    public static String[] getEmptyPaddedStrings() {
        if (emptyPaddedSpacesStrings == null) {
            emptyPaddedSpacesStrings = new String[250];
            for (int i = 0; i < emptyPaddedSpacesStrings.length; i++) {
                emptyPaddedSpacesStrings[i] = rightPad("", i);
            }
        }
        return emptyPaddedSpacesStrings;
    }

    /**
     * Return the percentage of free memory for this JVM.
     *
     * @return Percentage of free memory.
     */
    public static int getPercentageFreeMemory() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long totalFreeMemory = (freeMemory + (maxMemory - allocatedMemory));

        return (int) Math.round(100 * (double) totalFreeMemory / maxMemory);
    }

    /**
     * Return non digits only.
     *
     * @return non digits in a string.
     */

    public static String removeDigits(String input) {
        if (Const.isEmpty(input)) {
            return null;
        }
        StringBuilder digitsOnly = new StringBuilder();
        char c;
        for (int i = 0; i < input.length(); i++) {
            c = input.charAt(i);
            if (!Character.isDigit(c)) {
                digitsOnly.append(c);
            }
        }
        return digitsOnly.toString();
    }

    /**
     * Return digits only.
     *
     * @return digits in a string.
     */
    public static String getDigitsOnly(String input) {
        if (Const.isEmpty(input)) {
            return null;
        }
        StringBuilder digitsOnly = new StringBuilder();
        char c;
        for (int i = 0; i < input.length(); i++) {
            c = input.charAt(i);
            if (Character.isDigit(c)) {
                digitsOnly.append(c);
            }
        }
        return digitsOnly.toString();
    }

    /**
     * Remove time from a date.
     *
     * @return a date without hour.
     */
    public static Date removeTimeFromDate(Date input) {
        if (input == null) {
            return null;
        }
        // Get an instance of the Calendar.
        Calendar calendar = Calendar.getInstance();

        // Make sure the calendar will not perform automatic correction.
        calendar.setLenient(false);

        // Set the time of the calendar to the given date.
        calendar.setTime(input);

        // Remove the hours, minutes, seconds and milliseconds.
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Return the date again.
        return calendar.getTime();
    }

    /**
     * Escape XML content. i.e. replace characters with &values;
     *
     * @param content content
     * @return escaped content
     */
    public static String escapeXML(String content) {
        if (isEmpty(content)) {
            return content;
        }
        return StringEscapeUtils.escapeXml(content);
    }

    /**
     * Escape HTML content. i.e. replace characters with &values;
     *
     * @param content content
     * @return escaped content
     */
    public static String escapeHtml(String content) {
        if (isEmpty(content)) {
            return content;
        }
        return StringEscapeUtils.escapeHtml(content);
    }

    /**
     * UnEscape HTML content. i.e. replace characters with &values;
     *
     * @param content content
     * @return unescaped content
     */
    public static String unEscapeHtml(String content) {
        if (isEmpty(content)) {
            return content;
        }
        return StringEscapeUtils.unescapeHtml(content);
    }

    /**
     * UnEscape XML content. i.e. replace characters with &values;
     *
     * @param content content
     * @return unescaped content
     */
    public static String unEscapeXml(String content) {
        if (isEmpty(content)) {
            return content;
        }
        return StringEscapeUtils.unescapeXml(content);
    }

    /**
     * Escape SQL content. i.e. replace characters with &values;
     *
     * @param content content
     * @return escaped content
     */
    public static String escapeSQL(String content) {
        if (isEmpty(content)) {
            return content;
        }
        return StringEscapeUtils.escapeSql(content);
    }

    /**
     * Remove CR / LF from String - Better performance version
     * - Doesn't NPE
     * - 40 times faster on an empty string
     * - 2 times faster on a mixed string
     * - 25% faster on 2 char string with only CRLF in it
     *
     * @param in input
     * @return cleaned string
     */
    public static String removeCRLF(String in) {
        if ((in != null) && (in.length() > 0)) {
            int inLen = in.length(), posn = 0;
            char[] tmp = new char[inLen];
            char ch;
            for (int i = 0; i < inLen; i++) {
                ch = in.charAt(i);
                if ((ch != '\n' && ch != '\r')) {
                    tmp[posn] = ch;
                    posn++;
                }
            }
            return new String(tmp, 0, posn);
        } else {
            return "";
        }
    }

    /**
     * Remove Character from String - Better performance version
     * - Doesn't NPE
     * - 40 times faster on an empty string
     * - 2 times faster on a mixed string
     * - 25% faster on 2 char string with only CR/LF/TAB in it
     *
     * @param in input
     * @return cleaned string
     */
    public static String removeChar(String in, char badChar) {
        if ((in != null) && (in.length() > 0)) {
            int inLen = in.length(), posn = 0;
            char[] tmp = new char[inLen];
            char ch;
            for (int i = 0; i < inLen; i++) {
                ch = in.charAt(i);
                if (ch != badChar) {
                    tmp[posn] = ch;
                    posn++;
                }
            }
            return new String(tmp, 0, posn);
        } else {
            return "";
        }
    }

    /**
     * Remove CR / LF from String
     *
     * @param in input
     * @return cleaned string
     */
    public static String removeCR(String in) {
        return removeChar(in, '\n');
    } // removeCR

    /**
     * Remove CR / LF from String
     *
     * @param in input
     * @return cleaned string
     */
    public static String removeLF(String in) {
        return removeChar(in, '\r');
    } // removeCRLF

    /**
     * Remove horizontal tab from string
     *
     * @param in input
     * @return cleaned string
     */
    public static String removeTAB(String in) {
        return removeChar(in, '\t');
    }

    /**
     * Add time to an input date
     *
     * @param input      the date
     * @param time       the time to add (in string)
     * @param DateFormat the time format
     * @return date = input + time
     */
    public static Date addTimeToDate(Date input, String time, String DateFormat) throws Exception {
        if (isEmpty(time)) {
            return input;
        }
        if (input == null) {
            return null;
        }
        String dateformatString = NVL(DateFormat, "HH:mm:ss");
        int t = decodeTime(time, dateformatString);
        return new Date(input.getTime() + t);
    }

    // Decodes a time value in specified date format and returns it as milliseconds since midnight.
    public static int decodeTime(String s, String DateFormat) throws Exception {
        SimpleDateFormat f = new SimpleDateFormat(DateFormat);
        TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
        f.setTimeZone(utcTimeZone);
        f.setLenient(false);
        ParsePosition p = new ParsePosition(0);
        Date d = f.parse(s, p);
        if (d == null) {
            throw new Exception("Invalid time value " + DateFormat + ": \"" + s + "\".");
        }
        return (int) d.getTime();
    }

    /**
     * Get the number of occurrences of searchFor in string.
     *
     * @param string    String to be searched
     * @param searchFor to be counted string
     * @return number of occurrences
     */
    public static int getOccurenceString(String string, String searchFor) {
        if (string == null || string.length() == 0) {
            return 0;
        }
        int counter = 0;
        int len = searchFor.length();
        if (len > 0) {
            int start = string.indexOf(searchFor);
            while (start != -1) {
                counter++;
                start = string.indexOf(searchFor, start + len);
            }
        }
        return counter;
    }

    public static String[] GetAvailableFontNames() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font[] fonts = ge.getAllFonts();
        String[] FontName = new String[fonts.length];
        for (int i = 0; i < fonts.length; i++) {
            FontName[i] = fonts[i].getFontName();
        }
        return FontName;
    }

    /**
     * Mask XML content. i.e. protect with CDATA;
     *
     * @param content content
     * @return protected content
     */
    public static String protectXMLCDATA(String content) {
        if (isEmpty(content)) {
            return content;
        }
        return "<![CDATA[" + content + "]]>";
    }

    /**
     * Get the number of occurrences of searchFor in string.
     *
     * @param string    String to be searched
     * @param searchFor to be counted string
     * @return number of occurrences
     */
    public static int getOcuranceString(String string, String searchFor) {
        if (string == null || string.length() == 0) {
            return 0;
        }
        Pattern p = Pattern.compile(searchFor);
        Matcher m = p.matcher(string);
        int count = 0;
        while (m.find()) {
            ++count;
        }
        return count;
    }

    /**
     * Mask XML content. i.e. replace characters with &values;
     *
     * @param content content
     * @return masked content
     */
    public static String escapeXml(String content) {
        if (isEmpty(content)) {
            return content;
        }
        return StringEscapeUtils.escapeXml(content);
    }

    /**
     * New method avoids string concatenation is between 20% and > 2000% faster
     * depending on length of the string to pad, and the size to pad it to.
     * For larger amounts to pad, (e.g. pad a 4 character string out to 20 places)
     * this is orders of magnitude faster.
     *
     * @param valueToPad the string to pad
     * @param filler     the pad string to fill with
     * @param size       the size to pad to
     * @return the new string, padded to the left
     * <p>
     * Note - The original method was flawed in a few cases:
     * <p>
     * 1- The filler could be a string of any length - and the returned
     * string was not necessarily limited to size. So a 3 character pad
     * of an 11 character string could end up being 17 characters long.
     * 2- For a pad of zero characters ("") the former method would enter
     * an infinite loop.
     * 3- For a null pad, it would throw an NPE
     * 4- For a null valueToPad, it would throw an NPE
     */
    public static String Lpad(String valueToPad, String filler, int size) {
        if ((size == 0) || (valueToPad == null) || (filler == null)) {
            return valueToPad;
        }
        int vSize = valueToPad.length();
        int fSize = filler.length();
        // This next if ensures previous behavior, but prevents infinite loop
        // if "" is passed in as a filler.
        if ((vSize >= size) || (fSize == 0)) {
            return valueToPad;
        }
        int tgt = (size - vSize);
        StringBuilder sb = new StringBuilder(size);
        sb.append(filler);
        while (sb.length() < tgt) {
            // instead of adding one character at a time, this
            // is exponential - much fewer times in loop
            sb.append(sb);
        }
        sb.append(valueToPad);
        return sb.substring(Math.max(0, sb.length() - size)); // this makes sure you have the right size string returned.
    }

    /**
     * New method avoids string concatenation is between 50% and > 2000% faster
     * depending on length of the string to pad, and the size to pad it to.
     * For larger amounts to pad, (e.g. pad a 4 character string out to 20 places)
     * this is orders of magnitude faster.
     *
     * @param valueToPad the string to pad
     * @param filler     the pad string to fill with
     * @param size       the size to pad to
     * @return The string, padded to the right
     * <p>
     * 1- The filler can still be a string of any length - and the returned
     * string was not necessarily limited to size. So a 3 character pad
     * of an 11 character string with a size of 15 could end up being 17
     * characters long (instead of the "asked for 15").
     * 2- For a pad of zero characters ("") the former method would enter
     * an infinite loop.
     * 3- For a null pad, it would throw an NPE
     * 4- For a null valueToPad, it would throw an NPE
     */
    public static String Rpad(String valueToPad, String filler, int size) {
        if ((size == 0) || (valueToPad == null) || (filler == null)) {
            return valueToPad;
        }
        int vSize = valueToPad.length();
        int fSize = filler.length();
        // This next if ensures previous behavior, but prevents infinite loop
        // if "" is passed in as a filler.
        if ((vSize >= size) || (fSize == 0)) {
            return valueToPad;
        }
        int tgt = (size - vSize);
        StringBuilder sb1 = new StringBuilder(size);
        sb1.append(filler);
        while (sb1.length() < tgt) {
            // instead of adding one character at a time, this
            // is exponential - much fewer times in loop
            sb1.append(sb1);
        }
        StringBuilder sb = new StringBuilder(valueToPad);
        sb.append(sb1);
        return sb.substring(0, size);
    }

    public static boolean classIsOrExtends(Class<?> clazz, Class<?> superClass) {
        if (clazz.equals(Object.class)) {
            return false;
        }
        return clazz.equals(superClass) || classIsOrExtends(clazz.getSuperclass(), superClass);
    }

    /**
     * 交换List集合里的元素下标
     *
     * @param list
     * @param index1
     * @param index2
     * @param <T>
     * @return
     */
    public static <T> List<T> indexExChange(List<T> list, int index1, int index2) {
        T t = list.get(index1);
        list.set(index1, list.get(index2));
        list.set(index2, t);
        return list;
    }

    /**
     * JSONArray转为List
     *
     * @param array json数组
     * @return 转化后的List
     */
    public static List<Object> toList(JSONArray array) {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.size(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    /**
     * JSONObject转为map
     *
     * @param object json对象
     * @return 转化后的Map
     */
    public static Map<String, Object> toMap(JSONObject object) {
        Map<String, Object> map = new HashMap<String, Object>();

        for (String key : object.keySet()) {
            Object value = object.get(key);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }

        return map;
    }

    public void testurl() {
        //获取classpath路径
        System.out.println("classpath路径： " + Const.class.getClassLoader().getResource("").getPath());

        //获取当前类的加载路径
        System.out.println("当前类加载路径： " + Const.class.getResource("").getPath());

        // 读取文件resources目录中文件的若干种方法
        // 方法一：从classpath路径出发读取
//        readTxt(Demo1.class.getClassLoader().getResource("test/demo1.txt").getPath());
        // 方法二：从类加载路径出发，相当于使用绝对路径
//       readTxt(Const.class.getResource("/test/demo1.txt").getPath());
        // 方法三：从类加载路径出发，相当于使用相对路径
//       readTxt(Const.class.getResource("../../../test/demo1.txt").getPath());
    }

    /**
     * 获取服务器ip地址
     *
     * @return
     */
    public static String getlocalip() {
        try {
            return InetAddress.getLocalHost().getHostAddress().toString();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "";
    }



}
