package com.leweiyou.tools.cfg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.log4j.Logger;


/**
* 【注意：】 因为配置文件默认先查找服务容器下的配置文件，如果查找不到才去classpath下查找。所以定义配置文件的时候，最好定义唯一的配置文件，如添加项目名称的前缀<br/>
*  读取配置文件信息:
*  CfgUtil.getCfg(String filename) <br/>
*  配置文件路径: 
*  1. 如果运行在Tomcat容器下, 读取路径为:
*     {Tomcat HOME}/appcfg/filename
*  2. 如果运行在JBoss容器下, 读取路径为:
*     {Jboss HOME}/server/{SERVER}/appcfg/filename
*  3. 如果没有运行在容器下, 读取路径为,当前项目classes路径
*     {当前项目classes路径}/filename，建议设置cfg.path值设定配置文件绝对路径   
*/
public class CfgUtil {
	static Logger logger = Logger.getLogger(CfgUtil.class);
	private static Map<String,CfgData> cfgs=new HashMap<String,CfgData>();
	private static Object lock=new Object();	
	 
	private static CfgWatchdog watch = null;
	
	public static CfgData getCfg(String filename){
		CfgData cfg=cfgs.get(filename);		
		if(cfg==null){
			synchronized(lock){
				cfg=cfgs.get(filename);	
				if(cfg==null){
					cfg = new CfgData();
					cfg.addConfiguration(EnvUtil.env);	
					
				    PropertiesConfiguration pcfg = getCfgFile(filename);
				    pcfg.setReloadingStrategy(new FileChangedReloadingStrategy());	
				    cfg.addConfiguration(pcfg);
				    
				    cfgs.put(filename, cfg);
				}
			}
		}		
		return cfg;		
	}
	
//	@SuppressWarnings("rawtypes")
//	public static CfgData getCfg(Class clazz,String filename){
//		String full_filename=getFullName(clazz,filename);
//		CfgData cfg=cfgs.get(full_filename);		
//		if(cfg==null){
//			synchronized(lock){
//				cfg=cfgs.get(full_filename);	
//				if(cfg==null){
//				    PropertiesConfiguration pcfg = getCfgFile(clazz,filename);
//				    cfg = new CfgData();
//			        cfg.setCfg(pcfg);
//				    cfgs.put(full_filename, cfg);
//				    
//				    chkWatch(clazz,filename,cfg);
//				}
//			}
//		}	
//		
//		return cfg;		
//	}
	
//	@SuppressWarnings("rawtypes")
//    private static void chkWatch(Class clazz,String filename,CfgData cfg) {
//	    CfgInfo cinfo = new CfgInfo();
//	    cinfo.setClazz(clazz);
//	    cinfo.setFilename(filename);
//	    cinfo.setFilepath(CfgPath.getCfgPathRoot());
//	    cinfo.setCfgdata(cfg);
//	    
//	    String key = concatFilename(CfgPath.getCfgPathRoot(), filename);
//	    File file = new File(key);
//	    
//	    
//	      //需要监控并且监控线程没有启动(默认监控间隔15s)
//        if((watch == null)){
//           int interval  = cfg.getInt("Watch_Interval", 15);
//           watch = new CfgMonitor();
//           
//           if(file.exists()){
//               cinfo.setLastmodified(file.lastModified());
//               CfgWatchdog.watchArray.put(key, cinfo);
//           }
//           watch.setDelay(interval * 1000L);
//           watch.start();
//        }
//        
//        if(watch != null){
//            if(!CfgWatchdog.watchArray.containsKey(key)){
//                if(file.exists()){
//                    cinfo.setLastmodified(file.lastModified());
//                    CfgWatchdog.watchArray.put(key, cinfo);
//                }
//            }
//        }
//	}
	
	/**
	 * 主动退出监控线程操作
	 */
	public static void interruptMonitor(){
	    if(watch != null){
	        watch.setInterrupted(true);
	        watch = null;
	    }
	}
	
    public static PropertiesConfiguration getCfgFile(String filename) {
        PropertiesConfiguration cfg = null;
        try {
            String cfg_path = CfgPath.getCfgPathRoot();
            File f = new File(concatFilename(cfg_path, filename));
            if (f.exists() == false) {
            	//加载本地class的目录下的配置文件
            	String path = CfgUtil.class.getResource("/").getPath();
            	cfg_path = path + "../classes";
                f = new File(concatFilename(cfg_path, filename));
            }
            cfg = new PropertiesConfiguration();            
            if (f.exists()) {            	 
            	cfg.setEncoding("utf-8");
                cfg.load(f);
            }else{
            	throw new FileNotFoundException(filename + " Not Found!!");
            }
        } catch (Exception ex) {
            logger.error("Load cfg : " + filename + " fail: " + ex, ex);
        }
        return cfg;
    }
	
	public static InputStream getCfgAsStream(String name) throws IOException{
		String filepath = CfgPath.getCfgPath(name);		
		logger.info("Load cfg file: " +filepath );		
		return new FileInputStream(filepath);	
	}
	
	private static String concatFilename(String path,String name){
		if(path==null){
			return name;
		}else if(name==null){
			return path;
		}else {
			if(path.endsWith("/")){
				if(name.startsWith("/")){
					return path+name.substring(1);
				}else{
					return path+name;
				}
			}else{
				if(name.startsWith("/")){
					return path+name;
				}else{
					return path+"/"+name;
				}
			}
		}
	}
	 
}
