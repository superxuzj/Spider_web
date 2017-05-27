package com.superx.spider.controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MyTask {
	// 0 0 0/2 * * ? *
	@Scheduled(cron = "0 0 0/2 * * ?")
	public void taskCycle() {
		// linksController.runTask();
		System.out.println("MyTask start" +new Date());
		// 建立连接
		URL url;
		try {
			url = new URL("http://localhost/spider/all");
			HttpURLConnection httpConn = (HttpURLConnection) url
					.openConnection();
			// 设置参数
			httpConn.setDoOutput(true); // 需要输出
			httpConn.setDoInput(true); // 需要输入
			httpConn.setUseCaches(false); // 不允许缓存
			httpConn.setRequestMethod("GET"); // 设置POST方式连接
			// 设置请求属性
			httpConn.setRequestProperty("Content-Type", "text/html");
			httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			httpConn.setRequestProperty("Charset", "UTF-8");
			// 连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
			httpConn.connect();
			//System.out.println("htmlThePage connect建立连接");
			// 建立输入流，向指向的URL传入参数
			DataOutputStream dos = new DataOutputStream(
					httpConn.getOutputStream());
			dos.writeBytes("");
			dos.flush();
			dos.close();
			// 获得响应状态
			int resultCode = httpConn.getResponseCode();
			if (HttpURLConnection.HTTP_OK == resultCode) {
				System.out.println("spider/data " +new Date());
				
				URL urldata = new URL("http://localhost/spider/data");
				HttpURLConnection httpConndata = (HttpURLConnection) urldata
						.openConnection();
				// 设置参数
				httpConndata.setDoOutput(true); // 需要输出
				httpConndata.setDoInput(true); // 需要输入
				httpConndata.setUseCaches(false); // 不允许缓存
				httpConndata.setRequestMethod("GET"); // 设置POST方式连接
				// 设置请求属性
				httpConndata.setRequestProperty("Content-Type", "text/html");
				httpConndata.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
				httpConndata.setRequestProperty("Charset", "UTF-8");
				// 连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
				httpConndata.connect();
				// 建立输入流，向指向的URL传入参数
				DataOutputStream dosdata = new DataOutputStream(
						httpConndata.getOutputStream());
				dosdata.writeBytes("");
				dosdata.flush();
				dosdata.close();
				// 获得响应状态
				int resultCodedata = httpConndata.getResponseCode();
				if (HttpURLConnection.HTTP_OK == resultCodedata) {
					System.out.println("OK 2小时一次");
				}
				//调用成功再插入到数据库中的t_info表
			}
		} catch (MalformedURLException c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		} catch (IOException c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		} finally {
		}
		System.out.println("MyTask end" +new Date());
	}
}
