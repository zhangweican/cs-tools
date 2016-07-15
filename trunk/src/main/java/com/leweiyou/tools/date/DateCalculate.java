/*
 * @(#)DateCalculate.java
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
 * 公用方法描述：
 * 
 * 修改人：
 * 修改日期：
 * 修改原因：
 * 
 * 
 */
package com.leweiyou.tools.date;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

public class DateCalculate { 
	/**
	 * 计算日期间隔的天数
	 * 
	 * @param beginDate   开始日期
	 * @param endDate     结束日期
	 * @return
	 * @pre beginDate != null
	 */
	public static int daysBetweenDates(Date beginDate, Date endDate) {
		int days = 0;
		Calendar calo = Calendar.getInstance();
		Calendar caln = Calendar.getInstance();
		calo.setTime(beginDate);
		caln.setTime(endDate);
		int oday = calo.get(6);
		int nyear = caln.get(1);
		for (int oyear = calo.get(1); nyear > oyear;) {
			calo.set(2, 11);
			calo.set(5, 31);
			days += calo.get(6);
			oyear++;
			calo.set(1, oyear);
		}

		int nday = caln.get(6);
		days = (days + nday) - oday;
		return days;
	}

	/**
	 * 计算间隔天数后的日期 
	 * @param date
	 * @param intBetween 
	 */
	public static Date getDateBetween(Date date, int intBetween) {
		Calendar calo = Calendar.getInstance();
		calo.setTime(date);
		calo.add(Calendar.DATE, intBetween);
		return calo.getTime();
	}

	/**
	 * 计算指定天数后的日期 
	 * @param date
	 * @param intBetween
	 * @param strFromat  日期格式 
	 */
	public static String getDateBetween_String(Date date, int intBetween, String strFromat) {
		Date dateOld = getDateBetween(date, intBetween);
		return DateUtil.toString(dateOld, strFromat);
	}

	/**
	 * 年月值增1 
	 * @param yearMonth
	 * @return
	 * @pre yearMonth != null 
	 */
	public static String increaseYearMonth(String yearMonth) {
		int year = (new Integer(yearMonth.substring(0, 4))).intValue();
		int month = (new Integer(yearMonth.substring(4, 6))).intValue();
		if (++month <= 12 && month >= 10)
			return yearMonth.substring(0, 4) + (new Integer(month)).toString();
		if (month < 10)
			return yearMonth.substring(0, 4) + "0" + (new Integer(month)).toString();

		return (new Integer(year + 1)).toString() + "0" + (new Integer(month - 12)).toString();
	}

	/**
	 * 年月值增加指定的值 
	 * @param yearMonth 初始年月
	 * @param addMonth  指定的值
	 * @return 
	 */
	public static String increaseYearMonth(String yearMonth, int addMonth) {
		int year = (new Integer(yearMonth.substring(0, 4))).intValue();
		int month = (new Integer(yearMonth.substring(4, 6))).intValue();
		month += addMonth;
		year += month / 12;
		month %= 12;
		if (month <= 12 && month >= 10)
			return year + (new Integer(month)).toString(); 
		return year + "0" + (new Integer(month)).toString();
	}

	/**
	 * 年月值减1 
	 * @param yearMonth
	 * @return 
	 */
	public static String descreaseYearMonth(String yearMonth) {
		int year = (new Integer(yearMonth.substring(0, 4))).intValue();
		int month = (new Integer(yearMonth.substring(4, 6))).intValue();
		if (--month >= 10)
			return yearMonth.substring(0, 4) + (new Integer(month)).toString();
		if (month > 0 && month < 10)
			return yearMonth.substring(0, 4) + "0" + (new Integer(month)).toString(); 
		return (new Integer(year - 1)).toString() + (new Integer(month + 12)).toString();
	}

	/**
	 * 年月值增1 
	 * @param yearMonth 
	 */
	public static String addYearMonth(String yearMonth) {
		int year = (new Integer(yearMonth.substring(0, 4))).intValue();
		int month = (new Integer(yearMonth.substring(4, 6))).intValue();
		if (++month >= 10 && month < 12)
			return yearMonth.substring(0, 4) + (new Integer(month)).toString();
		if (month > 0 && month < 10)
			return yearMonth.substring(0, 4) + "0" + (new Integer(month)).toString(); 
		return (new Integer(year + 1)).toString() + "0" + (new Integer(month - 12)).toString();
	} 

	/**
	 * 计算当前日期的下个月的第一天 
	 * @return
	 */
	public static String getFirstDayOfNextMonth() {
		String strToday = DateUtil.toString(new Date(),null);
		return increaseYearMonth(strToday.substring(0, 6)) + "01";
	}
	

	public static Date getDateSpan(Date date1, Date date2) {
		if (null == date1 || null == date2)
			return null;
		long spanMillis = date1.getTime() - date2.getTime();
		return new Date(spanMillis);
	}

