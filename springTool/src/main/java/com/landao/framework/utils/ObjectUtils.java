package com.landao.framework.utils;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ObjectUtils {
	public static HashMap objectToMap(Object obj) throws Exception {
		if (obj == null) {
			throw new Exception("对象为空");
		}

		Class clazz = obj.getClass();
		HashMap map = new HashMap();
		getClass(clazz, map, obj);
		HashMap newMap = convertHashMap(map);
		return newMap;
	}

	public static String getPropertyValueFormObject(Object obj,
			String strProperty) throws Exception {
		if (strProperty == null) {
			return "";
		}
		if (obj == null) {
			return "";
		}
		Class clazz = obj.getClass();
		HashMap map = new HashMap();
		getClass(clazz, map, obj);
		HashMap newMap = convertHashMap(map);

		if (newMap == null) {
			return "";
		}
		Object objReturn = newMap.get(strProperty.trim());
		if (objReturn == null) {
			return "";
		}
		return objReturn.toString();
	}

	private static void getClass(Class clazz, HashMap map, Object obj)
			throws Exception {
		if (clazz.getSimpleName().equals("Object")) {
			return;
		}

		Field[] fields = clazz.getDeclaredFields();
		if ((fields == null) || (fields.length <= 0)) {
			throw new Exception("当前对象中没有任何属性值");
		}
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			String name = fields[i].getName();
			Object value = fields[i].get(obj);
			map.put(name, value);
		}

		Class superClzz = clazz.getSuperclass();
		getClass(superClzz, map, obj);
	}

	private static HashMap convertHashMap(HashMap map) throws Exception {
		HashMap newMap = new HashMap();
		Set keys = map.keySet();
		Iterator it = keys.iterator();
		while (it.hasNext()) {
			Object key = it.next();
			convertToString(map.get(key), newMap, key);
		}

		return newMap;
	}

	private static void convertToString(Object value, HashMap newMap, Object key) {
		if (value != null) {
			Class clazz = value.getClass();
			if (isBaseType(clazz)) {
				newMap.put(key, value.toString());
			} else if (clazz == String.class) {
				newMap.put(key, value.toString());
			} else if (clazz == java.util.Date.class) {
				java.util.Date date = (java.util.Date) value;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				newMap.put(key, sdf.format(date));
			} else if (clazz == Timestamp.class) {
				Timestamp timestamp = (Timestamp) value;
				long times = timestamp.getTime();
				java.util.Date date = new java.util.Date(times);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				newMap.put(key, sdf.format(date));
			} else if (clazz == java.sql.Date.class) {
				java.sql.Date sqlDate = (java.sql.Date) value;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				newMap.put(key, sdf.format(sqlDate));
			} else {
				newMap.put(key, value);
			}
		} else {
			newMap.put(key, value);
		}
	}

	private static boolean isBaseType(Class clazz) {
		if (clazz == Integer.class) {
			return true;
		}
		if (clazz == Long.class) {
			return true;
		}
		if (clazz == Double.class) {
			return true;
		}
		if (clazz == Byte.class) {
			return true;
		}
		if (clazz == Float.class) {
			return true;
		}
		if (clazz == Short.class) {
			return true;
		}

		return clazz == Boolean.class;
	}
}
