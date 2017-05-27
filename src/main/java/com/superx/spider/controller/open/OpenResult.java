package com.superx.spider.controller.open;

import java.util.Date;

public class OpenResult {
	private Integer statusCode;// 200：成功；204 没有数据 -1：入口参数错误；-2：数据错误。
	private Object result;
	private Integer totalSize;
	private Date serviceTime;
	
	
	
	public Date getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(Date serviceTime) {
		this.serviceTime = serviceTime;
	}

	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
	public Integer getTotalSize() {
		return totalSize;
	}

}
