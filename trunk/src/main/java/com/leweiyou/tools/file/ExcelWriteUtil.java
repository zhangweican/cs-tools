/*
 * @(#)ExcelWriteUtil.java
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
 * 功能描述：写Excel文件的辅助类
 * 公用方法描述：
 * 
 * 修改人：
 * 修改日期：
 * 修改原因：
 * 
 * 
 */
package com.leweiyou.tools.file;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.leweiyou.tools.bean.BeanUtils;


public class ExcelWriteUtil {

	/**
	 * 创建ExcelHSSFWorkbook，用于页面输出
	 * 
	 * @param title          Excel文件名称
	 * @param headNames      Excel文件列头内容
	 * @param voFieldNames   VO属性字段名称
	 * @param voList         VO对象列表
	 * @return
	 * @throws BaseAppException
	 */
	public static HSSFWorkbook createHSSFWorkbook(String title, String[] headNames, String[] voFieldNames, List voList) throws Exception {
		// 验证数据有效性 
		if (null == voFieldNames || voFieldNames.length < 1)
			throw new Exception("error.export.excel.file");
		if (null == headNames || headNames.length < 1)
			throw new Exception("error.export.excel.file");
		if (null == voList || voList.size() < 1)
			throw new Exception("error.export.excel.file");

		try {
			// 创建XLS文档
			HSSFWorkbook document = new HSSFWorkbook();
			// 创建Sheet
			HSSFSheet sheet = document.createSheet("SheetOne");

			creatExcelHeader(title,headNames, sheet);

			createExcelBody(voFieldNames,voList, sheet); 
			
			return document;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Create Excel Document is Error!");
		}
	}
	
