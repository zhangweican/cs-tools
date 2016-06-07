package com.leweiyou.tools.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.leweiyou.tools.log.Logger;
import com.leweiyou.tools.utils.IOUtils;

public class PropertiesUtils {
	static Logger logger = new Logger(PropertiesUtils.class);
	/**
	 * 获取配置文件中的key值
	 * @param config
	 * @param key
	 * @return
	 */
	public static String getConfigValue(Properties config,String key){
		try {
			return (String) config.get(key);
		} catch (Exception e) {
		}
		return null;
	}
	
	/**
	 * 获取配置文件中的ARRAY值，默认以,分隔
	 * @param config
	 * @param key
	 * @return
	 */
	public static String[] getConfigArray(Properties config,String key){
		return getConfigArray(config, key,",");
	}
	
	/**
	 * 获取配置文件中的ARRAY值，
	 * @param config properties
	 * @param key    健值
	 * @param regex  分隔符
	 * @return
	 */
	public static String[] getConfigArray(Properties config,String key,String regex){
		try {
			String configValue=(String) config.get(key);
			if(StringUtils.isNotBlank(configValue)){
				String[] configArray=configValue.trim().split(regex);
				return configArray;
			}
		} catch (Exception e) {
		}
		return null;
	}
	
	/**
	 * 根据路径获取properties文件
	 * @param configPath
	 * @return
	 * @throws IOException
	 */
	public static Properties getConfig(String configPath) throws IOException{
		Properties config=new Properties();
		try {
			config.load(PropertiesUtils.class.getClassLoader().getResourceAsStream(configPath));
		} catch(Exception e) {
			throw new IOException();
		}	
		return config;
	}
	
	public static Properties readToProperties(File f){
		InputStream in=null;
		try{
			Properties p=new Properties();
			
			if(f.exists()){
				try{
				in=new FileInputStream(f);
				p.load(in);
				}catch(Exception e){
					logger.error(""+e,e);
				}
			}
			return p;
		}finally{
			IOUtils.close(in);
		}
	}
	
	public static String readToString(String file,String charset) { 
		FileInputStream fin=null;
		try{
			fin=new FileInputStream(file);
			return readToString(fin,charset);
		} catch(IOException e) {            
			logger.error("文件读取失败: "+e,e);
			return null;
		}
	}
	public static String readToString(InputStream inputStream) {         
		return readToString(inputStream,null);
	}
	public static String readToString(InputStream inputStream,String charset) {         
		try{            
			ByteArrayOutputStream oStream = new ByteArrayOutputStream();            
			int length;    
			byte[] buf=new byte[1024];
			while((length = inputStream.read(buf)) != -1) {                
				oStream.write(buf,0,length);            
			}           
			if(charset!=null){
				return new String(oStream.toByteArray(),charset);
			}else{
				return new String(oStream.toByteArray());
			}
		} catch(IOException e) {            
			logger.error("文件读取失败: "+e,e);   
			return null;
		}finally{
			try{inputStream.close();}catch(Exception e){}
		}
	}	
	
}
