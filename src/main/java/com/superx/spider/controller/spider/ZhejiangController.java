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
public class ZhejiangController {
	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/zhejiang")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI=zhejiang " +" "+new Date());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.zhejiang);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try {
			Document doc = Jsoup.connect("http://www.zjdz.gov.cn/article/yingjizhunbei/index.html")
								.header("User-Agent", Constants.HEAD)
								.timeout(Constants.TIMEOUT)
								.get();
			Element page = doc.select("div.ppl_info").first();
			String pageString = page.text().substring(page.text().indexOf("/")+1, page.text().length());
			int pageSize = Integer.valueOf(pageString.trim());
			for(int i=1;i<=pageSize;i++){
				Document docpage ;
				docpage = Jsoup.connect("http://www.zjdz.gov.cn/article/yingjizhunbei/index-"+i+".html")
								.header("User-Agent", Constants.HEAD)
							   .timeout(Constants.TIMEOUT).get();
				Elements lis = docpage.select("div.news_list_03 ul li a");
				for (Element element : lis) {
					String title = element.text();
					Document docdetail ;
//					String ssource;
					String stime;
					String scontent;
					String url = "http://www.zjdz.gov.cn/"+element.attr("href");
					if(urlSet.contains(url)){
						continue;
					}
					docdetail = Jsoup.connect(url)
										.header("User-Agent", Constants.HEAD)
										.timeout(Constants.TIMEOUT).get();
					String time = docdetail.select("span.update").first().text();
					//ssource = time.substring(time.indexOf("来源")+3,time.indexOf("发布者")).trim().replace("\u00A0","");
					stime = time.substring(time.indexOf("最后更新")+5,time.length()).trim().replace("\u00A0","");
					stime = stime.replace("年", "-").replace("月", "-").replace("日", "").trim();
					stime = stime.substring(0, 10);
					Element content = docdetail.getElementById("article_content");
					scontent = content.text();
					Elements imgs = content.select("img");
					  for (Element img : imgs) {
						 String src = img.attr("src");
						 //System.out.println(src);
						 img.attr("src", "http://www.zjdz.gov.cn"+src);
						 //System.out.println(img.attr("src"));
					  }
//					System.out.println(time);
					String html = content.html();
					LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
					linksWithBLOBs.setWebId(Constants.zhejiang);
				    linksWithBLOBs.setTitle(title);
					linksWithBLOBs.setLink(url);
					linksWithBLOBs.setSource("");
					linksWithBLOBs.setAuthor("");
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		linksService.insertLinksWithBLOBsList(list);
		
		return "redirect:/";
	}
}
