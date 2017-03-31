package com.leweiyou.tools.page;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * Request的页面参数类
 * @author Zhangweican
 *
 */
public class PageData extends HashMap implements Map{
	
	private static final long serialVersionUID = 1L;
	
	Map map = null;
	HttpServletRequest request;
	
	public PageData(HttpServletRequest request){
		this.request = request;
		Map properties = request.getParameterMap();
		Map returnMap = new HashMap(); 
		Iterator entries = properties.entrySet().iterator(); 
		Map.Entry entry; 
		String name = "";  
		String value = "";  
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next(); 
			name = (String) entry.getKey(); 
			Object valueObj = entry.getValue(); 
			if(null == valueObj){ 
				value = ""; 
			}else if(valueObj instanceof String[]){ 
				String[] values = (String[])valueObj;
				for(int i=0;i<values.length;i++){ 
					 value += values[i] + ",";
				}
				value = value.substring(0, value.length()-1); 
			}else{
				value = valueObj.toString(); 
			}
			returnMap.put(name, value); 
		}
		map = returnMap;
	}
	
	public PageData() {
		map = new HashMap();
	}
	
	@Override
	public Object get(Object key) {
		Object obj = null;
		if(map.get(key) instanceof Object[]) {
			Object[] arr = (Object[])map.get(key);
			obj = request == null ? arr:(request.getParameter((String)key) == null ? arr:arr[0]);
		} else {
			obj = map.get(key);
		}
		return obj;
	}
	
	public String getString(Object key) {
		Object o = get(key);
		if(o == null){
			return null;
		}
		return String.valueOf(get(key));
	}
	public Integer getInteger(Object key) {
		String str = getString(key);
		if(StringUtils.isEmpty(str)){
			return null;
		}
		return Integer.valueOf(str);
	}
	public Long getLong(Object key) {
		String str = getString(key);
		if(StringUtils.isEmpty(str)){
			return null;
		}
		return Long.valueOf(str);
	}
	public Float getFloat(Object key) {
		String str = getString(key);
		if(StringUtils.isEmpty(str)){
			return null;
		}
		return Float.valueOf(str);
	}
	public Double getDouble(Object key) {
		String str = getString(key);
		if(StringUtils.isEmpty(str)){
			return null;
		}
		return Double.valueOf(str);
	}
	public ArrayList getList(Object key) {
		String str = getString(key);
		if(StringUtils.isEmpty(str)){
			return null;
		}
		String[] strs = str.split(",");
		ArrayList list = new ArrayList();
		Collections.addAll(list, strs);
		return list;
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Object put(Object key, Object value) {
		return map.put(key, value);
	}
	
	@Override
	public Object remove(Object key) {
		return map.remove(key);
	}

	public void clear() {
		map.clear();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public Set entrySet() {
		return map.entrySet();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set keySet() {
		return map.keySet();
	}

	@SuppressWarnings("unchecked")
	public void putAll(Map t) {
		map.putAll(t);
	}

	public int size() {
		return map.size();
	}

	public Collection values() {
		return map.values();
	}
	
}
