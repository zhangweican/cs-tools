package com.leweiyou.tools.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.leweiyou.tools.bean.BeanUtils;
import com.leweiyou.tools.date.DateUtil;

/**
 * <font color=red>写入现在只实现了写入xls文件，对2017Excel暂时不支持</font>
 * @author Zhangweican
 *
 */
public class ExcelWriteUtil {
	
	/**
	 * 创建Excel文件，并保存
	 * 
	 * @param fileName
	 *            文件名称
	 * @param excelHeads
	 *            头内容
	 * @param voList
	 *            主体内容
	 * @throws Exception
	 */
	public static void exportExcel(File file, String title,String[] headNames, String[] voFieldNames, List voList) throws Exception {
		// 验证数据有效性
		if (file == null)
			throw new Exception("error.export.excel.file");
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

			createExcelFile(file, document);

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("error.export.excel.file");
		}
	}
	/**
	 * 创建Excel文件，并保存
	 * 
	 * @param fileName
	 *            文件名称
	 * @param excelHeads
	 *            头内容
	 * @param voList
	 *            主体内容
	 * @throws Exception
	 */
	public static void exportExcel(OutputStream stream, String title,String[] headNames, String[] voFieldNames, List voList) throws Exception {
		// 验证数据有效性
		if (stream == null)
			throw new Exception("error.export.excel.stream");
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
			
			document.write(stream);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("error.export.excel.file");
		}
	}
	
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
	public static HSSFWorkbook exportExcel(String title, String[] headNames, String[] voFieldNames, List voList) throws Exception {
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


	/**
	 * 创建 Excel 文档的标题
	 * 
	 * @param excelHeads
	 * @param sheet
	 */
	private static void creatExcelHeader(String title,String[] headNames, HSSFSheet sheet) { 
		int column = 0;
		
		//创建标题
		HSSFRow titleRow = sheet.createRow(0);
		HSSFCell cell1 = titleRow.createCell(column);
		cell1.setCellValue(title);
			
		//创建列表名
		titleRow = sheet.createRow(1);
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
	private static void createExcelBody(String[] voFieldNames,List voList,HSSFSheet sheet) throws Exception{
  
		int row = 2, column = 0; 

		for (int i = 0; i < voList.size(); i++) {
			Object vo = voList.get(i);

			HSSFRow eachRow = sheet.createRow(row ++);
			for (int j = 0; j < voFieldNames.length; j++) {

				Object cellContent = BeanUtils.getProperty(vo, voFieldNames[j]);

				HSSFCell cell = eachRow.createCell(column++);
				if (cellContent == null) {
					cell.setCellValue("");
				} else {
					if(cellContent instanceof Date){
						cell.setCellValue(DateUtil.toString((Date)cellContent, DateUtil.yyyy_MM_dd_HH_mm_ss));
					}else{
						cell.setCellValue(cellContent.toString());
					}
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
	private static void createExcelFile(File file, HSSFWorkbook document) throws Exception {
 
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		
		FileOutputStream fos = null;
		
		try {
			fos = new FileOutputStream(file);
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



	




	public static void main(String[] args) { 
		
		try {  
			String title = "MueCard Fare Calculate Log List";
			String[] fieldNames = { "value", "delflag", "describe", "nameCn", "nameEn", "type"}; 

			List excelContents = new ArrayList ();
			
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("value", "ABC");
			map.put("delflag", 1);
			map.put("describe", 100L);
			map.put("nameCn", 101.56F);
			map.put("nameEn", Calendar.getInstance().getTime());
			map.put("type", null);
			
			
			excelContents.add(map); 
			 
			ExcelWriteUtil.exportExcel(new File("E:\\document\\项目相关\\test.xlsx"),title,fieldNames, fieldNames,excelContents); 
			
		} catch (Exception e) { 
			e.printStackTrace();
		} 
	}
}

