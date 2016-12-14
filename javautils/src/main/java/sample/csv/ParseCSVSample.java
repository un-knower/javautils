
/**    
* @Title: ParseCSV.java  
* @Package com.cenrise.csv  
* @Description: TODO(用一句话描述该文件做什么)  
* @author dongpo.jia  
* @date 2016年12月13日  
* @version V1.0    
*/

package sample.csv;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import au.com.bytecode.opencsv.CSVReader;

/**  
    * @ClassName: ParseCSV  
    * @Description: TODO(这里用一句话描述这个类的作用)  
    * @author dongpo.jia  
    * @date 2016年12月13日  
    *    
    */

public class ParseCSVSample {
	private static ConcurrentHashMap<String, Integer> indexColumnMaps = new ConcurrentHashMap<String, Integer>();

	@Test
	public void testCSV() {
		String csvFilePath = "/Users/yp-tc-m-2684/Downloads/20160914093000IndividualSale_head_tail.csv";
		List<Map<Integer, String>> result = new ArrayList<Map<Integer, String>>();
		CSVReader reader = null;
		try {
			File csvFile = new File(csvFilePath);
			InputStream in = new FileInputStream(csvFile);

			reader = new CSVReader(new InputStreamReader(in, "GBK"), ',');
			List<String[]> lstData = reader.readAll();
			System.out.println("上传Excel数据行数：" + lstData.size());

			String[] map;
			Map<Integer, String> tempMap = null;

			/**
			 * 列字段 add by dongpo.jia 20161212
			 */
			indexColumnMaps.clear();
			for (int j = 3; j < 4; j++) {
				map = lstData.get(j);
				int colNum = 0;
				for (String str : map) {
					indexColumnMaps.put(str.trim(), colNum);
					colNum++;
				}
				if (indexColumnMaps.containsKey("旅客姓名")) {
					System.out.println("确定是CVS文件！！！");
				}
				System.out.println("通过parseCSVMUB2T获取列字段名[" + transMapToString(indexColumnMaps) + "]");
			}

			for (int rowNum = 4; rowNum < lstData.size() - 6; rowNum++) {
				map = lstData.get(rowNum);
				tempMap = new HashMap<Integer, String>();
				int colNum = 0;
				for (String str : map) {
					// System.out.print(str+" ;");
					tempMap.put(colNum, str.trim());
					colNum++;
				}
				// System.out.println();
				result.add(tempMap);
				System.out.println("数据"+rowNum+"：" + transMapToString2(tempMap));
			}

		} catch (Exception e) {
			System.out.println("解析CSV文件失败！filePath:[" + csvFilePath + "]" + e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/** 
	 * 方法名称:transMapToString 
	 * 传入参数:map 
	 * 返回值:String 形如 username=chenziwen;password=1234;
	*/
	public static String transMapToString(Map<String, Integer> map) {
		StringBuffer sb = new StringBuffer();
		for (Entry<String, Integer> entry : map.entrySet()) {
			sb.append(entry.getKey().toString()).append("=")
					.append(null == entry.getValue() ? "" : entry.getValue().toString()).append(";");
		}
		return sb.toString();
	}

	public static String transMapToString2(Map<Integer, String> map) {
		StringBuffer sb = new StringBuffer();
		for (Entry<Integer, String> entry : map.entrySet()) {
			sb.append(entry.getKey().toString()).append("=")
					.append(null == entry.getValue() ? "" : entry.getValue().toString()).append(";");
		}
		return sb.toString();
	}
}
