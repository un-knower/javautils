package sample.poi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

/**  
    * @ClassName: XSSFCellTest  
    * @Description: TODO(这里用一句话描述这个类的作用)  
    * @author dongpo.jia  
    * @date 2016年12月12日  
    *    
    */

public class XSSFCellTest {
	public static FileParseData fileParse = new FileParseData();
	public static String DATE_FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss"; // 年/月/日
	public static SimpleDateFormat sdfDateTime = new SimpleDateFormat(DATE_FORMAT_DATETIME);

	@Test
	public void testPoiExcle2007() {
		List<Map<Integer, String>> list = null;
		BufferedInputStream bis = null;
		try {
			// 把一张xls的数据表读到wb里
			String xlsxFilePath = "/Users/yp-tc-m-2684/Downloads/B2T/20160905142026sale.xls";
			File xlsxFile = new File(xlsxFilePath);
			InputStream in = new FileInputStream(xlsxFile);
			bis = new BufferedInputStream(in);
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook(bis);
			list = new ArrayList<Map<Integer, String>>();
			// 循环工作表Sheet
			for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
				XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
				if (xssfSheet == null) {
					continue;
				}

				/**
				 * 列字段
				 */
				for (int j = 4; j < 5; j++) {
					XSSFRow xssfRow = xssfSheet.getRow(j);
					if (xssfRow == null) {
						System.out.println("列字段名称行为空，跳过！行[" + xssfRow + "]");
						continue;
					}
					Map< String,Integer> indexColumnMaps = new HashMap<String,Integer>();
					for (Cell cell : xssfRow) {
						XSSFCell xh = (XSSFCell) cell;
						indexColumnMaps.put( getValue(xh).trim(),xh.getColumnIndex());
					}
					fileParse.setIndexColumnMaps(indexColumnMaps);
					System.out.println("通过parseXLS2007MUB2T获取列字段名[" + transMapToString(indexColumnMaps) + "]");
				}
				// 循环行Row
				for (int rowNum = 4; rowNum <= xssfSheet.getLastRowNum() - 6; rowNum++) {
					XSSFRow xssfRow = xssfSheet.getRow(rowNum);
					if (xssfRow == null) {
						continue;
					}
					Map<Integer, String> map = new HashMap<Integer, String>();
					int i = 1;
					for (Cell cell : xssfRow) {
						XSSFCell xh = (XSSFCell) cell;
						if (i == 18) {
							xh.setCellType(XSSFCell.CELL_TYPE_STRING);
							Date date = org.apache.poi.ss.usermodel.DateUtil
									.getJavaDate(Double.valueOf(xh.getStringCellValue()));
							String value = XSSFCellTest.sdfDateTime.format(date);
							map.put(Integer.valueOf(i - 1), value);
						} else {
							map.put(Integer.valueOf(i - 1), getValue(xh).trim());
						}
						i++;
					}
					// System.out.println("map----size:" + map.size());
					list.add(map);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 得到Excel表中的值
	 * 
	 * @param hssfCell Excel中的每一个格子
	 * 
	 * @return Excel中每一个格子中的值
	 */
	@SuppressWarnings("static-access")
	private static String getValue(HSSFCell hssfCell) {
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			// 返回布尔类型的值
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			// 返回数值类型的值
			return String.valueOf(hssfCell.getNumericCellValue());
		} else {
			// 返回字符串类型的值
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}

	/**
	 * 得到Excel表中的值
	 * 
	 * @param xssfCell
	 *            Excel中的每一个格子
	 * @return Excel中每一个格子中的值
	 */
	@SuppressWarnings("static-access")
	private static String getValue(XSSFCell xssfCell) {
		if (xssfCell.getCellType() == xssfCell.CELL_TYPE_BOOLEAN) {
			// 返回布尔类型的值
			return String.valueOf(xssfCell.getBooleanCellValue());
		} else if (xssfCell.getCellType() == xssfCell.CELL_TYPE_NUMERIC) {
			// 返回数值类型的值
			return String.valueOf(xssfCell.getNumericCellValue());
		} else {
			// 返回字符串类型的值
			return String.valueOf(xssfCell.getStringCellValue());
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
}
