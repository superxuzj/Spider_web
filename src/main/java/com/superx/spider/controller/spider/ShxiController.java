package com.superx.spider.controller.spider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.superx.spider.entity.LinksWithBLOBs;
import com.superx.spider.service.LinksService;
import com.superx.spider.util.Constants;

/**
 * @author xuzj
 *
 */
@Controller
public class ShxiController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/shxi")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI=shxi "+request.getRequestURI());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.shxi);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try {
			//应急救援动态
			Document doc = Jsoup.connect("http://www.sxdzj.gov.cn/manage/html/8abd83af1c88b3f2011c88b74299001f/gzdt-yjjy/index.html")
								.header("User-Agent", Constants.HEAD)
								.timeout(Constants.TIMEOUT)
								.get();
			
			Element page = doc.select("div.lv_pagebar").first();
			String pageStringAll = page.text();
			String pageString  = pageStringAll.substring(pageStringAll.indexOf("/")+1, pageStringAll.indexOf("/")+3);
			
			System.out.println(pageString);
			int pageSize = Integer.valueOf(pageString.trim());
			for(int i=1;i<=pageSize;i++){
				Document docpage ;
				if(i==1){
					docpage = Jsoup.connect("http://www.sxdzj.gov.cn/manage/html/8abd83af1c88b3f2011c88b74299001f/gzdt-yjjy/index.html")
									.header("User-Agent", Constants.HEAD)
									.timeout(Constants.TIMEOUT)
									.get();
				}else{
					docpage = Jsoup.connect("http://www.sxdzj.gov.cn/manage/html/8abd83af1c88b3f2011c88b74299001f/gzdt-yjjy/index_"+i+".html")
									.header("User-Agent", Constants.HEAD)
									.timeout(Constants.TIMEOUT)
									.get();
				}
				Elements lis = docpage.select("ul.lv_third_ul li a");
				
				for (Element element : lis) {
					String title = element.text();
					    String url = "http://www.sxdzj.gov.cn/"+element.attr("href");
					    if(urlSet.contains(url)){
					    	continue;
					    }
						Document docdetail = Jsoup.connect(url)
												  .header("User-Agent", Constants.HEAD)
												  .timeout(Constants.TIMEOUT)
												  .get();
						Element telement = docdetail.select("div.lv_title p").first();
						String time = telement.text().replace("\u00A0","");
						String ssource = time.substring(time.indexOf("来源")+3,time.indexOf("作者"));
						
						String stime = time.substring(3,time.indexOf("来源"));
						String sauthor = time.substring(time.indexOf("作者")+3, time.length()).trim();
						Element content =docdetail.select("div.lv_article").first();
						Elements imgs = content.select("img");
						  for (Element img : imgs) {
							 String src = img.attr("src");
							 img.attr("src", "http://www.sxdzj.gov.cn"+src);
							 //System.out.println(img.attr("src"));
						  }
						 String html = content.html();
						LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.shxi);
					    linksWithBLOBs.setTitle(title);
						linksWithBLOBs.setLink(url);
						linksWithBLOBs.setSource(ssource);
						linksWithBLOBs.setAuthor(sauthor);
						linksWithBLOBs.setTime(stime);
						linksWithBLOBs.setSendcontent(html);
						linksWithBLOBs.setIdent("0");
						linksWithBLOBs.setStatus("0");
						linksWithBLOBs.setCreatorName("superxu");
						linksWithBLOBs.setCreatorTime(new Date());
						list.add(linksWithBLOBs);
						//int result = linksService.insertSelective(linksWithBLOBs);
						//System.out.println(result+"   result");
						System.out.println(++count+" -  ");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			//应急管理
			Document doc = Jsoup.connect("http://www.sxdzj.gov.cn/manage/html/8abd83af1c88b3f2011c88b74299001f/yjjy-yjgl/index.html")
								.header("User-Agent", Constants.HEAD)
								.timeout(Constants.TIMEOUT)
								.get();
				Elements lis = doc.select("ul.lv_third_ul li a");
				
				for (Element element : lis) {
					String title = element.text();
						String url = "http://www.sxdzj.gov.cn/"+element.attr("href");
					    if(urlSet.contains(url)){
					    	continue;
					    }
						Document docdetail = Jsoup.connect(url)
													.header("User-Agent", Constants.HEAD)
													.timeout(Constants.TIMEOUT)
													.get();
						Element telement = docdetail.select("div.lv_title p").first();
						String time = telement.text().replace("\u00A0","");
						String ssource = time.substring(time.indexOf("来源")+3,time.indexOf("作者"));
						String stime = time.substring(3,time.indexOf("来源"));
						String sauthor = time.substring(time.indexOf("作者")+3, time.length()).trim();
						Element content =docdetail.select("div.lv_article").first();
						/*Elements imgs = content.select("img");
						  for (Element img : imgs) {
							 String src = img.attr("src");
							 System.out.println(src);
							 img.attr("src", "http://www.sxdzj.gov.cn"+src);
							 System.out.println(img.attr("src"));
						  }*/
						 String html = content.html();
						LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.shxi);
					    linksWithBLOBs.setTitle(title);
						linksWithBLOBs.setLink(url);
						linksWithBLOBs.setSource(ssource);
						linksWithBLOBs.setAuthor(sauthor);
						linksWithBLOBs.setTime(stime);
						linksWithBLOBs.setSendcontent(html);
						linksWithBLOBs.setIdent("0");
						linksWithBLOBs.setStatus("0");
						linksWithBLOBs.setCreatorName("superxu");
						linksWithBLOBs.setCreatorTime(new Date());
						list.add(linksWithBLOBs);
						//int result = linksService.insertSelective(linksWithBLOBs);
						//System.out.println(result+"   result");
						System.out.println(++count+" -  ");
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			//应急预案
			Document doc = Jsoup.connect("http://www.sxdzj.gov.cn/manage/html/8abd83af1c88b3f2011c88b74299001f/yjjy-yjya/index.html")
								.header("User-Agent", Constants.HEAD)
								.timeout(Constants.TIMEOUT)
								.get();
				Elements lis = doc.select("ul.lv_third_ul li a");
				for (Element element : lis) {
					String title = element.text();
						String url = "http://www.sxdzj.gov.cn/"+element.attr("href");
					    if(urlSet.contains(url)){
					    	continue;
					    }
						Document docdetail = Jsoup.connect(url)
													.header("User-Agent", Constants.HEAD)
													.timeout(Constants.TIMEOUT)
													.get();
						Element telement = docdetail.select("div.lv_title p").first();
						String time = telement.text().replace("\u00A0","");
						//System.out.println(time);
						String ssource = time.substring(time.indexOf("来源")+3,time.indexOf("作者"));
						
						String stime = time.substring(3,time.indexOf("来源"));
						String sauthor = time.substring(time.indexOf("作者")+3, time.length()).trim();
						Element content =docdetail.select("div.lv_article").first();
						Elements imgs = content.select("img");
						  for (Element img : imgs) {
							 String src = img.attr("src");
							 img.attr("src", "http://www.sxdzj.gov.cn"+src);
							 //System.out.println(img.attr("src"));
						  }
						 String html = content.html();
						LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.shxi);
					    linksWithBLOBs.setTitle(title);
						linksWithBLOBs.setLink(url);
						linksWithBLOBs.setSource(ssource);
						linksWithBLOBs.setAuthor(sauthor);
						linksWithBLOBs.setTime(stime);
						linksWithBLOBs.setSendcontent(html);
						linksWithBLOBs.setIdent("0");
						linksWithBLOBs.setStatus("0");
						linksWithBLOBs.setCreatorName("superxu");
						linksWithBLOBs.setCreatorTime(new Date());
						list.add(linksWithBLOBs);
						//int result = linksService.insertSelective(linksWithBLOBs);
						//System.out.println(result+"   result");
						System.out.println(++count+" -  ");
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		linksService.insertLinksWithBLOBsList(list);
		return "redirect:/";
	}
}
