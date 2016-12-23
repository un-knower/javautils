package com.cenrise.test;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class TestCommon {

	public static void main(String[] args) {
		TestCommon test = new TestCommon();
		test.tesString();
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

}
