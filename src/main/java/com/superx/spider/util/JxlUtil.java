package com.superx.spider.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import com.superx.spider.entity.WebSite;

public class JxlUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JxlUtil.getWebSiteList();
		/*
		
		// TODO Auto-generated method stub
		// 创建一个list 用来存储读取的内容
		List list = new ArrayList();
		List<User> userList = new ArrayList<User>();
		Workbook rwb = null;
		Cell cell = null;

		// 创建输入流
		InputStream stream;
		try {
			stream = new FileInputStream("d:\\aaa.xls");
			// 获取Excel文件对象
			rwb = Workbook.getWorkbook(stream);

			// 获取文件的指定工作表 默认的第一个
			Sheet sheet = rwb.getSheet(0);

			// 行数(表头的目录不需要，从1开始)
			for (int i = 2; i < sheet.getRows(); i++) {
				// 创建一个数组 用来存储每一列的值
				String[] str = new String[sheet.getColumns()];
				// 列数
				for (int j = 0; j < sheet.getColumns(); j++) {
					// 获取第i行，第j列的值
					cell = sheet.getCell(j, i);
					str[j] = cell.getContents();
				}
				// 把刚获取的列存入list
				list.add(str);
			}
			System.out.println(list.size());
			for (int i = 0; i < list.size(); i++) {
				String[] str = (String[]) list.get(i);
				User user = new User();
				for (int j = 0; j < str.length; j++) {
					// System.out.println(str[j]);
					// User user
					if (j == 0) {
						user.setDepartment(str[j].trim());
					} else if (j == 1) {
						user.setName(str[j]);
					} else if (j == 4) {
						user.setPhone(str[j]);
					}
				}
				if(user.getPhone()!=""&&user.getDepartment()!=""&&user.getName()!="")
					userList.add(user);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(userList.size());
		for (User user : userList) {
			System.out.println(user.getDepartment() + "a " + user.getName()
					+ "b " + user.getPhone()+"c ");
		}

	*/}

	public static List<WebSite> getWebSiteList() {

		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		// 创建一个list 用来存储读取的内容
		List<String[]> list = new ArrayList<String[]>();
		List<WebSite> webSiteList = new ArrayList<WebSite>();
		Workbook rwb = null;
		Cell cell = null;

		// 创建输入流
		InputStream stream;
		try {
			stream = new FileInputStream("d:\\bbb.xls");
			// 获取Excel文件对象
			rwb = Workbook.getWorkbook(stream);

			// 获取文件的指定工作表 默认的第一个
			Sheet sheet = rwb.getSheet(0);

			// 行数(表头的目录不需要，从1开始)
			for (int i = 1; i < sheet.getRows(); i++) {
				// 创建一个数组 用来存储每一列的值
				String[] str = new String[sheet.getColumns()];
				// 列数
				for (int j = 0; j < sheet.getColumns(); j++) {
					// 获取第i行，第j列的值
					cell = sheet.getCell(j, i);
					str[j] = cell.getContents();
				}
				// 把刚获取的列存入list
				list.add(str);
			}
			System.out.println(list.size());
			for (int i = 0; i < list.size(); i++) {
				String[] str = (String[]) list.get(i);
				WebSite webSite = new WebSite();
				for (int j = 0; j < str.length; j++) {
					// System.out.println(str[j]);
					// User user
					if (j == 0) {
						webSite.setName(str[j].trim());
					} else if (j == 1) {
						webSite.setSection(str[j]);
					} else if (j == 2) {
						webSite.setLink(str[j]);
					}
				}
				System.out.println(webSite.getName()+"---------");
				if(webSite.getName().trim()!="" && null!=webSite.getName())
				webSiteList.add(webSite);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (WebSite webSite : webSiteList) {
			System.out.println(webSite.getName() + " " + webSite.getSection()
					+ " " +webSite.getLink());
		}

		return webSiteList;

	}

}
