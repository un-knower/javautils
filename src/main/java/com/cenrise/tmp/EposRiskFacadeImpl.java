package com.cenrise.tmp;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class EposRiskFacadeImpl {
	/**
	 * 山航定制（10003552347）要求 除APPLEPAY 和 指间付（终端号001），每笔交易必须下发短验。
	 * 商编正，终端反。配置的是包含的商编下不包含的终端。终端为空时，代表此商编下所有的都发。终端为ALL时，代表此商编下所有的都不发。
	 * 格式：10003552347|,10003552347|10003552347002
	 * 格式：10003552347|ALL,10003552347|10003552347002
	 * 格式：10003552347|001-002-003，10003552347| ,10003552347|ALL
	 * @param param
	 * @param history
	 */
	/*public void sendMessageValidation(RiskProcessParam param, EposRiskHistory history) {
		try {
			// 商编A|终端号,商编B|终端号，格式：10003552347|10003552347001,10003552347|10003552347002
			logger.info("............商户下发短信验证开始............");
			String eposCustomerNumberTerminalCode = EposCoreCommonUtil.getCashValue("EPOS",
					"eposCustomerNumberTerminalCode");
			logger.info("获取配置信息：[" + eposCustomerNumberTerminalCode + "]");
			if (StringUtils.isEmpty(eposCustomerNumberTerminalCode)) {
				logger.info("开关配置为空！");
			} else if (param.getEposCustomerWay() != null
					&& (param.getEposCustomerWay().equals(EposCustomerWay.INTERFACE_APPLE_CREDIT_SALE)
							|| param.getEposCustomerWay().equals(EposCustomerWay.INTERFACE_APPLE_DEBIT_SALE)
							|| param.getEposCustomerWay().equals(EposCustomerWay.INTERFACE_APPLE_AUTH))) {
				logger.info("APPLEPAY交易！");
			} else if (param.getEposCustomerWay() != null
					&& (param.getEposCustomerWay().equals(EposCustomerWay.GATE_MOBILE_DEBIT_PAY)// 手机网关借记卡
							|| param.getEposCustomerWay().equals(EposCustomerWay.GATE_MOBILE_CREDIT_PAY)// 手机网关信用卡
							|| param.getEposCustomerWay().equals(EposCustomerWay.GATE_MOBILE_CREDIT_AUTH_PAY)// 手机网关信用卡预授权
							|| param.getEposCustomerWay().equals(EposCustomerWay.GATE_MOBILE_SPLIT_CREDIT_AUTH_PAY)// wap网关分账
							|| param.getEposCustomerWay().equals(EposCustomerWay.GATE_MOBILE_SPLIT_CREDIT_PAY)// wap网关分账
							|| param.getEposCustomerWay().equals(EposCustomerWay.GATE_MOBILE_SPLIT_DEBIT_PAY))) {// wap网关分账
				logger.info("指间付交易！");
			} else {
				eposCustomerNumberTerminalCode = eposCustomerNumberTerminalCode.replace("，", ",");// 中文逗号支持
				String[] dotArrays = eposCustomerNumberTerminalCode.split(",");
				if (dotArrays == null || dotArrays.length == 0) {
					logger.info("没有参数配置项！");
					return;
				}
				for (int i = 0; i < dotArrays.length; i++) {
					String str = dotArrays[i];
					if (StringUtils.isBlank(str)) {
						logger.info("此参数[" + str + "]为空跳过！");
						continue;
					}
					String[] args = str.split("\\|");
					if (args == null || args.length == 0) {
						logger.info("此项参数配置为空跳过！");
						continue;
					}
					String _CustomerNumber = null;// 商编
					String _TerminalCode = null;// 终端号列表以-分开，ALL或空只能写一个
					if (args.length == 2) {
						_CustomerNumber = args[0];
						_TerminalCode = args[1];
					} else if (args.length == 1) {
						_CustomerNumber = args[0];
						_TerminalCode = null;
						logger.info("只配置了商编的情况，商编[" + args[0] + "]");
					} else if (args.length != 2) {
						logger.error("定制参数配置有误！");
						continue;
					}

					// 1.商编开关
					logger.info("商编[" + param.getCustomerNumber() + "]，配置项中的当前商编：[" + _CustomerNumber + "]，终端号["
							+ param.getTerminalCode() + "]，配置项中的当前终端号：[" + _TerminalCode + "]");

					// 2.过滤商编、不匹配的商编、匹配配置内容的终端
					if (StringUtils.isBlank(_CustomerNumber)) {
						continue;
					}

					if (!_CustomerNumber.contains(param.getCustomerNumber())) {
						continue;
					}

					if ("ALL".equals(_TerminalCode)) {
						break;// 所有的都不发
					}

					if (StringUtils.isNotBlank(_TerminalCode)) {// 如果终端不为空
						String[] terminals = _TerminalCode.split("-");
						List lists = Arrays.asList(terminals);
						if (lists != null && StringUtils.isNotBlank(param.getTerminalCode())) {
							lists.contains(param.getTerminalCode());
							break;
						}
					}

					// 3.操作码是否为拦截，如果是返回。
					logger.info("EposRiskHistory的风险码[" + param.getRmcsOperCode() + "],RiskProcessParam的风险码["
							+ history.getRmcsOperCode() + "],高危判断使用的是EposRiskHistory的风险码");
					if (history.getRmcsOperCode() != null
							&& (history.getRmcsOperCode().equals("8888") || history.getRmcsOperCode().equals("8000"))) {
						logger.info("高危拦截交易。");
					} else {
						// 4.更改风控参数
						param.setRmcsOperCode("8010");
						history.setRmcsOperCode("8010");
						entityDao.merge(history);
						logger.info("风控参数配置完成！");
					}
				}
			}
		} catch (Exception e) {
			logger.error("强制下发短验处理异常。" + e);
		} finally {
			logger.info("............商户下发短信验证结束............");
		}
	}*/
	
}
