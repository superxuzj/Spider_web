package com.superx.spider.controller.spider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Connection;
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

@Controller
public class ShanxiController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/shanxi")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI=shanxi "+request.getRequestURI());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.shanxi);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try {
			Connection conn = Jsoup.connect("http://www.shxdzj.gov.cn/n16/n1072/n1077/index.html");//获取连接
            //设置请求头，伪装成浏览器(否则会报403)
            conn.header("User-Agent", Constants.HEAD);
            Document docpagefirst=conn.timeout(Constants.TIMEOUT).get();
			Elements lisfirst = docpagefirst.select("table.sv_black14_30 tr td a");
			for (Element element : lisfirst) {
				String title = element.text();
				if(title.indexOf("应急")!=-1 || title.indexOf("救援")!=-1){
					Document docdetail ;
					String ssource="";
					String stime;
					String scontent;
					String sauthor="";
					String html;
					//System.out.println(element.attr("href").substring(5, element.attr("href").length()));
					String url = "http://www.shxdzj.gov.cn/n16"+element.attr("href").substring(5, element.attr("href").length());
					if(urlSet.contains(url)){
						continue;
					}
					docdetail = Jsoup.connect(url)
										.header("User-Agent", Constants.HEAD)
										.timeout(Constants.TIMEOUT).get();
					String time = docdetail.select("td.grey12_24").first().text();
					//System.out.println(time);
					String stitle = docdetail.getElementById("con_title").text();
					if(time.indexOf("信息来源")!=-1){
						ssource = time.substring(time.indexOf("信息来源")+5,time.length()).trim().replace("\u00A0","");
					}
					if(time.indexOf("作者")!=-1){
						if(time.indexOf("信息来源")!=-1){
							sauthor = time.substring(time.indexOf("作者")+3,time.indexOf("信息来源")).trim().replace("\u00A0","");
						}else{
							sauthor = time.substring(time.indexOf("作者")+3,time.length()).trim().replace("\u00A0","");
						}
					}
					stime = time.substring(time.indexOf("发布时间")+5,time.indexOf("发布时间")+15).trim().replace("\u00A0","");
					Element content = docdetail.select("table.sv_black14_30").first();
					Elements imgs = content.select("img");
					  for (Element img : imgs) {
						 String src = img.attr("src");
						 img.attr("src", "http://www.shxdzj.gov.cn"+src.substring(8, src.length()));
//						 System.out.println(img.attr("src"));
					  }
					
					scontent = content.text();
					html = content.html();
					LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
					linksWithBLOBs.setWebId(Constants.shanxi);
				    linksWithBLOBs.setTitle(stitle);
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
					System.out.println(++count);
				}
			}
			linksService.insertLinksWithBLOBsList(list);
			
			Document doc = Jsoup.connect("http://www.shxdzj.gov.cn/n16/n1072/n1077/index.html")
								.header("User-Agent", Constants.HEAD)
								.timeout(Constants.TIMEOUT)
								.get();
			
			Element page = doc.select("td.sv_blue").first();
			String pageString = page.html().substring(page.html().indexOf("maxPageNum238389")+17, page.html().indexOf("maxPageNum238389")+20);
			System.out.println(pageString);
			if(pageString.substring(pageString.length()-1, pageString.length()).equals("\"")){
				pageString = pageString.substring(0, pageString.length()-1);
			}
			int pageSize = Integer.valueOf(pageString);
			System.out.println(pageSize);
			for(int i=pageSize-1;i>0;i--){
				Document docpage ;
				docpage = Jsoup.connect("http://www.shxdzj.gov.cn/n16/n1072/n1077/index_238389_"+i+".html")
								.header("User-Agent", Constants.HEAD)
							   .timeout(Constants.TIMEOUT).get();
				Elements lis = docpage.select("table.sv_black14_30 tr td a");
				urlSet = linksService.selectLinksListByWebId(Constants.shanxi);
				for (Element element : lis) {
					String title = element.text();
					if(title.indexOf("应急")!=-1 || title.indexOf("救援")!=-1){
						Document docdetail ;
						String ssource="";
						String stime;
						String scontent;
						String sauthor="";
						String html="";
						//System.out.println(element.attr("href").substring(5, element.attr("href").length()));
						String url = "http://www.shxdzj.gov.cn/n16"+element.attr("href").substring(5, element.attr("href").length());
						if(urlSet.contains(url)){
							continue;
						}
						docdetail = Jsoup.connect(url)
										 .header("User-Agent", Constants.HEAD) 
										 .timeout(Constants.TIMEOUT).get();
						String time = docdetail.select("td.grey12_24").first().text();
						//System.out.println(time);
						String stitle = docdetail.getElementById("con_title").text();
						if(time.indexOf("信息来源")!=-1){
							ssource = time.substring(time.indexOf("信息来源")+5,time.length()).trim().replace("\u00A0","");
						}
						if(time.indexOf("作者")!=-1){
							if(time.indexOf("信息来源")!=-1){
								sauthor = time.substring(time.indexOf("作者")+3,time.indexOf("信息来源")).trim().replace("\u00A0","");
							}else{
								sauthor = time.substring(time.indexOf("作者")+3,time.length()).trim().replace("\u00A0","");
							}
						}
						
						stime = time.substring(time.indexOf("发布时间")+5,time.indexOf("发布时间")+15).trim().replace("\u00A0","");
						
						Element content = docdetail.select("table.sv_black14_30").first();
						Elements imgs = content.select("img");
						  for (Element img : imgs) {
							 String src = img.attr("src");
							 img.attr("src", "http://www.shxdzj.gov.cn"+src.substring(8, src.length()));
							// System.out.println(img.attr("src"));
						  }
						
						scontent = content.text();
						html = content.html();
						LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.shanxi);
					    linksWithBLOBs.setTitle(stitle);
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
						int result = linksService.insertSelective(linksWithBLOBs);
						System.out.println(result+"   result");
						System.out.println(++count);
					}
				}
			}
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return "redirect:/";
	}
}
