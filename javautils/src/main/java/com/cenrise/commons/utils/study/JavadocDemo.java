package com.cenrise.commons.utils.study;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.Map.Entry;

/**
 * 一例讲述lang,util,text包50种高级用法
 * @author Admin
 *
 */
/* 不是故意卖弄而是展示各个知识点
 * Title: XXXX DRIVER 3.0
 *Description: XXXX DRIVER 3.0
 *Copyright: Copyright (c) 2003
 *Company:XXXX有限公司
 *@deprecated
 *@since JDK1.2  可以运行的环境
 *
 * html标签 http://www.w3school.com.cn/tags/tag_blockquote.asp
 * @link标记
 语法：{@link package.class#member label}
 Label为链接文字。
 package.class#member将被自动转换成指向package.class的member文件的URL。
 * 
 * */
/* 伪代码   * @docRoot    @see   @seriaData   @serialField
 * 这是我为大家演示的一个javadco的例子，目的是让你们一下就能熟悉javadoc
 * <p>
 * 这个<code>JavadocDemo</code>是一个基类，将实现接口和继承抽象类，会对一些极少的操作
 * 属性和方法列举出来，主要是为了扫清知识死角
 * <p> javadoc是支持html标记格式的，
 * <pre><blockquote>
 *   public void show()
 *   {
 *      System.out.println(new Data());
 *   }
 * </blockquote>
 * </pre>
 *  * <ol>
 * <li>
 * <li>
 * </ol>
 * 
 * 这里采用XX，详请 @see java.util.List#method
 * 
 * @version 1.0 创建时间 2013-04-14
 * @author   张林 563143188@qq.com
 * @since JDK1.3
 * 
 * */

/*
 * 注意javadoc 只能为public（公共）和protected（受保护）成员处理注释文档。
 * “private”（私有）和“友好”（成员的注释会被忽略，
 * 不要将<h1>或<hr>这样的标题当作嵌入HTML 使用，因为
 javadoc 会插入自己的标题，我们给出的标题会与之冲撞。
 所有类型的注释文档——类、变量和方法——都支持嵌入HTML。
 * */

/*文件多线程、网络放在下一篇写*/

public class JavadocDemo {
	public enum OperatorType {
		all, mathOperator, bigMathOperator, bitOperator, listOperator, systemOperator, stringOperator, md5Operator
	}

	public static int rsCount = 0; // 记录递归的次数，放在接口里面

	/*
	 * @param a 传值
	 * 
	 * @return 执行的状态
	 * 
	 * @exception 没有异常抛出或者有
	 */

	public static boolean mathOperator() {
		rsCount = rsCount + 1;

		Random rnd = new Random();// 产生0到1之间的随机数

		int intRnd = (new Random()).nextInt(Integer.MAX_VALUE % 1000);// 等同于这个rnd.nextInt(10);
		// 整形的要在括号里面给种子
		double dblRnd = rnd.nextLong() * intRnd;// long, float
		// double获得随机数都是在外围*一个常数

		pln("第" + rsCount + "次递归的 随机值：" + intRnd); // 递归是先释放最外层的然后再释放里面层

		boolean isSuccess = (intRnd < 500) ? mathOperator() : true; // 三元表达式递归
		// ，右边一定要返回值
		if (isSuccess) {
			// 分割用正则表达式
			char chrArr[] = itbi(intRnd).toCharArray();// 将整形转为二进制再给字符数组
			StringBuffer strBin = new StringBuffer("二进制码：");

			for (char chrTemp : chrArr) {
				strBin.append(chrTemp + ",");
			}
			pln(strBin.toString());
			// StringTolen将,拆分为空格，也可以用split函数
			StringTokenizer stoke = new StringTokenizer(strBin.toString(), ",");
			List<String> lisStoke = new ArrayList<String>();
			while (stoke.hasMoreElements()) // 判断是否还有元素
			{
				// nextToken() 返回字符串，nextElement返回 对象
				lisStoke.add(stoke.nextElement().toString());
			}
			// ListIterator有hasPrevious()和previous()方法，可以实现逆向（顺序向前）遍历。对象的修改set()方法可以实现。
			// 另一个地方有，有一定条件向前，在一定条件向后，排序吧
			ListIterator<String> itStoke = lisStoke.listIterator();
			while (itStoke.hasNext()) {
				pln(itStoke.next().toString() + " ");
			}

		}
		return isSuccess;

	}

