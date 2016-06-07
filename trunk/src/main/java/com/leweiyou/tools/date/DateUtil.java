/*
 * @(#)DateUtil.java
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
 * 功能描述：日期工具类（转换、格式化、计算等）
 * 公用方法描述：
 * 
 * 修改人：
 * 修改日期：
 * 修改原因：
 * 
 * 
 */
package com.leweiyou.tools.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.leweiyou.tools.validate.FormatUtil;
import com.leweiyou.tools.validate.ValidateUtil;


public class DateUtil {
	//日期格式
	public static String yyyyMMdd         = "yyyyMMdd"; 
	public static String yyyyMMddHHmm     = "yyyyMMddHHmm";
	public static String yyyyMMddHHmmss   = "yyyyMMddHHmmss";
	public static String yyyyMMddHHmmssS  = "yyyyMMddHHmmssS";

	//中划线日期格式
	public static String yyyy_MM_dd            = "yyyy-MM-dd"; 
	public static String yyyy_MM_dd_HH_mm      = "yyyy-MM-dd HH:mm"; 
	public static String yyyy_MM_dd_HH_mm_ss   = "yyyy-MM-dd HH:mm:ss"; 
	public static String yyyy_MM_dd_HH_mm_ss_S = "yyyy-MM-dd HH:mm:ss.S"; 
	 
	//反斜杠日期格式
	public static String YYYYsMMsDD            = "yyyy/MM/dd"; 
	public static String MMsDDsYYYY            = "MM/dd/yyyy"; 
	public static String DDsMMsYYYY            = "dd/MM/yyyy"; 

	//时间按
	public static String MM_dd = "MM-dd"; 
	public static String HH_mm = "HH:mm";  //24小时制
	public static String hh_mm = "hh:mm";  //12小时制
	public static String HHmm  = "HHmm"; 
	public static String hhmm  = "hhmm"; 
	 
	//英文日期
	public static String ddMMM     = "ddMMM";       //09Oct
	public static String ddMMMyy   = "ddMMMyy";     //01Jan09
	public static String ddMMMyyyy = "ddMMMyyyy";   //01Jan2009  

