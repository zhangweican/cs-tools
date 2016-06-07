/*
 * @(#)ExcelReaderUtil.java
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
 * 功能描述：读取Excel文件的辅助类
 * 公用方法描述：
 * 
 * 修改人：
 * 修改日期：
 * 修改原因：
 * 
 * 
 */
package com.leweiyou.tools.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelReaderUtil{

	/**
	 * 根据文件名称读取Excel
	 * 返回：
	 * 
	 * @param fileName
	 * @return　List<String[][]>
	 */
	public List readExcel(String fileName){
		List datas = new ArrayList ();
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(fileName));		
			for(int i=0;i< workbook.getNumberOfSheets();i++){
				HSSFSheet tableSheet = workbook.getSheetAt(i);
				if(tableSheet == null) continue ;
				datas.add(readSheetData(tableSheet));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return datas;
	}

	/**
	 * 读取每个Sheet
	 * 
	 * @param tableSheet
	 * @return
	 */
	public String[][] readSheetData(HSSFSheet tableSheet) { 
		int rows = tableSheet.getPhysicalNumberOfRows();
		if(rows < 1) return null;
		
		HSSFRow tableRow = tableSheet.getRow(0);
		int columns = tableRow.getPhysicalNumberOfCells();
		String[][] sheetValues = new String[rows-1][columns];
		
		for (int i = 1; i < rows; i++) {
			HSSFRow row = tableSheet.getRow(i); 
			for (int j = 0; j < columns; j++) { 
				HSSFCell cell = row.getCell((short) j); 
				sheetValues[i-1][j] = getCellValue(cell); 
			} 	
		}  
		return sheetValues;		
	}

	/**
	 * 判断单元格的数据类型并将其值转换成String
	 */
	public String getCellValue(HSSFCell cell) {
		try{
		if (cell != null) {
			int type = cell.getCellType();
			switch (type) {
			case 0:
				return getTextByNumbericString(cell.getNumericCellValue());
			case 1:
				return cell.getStringCellValue();
			default:
				return "";
			}
		} else
			return "";
		}catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	    

	//////////////////////////////////////////////////////////////////////////////////
	//
	//////////////////////////////////////////////////////////////////////////////////
	public String getTextByNumbericString(double numericCellValue){
		String value = String.valueOf(numericCellValue);
		int point = value.indexOf(".");
		int ePost = value.indexOf("E");
		if(ePost > 0 ){
			//含有E的科学计数法，出去小数点和E后面的数据
			value = value.substring(0,ePost);	
			value = value.substring(0,point) + value.substring(point +1,value.length());
		}else{
			//不含有E，取小数点前面的数据
			if(point > 0) 
				value = value.substring(0,point);	
		}			
		return value;
	}
	
	/**
	 * 读取EXCEL的首行，得到表头信息并存 放到String数组中
	 */

	public String[] getTableHeaderNames(HSSFRow tableRow) {
		int columns = tableRow.getPhysicalNumberOfCells();
		String[] tableHeaderNames = new String[columns];
		for (int i = 0; i < columns; i++) {
			HSSFCell cell = tableRow.getCell((short) i);
			tableHeaderNames[i] = cell.getStringCellValue();
		}
		return tableHeaderNames;
	}
}
