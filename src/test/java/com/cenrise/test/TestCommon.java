package com.cenrise.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipException;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import com.cenrise.utils.FileUtil;

public class TestCommon {

	public static void main(String[] args) throws FileNotFoundException, ZipException, IOException {
		TestCommon test = new TestCommon();
		test.testDot();
	}

	@Test
	public void testDot() {
		String BOCOSaleSwitch = "1006166|10040007799";
		BOCOSaleSwitch = BOCOSaleSwitch.replace("，", ",");// 中文逗号支持
		String[] dotArrays = BOCOSaleSwitch.split(",");
		if (dotArrays == null || dotArrays.length == 0) {
			System.out.println("没有参数配置项！");
		}
		System.out.println("值1：" + dotArrays.length + dotArrays[0]);
		for (int j = 0; j < dotArrays.length; j++) {
			String[] args = dotArrays[j].split("\\|");
			if (args == null || args.length == 0) {
				System.out.println("此项参数配置为空跳过！");
			}
			System.out.println("结束！ 值1：" + args.length + args[0] + "  值2：" + args[1]);
		}

	}

	/*@Test
	public void testZip() throws FileNotFoundException, ZipException, IOException {
		String outFilePath = "Users/yp-tc-m-2684/jiadp/携程对账/TempOutFile1481609695062636.82382397984031.4816096970952566E12东航团卡散客0901-04/20160905142049IndividualSale.csv";
		File files = new File(outFilePath); // 如果压缩包内还有压缩包则解压内层压缩包
		for (File file : files.listFiles()) {
			if (file.isDirectory()) {
				File[] zipFiles = file.listFiles();
				for (File zip : zipFiles) {
					String zipName = zip.getPath().substring(0, zip.getPath().lastIndexOf(".zip"));
					FileUtil.unzip(zip.getPath(), zipName, "GBK");
				}
			}
		}
	}
*/
	public void testCatch2() {
		testCatch();
	}

	@Test
	public void testMap() {
		Map<String, String> _tmpMap = new HashMap<String, String>();
		_tmpMap.put("zha", "23");
		_tmpMap.put("zha", _tmpMap.get("zha") + "98");
		for (String key : _tmpMap.keySet()) {
			System.out.println("key= " + key + " and value= " + _tmpMap.get(key));
		}
	}

	@Test
	public boolean testCatch() {
		try {
			int i = 10 / 0; // 抛出 Exception，后续处理被拒绝
			System.out.println("i vaule is : " + i);
			return true; // Exception 已经抛出，没有获得被执行的机会
		} catch (Exception e) {
			System.out.println(" -- Exception --");
			return catchMethod(); // Exception 抛出，获得了调用方法的机会，但方法值在 finally
									// 执行完后才返回
		} finally {
			finallyMethod(); // Exception 抛出，finally 代码块将在 catch 执行 return 之前被执行
		}
	}

	// catch 后续处理工作
	public static boolean catchMethod() {
		System.out.print("call catchMethod and return  --->>  ");
		return false;
	}

	// finally后续处理工作
	public static void finallyMethod() {
		System.out.println();
		System.out.print("call finallyMethod and do something  --->>  ");
	}

	@Test
	public void tesString() {

		String str = "10011206985|123,10011206985|1234,10011206990|ALL,10011206995| ,";// 10003552347|10003552347001,10003552347|10003552347002,10003552347|10003552347003
		// ,10003552347|10003552347002
		// System.out.println(str.contains(null));//返回空异常
		// str = str.replace("，", ",");// 中文逗号转英文
		String[] args2 = str.split(",");
		System.out.println("args2的大小：" + args2.length);
		for (int i = 0; i < args2.length; i++) {
			String arg = args2[i];
			String args3[] = arg.split("\\|");
			if (args3.length != 2) {
				System.out.println("配置参数有误");
			}
			// System.out.println("args3的大小："+args3.length);
			// System.out.println(args3[0]);
			// System.out.println(args3[1]);
			System.out.println("当前位置：" + i + "，商编[" + args3[0] + "]，终端号[" + args3[1] + "]");
			if (i == args2.length - 1) {
				break;
			}
		}

	}

