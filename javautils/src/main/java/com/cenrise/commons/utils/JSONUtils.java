package com.cenrise.commons.utils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.sdicons.json.mapper.JSONMapper;
import com.sdicons.json.mapper.MapperException;
import com.sdicons.json.model.JSONValue;

public class JSONUtils {
	/** Commons Logging instance. */
	private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory
			.getLog(JSONUtils.class);

	private static final ObjectMapper objectMapper = new ObjectMapper();

	private JSONUtils() {
	}

	public static ObjectMapper getInstance() {
		return objectMapper;
	}


	/***
	 * 将List对象序列化为JSON文本
	 */
	public static <T> String toJSONString(List<T> list) {
		JSONArray jsonArray = JSONArray.fromObject(list);

		return jsonArray.toString();
	}

	/***
	 * 将对象序列化为JSON文本
	 * 
	 * @param object
	 * @return
	 */
	public static String toJSONString(Object object) {
		JSONArray jsonArray = JSONArray.fromObject(object);

		return jsonArray.toString();
	}

	/***
	 * 将JSON对象数组序列化为JSON文本
	 * 
	 * @param jsonArray
	 * @return
	 */
	public static String toJSONString(JSONArray jsonArray) {
		return jsonArray.toString();
	}

	/***
	 * 将JSON对象序列化为JSON文本
	 * 
	 * @param jsonObject
	 * @return
	 */
	public static String toJSONString(JSONObject jsonObject) {
		return jsonObject.toString();
	}

	/***
	 * 将对象转换为List对象
	 * 
	 * @param object
	 * @return
	 */
	public static List toArrayList(Object object) {
		List arrayList = new ArrayList();

		JSONArray jsonArray = JSONArray.fromObject(object);

		Iterator it = jsonArray.iterator();
		while (it.hasNext()) {
			JSONObject jsonObject = (JSONObject) it.next();

			Iterator keys = jsonObject.keys();
			while (keys.hasNext()) {
				Object key = keys.next();
				Object value = jsonObject.get(key);
				arrayList.add(value);
			}
		}

		return arrayList;
	}

	/***
	 * 将对象转换为Collection对象
	 * 
	 * @param object
	 * @return
	 */
	public static Collection toCollection(Object object) {
		JSONArray jsonArray = JSONArray.fromObject(object);

		return JSONArray.toCollection(jsonArray);
	}

	/***
	 * 将对象转换为JSON对象数组
	 * 
	 * @param object
	 * @return
	 */
	public static JSONArray toJSONArray(Object object) {
		return JSONArray.fromObject(object);
	}

	/***
	 * 将对象转换为JSON对象
	 * 
	 * @param object
	 * @return
	 */
	public static JSONObject toJSONObject(Object object) {
		return JSONObject.fromObject(object);
	}

	/***
	 * 将对象转换为HashMap
	 * 
	 * @param object
	 * @return
	 */
	public static HashMap toHashMap(Object object) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		JSONObject jsonObject = JSONUtils.toJSONObject(object);
		Iterator it = jsonObject.keys();
		while (it.hasNext()) {
			String key = String.valueOf(it.next());
			Object value = jsonObject.get(key);
			data.put(key, value);
		}

