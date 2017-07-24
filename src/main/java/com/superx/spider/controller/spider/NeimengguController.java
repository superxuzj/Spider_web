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
public class NeimengguController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/neimenggu")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI=neimenggu " +" "+new Date());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.neimenggu);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try {
			Document doc = Jsoup.connect("http://www.nmgdzj.gov.cn/sitefiles/services/cms/page.aspx?s=1&n=36")
								.header("User-Agent", Constants.HEAD)		
								.timeout(Constants.TIMEOUT)
								.get();
			
			Element page = doc.select("div.alert").first();
			String pageStringAll = page.text();
			String pageString  = pageStringAll.substring(pageStringAll.indexOf("/")+1, pageStringAll.indexOf("["));
			int pageSize = Integer.valueOf(pageString.trim());
			for(int i=0;i<=pageSize-1;i++){
				Document docpage ;
				if(i==0){
					docpage = Jsoup.connect("http://www.nmgdzj.gov.cn/sitefiles/services/cms/page.aspx?s=1&n=36")
									.header("User-Agent", Constants.HEAD)		
									.timeout(Constants.TIMEOUT)
									.get();
				}else{
					docpage = Jsoup.connect("http://www.nmgdzj.gov.cn/sitefiles/services/cms/page.aspx?s=1&n=36&p="+i)
									.header("User-Agent", Constants.HEAD)		
									.timeout(Constants.TIMEOUT)
									.get();
				}
				Elements lis = docpage.select("div.topnews2 a");
				
				for (Element element : lis) {
					//String title = element.text();
						String url = "http://www.nmgdzj.gov.cn/"+element.attr("href");
						if(urlSet.contains(url)){
							continue;
						}
						Document docdetail = Jsoup.connect(url)
												  .header("User-Agent", Constants.HEAD)		
												  .timeout(Constants.TIMEOUT)
												  .get();
						Element telement = docdetail.select("div.news_list_zhengwen3").first();
						Element titleElement = docdetail.select("div.news_list_zhengwen2").first();
						String title = titleElement.text();
						String time = telement.text();
						String ssource;
						ssource = time.substring(time.indexOf("文章来源")+5,time.indexOf("正文")).trim();
						String stime = time.substring(time.indexOf("更新时间")+5,time.indexOf("浏览次数")).trim();
						Element content =docdetail.getElementById("artibody");
						Elements imgs = content.select("img");
						  for (Element img : imgs) {
							 String src = img.attr("src");
							 img.attr("src", "http://www.nmgdzj.gov.cn"+src);
							//System.out.println(img.attr("src"));
						  }
						  String html = content.html();
						LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.neimenggu);
					    linksWithBLOBs.setTitle(title);
						linksWithBLOBs.setLink(url);
						linksWithBLOBs.setSource(ssource);
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
						System.out.println(++count+" -  ");
						
						
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
