
package com.cenrise.poi;

import java.util.Map;

/**  
* @ClassName: FileParseData  
* @Description: 文件解析运行时数据
* @author dongpo.jia  
* @date 2016年12月12日  
*    
*/
public class FileParseData {
	// 当前处理文件的列，key是对应的列名;value为所在位置，从0开始；
	private Map<String, Integer> indexColumnMaps;

	/**  
	* @return indexColumnMaps  
	*/

	public Map<String, Integer> getIndexColumnMaps() {
		return indexColumnMaps;
	}

	/**  
	 * @param paramtheparamthe{bare_field_name} to set  
	 */

	public void setIndexColumnMaps(Map<String, Integer> indexColumnMaps) {
		this.indexColumnMaps = indexColumnMaps;
	}

}