	public static void bitOperator() {
		int intNum = 124 % (-846 % 1000); // 负数求模是 (1000-864) 求法 7 -2 ≡ 7 + 10
		// (mod 12)
		float fltNum = 124.10f;
		do {
			// 正数求反码是加负号还减1，如果是负数求反码是绝对值-1
			pln(" 整数后一位小数保留一位 127.0f%3=" + (127.0f % 3) + "   有两位小数保留浮点位精度："
					+ fltNum + "%3=" + (fltNum % 3));
			pln("正数的原码、反码、补码都是一致的");
			pln(intNum + "  的原码值:" + String.format("%025d", 0) + itbi(intNum));
			pln(intNum + "  的反码值:" + String.format("%025d", 0) + itbi(intNum));
			pln(intNum + "  的补码值:" + String.format("%025d", 0) + itbi(intNum));
			pln("负数的原码最高位为1、反码(在其原码的基础上, 符号位不变，其余各个位取反)、\n补码(在其原码的基础上, 符号位不变, 其余各位取反, 最低位+1)");
			pln((-intNum) + " 的原码值:1" + String.format("%024d", 0)
					+ itbi(intNum));
			pln((~intNum + 1) + " 的反码值:" + itbi((~intNum) + 1));
			pln((~intNum + 1) + " 的补码值:" + itbi((~intNum) + 2));

			pln("位移的运算   1.左移后，低位会被自动补零（0）  2.右移如果原值是正数，则高位补上0；如果原值是负数，高位补1。");

			pln(intNum + "位移操作前的二进制码是       " + itbi(intNum));
			pln(intNum + "位移操作前的八进制码是       " + Integer.toOctalString(intNum));
			pln(intNum + "位移操作前十六进制码是       " + Integer.toHexString(intNum));
			pln(intNum + "位移操作前的二进制码是       " + itbi(intNum));
			pln(intNum + ">>有符号右移一位" + (intNum >> 1) + "  二进制码         "
					+ itbi(intNum >> 1));
			pln(intNum + ">>有符号左移一位" + (intNum << 1) + " 二进制码    "
					+ itbi(intNum << 1));
			pln("八进制174转十进制是:" + Integer.valueOf("174", 8) + " 十六进制7C转十进制是:"
					+ Integer.valueOf("7C", 16));
			pln("采用0补充，意思就是说，无论是正号还是负号，都在高位补0");
			pln(intNum + ">>>无符号右移一位" + (intNum >>> 1) + " 二进制码         "
					+ itbi(intNum >>> 1));

		} while (false);
	}

	// 取到内存中的类，缓存，并修改后然后调用改变数据类型 动态代理 回调，递归，迭代 类加载器
	// stack用法 list ,map的例子，数组
	// 接口，反射类的方法做 访问权限 组合，继承 runtime ,system.getp 代理放到后面 匿名类 instance，
	// 链表，递归，排序 多维数组 位疑惑权限， ，对象的比较

	/*
	 * 如果我们看到了implements子句。正如我们看到的，Comparable类通常被定义为可以同自己比较的。而且他们的子类
	 * 也可以同他们的父类比较。从另一个方面将，Enum实现了Comparable接口不是为了他本身，而是为了他的子类 E。 treeMap stack
	 * hashTable 写一个自己排序的方向 数据逆向反转
	 * 
	 * 大数据查询排序 请使用LinkedList来模拟一个队列（先进先出的特性）。拥有 放入对象的方法void put(Object o)、
	 * 取出对象的方法Object get()、 判断队列当中是否为空的方法boolean isEmpty()；
	 * 并且，编写测试代码，验证你的队列是否正确。 Calendar 字符转换，转码 Expression
	 */

	public static void listOperator() {
		// 通过list接口向下扩展类
		Class<List> clst = List.class;
		// 上界
		Class<? extends List> subList = LinkedList.class.asSubclass(clst);
		// Class.forName("java.lang.LinkedList").asSubclass(clist)
		// .newInstance(); // 这样加载一个对象
		Stack<?> st = new Stack();
		Iterator<?> it = st.iterator();

		Array ar;
		List v = new Vector();
		List lst = new ArrayList();
		Set hSet = new HashSet();
		Set tSet = new TreeSet();

		Map<?, ?> tmap = new TreeMap();
		Map hMap = new HashMap();
		Map htMap = new Hashtable();

		Iterator it1;
		ListIterator lit;
	}

