package com.leweiyou.tools.cfg;

import java.io.File;

import org.apache.log4j.Logger;

/**
 * 配置文件路径读取顺序<br>
 * 1.读取环境变量cfg.path<br>
 * 2.如环境变量中不存在，则读取jboss和tomcat的根目录，<br>
 * 3.如果根目录读取失败，则返回默认的appcfg，否则进入4<br>
 * 4.判定jboss或tomcat的根目录 + appcfg + 项目具体名称是否存在，如果返回，否则返回jboss或tomcat的根目录 + appcfg 路径<br>
 * <br>
 * <br>
 * 
 * 其实主要就是配置文件在服务器外面时候，如果多个项目部署在同一个服务器上面，则配置文件放到appcfg + 项目名的目录下，这样区分开来。
 * @author Zhangweican
 *
 */
public class CfgPath {
	static Logger logger = Logger.getLogger(CfgPath.class);
	private static String cfgPath = _getCfgPath("cfg.path","appcfg");
	private static String logPath = _getLogPath("log4j.dir");
	
	public static String getCfgPathRoot(){
		return cfgPath;
	}
	
	public static String getLogPathRoot(){
		return logPath;
	}
	
	public static String getCfgPath(String name){
		if(name.indexOf(":")>0 || name.startsWith("/")){
			return name;
		}else{
			String home=getCfgPathRoot();
			return home+"/"+name;
		}
	}
	
	private static String _getCfgPath(String env,String p){
		String path=System.getProperty(env);		 
		if(path==null){
			path=getJBossPathRoot();
			
			if(path==null){
				path=getTomcatPathRoot();
			}	
			
			if(path!=null){
				if(path.endsWith("/")){
					path=path+p;
				}else{
					path=path+"/"+p;
				}
				
				//获取webapp下具体项目的名称，如果不是webapp项目，则返回空
				String projectPath = CfgPath.class.getResource("/").getPath();
				int position = projectPath.indexOf("WEB-INF");
				if(position != -1){
					projectPath = projectPath.substring(0,position - 1);
					position = projectPath.lastIndexOf("/");
					projectPath = projectPath.substring(position + 1);
					//logger.info("xxxx:" + path + "/" + projectPath);
					if(new File(path + "/" + projectPath).exists()){
						path = path + "/" + projectPath;
					}
				}
			}
		}
		if(path==null){
			path=p;
		}			
		return path;	 
							 		 
	}
	
	private static String _getLogPath(String env){
		String p="logs";
		
		String path=System.getProperty(env);
		if(path==null){
			path=getJBossPathRoot();
			
			if(path==null){
				path=getTomcatPathRoot();
			}else{
				p="log"; //JBOSS 的log目录
			}
			
			if(path!=null){
				if(path.endsWith("/")){
					path=path+p;
				}else{
					path=path+"/"+p;
				}
			}
		}
		if(path==null){
			path=p;
		}			
		return path;	 
							 		 
	}
	
	private static String getJBossPathRoot(){ 
		String path=System.getProperty("jboss.server.base.dir"); //JBOSS-AS-7
		if(path==null){
			path=System.getProperty("jboss.server.home.dir");
		}
		 
		return path;
	}
	
	private static String getTomcatPathRoot(){
		String path=System.getProperty("catalina.home");
		 
		return path;
	}
}
