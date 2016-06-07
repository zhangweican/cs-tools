/*
 * @(#)CommMailManagerTest.java
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
package test.com.leweiyou.tools.mail;
 
import junit.framework.TestCase;

import com.leweiyou.tools.email.CommMailManager;
 
public class CommMailManagerTest extends TestCase {

    private CommMailManager mailSender;

    public static void main(String[] args) {
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        /*super.setUp();
        FileSystemXmlApplicationContext ctx;
        ctx = new FileSystemXmlApplicationContext(new String[] {
                "classpath:/conf/applicationContext.xml",  
                });

        mailSender = (CommMailManager) ctx.getBean("mailSender");*/
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    } 
    
//    public void testSendPasswordMail(){
//        try { 
//            mailSender.sendSimpleEmail("SimpleTest","This is the content","");
//            
//            String path = FileUtil.getRootPath();           
//            path = path + "\\upload\\final\\MuCredit_20090501.xls";        
//            mailSender.sendAttachEmail("附件邮件测试","这里是邮件内容","","MuCredit_20090501.xls",path);         
//
//            path = FileUtil.getRootPath();              
//            URL url = new URL("file:///" + path + "\\upload\\380.jpg");  
//            mailSender.sendHtmlEmail("HTML邮件测试","这是Txt内容","这是Html内容新",url,"");   
//            
//        } catch (Exception e) { 
//            e.printStackTrace();
//        }
//    }  
}