	// 高精度运算，浮点等等 指数 货币转换 科学计算法 内存交互 模拟矩阵 解方程

	/*
	 * java.math是一个包,执行任意精度整数算法 (BigInteger)和任意精度小数算法 (BigDecimal)类用于专业数学运算.
	 * java.lang.Math是一个类，类包含基本的数字操作，如指数、对数、平方根和三角函数。货币
	 */
	public static void bigMathOperator() {
		// ceil向上取整，返回double floor向下取整，返回double rint 返回近似数
		// round 是对其加0.5四舍五入求整
		// int number = 10 + (int) (Math.random() * 10); 如果要得到一个(10, 20]之间的随机整数：
		double maxValue = Math.max(Math.floor(Math.ceil(Math.tan(Math.sin(50)
				/ Math.cos(30)))
				+ Math.rint(Math.round(Math.PI) + Math.E)), (10 + (int) (Math
				.random() * 10)));
		double expValue = Math.pow(Math.E, 2);// Math.E的二次方
		// 指数用对数表示 Math.exp求e的任意次方 log_8(2)对数表示为 Math.log(8)/Math.log(2)
		pln("exp(Math.PI)=" + Math.exp(Math.PI)
				+ "  Math.pow(2,Math.PI*log_2(E))="
				+ Math.pow(2, Math.PI * (Math.log(Math.E) / Math.log(2))));

		/*
		 * java.math.BigInteger(大整数) java.math.BigDecimal(大浮点数)
		 * BigDecimal.setScale()方法用于格式化小数点 setScale(1)表示保留一位小数，默认用四舍五入方式
		 * setScale(1,BigDecimal.ROUND_DOWN)直接删除多余的小数位，如2.35会变成2.3
		 * setScale(1,BigDecimal.ROUND_UP)进位处理，2.35变成2.4
		 * setScale(1,BigDecimal.ROUND_HALF_UP)四舍五入，2.35变成2.4
		 * setScaler(1,BigDecimal.ROUND_HALF_DOWN)四舍五入，2.35变成2.3，如果是5则向下舍
		 */
		BigDecimal bdValue = new BigDecimal(Double.MAX_VALUE);
		pln("直接删除三位以后的小数位："
				+ (new BigDecimal(Math.PI)).setScale(3, BigDecimal.ROUND_DOWN));
		pln("打印double的全部值并加两个小位数：" + bdValue.setScale(2).toEngineeringString());

		pln("(大精度运算) Math.PI*Math.E = " + mul(Math.PI, Math.E));
		pln("(大精度运算) Double.MAX_VALUE/Float.MAX_VALUE = "
				+ new BigDecimal(div(Double.MAX_VALUE, Float.MAX_VALUE, 3)));

		// DecimalFormat 与科学计算法 E20后面表示的10的20次方  如果是E-20则表是是 10的(1/N)次方
		 
        pln("分解科学计算法表示的：3.1415E-20的值="+new BigDecimal("3.1415E-20").toPlainString() );
        pln("分解科学计算法表示的：3.1415E20 的值="+new BigDecimal("3.1415E20").toPlainString() );
	    //分解科学计算法表示的：3.1415E-20的值=0.000000000000000000031415
        //分解科学计算法表示的：3.1415E20 的值=0.000000000000000000031415
	}

	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */

