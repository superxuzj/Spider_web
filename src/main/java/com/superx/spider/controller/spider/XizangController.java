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
public class XizangController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/xizang")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI=xizang " +" "+new Date());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.xizang);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try {                             
			Document doc = Jsoup.connect("http://www.xizdzj.gov.cn/action/pageUI_showNewsList.action?mid=203")
								.header("User-Agent", Constants.HEAD)
								.timeout(Constants.TIMEOUT)
								.get();
			Elements pages = doc.select("select[name=pageNo] option");
			int pageSize = pages.size();
			for(int i=1;i<=pageSize;i++){
				Document docpage ;
				docpage = Jsoup.connect("http://www.xizdzj.gov.cn/action/pageUI_showNewsList.action?currentPage="+i+"&mid=203")
								.header("User-Agent", Constants.HEAD)	  
								.timeout(Constants.TIMEOUT).get();
				
				Elements lis = docpage.getElementById("pagelist").select("li a");
				
				for (Element element : lis) {
					String title = element.text();
						String url = "http://www.xizdzj.gov.cn"+element.attr("href");
						if(urlSet.contains(url)){
							continue;
						}
						Document docdetail = Jsoup.connect(url)
												  .header("User-Agent", Constants.HEAD)
												  .timeout(Constants.TIMEOUT).get();
						Element telement = docdetail.select("div.pageText_caption").first();
						String time = telement.text().replace("\u00A0","");
						//System.out.println(time);
						String ssource = time.substring(time.indexOf("新闻来源")+5,time.length());
						
						String stime = time.substring(time.indexOf("日期")+3,time.indexOf("新闻")).trim();
						stime = stime.substring(0, 10);
						Element content =docdetail.select("div.pageText_content").first();
						Elements imgs = content.select("img");
						  for (Element img : imgs) {
							 String src = img.attr("src");
							 if(src.indexOf("http")==-1){
								 src = "http://www.xizdzj.gov.cn"+src;
							 }
							img.attr("src", src);
							//System.out.println(img.attr("src"));
						  }
						String html = content.html();
						LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.xizang);
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
