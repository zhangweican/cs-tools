package com.leweiyou.tools.pdf;
import java.io.File;  
import java.io.FileOutputStream;
import java.io.IOException;  
import java.io.OutputStream;
import java.io.StringWriter;  
import java.util.HashSet;
import java.util.List;
import java.util.Locale;  
  



import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;  
  





import com.lowagie.text.DocumentException;  
import com.lowagie.text.pdf.BaseFont;  
  





import freemarker.core.ParseException;  
import freemarker.template.Configuration;  
import freemarker.template.MalformedTemplateNameException;  
import freemarker.template.Template;  
import freemarker.template.TemplateException;  
import freemarker.template.TemplateNotFoundException;  
   
/** 
 * PDF生成辅助类 
 * 
 */  
public class PDFTools {  
	
	/**
	 * 字体所在的路径
	 */
	private static Set<String> font = new HashSet<String>();
	
	/**
	 * 设置字体，因为在jar里面设置字体ITextRenderer.getFontResolver().addFont()不能访问jar里面的font字体，所以这里就设置<br><br>
	 * 注：<font color=red>如果需要设置中文字体可以使用arialuni.ttf字体</font>
	 * @param fontFile
	 */
	public static void setFont(Set<File> fontFile){
		if(CollectionUtils.isNotEmpty(fontFile)){
			for(File f : fontFile){
				font.add(f.getPath());
			}
		}
	}
    /** 
     * 生成PDF到文件 ，支持加入图片，图片的相对路径使用模板所在的相对路径
     * @param ftlFile 模板文件
     * @param data 数据 一般是map格式，
     * @param outputFile 目标文件（全路径名称） 
     */  
    public static void generateToFile(File ftlFile,Object data,String outputFile) throws Exception {  
        String html = getPdfContent(ftlFile.getParent(), ftlFile.getName(), data);  
        OutputStream out = null;  
        ITextRenderer render = null;  
        out = new FileOutputStream(outputFile);  
        render = getRender();  
        render.setDocumentFromString(html);  
        //html中如果有图片，图片的路径则使用这里设置的路径的相对路径，这个是作为根路径  
        render.getSharedContext().setBaseURL("file:/" + ftlFile.getParent() + File.separator);  
        render.layout();  
        render.createPDF(out);  
        render.finishPDF();  
        render = null;  
        out.close();  
    }  
       
    /** 
     * 生成PDF到输出流中（ServletOutputStream用于下载PDF）  支持加入图片，图片的相对路径使用模板所在的相对路径
     * @param ftlFile ftl模板文件
     * @param data 输入到FTL中的数据 
     * @param response HttpServletResponse 
     */  
    public static OutputStream generateToServletOutputStream(File ftlFile,Object data,HttpServletResponse response) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException, DocumentException{  
    	String html = getPdfContent(ftlFile.getParent(), ftlFile.getName(), data);  
        OutputStream out = null;  
        ITextRenderer render = null;  
        out = response.getOutputStream();  
        render = getRender();  
        render.setDocumentFromString(html);  
        //html中如果有图片，图片的路径则使用这里设置的路径的相对路径，这个是作为根路径  
        render.getSharedContext().setBaseURL("file:/" + ftlFile.getParent() + File.separator);  
        render.layout();  
        render.createPDF(out);  
        render.finishPDF();  
        render = null;  
        return out;  
    }  	
	
	
    private static ITextRenderer getRender() throws DocumentException, IOException {  
        ITextRenderer render = new ITextRenderer();  
        if(CollectionUtils.isNotEmpty(font)){
        	for(String ft : font){
        		render.getFontResolver().addFont(ft, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);  
        	}
        }
        return render;  
    }  
   
    //获取要写入PDF的内容  
    private static String getPdfContent(String ftlPath, String ftlName, Object o) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {  
        return useTemplate(ftlPath, ftlName, o);  
    }  
   
    //使用freemarker得到html内容  
    private static String useTemplate(String ftlPath, String ftlName, Object o) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {  
        String html = null;  
        Template tpl = getFreemarkerConfig(ftlPath).getTemplate(ftlName);  
        tpl.setEncoding("UTF-8");  
        StringWriter writer = new StringWriter();  
        tpl.process(o, writer);  
        writer.flush();  
        html = writer.toString();  
        return html;  
    }  
   
    /** 
     * 获取Freemarker配置 
     * @param templatePath 
     * @return 
     * @throws IOException 
     */  
    private static Configuration getFreemarkerConfig(String templatePath) throws IOException {  
        freemarker.template.Version version = new freemarker.template.Version("2.3.22");  
        Configuration config = new Configuration(version);  
        config.setDirectoryForTemplateLoading(new File(templatePath));  
        config.setEncoding(Locale.CHINA, "utf-8");  
        return config;  
    }  
       
    /** 
     * 获取类路径 
     * @return 
     */  
    private static String getPath(){  
        return PDFTools.class.getResource("").getPath();  
    }  
   
}  