	public static double mul(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * 
	 * @param v1
	 *            被除数
	 * @param v2
	 *            除数
	 * @param scale
	 *            表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */

	public static double div(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The   scale   must   be   a   positive   integer   or   zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	// Thread t = new ShutdownHookThread ( "Here I am !!!" );

	// Runtime.getRuntime().addShutdownHook ( t );

	// System.out.println ( "Now shut me down …");
	public static void md5Operator() {
		// 加密后，然后解密
	}

	public static void stringOperator() {
		Charset dCharset = java.nio.charset.Charset.defaultCharset();
		pln("当前环境编码：" + dCharset.name());
		StringBuilder sber = new StringBuilder("StringBuilder是非线程安全的,");
		sber.append("StringBuffer是线程安全的");
		sber.append("两种用法差不多,很多方法与string一样");
		sber.append("如果不考虑线程安全用StringBuiler ");

		CharSequence chrSeq = sber.subSequence(60, sber.length() - 1);
		pln(chrSeq.toString());
		String strTemp = "53 48 07 03 0B 43 50 C6 00 00 67";// 一条机器执行状态返回指令
		String testResult = ((strTemp.indexOf("C6") >= 0) ? true : false) ? "执行成功"
				: "执行失败";
		String isOkCode = strTemp.split(" ")[(strTemp.lastIndexOf("C6") / 3) % 21];
		pln("这次测试".concat(testResult.intern()) + " 正确代码：" + isOkCode);
		pln("仪器型号："
				+ strTemp.substring(strTemp.indexOf("53"), strTemp
						.indexOf("48")));
		pln("指令帧长:" + strTemp.charAt(7));
		pln(" 指令效验合:" + strTemp.substring(strTemp.lastIndexOf("00") + 3));

	}

	// secrurity rmi net beans 合用一个例子
	// Preferences pf = Preferences.systemRoot(); pln(pf.absolutePath());//
	public static void systemOperator() {
		String temp = "自1970年1月1日到现在的毫秒数:";
		pln(temp + System.currentTimeMillis());
		Properties sysPorp = System.getProperties(); // 获得系统属性
		Set<Entry<Object, Object>> sysSet = sysPorp.entrySet(); // 1.用Set的entry方式取信息
		// 2.用map的方式读取 通过下界通配符定义 key值
		Map<? super String, Object> sysMap = new HashMap<String, Object>();

		Iterator<Entry<Object, Object>> sysIt = sysSet.iterator();// 同理：sysMap.entrySet().iterator();
		for (; sysIt.hasNext();) {
			Map.Entry<Object, Object> sysEt = (Entry<Object, Object>) sysIt
					.next();
			sysMap.put(sysEt.getKey().toString(), sysEt.getValue());
		}
		// 2.用map的方式读取 通过上界通配符定义
		Enumeration<? extends Object> sysEm = sysPorp.propertyNames();
		while (sysEm.hasMoreElements()) {
			Object obj = sysEm.nextElement();
			// pln(obj + "=" + sysPorp.get(obj));// 通过枚举打印所有的系统参数
		}
		// new Properties(System.getProperties()).list(System.out); 3.用输出流的方法
		// scanner一般都是扫描控制台或者扫描文件
		Scanner scer = new Scanner(System.in);

		do {
			System.out.println("请输入命令是否要继续下面的操作(1/0)：");
			Scanner scer1 = new Scanner("张林,好人嘛,你只能在这输入1才能继续下面的操作");
			scer1.useDelimiter(",");
			while (scer1.hasNext())
				pln(scer1.next());
			String line = scer.nextLine();
			if (line.equals("1"))
				break;
			pln(">>>" + line);
		} while (true);

		try {
			Thread.currentThread();
			Thread.sleep((new Random()).nextInt(1000));
			try {
				String[] cmd = { "cmd", "/C", "copy exe1 exe2" };
				Process proc = Runtime.getRuntime().exec(cmd);

				// int exitVal = proc.waitFor();
				// System.out.println("Process exitValue: " + exitVal);
				// Runtime.getRuntime().exit(0); 这个是直接结束jvm，不会执行finally
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			// Calendar.getInstance() 是静态方法，可以直接调用，不用new
			pln(temp + Calendar.getInstance().getTimeInMillis());
			Runtime rt = Runtime.getRuntime();
			pln("系统内存大小清理前：" + rt.totalMemory() + " 空闲内存大小清理前："
					+ rt.freeMemory());
			rt.gc(); // 手工垃圾清理
			pln("系统内存大小清理后：" + rt.totalMemory() + " 空闲内存大小清理后："
					+ rt.freeMemory());

		}

	}

	public static void main(String args[]) {
		// + " 进入函数时间：" + System.currentTimeMillis() +
		// new JavadocDemo().testMath(); // 匿名调用，不用实例调用
		// bitOperator();// 用位置到最后，逻辑异或。走到偶数位,科学计数
		// listOperator();
		// systemOperator();
		// 匿名类内部类 知识点只是希望大家能够知道有这些知识点，然后有兴趣去深入
		// 这里需要一个字符串操作的类，spilt的正则表达式方法
		// java 加密解码 这样省字符 这里加上抽象接口，并用上代理，工厂模式
		switchOperator(OperatorType.bigMathOperator);
	}

	// 通过二进制位置确定操作那几个方法 ，与或关系来解决
	public static void switchOperator(OperatorType opt) {
		switch (opt) { // 条件只是整型或者字符型 枚举可以，浮点型不行
		case all:// 可以带表达式，但不能是变量
			break;
		case mathOperator:
			mathOperator();
			break;
		case bitOperator:
			bitOperator();
			break;
		case listOperator:
			listOperator();
			break;
		case systemOperator:
			systemOperator();
			break;
		case stringOperator:
			stringOperator();
			break;
		case md5Operator:
			md5Operator();
			break;
		case bigMathOperator:
			bigMathOperator();
			break;
		default:
			System.out.println("没有该类型的操作");
		}
	}

	public static void pln(String str) {
		System.out.println(str);
	}

	public static String itbi(int x) {
		return Integer.toBinaryString(x);
	}

	protected void finalize() {
		System.out.println("清理系统垃圾");
	}
}

// 动态创建了两个对象 (一个s对象，一个"This is a string"对象)
// String s = new String("This is a string");
/*
 * 使用静态方式创建的字符串，在堆内存的缓冲池中只会产生唯一的一个字符串对象。
 * 当使用该方式产生同样一个字符串时，堆内存中不再开辟另外一块空间，而是两个引用变量指向同一个字符串对象。
 */

// 所谓数float,double据长度是指像465465.4 这是八个数据长度0.465465是八个数据长度、
// 所以 小数点后多少位并不是很准确 ,整数位和小数位总长是8位
// char cValue = (char) (iValue + '1'); // 先将'1'转化为49
// /（因为char是16位，int是32位，所以one会转化为int 1值,而不会同C、C++那样转为asci的49）
// (char)(3+49)=ansi(52)=4

/*
 * 上界通配符： – 泛型中只允许类自身或者其子类作为参数传入。 – 表示方式为：泛型类型<? extends 父类> 。 • 下界通配符： –
 * 泛型中只允许类自身或者该类的父类作为参数传入。 – 表示方式为：泛型类型<? super 子类>。
 */

// utf-8是3个字节

// UTF-32 是4个字节 32位 'A' utf-8 与 uft-16 表示 'a' a的ascii是0X61 utf-8为[0X61]
// uft-16 [0x00,0X61]

// unicode是国际通用编码 这是最统一的编码，可以用来表示所有语言的字符，而且是定长双字节（也有四字节的）编码
// .utf-8编码是unicode编码在网络之间（主要是网页）传输时的一种“变通”和“桥梁”编码。
// utf-8在网络之间传输时可以节约数据量。所以，使用操作系统无法搜索出txt文本。
// 。其中gbk编码能够用来同时表示繁体字和简体字，而gb2312只能表示简体字，gbk是兼容gb2312编码的。
// 运行时类型信息（RunTime Type Information，RTTI）使得你在程序运行时发现和使用类型
// 信息。RTTI主要用来运行时获取向上转型之后的对象到底是什么具体的类型。
// Class对象的创建发生在类加载（java.lang.ClassLoader）的时候。
// java.lang.Class类实现了Serializable、GenericDeclaration、Type、AnnotatedElement四个接口，
// 分别实现了可序列化、泛型定义、类型、元数据（注解）的功能。
// Class只有一个私有的无参构造方法，也就是说Class的对象创建只有JVM可以完成。 通过代理实现
// 如何通过jndi连接数据源 string 格式化输出 正则表达式 java命名规范 谈谈编码规范
// extends T是我们熟悉的上界绑定：这意味着T或者其子类。? super T比较少用：这意味着T或者他的超类。

/*
 * double maxValue = Double.MAX_VALUE; float fValue = 0.465465f; // 可以是 465465.4
 * 也可以是0.465465的精度，是由长长度决定的共7七位 double dValue = 3.1415926; //
 * 带小数位没有f,d则默认为double类型也就是默认加 d 整 型 变 量 的 类 型 有 byte、 short、 int、 long四 种 byte
 * 1(字节) short 2 int 4 long 8 float 4 double 8 char 2
 */
class SystemInfo {
	/* Windows security masks */
	private final static int KEY_QUERY_VALUE = 1;

	/* Constants used to interpret returns of native functions */
	private final static int NATIVE_HANDLE = 0;
	private final static int ERROR_CODE = 1;

	/* Windows error codes. */
	private final static int ERROR_SUCCESS = 0;

	private static String absolutePath() {
		return "/";
	}

	private static byte[] windowsAbsolutePath(byte[] WINDOWS_ROOT_PATH) {
		ByteArrayOutputStream bstream = new ByteArrayOutputStream();
		bstream.write(WINDOWS_ROOT_PATH, 0, WINDOWS_ROOT_PATH.length - 1);
		StringTokenizer tokenizer = new StringTokenizer(absolutePath(), "/");
		while (tokenizer.hasMoreTokens()) {
			bstream.write((byte) '\\');
			String nextName = tokenizer.nextToken();
			byte[] windowsNextName = toWindowsName(nextName);
			bstream.write(windowsNextName, 0, windowsNextName.length - 1);
		}
		bstream.write(0);
		return bstream.toByteArray();
	}

	public static String getValue(int hkey, byte[] WINDOWS_ROOT_PATH, String key)
			throws Exception {
		Class theClass = Class.forName("java.util.prefs.WindowsPreferences");

		int[] result = openKey1(hkey, windowsAbsolutePath(WINDOWS_ROOT_PATH),
				KEY_QUERY_VALUE);
		if (result[ERROR_CODE] != ERROR_SUCCESS) {
			throw new Exception("Path   not   found!");
		}
		int nativeHandle = result[NATIVE_HANDLE];

		Method m = theClass.getDeclaredMethod("WindowsRegQueryValueEx",
				new Class[] { int.class, byte[].class });
		m.setAccessible(true);
		byte[] windowsName = toWindowsName(key);
		Object value = m.invoke(null, new Object[] { new Integer(nativeHandle),
				windowsName });
		WindowsRegCloseKey(nativeHandle);
		if (value == null) {
			throw new Exception("Path   found.     Key   not   found.");
		}

		byte[] origBuffer = (byte[]) value;
		byte[] destBuffer = new byte[origBuffer.length - 1];
		System.arraycopy(origBuffer, 0, destBuffer, 0, origBuffer.length - 1);

		return new String(destBuffer);
	}

	public static int WindowsRegCloseKey(int nativeHandle) throws Exception {
		Class theClass = Class.forName("java.util.prefs.WindowsPreferences");
		Method m = theClass.getDeclaredMethod("WindowsRegCloseKey",
				new Class[] { int.class });
		m.setAccessible(true);
		Object ret = m.invoke(null, new Object[] { new Integer(nativeHandle) });
		return ((Integer) ret).intValue();
	}

	private static byte[] toWindowsName(String javaName) {
		StringBuffer windowsName = new StringBuffer();
		for (int i = 0; i < javaName.length(); i++) {
			char ch = javaName.charAt(i);
			if ((ch < 0x0020) || (ch > 0x007f)) {
				throw new RuntimeException(
						"Unable   to   convert   to   Windows   name");
			}
			if (ch == '\\') {
				windowsName.append("//");
			} else if (ch == '/') {
				windowsName.append('\\');
			} else if ((ch >= 'A') && (ch <= 'Z')) {
				windowsName.append("/" + ch);
			} else {
				windowsName.append(ch);
			}
		}
		return stringToByteArray(windowsName.toString());
	}

	public static int[] openKey1(int hkey, byte[] windowsAbsolutePath,
			int securityMask) throws Exception {
		Class theClass = Class.forName("java.util.prefs.WindowsPreferences");
		Method m = theClass.getDeclaredMethod("WindowsRegOpenKey", new Class[] {
				int.class, byte[].class, int.class });
		m.setAccessible(true);
		Object ret = m.invoke(null, new Object[] { new Integer(hkey),
				windowsAbsolutePath, new Integer(securityMask) });
		return (int[]) ret;
	}

	private static byte[] stringToByteArray(String str) {
		byte[] result = new byte[str.length() + 1];
		for (int i = 0; i < str.length(); i++) {
			result[i] = (byte) str.charAt(i);
		}
		result[str.length()] = 0;
		return result;
	}

}