	public static Calendar getCalendar(Date date) {
		try{
			Calendar calendar = Calendar.getInstance();
			calendar.setFirstDayOfWeek(Calendar.MONDAY);
			calendar.setTime(date);
			return calendar;
		}catch (Exception e){ return null; }
	}
	public static Calendar getCalendarByDateStr(String dateString,String formater) throws Exception{		
		try	{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new SimpleDateFormat(formater).parse(dateString));
			return calendar;
		}catch (Exception e){ return null; } 
	} 
	
	/////////////////////////////////////////////////////////////////////////////////////
	//-----------------------     取得相隔指定天数的日期  ---------------------------------//
	/////////////////////////////////////////////////////////////////////////////////////
	
	public static String BEFORE = "-"; //减少天数
	public static String AFTER  = "+"; //增加天数
	
	/**
	 * 取得相隔数量的日期的日期
	 * @param date      指定日期
	 * @param type      BEFORE:往前，AFTER：往后
	 * @param interval  相隔的日期 
	 */
	public static Date getIntervalDate(Date date,String type,int interval){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if(DateUtil.BEFORE.equals(type))  cal.add(Calendar.DATE, -interval);
		if(DateUtil.AFTER.equals(type))   cal.add(Calendar.DATE, +interval); 
		return cal.getTime();
	}
	
	/** 取得相隔数量的日期的日期字符串*/
	public static String getIntervalDateStr(Date date,String formater,String type,int interval){
		Date lastDate=  getIntervalDate(date,type,interval);
		return DateUtil.toString(lastDate,formater);
	}    
	
	/**
	 * 取得两个时间的间隔，于得到的时间为格林尼治时间，相应间去8小时，得到北京时间
	 * @param depTime  出发日期
	 * @param arrTime  到达日期 
	 */
	public static Date getSpanTime(Date depTime,Date arrTime){ 
		Date spanTime = new Date();
		spanTime.setTime(arrTime.getTime() - depTime.getTime() - 28800000);
		return spanTime;
	}
	public static Date getSpanTime(String depTimeStr,String arrTimeStr){ 
		Date depTime = DateUtil.toDate(depTimeStr, DateUtil.HH_mm);
		Date arrTime = DateUtil.toDate(arrTimeStr, DateUtil.HH_mm); 
		Date spanTime = new Date();
		spanTime.setTime(arrTime.getTime() - depTime.getTime() - 28800000);
		return spanTime;
	}
	public static long getSpanSeconds(Date startTime,Date endTime){ 
		return endTime.getTime() - startTime.getTime();
	} 
	
	/** 取得当前年、月、日、小时、分、秒等数值**/
	public static int getCurrentYear()   { return Calendar.getInstance().get(Calendar.YEAR);}
	public static int getCurrentMonth()  { return Calendar.getInstance().get(Calendar.MONTH);}
	public static int getCurrentDay()    { return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);}
	public static int getCurrentHour12() { return Calendar.getInstance().get(Calendar.HOUR);}
	public static int getCurrentHour24() { return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);}
	public static int getCurrentMinute() { return Calendar.getInstance().get(Calendar.MINUTE);}
	public static int getCurrentSecond() { return Calendar.getInstance().get(Calendar.SECOND);}
	

	/////////////////////////////////////////////////////////////////////////////////////
	//----------------------------  日期类型之间的转换    ---------------------------------//  
	/////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 把字符串日期类型按照格式转换成Date日期，缺省的日期格式为yyyy-MM-dd
	 * @param dateStr     字符串类型日期
	 * @param formater    日期格式，缺省为yyyy-MM-dd 
	 */
    public static Date toDate(String dateStr,String formater) {
		if (ValidateUtil.isNullOrEmpty(dateStr)) return null;  
        try {
        	if(!ValidateUtil.isNullOrEmpty(formater))
        		return new SimpleDateFormat(formater).parse(dateStr);
        	else
        		return new SimpleDateFormat(yyyy_MM_dd).parse(dateStr); 
		} catch (Exception pe) {
			return null;
		} 
    }  
    
    /**
	 * Date类型：不同格式之间的转换
     * @param oldDate      老的日期
     * @param oldFormater  老的日期格式
     * @param newFormater  新的日期格式 
     */
    public static Date toDate(Date oldDate,String oldFormater,String newFormater) {
		if (oldDate == null) return null;  
        try { 
        	String oldDateString = DateUtil.toString(oldDate, oldFormater);
        	return DateUtil.toDate(oldDateString, newFormater);
		} catch (Exception pe) { return null; } 
    }  

	/**
	 * 把字符串日期类型按照格式转换成Date日期，缺省的日期格式为yyyy-MM-dd
	 * @param dateStr     字符串类型日期
	 * @param formater    日期格式，缺省为yyyy-MM-dd
	 * @param locale      语言变量，为NULL使用缺省的Locale 
	 */
    public static Date toDate(String dateStr,String oriFormater,Locale oriLocale) {
    	//如果语言环境为空，则直接返回toDate(dateStr,formater);
    	if(oriLocale == null) return toDate(dateStr,oriFormater); 
		if (ValidateUtil.isNullOrEmpty(dateStr)) return null;  
        try {
        	if(!ValidateUtil.isNullOrEmpty(oriFormater))
        		return new SimpleDateFormat(oriFormater,oriLocale).parse(dateStr);
        	else
        		return new SimpleDateFormat(yyyy_MM_dd,oriLocale).parse(dateStr); 
		} catch (Exception pe) { return null; } 
    }  
    
    /**
	 * 把日期类型按照格式转换成字符串，缺省的日期格式为yyyy-MM-dd
	 * @param date        Date类型日期
	 * @param formater    日期格式，缺省为yyyy-MM-dd 
	 */
	public static String toString(Date date,String dstFormater){
		try {
			//缺省的日期格式为
			if(ValidateUtil.isNullOrEmpty(dstFormater))
				return new SimpleDateFormat("yyyy-MM-dd").format(date);
			else
				return new SimpleDateFormat(dstFormater).format(date);
		} catch (Exception e) { return null; }  
	}
	
    /**
	 * 把日期类型按照格式转换成字符串
	 * @param date        Date类型日期
	 * @param formater    日期格式，缺省为yyyy-MM-dd 
	 */
	public static String toString(Date date,String dstFormater,Locale dstLocale){
		try { 
			if(ValidateUtil.isBlank(dstFormater)) dstFormater = DateUtil.ddMMMyy;
			if(dstLocale == null)
				return new SimpleDateFormat(dstFormater).format(date).toUpperCase();
			else
				return new SimpleDateFormat(dstFormater,dstLocale).format(date).toUpperCase();
		} catch (Exception e) { return null; }  
	}
	
    /**
	 * 把日期字符串按照格式相互转换 
	 * @param oriDateStr   原始日期字符串
	 * @param oriFormater  日期日期的格式
	 * @param dstFormater  转换后的日期格式  
	 */
	public static String toString(String oriDateStr, String oriFormater,String dstFormater){
		try {
			if (ValidateUtil.isBlank(oriDateStr))return "";
			if (ValidateUtil.isBlank(oriFormater))  oriFormater = DateUtil.yyyy_MM_dd;
			Date date = DateUtil.toDate(oriDateStr, oriFormater);
			
			if (ValidateUtil.isBlank(dstFormater))	dstFormater = DateUtil.yyyy_MM_dd;
			return new SimpleDateFormat(dstFormater).format(date);
		} catch (Exception e) { return null; }
	} 

	/**
	 * 把日期字符串按照格式相互转换（原始日期和目标日期都有LOCALE要求） 
	 * @param oriDateStr   原始日期字符串
	 * @param oriFormater  日期日期的格式
	 * @param oriLocale    原始日期的LOCALE
	 * @param dstFormater  目标日期的格式
	 * @param dstLocale    目标日期的LOCALE 
	 */
	public static String toString(String oriDateStr, String oriFormater,Locale oriLocale,String dstFormater,Locale dstLocale){
		try {
			if(ValidateUtil.isBlank(oriDateStr))  return "";
			if(ValidateUtil.isBlank(oriFormater)) oriFormater = DateUtil.yyyy_MM_dd;	 
			Date date = DateUtil.toDate(oriDateStr, oriFormater,oriLocale); 

			if(ValidateUtil.isBlank(dstFormater)) dstFormater = DateUtil.ddMMMyy;
			if(dstLocale == null)
				return new SimpleDateFormat(dstFormater).format(date).toUpperCase();
			else
				return new SimpleDateFormat(dstFormater,dstLocale).format(date).toUpperCase();
		} catch (Exception e) { return null; }
	}
	
	public static String formatDate(String value, String oldStyle, String newStyle)
	{
		if (ValidateUtil.isNullOrEmpty(value))
			return "";
		SimpleDateFormat df = new SimpleDateFormat(oldStyle);
		SimpleDateFormat df1 = new SimpleDateFormat(newStyle);
		try
		{
			return df1.format(df.parse(value));
		}
		catch (ParseException e)
		{
			
			return "";
		}
	}
	////////////////////////////////////////////////////////////////////////////////////
	//                                      中文日期                                        ///
	//////////////////////////////////////////////////////////////////////////////////// 
	/** 取得中文格式的日期 --> 2010年03月15日*/
	public static String getChineseDate(Date d) {
		if (d == null) return null;  
		String dtrDate = new SimpleDateFormat(yyyyMMdd).format(d);
		return dtrDate.substring(0, 4) + "\u5E74" + Integer.parseInt(dtrDate.substring(4, 6)) + "\u6708" + Integer.parseInt(dtrDate.substring(6, 8)) + "\u65E5";
	} 
	/** 取得中文格式的日期 --> 03月15日 16:15*/
	public static String getChineseDateMMddHHss(Date d) {
		if (d == null) return null;   
		String dtrDate = DateUtil.toString(d, DateUtil.yyyy_MM_dd_HH_mm);  
		return dtrDate.substring(5, 7) + "\u6708" + dtrDate.substring(8, 10) + "\u65E5" + dtrDate.substring(11);
	} 
	/** 取得中文的星期名称 */
	public static String getCnDayNameOfWeek(Date date) { 
		String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"}; 
		Calendar calendar = DateUtil.getCalendar(date);
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		return weeks[day-1];
	}
	/** 取得中文的星期名称 */
	public static String getCnDayNameOfWeek(String strDate) {
		Date date =DateUtil.toDate(strDate,null);
		return getCnDayNameOfWeek(date);
	} 
	/** 取得月份的序号 */
	public static String getNumberByEnMonth(String enMonthString) { 		
		String[] months = {"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"}; 
		for(int i=0;i<months.length;i++){
			if(months[i].equalsIgnoreCase(enMonthString)){
				if(i<9) return "0"+(i+1);
				return Integer.toString(i+1);
			}
		}
		return null;
	}   

	/**
	 * 取得相隔数量的【日或者月】的日期
	 * @param date      指定日期
	 * @param type      BEFORE:往前，AFTER：往后
	 * @param interval  相隔的日或者月，格式为 14D/12M【相隔14日/相隔12月】
	 */
	public static Date getIntervalDate(Date date,String type,String intervalString){	 
		try {
			//14D --> interval=14,dayOrMonth=D
			int interval = FormatUtil.toInt(intervalString.substring(0, intervalString.length() - 1));
			String dayOrMonth = intervalString.substring(intervalString.length() - 1);
			 
			Calendar cal = Calendar.getInstance();
			cal.setTime(date); 
			if ("S".equals(dayOrMonth.toUpperCase())){
				if (DateUtil.BEFORE.equals(type)) cal.add(Calendar.SECOND, -interval);
				if (DateUtil.AFTER.equals(type))  cal.add(Calendar.SECOND, +interval);
			}else if ("I".equals(dayOrMonth.toUpperCase())){
				if (DateUtil.BEFORE.equals(type)) cal.add(Calendar.MINUTE, -interval);
				if (DateUtil.AFTER.equals(type))  cal.add(Calendar.MINUTE, +interval);
			}else if ("H".equals(dayOrMonth.toUpperCase())){
				if (DateUtil.BEFORE.equals(type)) cal.add(Calendar.HOUR, -interval);
				if (DateUtil.AFTER.equals(type))  cal.add(Calendar.HOUR, +interval);
			}else if ("D".equals(dayOrMonth.toUpperCase())){
				if (DateUtil.BEFORE.equals(type)) cal.add(Calendar.DATE, -interval);
				if (DateUtil.AFTER.equals(type))  cal.add(Calendar.DATE, +interval);
			}else if ("M".equals(dayOrMonth.toUpperCase())){
				if (DateUtil.BEFORE.equals(type)) cal.add(Calendar.MONTH, -interval);
				if (DateUtil.AFTER.equals(type))  cal.add(Calendar.MONTH, +interval);
			}else if ("Y".equals(dayOrMonth.toUpperCase())){
				if (DateUtil.BEFORE.equals(type)) cal.add(Calendar.YEAR, -interval);
				if (DateUtil.AFTER.equals(type))  cal.add(Calendar.YEAR, +interval);
			}
			return cal.getTime();
		}catch (Exception e)	{return null;}
	}
	public static Date getIntervalDate(String dateStr,String type,String intervalString){	 
		Date date = DateUtil.toDate(dateStr,DateUtil.yyyy_MM_dd);
		return getIntervalDate(date,type,intervalString);
	}
	
	////////////////////////////////////////////////////////////////////////////////////
	//                                      国际化日期                                      //
	////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * 根据语言类型获取星期名称
	 * @param date
	 * @param locale
	 * @return
	 */
	public static String getDayofTheWeek(Date date,Locale locale){
		if (locale != null && locale.toString().startsWith(Locale.UK.toString())){
			String[] weeks = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"}; 
			Calendar calendar = DateUtil.getCalendar(date);
			int day = calendar.get(Calendar.DAY_OF_WEEK);
			return weeks[day-1];
		}
		return DateUtil.getCnDayNameOfWeek(date);
	}
	
	/**
	 * 根据语言类型获取日期格式
	 * @param date
	 * @param locale
	 * @return
	 */
	public static String getDate(Date date,Locale locale){
		if (locale != null && locale.toString().startsWith(Locale.UK.toString())){
			return DateUtil.toString(date, DateUtil.MM_dd);
		}
		return DateUtil.getChineseDate(date).substring(5);
	}

	/**
	 * 根据语言类型获取日期格式
	 * @param date
	 * @param locale
	 * @return
	 */
	public static String getFormatDateForUpdateTime(Date date,Locale locale){
		
		if (locale != null && locale.toString().startsWith(Locale.UK.toString())){
			return DateUtil.toString(date, DateUtil.yyyy_MM_dd_HH_mm);
		}
		return DateUtil.getChineseDateMMddHHss(date);
	}
	public static XMLGregorianCalendar date2XMLGregorianCalendar(Date date) {
		if (date == null)
			return null;
		GregorianCalendar cal = new GregorianCalendar();
		
		cal.setTime(date);
		 XMLGregorianCalendar xmlCa=null;		
		try {
			 xmlCa = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmlCa;
	}
	
}
