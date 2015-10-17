package com.cenrise.commons.utils.JavaJs;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleScriptContext;

import org.apache.log4j.Logger;

/**Java中调用js
 * 使用方式：
 * 	boolean flag = false;
	 String js = "var a = 1; var b = a + aKey;println(b);";
	 Map<String,Object> map = new HashMap<String,Object>();
	 map.put("aKey", "aValue");
	 try {
	 flag = execJs.execJs(js, map);
	 } catch (Exception e) {
	 // TODO Auto-generated catch block
	 e.printStackTrace();
	 }
 */
public class ExecJs {

	/**
	 * 记录日志类
	 */
	private Logger log = Logger.getLogger(ExecJs.class);

	/**
	 * 后置处理，执行js脚本
	 * js 就是要执行的js脚本，map 是参数，就允许js脚本中用动态的参数 处理map中的值
	 * 
	 * @param js
	 * @throws Exception
	 */
	public void execJs(String js, Map<String, Object> map) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("execJs js : " + js);
			Iterator<Entry<String, Object>> it = map.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Object> entry = (Entry<String, Object>) it.next();
				log.info("EXECJS MAP : " + entry.getKey() + "---"
						+ entry.getValue());
			}// end while
		}// end if
		if ("".equals(js) || js == null) {
			log.info("EXECJS ERROR : JAVASCRIPT CONTENT IS NULL");
		} else if (map == null || map.size() <= 0) {
			log.info("EXECJS ERROR : MAP CONTENT IS NULL");
		} else {
			// 获取脚本引擎
			ScriptEngineManager mgr = new ScriptEngineManager();
			ScriptEngine engine = mgr.getEngineByName("javascript");
			// 绑定数据
			ScriptContext newContext = new SimpleScriptContext();
			Bindings bind = newContext.getBindings(ScriptContext.ENGINE_SCOPE);
			bind.putAll(map);
			try {
				engine.setBindings(bind, ScriptContext.ENGINE_SCOPE);
				engine.eval(js);
			} catch (Exception e) {
				log.info("EXECJS EXCEPTION : EXECUTE JAVASCRIPT EXCEPTION", e);
				throw (e);
			}// end try
		}// end if
	}
}
/**
引用来自“唯一”的评论
楼主的例子明明是void类型的，demo怎么又返回个boolean，呵呵，自相矛盾
后来改造了，执行完了你得告诉人家执行的怎么样了啊，就改了，不抛异常就返回true其实返回个什么并不重要，
自己改改就行了，我想知道为什么alert()不能执行呢，这个跟真正的js环境有什么相同点跟不同点吗我也试alert 不行，
具体的我也没搞太清楚，只是用了一下，要深入研究的话，在网上搜索一下吧 
*/
