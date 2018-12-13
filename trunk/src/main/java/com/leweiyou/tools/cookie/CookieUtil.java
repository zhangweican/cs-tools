package com.leweiyou.tools.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie工具类
 * @author hanjun
 */
public class CookieUtil {

	// 设置cookie默认有效期是0天，浏览器关闭时候自动清除cookie
	private final static int cookieDefaultAge = -1;

	/**
	 * 保存必要信息到cookie(默认保存一天)
	 * @param cookieName 要保存的cookie名字
	 * @param domainName 要保存的cookie的domain名字
	 * @param cookieInfo 要保存的cookie内容
	 * @param response
	 */
	public static void saveCookie(String cookieName,
			String domainName,
			String cookieInfo,
			HttpServletResponse response) {
		saveCookie(cookieName, domainName, cookieInfo, cookieDefaultAge,response);
	}	
	
	/**
	 * 保存必要信息到cookie
	 * @param cookieName 要保存的cookie名字
	 * @param domainName 要保存的cookie的domain名字
	 * @param cookieInfo 要保存的cookie内容
	 * @param seconds 要保存的最大时间
	 * @param response
	 */
	public static void saveCookie(String cookieName,
			String domainName,
			String cookieInfo,
			Integer seconds,
			HttpServletResponse response) {

		// 加密Cookie信息
		String cookieValueWithEncode = cookieInfo;

		// 开始保存Cookie
		Cookie cookie = new Cookie(cookieName, cookieValueWithEncode);

		// 保存默认时间
		cookie.setMaxAge(seconds == null ? cookieDefaultAge : seconds);
		
		// 设置cookie的domain
		cookie.setDomain(domainName);

		// cookie有效路径是网站根目录
		cookie.setPath("/");
		
		// 向客户端写入
		response.addCookie(cookie);
	}
	
	
	/**
	 * 读取cookie中的用户信息
	 * 
	 * @param cookieName 要读取的cookie名字
	 * @param request
	 * @return Cookie中的用户信息 无Cookie信息返回空字符串
	 */
	public static String readCookie(String cookieName, 
			HttpServletRequest request) {

		// 根据cookieName取cookieValue
		Cookie cookies[] = request.getCookies();
		String cookieValue = "";
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookieName.equals(cookies[i].getName())) {
					cookieValue = cookies[i].getValue();
					return cookieValue;
				}
			}
		}
		return "";
	}

	/**
	 * 用户注销时,清除Cookie,在需要时可随时调用
	 * @param cookieName 要保存的cookie名字
	 * @param domainName 要保存的cookie的domain名字
	 * @param response
	 */
	public static void clearCookie(String cookieName, 
			String domainName, 
			HttpServletResponse response) {
		Cookie cookie = new Cookie(cookieName, null);
		cookie.setMaxAge(0);
		cookie.setDomain(domainName);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}
