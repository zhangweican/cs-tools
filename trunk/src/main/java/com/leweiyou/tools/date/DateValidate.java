/*
 * @(#)DateValidate.java
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
 * 功能描述：日期格式验证类
 * 公用方法描述：
 * 
 * 修改人：
 * 修改日期：
 * 修改原因：
 * 
 * 
 */
package com.leweiyou.tools.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import com.leweiyou.tools.validate.ValidateUtil;

public class DateValidate { 
	
	  
	/**
	 * Check the str whether can be converted to the Date<BR> 
	 */
	public static boolean isDate(String str) { 
		try {
			DateFormat.getDateInstance().parse(str);
		} catch (ParseException e) { return false; } 
		return true;
	}
 
	/**
	 * Check the str whether can be converted to the Date<BR>
	 * @param str 
	 * @return if can convert then true else false.
	 */
	public static boolean isDateTime(String str) {  
		try {
			DateFormat.getDateTimeInstance().parse(str);
		} catch (ParseException e) { return false; } 
		return true;
	}  
	
	public static boolean isDateBefore(String strDate1, String strDate2) {
		Date date1 = DateUtil.toDate(strDate1,null);
		Date date2 = DateUtil.toDate(strDate2,null);
		if(date1.equals(date2))
			return true;
		else
			return date1.before(date2);
	}  
	public static boolean isDateBefore(String strDate1, Date date2) {
		Date date1 = DateUtil.toDate(strDate1,null);
		return date1.before(date2);
	}
	public static boolean isDateBefore(Date date1, String strDate2) {
		Date date2 = DateUtil.toDate(strDate2,null);
		return date1.before(date2);
	}
	public static boolean isDateBefore(Date date1, Date date2) {
		return date1.before(date2);
	}

	public static boolean isDateTimeBefore(String strDate1, String strDate2) {
		Date date1 = DateUtil.toDate(strDate1,"yyyy-MM-dd HH:mm");
		Date date2 = DateUtil.toDate(strDate2,"yyyy-MM-dd HH:mm");
		return date1.before(date2);
	} 
	public static boolean isDateEquals(String strDate1,String strDate2){
		Date date1 = DateUtil.toDate(strDate1,null);
		Date date2 = DateUtil.toDate(strDate2,null);
		if(date1.equals(date2))
			return true;
		return false;
	} 
	public static boolean isEqualsDateStr(Date date1,Date date2){
		try{
			if (date1 == null && date2 == null)				return true;
			if (date1 == null || date2 == null)				return false;
			if (DateUtil.toString(date1, DateUtil.yyyy_MM_dd).equals(DateUtil.toString(date2, DateUtil.yyyy_MM_dd))) 
				return true;
		}catch (Exception e){}
		return false;
	}

	/**
	 * 是否第一个时间比第二个时间早半个小时
	 * 10:25 比 11:00 早半个小时，返回 true
	 * @param strDate1
	 * @param strDate2
	 * @return
	 */
	public static boolean isBeforeDateTimeHalfHour(String strDate1, String strDate2) { 
		Date date1 = DateUtil.toDate(strDate1,null); 
		Date date2 = DateUtil.toDate(strDate2,null);
		
		GregorianCalendar tktl = new GregorianCalendar();
		tktl.setTime(date1); 
		tktl.add(GregorianCalendar.MINUTE, 30);
		date1 = tktl.getTime();
		
		return date1.before(date2);
	} 

	/**
	 * 是否第一个时间比第二个时间早一个小时
	 * 10:25 比 11:00 没早个小时，返回 false
	 * @param strDate1
	 * @param strDate2
	 * @return
	 */
	public static boolean isBeforeDateTimeOneHour(String strDate1, String strDate2) {
		Date date1 = DateUtil.toDate(strDate1,"yyyy-MM-dd HH:mm"); 
		Date date2 = DateUtil.toDate(strDate2,"yyyy-MM-dd HH:mm");
		
		GregorianCalendar tktl = new GregorianCalendar();
		tktl.setTime(date1); 
		tktl.add(GregorianCalendar.HOUR, 1);
		date1 = tktl.getTime();
		
		return date1.before(date2);
	}
 
	/**
	 * 是否第一个时间比第二个时间早n个小时
	 * 10:25 比 11:00 没早个小时，返回 false
	 * @param strDate1
	 * @param strDate2
	 * @param hours
	 * @return
	 */
	public static boolean isBeforeDateTimeHours(String strDate1, String strDate2,int hours) {
		Date date1 = DateUtil.toDate(strDate1,"yyyy-MM-dd HH:mm"); 
		Date date2 = DateUtil.toDate(strDate2,"yyyy-MM-dd HH:mm");
		
		GregorianCalendar tktl = new GregorianCalendar();
		tktl.setTime(date1); 
		tktl.add(GregorianCalendar.HOUR, hours);
		date1 = tktl.getTime();
		
		return date1.before(date2);
	}
	/**
	 * 是否第一个时间比第二个时间早n个小时
	 * 10:25 比 11:00 没早个小时，返回 false
	 * @param strDate1
	 * @param strDate2
	 * @param hours
	 * @return
	 */
	public static boolean isBeforeDateTimeHours(Date date1, Date date2,int hours) { 
		GregorianCalendar tktl = new GregorianCalendar();
		tktl.setTime(date1); 
		tktl.add(GregorianCalendar.HOUR, hours);
		date1 = tktl.getTime(); 
		return date1.before(date2);
	}

	// ///////////////////////////////// 日期验证 /////////////////////////////
	/**
	 * 带反斜杠的日期格式,yyyyMMdd或者yyyyMMd 日期的格式可以只有一位 20006011 --> 20060101  
	 */
	public static boolean isStrDate(String date) {
		if (date == null || "".equals(date))
			return false; 
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		// 这个的功能是不把1996-13-3 转换为1997-1-3
		df.setLenient(false);
		try {
			df.parse(date);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 带横线的日期格式,yyyy-MM-dd或者yy-MM-dd/yy-M-d  
	 */
	public static boolean isLineDate(String date) {
		if (date == null || "".equals(date))
			return false;

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		// 这个的功能是不把1996-13-3 转换为1997-1-3
		df.setLenient(false);
		try {
			df.parse(date);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 带反斜杠的日期格式,yyyy/MM/dd或者yy/MM/dd 
	 */
	public static boolean isSlashDate(String date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		// 这个的功能是不把1996/13/3 转换为1997/1/3
		df.setLenient(false);
		try {
			df.parse(date);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	
	// ///////////////////////////////// 时间验证 ///////////////////////////// 
	/**
	 * 13:25 
	 * @param date
	 * @return
	 */
	public static boolean isTime24(String time) {

		String[] times = time.split(":");
		if (times == null || times.length < 2)
			return false;
		if (!ValidateUtil.isDigit(times[0]) || !ValidateUtil.isDigit(times[1]))
			return false;

		if (Integer.parseInt(times[0]) < 0 || Integer.parseInt(times[0]) > 23)
			return false;
		if (Integer.parseInt(times[1]) < 0 || Integer.parseInt(times[1]) > 59)
			return false;
		return true;
	}  
}
