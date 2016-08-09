/*
 * @(#)CommMailManager.java
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
package com.leweiyou.tools.email;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

public class MailManager{
    
	protected transient final Logger log = Logger.getLogger(this.getClass().getName());
    
    /**
     * 简单邮件发送
     * 
     * @param subject     邮件主题
     * @param msgContent  邮件内容
     * @param receiver    收信人，如果为空，则直接从配置文件中取得收信人信息，可群发比如"hujq@cares.sh.cn,liuke@cares.sh.cn"
     */
    public void sendSimpleEmail(String subject,String msgContent,String receiver)throws Exception{
         
        SimpleEmail email = new SimpleEmail();
        email.setHostName(this.getSmtp());
        email.setFrom(this.getSender());
        email.setAuthentication(this.getUsername(), this.getPassword());
        if (StringUtils.isEmpty(receiver))
            email.setTo(this.getRecipients(this.getReceiver()));
        else
            email.setTo(this.getRecipients(receiver));
        email.setCharset("utf-8");
        email.setSubject(subject);
        email.setMsg(msgContent);
        email.send();

        log.info("Send Simple Email to " + receiver + " is Successful !");  
    }
    
    /**
     * 发送带有附件的邮件
     * 
     * @param subject
     *            邮件主题
     * @param msgContent
     *            邮件内容
     * @param receiver
     *            收件人，为空时直接从配置文件中读取邮件人信息，可群发比如"hujq@cares.sh.cn,liuke@cares.sh.cn"
     * @param attachName
     *            附件文件名称（需要在邮件中显示的名称）
     * @param attachUrl
     *            附件文件的全路径
     */
    public void sendAttachEmail(String subject,String msgContent,String receiver,String attachName,String attachUrl)throws Exception{
                    
        // 添加附件
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath(attachUrl);
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription(attachName);
        //sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
        attachment.setName(MimeUtility.encodeText(attachName));

        MultiPartEmail email = new MultiPartEmail();
        email.setHostName(this.getSmtp());
        email.setFrom(this.getSender());
        email.setAuthentication(this.getUsername(), this.getPassword());
        if (StringUtils.isEmpty(receiver))
            email.setTo(this.getRecipients(this.getReceiver()));
        else
            email.setTo(this.getRecipients(receiver));
        email.setSubject(subject);
        email.setMsg(msgContent);

        email.attach(attachment);
        email.send();
        log.info("Send Attach Email to " + receiver + " is Successful !");  
         
    }

    /**
     * 发送HTML格式的邮件
     * 
     * @param subject
     *            邮件主题
     * @param txtMsg
     *            邮件的文本内容（只有在不能显示HTMLMSG时才显示）
     * @param htmlMsg
     *            邮件的HTML内容
     * @param imgUrl
     *            邮件的底色图片
     * @param receiver
     *            邮件人列表
     */
    public void sendHtmlEmail(String subject,String txtMsg,String htmlMsg,URL imgUrl,String receiver)throws Exception{
         
        // HtmlEmail!
        HtmlEmail email = new HtmlEmail();
        email.setHostName(this.getSmtp());
        email.setFrom(this.getSender());
        email.setAuthentication(this.getUsername(), this.getPassword());
        if (StringUtils.isEmpty(receiver))
            email.setTo(this.getRecipients(this.getReceiver()));
        else
            email.setTo(this.getRecipients(receiver));
        email.setSubject(subject);
        email.setCharset("utf-8");
        email.setHtmlMsg(htmlMsg);
        email.setTextMsg(txtMsg); //对于不支持html的邮件客户端 会显示此消息
        email.send();
        log.info("Send HTML Email Successful !!!"); 
    } 
    /**
     * 发送带有附件的邮件
     * 
     * @param subject
     *            邮件主题
     * @param msgContent
     *            邮件内容
     * @param receiver
     *            收件人，为空时直接从配置文件中读取邮件人信息，可群发比如"hujq@cares.sh.cn,liuke@cares.sh.cn"
     * @param attachsMap
     * 			附件名为key，附件路径为value
     */
    public void sendAttachsEmail(String subject,String msgContent,String receiver,Map<String,String> attachsMap)throws Exception{
    	MultiPartEmail email = new MultiPartEmail();
        email.setHostName(this.getSmtp());
        email.setFrom(this.getSender());
        email.setAuthentication(this.getUsername(), this.getPassword());
        if (StringUtils.isEmpty(receiver))
            email.setTo(this.getRecipients(this.getReceiver()));
        else
            email.setTo(this.getRecipients(receiver));
        email.setSubject(subject);
        email.setMsg(msgContent);
    	Set<String> attachsSets = attachsMap.keySet();
    	Iterator<String> attachsIt = attachsSets.iterator();
    	while(attachsIt.hasNext()){
    			String attachsName = attachsIt.next();
    			String value = attachsMap.get(attachsName);
    		    // 添加附件
            	EmailAttachment attachment = new EmailAttachment();
	            try {
					attachment.setPath(value);
					attachment.setDisposition(EmailAttachment.ATTACHMENT);
					attachment.setDescription(attachsName);
					attachment.setName(MimeUtility.encodeText(attachsName));
					email.attach(attachment);
				} catch (Exception e) {
					log.error("Attach:attachsName add Failure! Path is "+value);
				}
    	}
        email.send();
        log.info("Send Attachs Email to " + receiver + " is Successful !");  
         
    }
    
    /**
     * 发送HTML格式的带附件邮件
     * 
     * @param subject
     *            邮件主题
     * @param txtMsg
     *            邮件的文本内容（只有在不能显示HTMLMSG时才显示）
     * @param htmlMsg
     *            邮件的HTML内容
     * @param imgUrl
     *            邮件的底色图片
     * @param receiver
     *            邮件人列表
     *  @param   attachsMap
     *            附件名为key，附件路径为value
     */
    public void sendHtmlAttachsEmail(String subject,String txtMsg,String htmlMsg,URL imgUrl,String receiver,Map<String,String> attachsMap)throws Exception{
         
        // HtmlEmail!
        HtmlEmail email = new HtmlEmail();
        email.setHostName(this.getSmtp());
        email.setFrom(this.getSender());
        email.setAuthentication(this.getUsername(), this.getPassword());
        if (StringUtils.isEmpty(receiver))
            email.setTo(this.getRecipients(this.getReceiver()));
        else
            email.setTo(this.getRecipients(receiver));
        email.setSubject(subject);
        email.setCharset("utf-8");
        email.setHtmlMsg(htmlMsg);
        email.setTextMsg(txtMsg);
        Set<String> attachsSets = attachsMap.keySet();
        Iterator<String> attachsIt = attachsSets.iterator();
        while(attachsIt.hasNext()){
                String attachsName = attachsIt.next();
                String value = attachsMap.get(attachsName);
                // 添加附件
                EmailAttachment attachment = new EmailAttachment();
                try {
                    attachment.setPath(value);
                    attachment.setDisposition(EmailAttachment.ATTACHMENT);
                    attachment.setDescription(attachsName);
                    attachment.setName(MimeUtility.encodeText(attachsName));
                    email.attach(attachment);
                } catch (Exception e) {
                    log.error("Attach:attachsName add Failure! Path is "+value);
                }
        }
        email.send();
        log.info("Send HTML Email Successful !!!"); 
    } 
    
    
    
    /**
     * 根据收件人字符串，分析出邮箱地址列表，以List<InternetAddress>形式返回
     * 
     * @param recipients
     *            邮箱地址字符串，比如"hujq@cares.sh.cn,liuke@cares.sh.cn"
     * @return List<InternetAddress>
     */
    public Collection getRecipients(String recipients){
        List list = new ArrayList();
        try {
            StringTokenizer tokens = new StringTokenizer(recipients,";");
            while(tokens.hasMoreElements()){
                String recipient = tokens.nextToken(); 
                InternetAddress mailAddress = new InternetAddress(recipient);
                list.add(mailAddress);
            }
        } catch (AddressException e) {
            e.printStackTrace();
        }       
        return list;
    }
    
    /**
     * 简单邮件发送(国际化)
     * 
     * @param subject     邮件主题
     * @param msgContent  邮件内容
     * @param receiver    收信人，如果为空，则直接从配置文件中取得收信人信息，可群发比如"hujq@cares.sh.cn,liuke@cares.sh.cn"
     * @param locale      国际化内容
     */
    public void sendSimpleEmail(String subject,String msgContent,String receiver, Locale locale)throws Exception{
         
        SimpleEmail email = new SimpleEmail();
        email.setHostName(this.getSmtp());
        if(Locale.SIMPLIFIED_CHINESE.equals(locale)){
        	email.setFrom(this.getSender());
        }
        else if(Locale.UK.equals(locale)){
        	email.setFrom(this.getSender(), "China Eastern Airlines e-ticketing website");
        }
        else{
        	 email.setFrom(this.getSender());
        }
        email.setAuthentication(this.getUsername(), this.getPassword());
        if (StringUtils.isEmpty(receiver))
            email.setTo(this.getRecipients(this.getReceiver()));
        else
            email.setTo(this.getRecipients(receiver));
        email.setCharset("utf-8");
        email.setSubject(subject);
        email.setMsg(msgContent);
        email.send();

        log.info("Send Simple Email to " + receiver + " is Successful !");  
    }
    
    /**
     * 发送带有附件的邮件(国际化)
     * 
     * @param subject
     *            邮件主题
     * @param msgContent
     *            邮件内容
     * @param receiver
     *            收件人，为空时直接从配置文件中读取邮件人信息，可群发比如"hujq@cares.sh.cn,liuke@cares.sh.cn"
     * @param attachName
     *            附件文件名称（需要在邮件中显示的名称）
     * @param attachUrl
     *            附件文件的全路径
     * @param locale   
     * 			  国际化内容      
     */
    public void sendAttachEmail(String subject,String msgContent,String receiver,String attachName,String attachUrl,Locale locale)throws Exception{
                    
        // 添加附件
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath(attachUrl);
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        attachment.setDescription(attachName);
        attachment.setName(MimeUtility.encodeText(attachName));

        MultiPartEmail email = new MultiPartEmail();
        email.setHostName(this.getSmtp());
        if(Locale.SIMPLIFIED_CHINESE.equals(locale)){
        	email.setFrom(this.getSender());
        }
        else if(Locale.UK.equals(locale)){
        	email.setFrom(this.getSender(), "China Eastern Airlines e-ticketing website");
        }
        else{
        	 email.setFrom(this.getSender());
        }
        email.setAuthentication(this.getUsername(), this.getPassword());
        if (StringUtils.isEmpty(receiver))
            email.setTo(this.getRecipients(this.getReceiver()));
        else
            email.setTo(this.getRecipients(receiver));
        email.setSubject(subject);
        email.setMsg(msgContent);

        email.attach(attachment);
        email.send();
        log.info("Send Attach Email to " + receiver + " is Successful !");  
         
    }
    
    //=====================================================================
    String smtp;      //mail.smtp
    String pop;       //mail.pop
    String sender;    //邮件发信人
    String username;  //发信人账号
    String password;  //发信人密码
    String receiver;  //收信人
        
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPop() {
        return pop;
    }
    public void setPop(String pop) {
        this.pop = pop;
    }
    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getSmtp() {
        return smtp;
    }
    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