	@Test
	public void sendMessageValidation2() {
		try {
			// 商编A|终端号,商编B|终端号，格式：10003552347|10003552347001,10003552347|10003552347002
			System.out.println("............商户下发短信验证开始............");
			String eposCustomerNumberTerminalCode = "10003552347|001-002-003，10003552347| ,10003552347|ALL";
			System.out.println("获取配置信息：[" + eposCustomerNumberTerminalCode + "]");
			if (StringUtils.isEmpty(eposCustomerNumberTerminalCode)) {
				System.out.println("开关配置为空！");
			} else {
				boolean sendFlag = false;// 是否强制下发短信验证
				String[] dotArrays = eposCustomerNumberTerminalCode.split(",");
				for (int i = 0; i < dotArrays.length; i++) {
					String str = dotArrays[i];
					String[] args = str.split("\\|");
					if (args.length != 2) {
						System.out.println("定制参数配置有误！参数[" + args.toString() + "]");
						continue;
					}
					// 商编
					String _CustomerNumber = args[0];
					// 终端号
					String _TerminalCode = args[1];
					// 1.商编开关
					System.out.println("配置项中的当前商编：[" + _CustomerNumber + "]，配置项中的当前终端号：[" + _TerminalCode + "]");

					// 2.过滤商编、不匹配的商编、匹配配置内容的终端
					if (StringUtils.isBlank(_CustomerNumber)) {
						continue;
					}
					// 3.操作码是否为拦截，如果是返回。

					sendFlag = true;
					// 因为配置中，商编相同，终端不同有多个，第一个不匹配，后面有可能匹配，所以要等所有配置遍历完成后再决定
					if (sendFlag && i == dotArrays.length - 1) {
						// 4.更改风控参数
						System.out.println("风控参数配置完成！");
					}
				}
			}
		} catch (Exception e) {
			System.out.println("强制下发短验处理异常。" + e);
		} finally {
			System.out.println("............商户下发短信验证结束............");
		}
	}

	@Test
	public void sendMessageValidation() {
		try {
			// 商编A|终端号,商编B|终端号，格式：10003552347|10003552347001,10003552347|10003552347002
			System.out.println("............商户下发短信验证开始............");
			String eposCustomerNumberTerminalCode = "10011206985|123,10011206985|1234,10011206990|ALL,10011206995| ,";
			System.out.println("获取配置信息：[" + eposCustomerNumberTerminalCode + "]");
			if (StringUtils.isEmpty(eposCustomerNumberTerminalCode)) {
				System.out.println("开关配置为空！");
			} else {
				boolean sendFlag = false;// 是否强制下发短信验证
				String[] dotArrays = eposCustomerNumberTerminalCode.split(",");
				for (int i = 0; i < dotArrays.length; i++) {
					String str = dotArrays[i];
					String[] args = str.split("\\|");
					if (args.length != 2) {
						System.out.println("定制参数配置有误！参数[" + args.toString() + "]");
						continue;
					}
					// 商编
					String _CustomerNumber = args[0];
					// 终端号
					String _TerminalCode = args[1];
					// 1.商编开关
					System.out.println("配置项中的当前商编：[" + _CustomerNumber + "]，配置项中的当前终端号：[" + _TerminalCode + "]");

					// 2.过滤商编、不匹配的商编、匹配配置内容的终端
					if (StringUtils.isBlank(_CustomerNumber)) {
						continue;
					}
					// 3.操作码是否为拦截，如果是返回。

					sendFlag = true;
					// 因为配置中，商编相同，终端不同有多个，第一个不匹配，后面有可能匹配，所以要等所有配置遍历完成后再决定
					if (sendFlag && i == dotArrays.length - 1) {
						// 4.更改风控参数
						System.out.println("风控参数配置完成！");
					}
				}
			}
		} catch (Exception e) {
			System.out.println("强制下发短验处理异常。" + e);
		} finally {
			System.out.println("............商户下发短信验证结束............");
		}
	}

	private static Pattern SMS_CODE_PATTERN = Pattern.compile("\\d\\d\\d");

	@Test
	public void testPattern627() {
		Matcher m = SMS_CODE_PATTERN.matcher("6278");
		if (!m.find()) {
			System.out.println("上行信息中不包含验证码：6278");
			return;
		} else {
			System.out.println("匹配之后的数字" + m.group(0));
		}
		System.out.println("over!");
	}

	private static Pattern SMS_CODE_PATTERN_Number = Pattern.compile("^[0-9]{3,8}$");

	@Test
	public void testPatternNumber() {
		Matcher m = SMS_CODE_PATTERN_Number.matcher("601");
		if (!m.find()) {
			System.out.println("不匹配");
			return;
		} else {
			System.out.println("匹配" + m.group(0));
		}
		System.out.println("over!");
	}

}
