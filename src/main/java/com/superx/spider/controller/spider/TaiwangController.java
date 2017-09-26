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
public class TaiwangController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/taiwang")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI=taiwang " +" "+new Date());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.taiwang);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try {
			Document doc = Jsoup.connect("http://www.cenc.ac.cn/cenc/320429/index.html")
								.header("User-Agent", Constants.HEAD)		
								.timeout(Constants.TIMEOUT)
								.get();
			Element page = doc.select("a.pagingNormal").get(1);
			String pages = page.toString();
			int indexof = pages.indexOf("-");
			String pagesizes = pages.substring(indexof+1,indexof+2);
			int pageSize = Integer.valueOf(pagesizes);
			for(int i=1;i<=pageSize;i++){
				Document docpage ;
				docpage = Jsoup.connect("http://www.cenc.ac.cn/cenc/320429/4bfd7158-"+i+".html")
								.header("User-Agent", Constants.HEAD)		
								.timeout(Constants.TIMEOUT)
								.get();
				Elements lis = docpage.select("div.erji_art_con p a");
				
				for (Element element : lis) {
					String title = element.text();
					String url = "http://www.cenc.ac.cn"+element.attr("href");
					if(urlSet.contains(url)){
						continue;
					}
					Document docdetail = Jsoup.connect(url)
												.header("User-Agent", Constants.HEAD)		
												.timeout(Constants.TIMEOUT)
												.get();
					Element telement = docdetail.select("div.pages-date").first();
					String time = telement.text();
					//System.out.println(time);
					String ssource;
					ssource = time.substring(time.indexOf("来源")+3,time.indexOf("【")).trim();
					String stime = time.substring(time.indexOf("发布时间")+5,time.indexOf("来源")).trim();
					stime = stime.replace("年", "-").replace("月", "-").replace("日", "");
					Element content =docdetail.getElementById("news_content");
					Elements imgs = content.select("img");
					  for (Element img : imgs) {
						 String src = img.attr("src");
						 img.attr("src", "http://www.cenc.ac.cn"+src);
						 //System.out.println(img.attr("src"));
					  }
					String html = content.html();
					LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
					linksWithBLOBs.setWebId(Constants.taiwang);
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
