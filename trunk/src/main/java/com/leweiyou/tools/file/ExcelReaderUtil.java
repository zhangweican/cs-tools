package com.leweiyou.tools.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReaderUtil {

	/**
	 * 根据SheetName读取Excel内容
	 */
	public static List<List<Object>> readExcel(boolean isXlsx,InputStream stream,String sheetName) throws IOException {
		if (!isXlsx) {
			Sheet sheet = getSheet(false,stream, sheetName);
			return read2003Excel(sheet);
		} else {
			Sheet sheet = getSheet(true,stream, sheetName);
			return read2007Excel(sheet);
		}
	}
	/**
	 * 根据SheetName读取Excel内容
	 */
	public static List<List<Object>> readExcel(boolean isXlsx,InputStream stream,int sheetAt) throws IOException {
		if (!isXlsx) {
			Sheet sheet = getSheet(false,stream, sheetAt);
			return read2003Excel(sheet);
		} else {
			Sheet sheet = getSheet(true,stream, sheetAt);
			return read2007Excel(sheet);
		}
	}
	/**
	 * 根据SheetName读取Excel内容
	 */
	public static List<List<Object>> readExcel(File file,String sheetName) throws IOException {
		String fileName = file.getName();
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
				.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equalsIgnoreCase(extension)) {
			Sheet sheet = getSheet(false,file, sheetName);
			return read2003Excel(sheet);
		} else if ("xlsx".equalsIgnoreCase(extension)) {
			Sheet sheet = getSheet(true,file, sheetName);
			return read2007Excel(sheet);
		} else {
			throw new IOException("不支持的文件类型");
		}
	}
	/**
	 * 根据SheetAt读取Excel内容
	 */
	public static List<List<Object>> readExcel(File file,int sheetAt) throws IOException {
		String fileName = file.getName();
		String extension = fileName.lastIndexOf(".") == -1 ? "" : fileName
				.substring(fileName.lastIndexOf(".") + 1);
		if ("xls".equalsIgnoreCase(extension)) {
			Sheet sheet = getSheet(false,file, sheetAt);
			return read2003Excel(sheet);
		} else if ("xlsx".equalsIgnoreCase(extension)) {
			Sheet sheet = getSheet(true,file, sheetAt);
			return read2007Excel(sheet);
		} else {
			throw new IOException("不支持的文件类型");
		}
	}
	
	private static Sheet getSheet(boolean isXlsx,File file,String sheetName) throws IOException{
		if(isXlsx){
			XSSFWorkbook hwb = new XSSFWorkbook(new FileInputStream(file));
			return hwb.getSheet(sheetName);
		}else{
			HSSFWorkbook hwb = new HSSFWorkbook(new FileInputStream(file));
			return hwb.getSheet(sheetName);
		}
	}
	private static Sheet getSheet(boolean isXlsx,File file,int sheetAt) throws IOException{
		if(isXlsx){
			XSSFWorkbook hwb = new XSSFWorkbook(new FileInputStream(file));
			return hwb.getSheetAt(sheetAt);
		}else{
			HSSFWorkbook hwb = new HSSFWorkbook(new FileInputStream(file));
			return hwb.getSheetAt(sheetAt);
		}
	}
	private static Sheet getSheet(boolean isXlsx,InputStream stream,String sheetName) throws IOException{
		if(isXlsx){
			XSSFWorkbook hwb = new XSSFWorkbook(stream);
			return hwb.getSheet(sheetName);
		}else{
			HSSFWorkbook hwb = new HSSFWorkbook(stream);
			return hwb.getSheet(sheetName);
		}
	}
	private static Sheet getSheet(boolean isXlsx,InputStream stream,int sheetAt) throws IOException{
		if(isXlsx){
			XSSFWorkbook hwb = new XSSFWorkbook(stream);
			return hwb.getSheetAt(sheetAt);
		}else{
			HSSFWorkbook hwb = new HSSFWorkbook(stream);
			return hwb.getSheetAt(sheetAt);
		}
	}
	
	/**
	 * 读取 office 2003 excel
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private static List<List<Object>> read2003Excel(Sheet s) throws IOException {
		if(s == null){
			return null;
		}
		HSSFSheet sheet = (HSSFSheet) s;
		List<List<Object>> list = new LinkedList<List<Object>>();
		Object value = null;
		HSSFRow row = null;
		HSSFCell cell = null;
		for (int i = sheet.getFirstRowNum(); i <= sheet.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			List<Object> linked = new LinkedList<Object>();
			//处理Excel当前面没有没有值时候，返回的起始列号不是从零开始
			if(row.getFirstCellNum() != 0){
				for(int j = 0 ;j < row.getFirstCellNum(); j++){
					linked.add(null);
				}
			}
			for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
				cell = row.getCell(j);
				if (cell == null) {
					linked.add(null);
					continue;
				}
				DecimalFormat df = new DecimalFormat("0");// 格式化 number String
				// 字符
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
				//DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字
				switch (cell.getCellType()) {
				case XSSFCell.CELL_TYPE_STRING:
					value = cell.getStringCellValue();
					break;
				case XSSFCell.CELL_TYPE_NUMERIC:
					if ("@".equals(cell.getCellStyle().getDataFormatString())) {
						value = df.format(cell.getNumericCellValue());

					} else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
						Double d = cell.getNumericCellValue();  
						DecimalFormat dff = new DecimalFormat("#.###########");  
						value = dff.format(d);
					} else {
						value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
					}
					break;
				case XSSFCell.CELL_TYPE_BOOLEAN:
					value = cell.getBooleanCellValue();
					break;
				case XSSFCell.CELL_TYPE_BLANK:
					value = "";
					break;
				default:
					value = cell.toString();
				}
				linked.add(value);

			}
			list.add(linked);
		}

		return list;
	}

	/**
	 * 读取Office 2007 excel
	 */

	private static List<List<Object>> read2007Excel(Sheet s) throws IOException {
		if(s == null){
			return null;
		}
		XSSFSheet sheet = (XSSFSheet) s;
		List<List<Object>> list = new LinkedList<List<Object>>();
		Object value = null;
		XSSFRow row = null;
		XSSFCell cell = null;
		for (int i = sheet.getFirstRowNum(); i <= sheet
				.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			List<Object> linked = new LinkedList<Object>();
			//处理Excel当前面没有没有值时候，返回的起始列号不是从零开始
			if(row.getFirstCellNum() != 0){
				for(int j = 0 ;j < row.getFirstCellNum(); j++){
					linked.add(null);
				}
			}
			for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
				cell = row.getCell(j);
				if (cell == null) {
					linked.add(null);
					continue;
				}
				DecimalFormat df = new DecimalFormat("0");// 格式化 number String
				// 字符
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 格式化日期字符串
				//DecimalFormat nf = new DecimalFormat("0.00");// 格式化数字

				switch (cell.getCellType()) {
				case XSSFCell.CELL_TYPE_STRING:
					value = cell.getStringCellValue();
					break;
				case XSSFCell.CELL_TYPE_NUMERIC:
					if ("@".equals(cell.getCellStyle().getDataFormatString())) {
						value = df.format(cell.getNumericCellValue());

					} else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
						Double d = cell.getNumericCellValue();  
						DecimalFormat dff = new DecimalFormat("#.#########");  
						value = dff.format(d);  
					} else {
						value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
					}
					break;
				case XSSFCell.CELL_TYPE_BOOLEAN:
					value = cell.getBooleanCellValue();
					break;
				case XSSFCell.CELL_TYPE_BLANK:
					value = "";
					break;
				default:
					value = cell.toString();
				}
				
				linked.add(value);
			}
			list.add(linked);
		}
		return list;
	}

	public static void main(String[] args) {
		List list;
		try {
			list = ExcelReaderUtil.readExcel(new File("E:\\document\\项目相关\\设备导入模板2.xls"),"网络设备");
			System.out.println(list.size());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
