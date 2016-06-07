/*
 * @(#)IDGen.java
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
 * 功能描述：使用JDK1.5提供的功能生成32位的UUID
 * 公用方法描述：
 * 
 * 修改人：
 * 修改日期：
 * 修改原因：
 * 
 * 
 */
package com.leweiyou.tools.utils;

import java.util.Date;
import java.util.UUID;

import com.leweiyou.tools.date.DateUtil;
import com.leweiyou.tools.validate.ValidateUtil;


public class IDGen {

	/**
	 * 使用JDK1.5提供的功能生成32位的UUID
	 * 
	 * @return
	 */
	public static String getUuid() { 
		String id = java.util.UUID.randomUUID().toString(); 
		return id.substring(0,8)   + id.substring(9,13) + id.substring(14,18) + id.substring(19,23) + id.substring(24); 
	}
	public static String get32UUID() {
		String id = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return id;
	}
	
	public static String getSerialNo(){
		return DateUtil.toString(new Date(),DateUtil.yyyyMMddHHmmssS);
	}
	
	public static String getSerialNo(String type){
		String timeStr = DateUtil.toString(new Date(),DateUtil.yyyyMMddHHmmssS);
		
		if(ValidateUtil.isBlank(type))
			return timeStr;
		else
			return type+timeStr;
	}

	public static String getSerialNo(String type,Date date){
		String timeStr = DateUtil.toString(date,DateUtil.yyyyMMddHHmmssS);
		
		if(ValidateUtil.isBlank(type))
			return timeStr;
		else
			return type+timeStr;
	}
	
	public static String getSerialNo(String type,Date date,String format){
		String timeStr = DateUtil.toString(date,format);
		
		if(ValidateUtil.isBlank(type))
			return timeStr;
		else
			return type+timeStr;
	}  
}
