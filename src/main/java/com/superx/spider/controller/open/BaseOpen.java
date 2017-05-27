package com.superx.spider.controller.open;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class BaseOpen {
	private static final Logger log = LoggerFactory
			.getLogger(BaseOpen.class);
	protected static final Integer DEFAUL_TPAGESIZE=10;
	protected static final Integer OK_STATUS_CODE=200;//200：成功；
	protected static final Integer NODATA_STATUS_CODE=204;//204； 没有数据
	protected static final Integer VAL_STATUS_CODE=401;//验证错误 未授权
	protected static final Integer PARAM_ERROR_STATUS_CODE=-1;// -1：入口参数错误；
	protected static final Integer DATA_ERROR_STATUS_CODE=-2;//  -2：数据错误。
	protected static final String TOKEN="spiderf252fc72690b780b2a14e140ef6a9e0";//用关键参数+token用md5加密，从而防止传输过程被恶意篡改参数

	/**
	 * json跨域输出内容
	 */
	/**
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	protected String responseWrite(HttpServletRequest request,
			HttpServletResponse response, Object map) {
		try {
			String json = JSON.toJSONStringWithDateFormat(map, "yyyy-MM-dd HH:mm:ss");
			Object param = request.getParameter("jsonpcallback");
			String content = json;
			if (param != null) {
				content = param + "(" + json + ")";
			}
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().print(content);
			response.flushBuffer();

		} catch (IOException e) {
			log.error("服务器返回结果错误."+e.getMessage());
		}
		return null;
	}

	protected String readJSONString(HttpServletRequest request) {
		StringBuffer json = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				json.append(line);
			}
		} catch (Exception e) {
			log.error("服务器解析客户端参数错误."+e.getMessage());
		}
		return json.toString();
	}
	

	
	
	
	
	/**
	 *    判断是否是整数
	 *    isNumber(null) = false
	 *    isNumber("") = false
	 *    isNumber("   ") = false
	 *    isNumber(" xd") = false
	 *    isNumber("  123") = true
	 *    isNumber("789 ") = true
	 *    isNumber("345") = true
	 */
	protected boolean isNumber(Object value) {
		if (value == null) {
			return false;
		}
		int length;
		if (value instanceof String) {
			// 没有任何内容
			if ((length = ((String) value).length()) == 0) {
				return false;
			}
			// 有内容，但包含非数字 eg:“ 1b2”
			for (int i = 0; i < length; i++) {
				char c = ((String) value).charAt(i);
				if (!Character.isWhitespace(c)) {// 有内容存在
					if (!Character.isDigit(c)) {// 不是数字
						return false;
					}
				}
			}
			return true;

		} else if (value instanceof String || value instanceof Long
				|| value instanceof Integer || value instanceof Character
				|| value instanceof Short

		) {

			return true;

		} else {
			return false;
		}

	}
	
	
	protected Integer getDefaultPageSize(Object pageSize, Integer _pageSize) {
		if (isNumber(pageSize) && Integer.parseInt(pageSize.toString()) > 0) {
			_pageSize=Integer.parseInt(pageSize.toString());
		}else{
			log.warn("未接收到正确的pageSize参数,将返回当前页" + DEFAUL_TPAGESIZE + "条数据.");
		}
		return _pageSize;
	}


	protected Integer getDefaultPageNo(Object pageNo, Integer _pageNo) {
		if (isNumber(pageNo) && Integer.parseInt(pageNo.toString())>0) {
			_pageNo=Integer.parseInt(pageNo.toString());
		}else{
			log.warn("未接收到正确的pageNo参数,将返回第一页数据.");
		}
		return _pageNo;
	}
	
	
}