		return data;
	}

	/***
	 * 将对象转换为List<Map<String,Object>>
	 * 
	 * @param object
	 * @return
	 */
	// 返回非实体类型(Map<String,Object>)的List
	public static List<Map<String, Object>> toList(Object object) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		JSONArray jsonArray = JSONArray.fromObject(object);
		for (Object obj : jsonArray) {
			JSONObject jsonObject = (JSONObject) obj;
			Map<String, Object> map = new HashMap<String, Object>();
			Iterator it = jsonObject.keys();
			while (it.hasNext()) {
				String key = (String) it.next();
				Object value = jsonObject.get(key);
				map.put((String) key, value);
			}
			list.add(map);
		}
		return list;
	}

	/***
	 * 将JSON对象数组转换为传入类型的List
	 * 
	 * @param <T>
	 * @param jsonArray
	 * @param objectClass
	 * @return
	 */
	public static <T> List<T> toList(JSONArray jsonArray, Class<T> objectClass) {
		return JSONArray.toList(jsonArray, objectClass);
	}

	/***
	 * 将对象转换为传入类型的List
	 * 
	 * @param <T>
	 * @param jsonArray
	 * @param objectClass
	 * @return
	 */
	public static <T> List<T> toList(Object object, Class<T> objectClass) {
		JSONArray jsonArray = JSONArray.fromObject(object);

		return JSONArray.toList(jsonArray, objectClass);
	}

	/***
	 * 将JSON对象转换为传入类型的对象
	 * 
	 * @param <T>
	 * @param jsonObject
	 * @param beanClass
	 * @return
	 */
	public static <T> T toBean(JSONObject jsonObject, Class<T> beanClass) {
		return (T) JSONObject.toBean(jsonObject, beanClass);
	}

	/***
	 * 将将对象转换为传入类型的对象
	 * 
	 * @param <T>
	 * @param object
	 * @param beanClass
	 * @return
	 */
	public static <T> T toBean(Object object, Class<T> beanClass) {
		JSONObject jsonObject = JSONObject.fromObject(object);

		return (T) JSONObject.toBean(jsonObject, beanClass);
	}

	/***
	 * 将JSON文本反序列化为主从关系的实体
	 * 
	 * @param <T>
	 *            泛型T 代表主实体类型
	 * @param <D>
	 *            泛型D 代表从实体类型
	 * @param jsonString
	 *            JSON文本
	 * @param mainClass
	 *            主实体类型
	 * @param detailName
	 *            从实体类在主实体类中的属性名称
	 * @param detailClass
	 *            从实体类型
	 * @return
	 */
	public static <T, D> T toBean(String jsonString, Class<T> mainClass,
			String detailName, Class<D> detailClass) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		JSONArray jsonArray = (JSONArray) jsonObject.get(detailName);

		T mainEntity = JSONUtils.toBean(jsonObject, mainClass);
		List<D> detailList = JSONUtils.toList(jsonArray, detailClass);

		try {
			BeanUtils.setProperty(mainEntity, detailName, detailList);
		} catch (Exception ex) {
			throw new RuntimeException("主从关系JSON反序列化实体失败！");
		}

		return mainEntity;
	}

	/***
	 * 将JSON文本反序列化为主从关系的实体
	 * 
	 * @param <T>泛型T 代表主实体类型
	 * @param <D1>泛型D1 代表从实体类型
	 * @param <D2>泛型D2 代表从实体类型
	 * @param jsonString
	 *            JSON文本
	 * @param mainClass
	 *            主实体类型
	 * @param detailName1
	 *            从实体类在主实体类中的属性
	 * @param detailClass1
	 *            从实体类型
	 * @param detailName2
	 *            从实体类在主实体类中的属性
	 * @param detailClass2
	 *            从实体类型
	 * @return
	 */
	public static <T, D1, D2> T toBean(String jsonString, Class<T> mainClass,
			String detailName1, Class<D1> detailClass1, String detailName2,
			Class<D2> detailClass2) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		JSONArray jsonArray1 = (JSONArray) jsonObject.get(detailName1);
		JSONArray jsonArray2 = (JSONArray) jsonObject.get(detailName2);

		T mainEntity = JSONUtils.toBean(jsonObject, mainClass);
		List<D1> detailList1 = JSONUtils.toList(jsonArray1, detailClass1);
		List<D2> detailList2 = JSONUtils.toList(jsonArray2, detailClass2);

		try {
			BeanUtils.setProperty(mainEntity, detailName1, detailList1);
			BeanUtils.setProperty(mainEntity, detailName2, detailList2);
		} catch (Exception ex) {
			throw new RuntimeException("主从关系JSON反序列化实体失败！");
		}

		return mainEntity;
	}

	/***
	 * 将JSON文本反序列化为主从关系的实体
	 * 
	 * @param <T>泛型T 代表主实体类型
	 * @param <D1>泛型D1 代表从实体类型
	 * @param <D2>泛型D2 代表从实体类型
	 * @param jsonString
	 *            JSON文本
	 * @param mainClass
	 *            主实体类型
	 * @param detailName1
	 *            从实体类在主实体类中的属性
	 * @param detailClass1
	 *            从实体类型
	 * @param detailName2
	 *            从实体类在主实体类中的属性
	 * @param detailClass2
	 *            从实体类型
	 * @param detailName3
	 *            从实体类在主实体类中的属性
	 * @param detailClass3
	 *            从实体类型
	 * @return
	 */
	public static <T, D1, D2, D3> T toBean(String jsonString,
			Class<T> mainClass, String detailName1, Class<D1> detailClass1,
			String detailName2, Class<D2> detailClass2, String detailName3,
			Class<D3> detailClass3) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		JSONArray jsonArray1 = (JSONArray) jsonObject.get(detailName1);
		JSONArray jsonArray2 = (JSONArray) jsonObject.get(detailName2);
		JSONArray jsonArray3 = (JSONArray) jsonObject.get(detailName3);

		T mainEntity = JSONUtils.toBean(jsonObject, mainClass);
		List<D1> detailList1 = JSONUtils.toList(jsonArray1, detailClass1);
		List<D2> detailList2 = JSONUtils.toList(jsonArray2, detailClass2);
		List<D3> detailList3 = JSONUtils.toList(jsonArray3, detailClass3);

		try {
			BeanUtils.setProperty(mainEntity, detailName1, detailList1);
			BeanUtils.setProperty(mainEntity, detailName2, detailList2);
			BeanUtils.setProperty(mainEntity, detailName3, detailList3);
		} catch (Exception ex) {
			throw new RuntimeException("主从关系JSON反序列化实体失败！");
		}

		return mainEntity;
	}

	/***
	 * 将JSON文本反序列化为主从关系的实体
	 * 
	 * @param <T>
	 *            主实体类型
	 * @param jsonString
	 *            JSON文本
	 * @param mainClass
	 *            主实体类型
	 * @param detailClass
	 *            存放了多个从实体在主实体中属性名称和类型
	 * @return
	 */
	public static <T> T toBean(String jsonString, Class<T> mainClass,
			HashMap<String, Class> detailClass) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		T mainEntity = JSONUtils.toBean(jsonObject, mainClass);
		for (Object key : detailClass.keySet()) {
			try {
				Class value = (Class) detailClass.get(key);
				BeanUtils.setProperty(mainEntity, key.toString(), value);
			} catch (Exception ex) {
				throw new RuntimeException("主从关系JSON反序列化实体失败！");
			}
		}
		return mainEntity;
	}

	/**
	 * @param obj
	 *            任意对象
	 * @return String
	 */
	public static String object2json(Object obj) {
		StringBuilder json = new StringBuilder();
		if (obj == null) {
			json.append("\"\"");
		} else if (obj instanceof String || obj instanceof Integer
				|| obj instanceof Float || obj instanceof Boolean
				|| obj instanceof Short || obj instanceof Double
				|| obj instanceof Long || obj instanceof BigDecimal
				|| obj instanceof BigInteger || obj instanceof Byte) {
			json.append("\"").append(string2json(obj.toString())).append("\"");
		} else if (obj instanceof Object[]) {
			json.append(array2json((Object[]) obj));
		} else if (obj instanceof List) {
			json.append(list2json((List<?>) obj));
		} else if (obj instanceof Map) {
			json.append(map2json((Map<?, ?>) obj));
		} else if (obj instanceof Set) {
			json.append(set2json((Set<?>) obj));
		} else {
			json.append(bean2json(obj));
		}
		return json.toString();
	}

	/**
	 * @param bean
	 *            bean对象
	 * @return String
	 */
	public static String bean2json(Object bean) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		PropertyDescriptor[] props = null;
		try {
			props = Introspector.getBeanInfo(bean.getClass(), Object.class)
					.getPropertyDescriptors();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		if (props != null) {
			for (int i = 0; i < props.length; i++) {
				try {
					String name = object2json(props[i].getName());
					String value = object2json(props[i].getReadMethod().invoke(
							bean));
					json.append(name);
					json.append(":");
					json.append(value);
					json.append(",");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	/**
	 * @param list
	 *            list对象
	 * @return String
	 */
	public static String list2json(List<?> list) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	/**
	 * @param array
	 *            对象数组
	 * @return String
	 */
	public static String array2json(Object[] array) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (array != null && array.length > 0) {
			for (Object obj : array) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	/**
	 * @param map
	 *            map对象
	 * @return String
	 */
	public static String map2json(Map<?, ?> map) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		if (map != null && map.size() > 0) {
			for (Object key : map.keySet()) {
				json.append(object2json(key));
				json.append(":");
				json.append(object2json(map.get(key)));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}

	/**
	 * @param set
	 *            集合对象
	 * @return String
	 */
	public static String set2json(Set<?> set) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (set != null && set.size() > 0) {
			for (Object obj : set) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	/**
	 * @param s
	 *            参数
	 * @return String
	 */
	public static String string2json(String s) {
		if (null == s) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				if (ch >= '\u0000' && ch <= '\u001F') {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 使用Jackson 数据绑定 将对象转换为 json字符串
	 *
	 * 还可以 直接使用 JsonUtils.getInstance().writeValueAsString(Object obj)方式
	 * 
	 * @param obj
	 * @return
	 */
	public static String toJsonString(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			log.error("转换为json字符串失败" + e.toString());
		} catch (JsonMappingException e) {
			log.error("转换为json字符串失败" + e.toString());
		} catch (IOException e) {
			log.error("转换为json字符串失败" + e.toString());
		}
		return null;
	}

	/**
	 * json字符串转化为 JavaBean
	 *
	 * 还可以直接JsonUtils.getInstance().readValue(String content,Class
	 * valueType)用这种方式
	 * 
	 * @param <T>
	 * @param content
	 * @param valueType
	 * @return
	 */
	public static <T> T toJavaBean(String content, Class<T> valueType) {
		try {
			return objectMapper.readValue(content, valueType);
		} catch (JsonParseException e) {
			log.error("json字符串转化为 javabean失败" + e.toString());
		} catch (JsonMappingException e) {
			log.error("json字符串转化为 javabean失败" + e.toString());
		} catch (IOException e) {
			log.error("json字符串转化为 javabean失败" + e.toString());
		}
		return null;
	}

	/**
	 * json字符串转化为list
	 *
	 * 还可以 直接使用 JsonUtils.getInstance().readValue(String content, new
	 * TypeReference<List<T>>(){})方式
	 * 
	 * @param <T>
	 * @param content
	 * @param valueType
	 * @return
	 * @throws IOException
	 */
	public static <T> List<T> toJavaBeanList(String content,
			TypeReference<List<T>> typeReference) throws IOException {
		try {
			return objectMapper.readValue(content, typeReference);
		} catch (JsonParseException e) {
			log.error("json字符串转化为 list失败,原因:" + e.toString());
			throw new RuntimeException("json字符串转化为 list失败");
		} catch (JsonMappingException e) {
			log.error("json字符串转化为 list失败,原因" + e.toString());
			throw new JsonMappingException("json字符串转化为 list失败");
		} catch (IOException e) {
			log.error("json字符串转化为 list失败,原因" + e.toString());
			throw new IOException("json字符串转化为 list失败");
		}
	}

	/**
	 * Map a POJO to the JSON representation
	 * 
	 * @param obj
	 *            需要转换的对象
	 * @return JSON
	 */
	public static String objectToJson(Object obj) {
		try {
			JSONValue value = JSONMapper.toJSON(obj);
			// Convert the JSON value into a string representation
			return value.render(true);
		} catch (MapperException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将制定对象转换为Json字符
	 * 
	 * @param o
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static String toJson(Object o) throws UnsupportedEncodingException,
			ParseException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		if (null == o) {
			return "";
		}

		// 支持的基本数据类型
		if (isSupport(o.getClass())) {
			return convert(o, o.getClass()).toString();
			// 是否是集合类型
		} else if (o instanceof Collection || o.getClass().isArray()) {
			return toJsonArray(o);
			// 是否是Map类型
		} else if (o instanceof Map) {
			return toJsonMap(o);
			// 其他POJO对象
		} else {
			return toJson(o);
		}
	}

	/**
	 * 将指定POJO对象转换为Json
	 * 
	 * @param o
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IllegalArgumentException
	 * @throws ParseException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	/*public static String toJsonObject(Object o)
			throws UnsupportedEncodingException, IllegalArgumentException,
			ParseException, IllegalAccessException, InvocationTargetException {

		if (o == null) {
			return "";
		}
		Class<?> clazz = o.getClass();

		*//**
		 * 根据项目实际情况，只查找2级父类
		 *//*
		Class<?> superClazz = clazz.getSuperclass();
		Method[] superMethods = null;
		Method[] superSuperMethods = null;

		if (null != superClazz && !superClazz.getSimpleName().equals("Object")
				&& !superClazz.getSimpleName().equals("ActionForm")) {
			superMethods = superClazz.getDeclaredMethods();

			Class<?> superSuperClazz = superClazz.getSuperclass();
			if (null != superSuperClazz) {
				superSuperMethods = superSuperClazz.getDeclaredMethods();
			}
		}
		// 获取当前对象所有的方法
		Method[] methods = clazz.getDeclaredMethods();
		// 对象所有的方法，包括2级父类
		Method[] allMethods = null;

		if ((null != superMethods && superMethods.length > 0)
				|| (null != superSuperMethods && superSuperMethods.length > 0)) {

			if (null != superSuperMethods && superSuperMethods.length > 0) {
				allMethods = new Method[methods.length + superMethods.length
						+ superSuperMethods.length];
				System.arraycopy(methods, 0, allMethods, 0, methods.length);
				System.arraycopy(superMethods, 0, allMethods, methods.length,
						superMethods.length);
				System.arraycopy(superSuperMethods, 0, allMethods,
						methods.length + superMethods.length,
						superSuperMethods.length);

			} else {
				allMethods = new Method[methods.length + superMethods.length];
				System.arraycopy(methods, 0, allMethods, 0, methods.length);
				System.arraycopy(superMethods, 0, allMethods, methods.length,
						superMethods.length);
				allMethods = new Method[methods.length + superMethods.length];
			}

		} else {
			allMethods = methods;
		}

		// 获取对象所有以get开头的方法
		Method[] getMethods = BeanHelper.methodStartWithGet(allMethods);
		StringBuffer buffer = new StringBuffer();
		// 字段名
		String key;
		// 字段值
		Object value;
		buffer.append("{");
		for (Method method : getMethods) {
			key = StringUtils.changFirstCharacterCase(method.getName()
					.substring(3), false);
			buffer.append(key);
			if ("class".equals(key)) {
				continue;
			}

			buffer.append(": ");
			value = null;
			if (null != method) {
				try {
					value = method.invoke(o);
				} catch (Exception exp) {
					value = null;
				}
			}
			if (value == null) {
				buffer.append("''");
			} else if (isSupport(method.getReturnType())) {
				buffer.append(convert(value, method.getReturnType()));
			} else if (value instanceof Collection
					|| value.getClass().isArray()) {
				buffer.append(toJsonArray(value));
			} else {
				buffer.append("''");
			}
			buffer.append(", ");
		}
		int end = buffer.lastIndexOf(",");
		// 返回Json结果
		String result;
		if (end > 0) {
			result = buffer.substring(0, end) + "}";
		} else {
			result = buffer.toString() + "}";
		}
		return result;

	}*/

	/**
	 * 将指定对象转换为Json数组
	 * 
	 * @param o
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IllegalArgumentException
	 * @throws ParseException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static String toJsonArray(Object o)
			throws UnsupportedEncodingException, IllegalArgumentException,
			ParseException, IllegalAccessException, InvocationTargetException {
		if (o == null) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		if (o instanceof Collection) {
			Collection<?> temp = (Collection<?>) o;
			for (Object oo : temp) {
				buffer.append(toJson(oo));
				buffer.append(", ");
			}
		} else if (o.getClass().isArray()) {
			int arrayLength = Array.getLength(o);
			for (int i = 0; i < arrayLength; i++) {
				Object oo = Array.get(o, i);
				buffer.append(toJson(oo));
				buffer.append(", ");
			}
		}
		int end = buffer.lastIndexOf(",");
		String result;
		if (end > 0) {
			result = buffer.substring(0, end) + "]";
		} else {
			result = buffer.toString() + "]";
		}
		return result;
	}

	/**
	 * 将指定map对象转换为Json
	 * 
	 * @param o
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IllegalArgumentException
	 * @throws ParseException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static String toJsonMap(Object o)
			throws UnsupportedEncodingException, IllegalArgumentException,
			ParseException, IllegalAccessException, InvocationTargetException {
		if (o == null) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		Map<?, ?> temp = (Map<?, ?>) o;
		for (Object oo : temp.keySet()) {
			buffer.append(toJson(oo));
			buffer.append(": ");
			buffer.append(toJson(temp.get(oo)));
			buffer.append(", ");
		}
		int end = buffer.lastIndexOf(",");
		String result;
		if (end > 0) {
			result = buffer.substring(0, end) + "}";
		} else {
			result = buffer.toString() + "}";
		}
		return result;
	}

	/**
	 * 数据转换，将数据转换为指定类型的数据
	 * 
	 * @param o
	 * @param clazz
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */

	public static Object convert(Object o, Class<?> clazz)
			throws UnsupportedEncodingException, ParseException {

		String value = o.toString();
		if (clazz.equals(String.class))
			return "'" + value + "'";
		if (clazz.equals(int.class) || clazz.equals(Integer.class))
			return Integer.valueOf(value);
		if (clazz.equals(boolean.class) || clazz.equals(Boolean.class))
			return Boolean.valueOf(value);
		if (clazz.equals(long.class) || clazz.equals(Long.class))
			return Long.valueOf(value);
		if (clazz.equals(float.class) || clazz.equals(Float.class))
			return Float.valueOf(value);
		if (clazz.equals(double.class) || clazz.equals(Double.class))
			return Double.valueOf(value);
		if (clazz.equals(short.class) || clazz.equals(Short.class))
			return Short.valueOf(value);
		if (clazz.equals(byte.class) || clazz.equals(Byte.class))
			return Byte.valueOf(value);
		if (clazz.equals(BigDecimal.class))
			return "'" + value.toString() + "'";

		if (value.length() > 0 && clazz.equals(char.class)
				|| clazz.equals(Character.class))
			return value.charAt(0);
		if (clazz.equals(Date.class))
			return "'" + new SimpleDateFormat("yyyy-MM-dd").format(o) + "'";
		if (clazz.equals(Timestamp.class))
			return new Timestamp(new SimpleDateFormat("yyyy-MM-dd")
					.parse(value).getTime());
		throw new IllegalArgumentException("Cannot convert to type: "
				+ clazz.getName());
	}

	public static boolean isSupport(Class<?> clazz) {
		return supportedClasses.contains(clazz);
	}

	/**
	 * 将对象输出为Json格式数据
	 * 
	 * @param response
	 * @param Object
	 */
	public static void writeJson(HttpServletResponse response, Object Object) {
		response.setContentType("text/text;charset=UTF-8");
		response.setHeader("Charset", "UTF-8");
		String result = "";
		try {
			result = toJson(Object);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (ParseException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		}
		try {
			response.getWriter().write(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Set<Class<?>> supportedClasses = new HashSet<Class<?>>();

	static {
		supportedClasses.add(boolean.class);
		supportedClasses.add(char.class);
		supportedClasses.add(byte.class);
		supportedClasses.add(short.class);
		supportedClasses.add(int.class);
		supportedClasses.add(long.class);
		supportedClasses.add(float.class);
		supportedClasses.add(double.class);
		supportedClasses.add(Boolean.class);
		supportedClasses.add(Character.class);
		supportedClasses.add(Byte.class);
		supportedClasses.add(Short.class);
		supportedClasses.add(Integer.class);
		supportedClasses.add(Long.class);
		supportedClasses.add(Float.class);
		supportedClasses.add(Double.class);
		supportedClasses.add(String.class);
		supportedClasses.add(Date.class);
		supportedClasses.add(Timestamp.class);
		supportedClasses.add(BigDecimal.class);

	}

	/**
	 * 从json数组中得到相应java数组 JSONArray下的toArray()方法的使用
	 * 
	 * @param str
	 * @return
	 */
	public static Object[] getJsonToArray(String str) {
		JSONArray jsonArray = JSONArray.fromObject(str);
		return jsonArray.toArray();
	}

	/**
	 * 将json数组转化为Long型
	 * 
	 * @param str
	 * @return
	 */
	public static Long[] getJsonToLongArray(String str) {
		JSONArray jsonArray = JSONArray.fromObject(str);
		Long[] arr = new Long[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			arr[i] = jsonArray.getLong(i);
			System.out.println(arr[i]);
		}
		return arr;
	}

	/**
	 * 将json数组转化为String型
	 * 
	 * @param str
	 * @return
	 */
	public static String[] getJsonToStringArray(String str) {
		JSONArray jsonArray = JSONArray.fromObject(str);
		String[] arr = new String[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			arr[i] = jsonArray.getString(i);
			System.out.println(arr[i]);
		}
		return arr;
	}

	/**
	 * 将json数组转化为Double型
	 * 
	 * @param str
	 * @return
	 */
	public static Double[] getJsonToDoubleArray(String str) {
		JSONArray jsonArray = JSONArray.fromObject(str);
		Double[] arr = new Double[jsonArray.size()];
		for (int i = 0; i < jsonArray.size(); i++) {
			arr[i] = jsonArray.getDouble(i);
		}
		return arr;
	}

	/**
	 * 将json数组转化为Date型
	 * 
	 * @param str
	 * @return
	 */
	public static Date[] getJsonToDateArray(String jsonString) {
		JSONArray jsonArray = JSONArray.fromObject(jsonString);
		Date[] dateArray = new Date[jsonArray.size()];
		String dateString;
		Date date;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < jsonArray.size(); i++) {
			dateString = jsonArray.getString(i);
			try {
				date = sdf.parse(dateString);
				dateArray[i] = date;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dateArray;
	}

	/*
	 * 结果如下： 111 222 333 444
	 * 
	 * Sat Jan 01 00:00:00 CST 2011 Mon Jan 03 00:00:00 CST 2011 Tue Jan 04
	 * 00:00:00 CST 2011
	 */

	/**
	 * JAVA对象转换成JSON字符串
	 * 
	 * @param obj
	 *            JAVA对象
	 * @return JSON字符串
	 * @throws MapperException
	 */
	public static String obj2Json(Object obj) throws MapperException {
		return obj2Json(obj, false);
	}

	/**
	 * JAVA数组对象转换成JSON字符串
	 * 
	 * @param list
	 *            JAVA数组对象
	 * @return JSON字符串
	 * @throws MapperException
	 */
	public static String obj2Json(List<Class<?>> list) throws MapperException {
		if (list == null || list.size() == 0) {
			return "{}";
		}
		StringBuilder jsonString = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			if (i != 0) {
				jsonString.append(",");
			}
			Class<?> cla = list.get(i);
			jsonString.append(obj2Json(cla, false));
		}
		return jsonString.toString();
	}

	/**
	 * JAVA集合对象转换成JSON字符串
	 * 
	 * @param map
	 *            JAVA集合对象
	 * @return JSON字符串
	 * @throws MapperException
	 */
	public static String obj2Json(Map<String, Class<?>> map)
			throws MapperException {
		if (map == null || map.size() == 0) {
			return "{}";
		}
		StringBuilder jsonString = new StringBuilder();
		Set<String> keySet = map.keySet();
		boolean isFirst = true;
		for (String key : keySet) {
			if (isFirst) {
				isFirst = false;
			} else {
				jsonString.append(",");
			}
			Class<?> cla = map.get(key);
			jsonString.append(obj2Json(cla, false));
		}
		return jsonString.toString();
	}

	/**
	 * JAVA集合对象转换成JSON字符串
	 * 
	 * @param map
	 *            JAVA集合对象
	 * @return JSON字符串
	 * @throws MapperException
	 */
	/*@SuppressWarnings("unchecked")
	public static String map2Json(Map<?, ?> map) throws MapperException {
		MapBean mapBean = new MapBean((Map<String, Object>) map);
		return obj2Json(mapBean);
	}*/

	/**
	 * JAVA数组对象转换成JSON字符串
	 * 
	 * @param list
	 *            JAVA数组对象
	 * @return JSON字符串
	 * @throws MapperException
	 */
	/*public static String list2Json(List<?> list) throws MapperException {
		ListBean listBean = new ListBean(list);
		return obj2Json(listBean);
	}*/

	/**
	 * 重载objectToJsonStr方法
	 * 
	 * @param obj
	 *            需要转换的JAVA对象
	 * @param format
	 *            是否格式化
	 * @return JSON字符串
	 * @throws MapperException
	 */
	public static String obj2Json(Object obj, boolean format)
			throws MapperException {
		JSONValue jsonValue = JSONMapper.toJSON(obj);
		String jsonStr = jsonValue.render(format);
		return jsonStr;
	}

	/**
	 * JSON字符串转换成JAVA对象
	 * 
	 * @param jsonStr
	 *            JSON字符串
	 * @param cla
	 *            JAVA对象
	 * @return 转换后的对象
	 */
	/*public static Object json2Obj(String jsonStr, Class<?> cla)
			throws Exception {
		Object obj = null;
		try {
			JSONParser parser = new JSONParser(new StringReader(jsonStr));
			JSONValue jsonValue = parser.nextValue();
			if (jsonValue instanceof com.sdicons.json.model.JSONArray) {
				List<Object> list = new ArrayList<Object>();
				JSONArray jsonArray = (JSONArray) jsonValue;
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONValue jsonObj = jsonArray.get(i);
					Object javaObj = JSONMapper.toJava(jsonObj, cla);
					list.add(javaObj);
				}
				obj = list;
			} else if (jsonValue instanceof com.sdicons.json.model.JSONObject) {
				obj = JSONMapper.toJava(jsonValue, cla);
			} else {
				obj = jsonValue;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}*/

}
