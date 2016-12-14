
package sample.poi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.Test;

/**  
    * @ClassName: ExcelParse  
    * @Description: java使用POI获取sheet、行数、列数
    * @author dongpo.jia  
    * @date 2016年12月12日  
    *    
    */

public class Excel2003ParseSample {
	public static void main(String[] args) {

	}

	/**
	 * 
	* @Title: testPoiExcle2003  
	* @Description: excel2003的对账解析
	* @param     参数  
	* @return void    返回类型  
	* @throws
	 */
	@Test
	public void testPoiExcle2003() {
		try {
			// 把一张xls的数据表读到wb里
			List<Map<Integer, String>> list = null;
			BufferedInputStream bis = null;
			String xlsFilePath = "/Users/yp-tc-m-2684/Downloads/20160905142026sale.xls";
			File xlsFile = new File(xlsFilePath);
			bis = new BufferedInputStream(new FileInputStream(xlsFile));
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(bis);

			list = new ArrayList<Map<Integer, String>>();
			// 循环工作表Sheet
			for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
				HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
				if (hssfSheet == null) {
					continue;
				}

				Map<String, Integer> maps = new HashMap<String, Integer>();
				for (int j = 4; j < 5; j++) {
					HSSFRow row = hssfSheet.getRow(j);
					for (int i = 0; i < row.getLastCellNum(); i++) {
						HSSFCell cell = row.getCell(i);
						maps.put(cell.getRichStringCellValue().getString(), cell.getColumnIndex());
					}
					System.out.println("获取文件[" + xlsFile.getName() + "]列，字段名列表[" + transMapToString(maps) + "]");
				}

				// 循环行Row
				for (int rowNum = 5; rowNum <= hssfSheet.getLastRowNum() - 7; rowNum++) { // 默认从第一行开始解析，只读内容
					HSSFRow hssfRow = hssfSheet.getRow(rowNum);
					if (hssfRow == null) {
						continue;
					}
					Map<Integer, String> mapRows = new HashMap<Integer, String>();
					int i = 1; // 默认从第一列开始解析
					for (Cell cell : hssfRow) {
						HSSFCell xh = (HSSFCell) cell;
						mapRows.put(Integer.valueOf(i - 1), getValue(xh).trim());
						i++;
					}
					// System.out.println("map----size:" + map.size());
					list.add(mapRows);
					System.out.println("获取文件[" + xlsFile.getName() + "]数据，第" + rowNum + "行记录["
							+ transMapToStringInteger(mapRows) + "]");
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
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

	public static String transMapToStringInteger(Map<Integer, String> map) {
		StringBuffer sb = new StringBuffer();
		for (Entry<Integer, String> entry : map.entrySet()) {
			sb.append(entry.getKey().toString()).append("=")
					.append(null == entry.getValue() ? "" : entry.getValue().toString()).append(";");
		}
		return sb.toString();
	}

	/**
	 * 得到Excel表中的值
	 * 
	 * @param hssfCell
	 *            Excel中的每一个格子
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
}