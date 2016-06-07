/*
 * @(#)BeanUtils.java
 *       
 * 系统名称：东航电子商务国内B2C系统
 * 版本号：1.0
 * 
 * Copyright (c)  TravelSky
 * All Rights Reserved.
 * 
 * 作者：hujq
 * 创建日期：Oct 29, 2009
 * 
 * 功能描述：
 *   访问在当前类声明的private/protected成员变量及private/protected函数的BeanUtils.
 *   注意,因为必须为当前类声明的变量,通过继承获得的protected变量将不能访问,
 *   需要转型到声明该变量的类才能访问.
 *   反射的其他功能请使用Commons BeanUtils
 *   
 * 公用方法描述：
 * 
 * 修改人：
 * 修改日期：
 * 修改原因：
 * 
 * 
 */
package com.leweiyou.tools.bean;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;

/**
 * Bean对象操作工具类
 * @author jiangjinpeng
 *
 */
public class BeanUtils {

	private static Logger log = Logger.getLogger(BeanUtils.class.getName());

	// ////////////////////////////////////////////////////
	// Bean Reflect Operate //
	// ////////////////////////////////////////////////////
	/**
	 * 获取当前类声明的private/protected变量
	 */
	public static Object getPrivateProperty(Object object, String propertyName) throws IllegalAccessException, NoSuchFieldException {
		Assert.assertNotNull(object);
		Assert.assertNotNull(propertyName);
		Field field = object.getClass().getDeclaredField(propertyName);
		field.setAccessible(true);
		return field.get(object);
	}

	/**
	 * 设置当前类声明的private/protected变量
	 */
	public static void setPrivateProperty(Object object, String propertyName, Object newValue) throws Exception{
		Assert.assertNotNull(object);
		Assert.assertNotNull(propertyName);

		Field field = object.getClass().getDeclaredField(propertyName);
		field.setAccessible(true);
		field.set(object, newValue);
	}

	/**
	 * 调用当前类声明的private/protected函数
	 */
	public static Object invokePrivateMethod(Object object, String methodName,Object[] params) throws Exception{
		Assert.assertNotNull(object);
		Assert.assertNotNull(methodName);
		Class[] types = new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			types[i] = params[i].getClass();
		}
		Method method = object.getClass().getDeclaredMethod(methodName, types);
		method.setAccessible(true);
		return method.invoke(object, params);
	}

	/**
	 * 调用当前类声明的private/protected函数
	 */
	public static Object invokePrivateMethod(Object object, String methodName,Object param) throws Exception{
		return invokePrivateMethod(object, methodName, new Object[] { param });
	}

	/**
	 * 根据属性名称调用相应的Get方法 输入"name"-->调用getName()方法
	 */
	public static Object doGetMethod(Object object, String propertyName)
			throws Exception {
		String getMethodName = "get" + upperFirstChar(propertyName);
		Method[] methods = object.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method method = methods[i];
			if (getMethodName.equals(method.getName())) {
				return method.invoke(object, new Object[] {});
			}
		}
		return null;
	}

	public static String upperFirstChar(String string) {
		if (string == null || "".equals(string))
			return null;

		String first = string.substring(0, 1);
		return first.toUpperCase() + string.substring(1);
	}

	public static Object getPropertyValueTry(Object item, String property) {
		try {
			return PropertyUtils.getProperty(item, property);
		} catch (Exception e) {
		}
		return null;
	}

	// ////////////////////////////////////////////////////
	// Bean Property Copy //
	// ////////////////////////////////////////////////////

	/**
	 * @param dest
	 * @param orig
	 * @throws LsamSystemException
	 */
	public static Object copyProperties(Object dest, Object orig)
			throws Exception {
		if (orig == null) {
			log.debug("");
			return null;
		}
		PropertyUtils.copyProperties(dest, orig);
		return dest;
	}

	public static void clearProperties(Object bean) throws Exception {
		PropertyDescriptor[] props = PropertyUtils.getPropertyDescriptors(bean);
		for (int i = 0; i < props.length; i++) {
			if (props[i] == null) {
				continue;
			}
			if (props[i].getWriteMethod() == null) {
				continue;
			}
			if (props[i].getReadMethod() == null) {
				continue;
			}

			String propName = props[i].getName();
			PropertyUtils.setProperty(bean, propName, null);
		}
	}

	public static boolean hasProperty(Object bean, String propertyName) {
		boolean hasProperty = true;
		try {
			PropertyUtils.getPropertyDescriptor(bean, propertyName);
		} catch (IllegalAccessException e) {
			hasProperty = false;
		} catch (InvocationTargetException e) {
			hasProperty = false;
		} catch (NoSuchMethodException e) {
			hasProperty = false;
		}

		return hasProperty;
	}

	public static Object getProperty(Object bean, String propertyName)
			throws Exception {
		Object propertyValue = null;
		propertyValue = PropertyUtils.getProperty(bean, propertyName);
		return propertyValue;
	}

	// ////////////////////////////////////////////////////
	// Bean Conver Util //
	// ////////////////////////////////////////////////////
	/**
	 * Method to convert a ResourceBundle to a Map object.
	 * 
	 * @param rb
	 *            a given resource bundle
	 * @return Map a populated map
	 */
	public static Map convertBundleToMap(ResourceBundle rb) {
		Map map = new HashMap();

		for (Enumeration keys = rb.getKeys(); keys.hasMoreElements();) {
			String key = (String) keys.nextElement();
			map.put(key, rb.getString(key));
		}

		return map;
	}

	/**
	 * Method to convert a ResourceBundle to a Properties object.
	 * 
	 * @param rb
	 *            a given resource bundle
	 * @return Properties a populated properties object
	 */
	public static Properties convertBundleToProperties(ResourceBundle rb) {
		Properties props = new Properties();

		for (Enumeration keys = rb.getKeys(); keys.hasMoreElements();) {
			String key = (String) keys.nextElement();
			props.put(key, rb.getString(key));
		}

		return props;
	}

	/**
	 * Convenience method used by tests to populate an object from a
	 * ResourceBundle
	 * 
	 * @param obj
	 *            an initialized object
	 * @param rb
	 *            a resource bundle
	 * @return a populated object
	 */
	public static Object populateObject(Object obj, ResourceBundle rb) {
		try {
			Map map = convertBundleToMap(rb);

			BeanUtils.copyProperties(obj, map);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception occured populating object: " + e.getMessage());
		}

		return obj;
	}
}