	/**
	 * 取得 date1 和 date2之间相差的小时和分钟（HH:mm） 
	 */
	public static String getDateSpanStr(Date date1, Date date2) {
		Date dateSpan = getDateSpan(date1, date2);
		if (null == dateSpan) return ""; 
		new SimpleDateFormat(DateUtil.HH_mm).setTimeZone(TimeZone.getTimeZone("GMT"));
		return new SimpleDateFormat(DateUtil.HH_mm).format(dateSpan);  
	}

	
	///////////////////////////////  IBE 所用到的时间函数　　//////////////////////////// 
	public static Date getSpanTime(Date orgTime,Date destTime){
		long span = destTime.getTime() - orgTime.getTime();
		Date spanTime = new Date();
		//由于得到的时间为格林尼治时间，相应间去8小时，得到北京时间
		spanTime.setTime(span - 28800000);
		return spanTime;
	} 
	
	/**
	 * 计算两个日期之间的年差，用于计算年龄 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getYearsBetweent2Date(Date date1,Date date2) throws Exception{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);		
		int year1 = cal.get(1);
		cal.setTime(date2);
		int year2 = cal.get(1);
		return year2 - year1;
	}
	
	/**
	 * 计算两个日期之间的年差，用于计算年龄
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getYearsBetweent2Date(String date1Str,Date date2) throws Exception{  
		Calendar cal = Calendar.getInstance();
		cal.setTime(new SimpleDateFormat(DateUtil.yyyy_MM_dd).parse(date1Str));		
		int year1 = cal.get(1);
		cal.setTime(date2);
		int year2 = cal.get(1);
		return year2 - year1; 
	} 

	/**
	 * 给时间字符串减少几个小时
	 * 1400 --- > 12:00
	 * 14:00 ---> 12:00
	 * 
	 * @param time
	 * @return
	 */
	public static String minusOurs(String time, int minusOurNO){
		time = addColon4FlightTime(time);
		if(time == null) return null; 
		if(!DateValidate.isTime24(time)) return null; 
		String[] times = time.split(":");
		int our = Integer.parseInt(times[0]) - minusOurNO; 
		return our + ":" + times[1];
	}

	/**
	 * 返回两个年月之间间隔的月数 
	 * @param dealMonth
	 * @param alterMonth
	 * @return
	 * @pre alterMonth != null
	 * @pre dealMonth != null
	 */
	public static int calBetweenTwoMonths(String dealMonth, String alterMonth) {
		if (dealMonth.length() != 6 || alterMonth.length() != 6) 
			return -1; 

		int dealInt = Integer.parseInt(dealMonth);
		int alterInt = Integer.parseInt(alterMonth);
		if (dealInt < alterInt)
			return -2;
 
		int dealYearInt = Integer.parseInt(dealMonth.substring(0, 4));
		int dealMonthInt = Integer.parseInt(dealMonth.substring(4, 6));
		int alterYearInt = Integer.parseInt(alterMonth.substring(0, 4));
		int alterMonthInt = Integer.parseInt(alterMonth.substring(4, 6));
		int length = (dealYearInt - alterYearInt) * 12 + (dealMonthInt - alterMonthInt); 
		return length;
	}
	

	/**
	 * 比较日期1是否大于等于日期2 
	 * @param s1 日期1
	 * @param s2 日期2 
	 */
	public static boolean yearMonthGreatEqual(String s1, String s2) {
		String temp1 = s1.substring(0, 4);
		String temp2 = s2.substring(0, 4);
		String temp3 = s1.substring(4, 6);
		String temp4 = s2.substring(4, 6);
		if (Integer.parseInt(temp1) > Integer.parseInt(temp2))
			return true;
		if (Integer.parseInt(temp1) == Integer.parseInt(temp2))
			return Integer.parseInt(temp3) >= Integer.parseInt(temp4);

		return false;
	}

	/**
	 * 比较日期1是否大于日期2 
	 * @param s1 日期1
	 * @param s2 日期2 
	 */
	public static boolean yearMonthGreater(String s1, String s2) {
		String temp1 = s1.substring(0, 4);
		String temp2 = s2.substring(0, 4);
		String temp3 = s1.substring(4, 6);
		String temp4 = s2.substring(4, 6);
		if (Integer.parseInt(temp1) > Integer.parseInt(temp2))
			return true;
		if (Integer.parseInt(temp1) == Integer.parseInt(temp2))
			return Integer.parseInt(temp3) > Integer.parseInt(temp4);

		return false;
	}
	