	public static HSSFWorkbook createHSSFWorkbook(String title, String[] headNames, String[] voFieldNames, List voList, int columNumber)throws Exception  {
		// 验证数据有效性 
		if (null == voFieldNames || voFieldNames.length < 1)
			throw new Exception("error.export.excel.file");
		if (null == headNames || headNames.length < 1)
			throw new Exception("error.export.excel.file");
		if (null == voList || voList.size() < 1)
			throw new Exception("error.export.excel.file");

		try {
			// 创建XLS文档
			HSSFWorkbook document = new HSSFWorkbook();
			// 创建Sheet
			HSSFSheet sheet = document.createSheet("SheetOne");

			creatExcelHeader(title,headNames, sheet);

			createExcelBody(voFieldNames,voList, sheet,columNumber); 
			
			return document;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Create Excel Document is Error!");
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static void createExcelBody(String[] voFieldNames, List voList, HSSFSheet sheet, int columNumber) throws Exception {
		int row = 2, column = 0; 

		for (int i = 0; i < voList.size(); i++) {
			Object vo = voList.get(i);

			HSSFRow eachRow = sheet.createRow(++row);
			for (int j = 0; j < voFieldNames.length; j++) {

				Object cellContent = BeanUtils.getProperty(vo, voFieldNames[j]);

				HSSFCell cell = eachRow.createCell(column++);
				if (cellContent == null) {
					//if(column==columNumber){
				//		cell.setCellValue(Double.valueOf(0));
				//	}else{
						cell.setCellValue("");
				//	}
					
				} else {
					if(column==columNumber){
						cell.setCellValue(Double.valueOf(cellContent.toString()));
					}else{
						cell.setCellValue(cellContent.toString());
					}
					
				}
			}
			column = 0;
		}
	}

	/**
	 * 创建Excel文件，并保存
	 * 
	 * @param fileName
	 *            文件名称
	 * @param excelHeads
	 *            头内容
	 * @param excelContents
	 *            主体内容
	 * @throws Exception
	 */
	public static void createExcel(String fileName, String title,String[] excelHeads, List excelContents) throws Exception {
		// 验证数据有效性
		if (fileName == null || "".equals(fileName))
			throw new Exception("error.export.excel.file");
		if (null == excelHeads || excelHeads.length < 1)
			throw new Exception("error.export.excel.file");
		if (null == excelContents || excelContents.size() < 1)
			throw new Exception("error.export.excel.file");

		try {
			// 创建XLS文档
			HSSFWorkbook document = new HSSFWorkbook();

			// 创建Sheet
			HSSFSheet sheet = document.createSheet("SheetOne");

			creatExcelHeader(title,excelHeads, sheet);

			createExcelBody(excelHeads,excelContents, sheet);

			createExcelFile(fileName, document);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("error.export.excel.file");
		}
	}

	/**
	 * 创建 Excel 文档的标题
	 * 
	 * @param excelHeads
	 * @param sheet
	 */
	public static void creatExcelHeader(String title,String[] headNames, HSSFSheet sheet) { 
		int column = 0;
		
		//创建标题
		HSSFRow titleRow = sheet.createRow(0);
		HSSFCell cell1 = titleRow.createCell(column);
		cell1.setCellValue(title);
			
		//创建列表名
		titleRow = sheet.createRow(2);
		for (int i=0;i<headNames.length;i++) {
			String head = headNames[i];
			HSSFCell cell = titleRow.createCell(column++);
			cell.setCellValue(head);
		}
	}

	/**
	 * 创建 Excel 主体
	 * 
	 * @param excelContents
	 * @param sheet
	 */
	public static void createExcelBody(String[] voFieldNames,List voList,HSSFSheet sheet) throws Exception{
  
		int row = 2, column = 0; 

		for (int i = 0; i < voList.size(); i++) {
			Object vo = voList.get(i);

			HSSFRow eachRow = sheet.createRow(++row);
			for (int j = 0; j < voFieldNames.length; j++) {

				Object cellContent = BeanUtils.getProperty(vo, voFieldNames[j]);

				HSSFCell cell = eachRow.createCell(column++);
				if (cellContent == null) {
					cell.setCellValue("");
				} else {
					cell.setCellValue(cellContent.toString());
				}
			}
			column = 0;
		}
		 
	} 

	/**
	 * 根据文件名和创建的Excel电子文档，创建物理文件
	 * 
	 * @param fileName
	 * @param document
	 * @throws Exception
	 */
	public static void createExcelFile(String fileName, HSSFWorkbook document) throws Exception {
 
		File file = new File(new PathDetector().getResult() + "/download/" );
		if(!file.exists()){
			file.mkdir();
		}
		fileName = new PathDetector().getResult() + "/download/" + fileName;
		
		File outputFile = null;
		FileOutputStream fos = null;
		
		try {
			outputFile = new File(fileName);
			fos = new FileOutputStream(outputFile);
			document.write(fos);
			fos.close();
		} catch (Exception e) { 
			e.printStackTrace();
			throw new Exception("Create Excel File Error!");
		}finally{
			try{
				fos.close();				
			}catch(Exception ex){
				throw new Exception("Create Excel File Error!");
			}
		}
	}



	




//	public static void main(String[] args) { 
//		
//		try {  
//			String title = "MueCard Fare Calculate Log List";
//			String[] fieldNames = { "value", "delflag", "describe", "nameCn", "nameEn", "type"}; 
//
//			List excelContents = new ArrayList ();
//			
//			SystemDictVO vo = new SystemDictVO();
//			vo.setValue(new Integer(1));
//			vo.setDelflag(new Integer(0));
//			vo.setDescribe("DESCRIBE");
//			vo.setNameCn("胡建强");
//			vo.setNameEn("HUJIANQIANG");
//			vo.setType(new Integer(3));
//			
//			excelContents.add(vo); 
//			 
//			ExcelWriteUtil.createExcel("MuCredit_" + DateUtil.getCurrentDateString("yyyyMMdd") + ".xls",title,fieldNames, excelContents); 
//			
//		} catch (Exception e) { 
//			e.printStackTrace();
//		} 
//	}
}