	/**
	 * 增加月 
	 * @param strDate 初始年月
	 * @param intDiff 增加的数量 
	 */
	public static String getMonthBetween(String strDate, int intDiff) {
		try {
			int intYear = Integer.parseInt(strDate.substring(0, 4));
			int intMonth = Integer.parseInt(strDate.substring(4, 6));
			String strDay = "";
			if (strDate.length() > 6)
				strDay = strDate.substring(6, strDate.length());
			for (intMonth += intDiff; intMonth <= 0; intMonth += 12)
				intYear--; 
			for (; intMonth > 12; intMonth -= 12)
				intYear++; 
			if (intMonth < 10) {
				return Integer.toString(intYear) + "0" + Integer.toString(intMonth) + strDay;
			}
			return Integer.toString(intYear) + Integer.toString(intMonth) + strDay;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 计算两个年月之间的相差月数 
	 * @param strDateBegin
	 * @param strDateEnd
	 * @return
	 */
	public static String getMonthBetween(String strDateBegin, String strDateEnd) {
		try {
			String strOut;
			if (strDateBegin.equals("") || strDateEnd.equals("") || strDateBegin.length() != 6 || strDateEnd.length() != 6) {
				strOut = "";
			} else {
				int intMonthBegin = Integer.parseInt(strDateBegin.substring(0, 4)) * 12 + Integer.parseInt(strDateBegin.substring(4, 6));
				int intMonthEnd = Integer.parseInt(strDateEnd.substring(0, 4)) * 12 + Integer.parseInt(strDateEnd.substring(4, 6));
				strOut = String.valueOf(intMonthBegin - intMonthEnd);
			}
			return strOut;
		} catch (Exception e) {
			return "0";
		}
	} 

    /**
     * 在日期上增加数个整月
     */
    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    } 

    /**
     * 在日期上增加天
     */
    public static Date addDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, n);
        return cal.getTime();
    } 
    /**
     * 在日期上增加小时
     */
    public static Date addHour(Date date, int n) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	cal.add(Calendar.HOUR, n);
    	return cal.getTime();
    } 
    /**
     * 在日期上增加分钟
     */
    public static Date addMinute(Date date, int n) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	cal.add(Calendar.MINUTE, n);
    	return cal.getTime();
    } 
    /**
     * 在日期上增加秒
     */
    public static Date addSecond(Date date, int n) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	cal.add(Calendar.SECOND, n);
    	return cal.getTime();
    } 
    /**
     * 为航班日期加上 ：
     */
	public static String addColon4FlightTime(String time){
		if(StringUtils.isNotEmpty(time)) return null;
		if(time.indexOf(":") > 0) return time; 
		String first = time.substring(0,time.length()-2);
		first = first + ":" + time.substring(time.length()-2); 
		return first;
	} 
	
	/**
	 * 通过格林威治时间计算时差(国内版b2c用)
	 * @param startDate
	 * @param endDate
	 * @param orgTimezone
	 * @param dstTimezone
	 * @return
	 */
	public static String calculateTimeZone(Date orgDt, Date dstDt, int orgTimezone,int dstTimezone){
		
		Calendar orgCal = Calendar.getInstance();
		orgCal.setTime(orgDt);
		orgCal.add(Calendar.HOUR, -orgTimezone);
		
		Calendar dstCal = Calendar.getInstance();
		dstCal.setTime(dstDt);
		dstCal.add(Calendar.HOUR, -dstTimezone);
		
		// 得到出发与到达的时差 
		long temp =  dstCal.getTimeInMillis() - orgCal.getTimeInMillis();
		if(temp<0){				
			temp=temp*(-1);
		}
		long ss = temp / 1000;
    	long min = (ss / 60) % 60;
    	long hh = ss / 3600;
		
    	return hh + ":" + min;
	}
	
	/**
	 * 通过格林威治时间计算时差(海外b2c用，返回格式HH,精确到小数点后两位)
	 * @param startDate
	 * @param endDate
	 * @param orgTimezone
	 * @param dstTimezone
	 * @return
	 */
	public static String calculateTimeSpan(Date orgDt, int orgTimezone,Date dstDt, int dstTimezone){
		try{
			// 得到出发与到达的时差 
			Double temp = getSecondsSpan(orgDt, orgTimezone,dstDt, dstTimezone);
			DecimalFormat df = new DecimalFormat("#.##");
	    	String hh = df.format(temp / (3600*1000));;
	    	return hh;
		}
		catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 通过格林威治时间计算时差(海外b2c用，返回格式HH%sMI%s)
	 * @param startDate
	 * @param endDate
	 * @param orgTimezone
	 * @param dstTimezone
	 * @return
	 */
	public static String calculateTimeSpan1(Date orgDt, int orgTimezone,Date dstDt, int dstTimezone){
		try{
			// 得到出发与到达的时差 
			Double temp = getSecondsSpan(orgDt, orgTimezone,dstDt, dstTimezone);
			DecimalFormat df = new DecimalFormat("###");
	    	String hh = df.format(Math.floor(temp/(3600*1000)));
	    	String min = df.format(Math.floor((temp/(1000*60))%60));
	    	return hh+"%s"+min+"%s";
		}
		catch(Exception e){
			return null;
		}
	}
	
	public static Double getSecondsSpan(Date orgDt, int orgTimezone,Date dstDt, int dstTimezone){
		Calendar orgCal = Calendar.getInstance();
		orgCal.setTime(orgDt);
		orgCal.add(Calendar.HOUR, -orgTimezone);
		
		Calendar dstCal = Calendar.getInstance();
		dstCal.setTime(dstDt);
		dstCal.add(Calendar.HOUR, -dstTimezone);
		
		// 得到出发与到达的时差 
		double temp =  dstCal.getTimeInMillis() - orgCal.getTimeInMillis();
		if(temp<0){				
			temp=temp*(-1);
		}
		return temp;	
	}
}
