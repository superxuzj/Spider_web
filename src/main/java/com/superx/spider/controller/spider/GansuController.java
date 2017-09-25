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
public class GansuController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/gansu")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI= gansu" +" "+new Date());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.gansu);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try {
			Document doc = Jsoup.connect("http://www.gsdzj.gov.cn/pub/yingji/index.html")
								.header("User-Agent", Constants.HEAD)
								.timeout(Constants.TIMEOUT)
								.get();
			Elements page = doc.select("span.NavGoto select option");
			int pageSize = page.size();
			for(int i=1;i<=pageSize;i++){
				Document docpage ;
				if(i==1){
					docpage = Jsoup.connect("http://www.gsdzj.gov.cn/pub/yingji/index.html")
									.header("User-Agent", Constants.HEAD)
									.timeout(Constants.TIMEOUT)
								    .get();
				}else{
					docpage = Jsoup.connect("http://www.gsdzj.gov.cn/pub/yingji/index"+i+".html")
								   .header("User-Agent", Constants.HEAD)
								   .timeout(Constants.TIMEOUT)
								   .get();
				}
				Elements lis = docpage.select("div.listBox li a");
				
				for (Element element : lis) {
					String title = element.text();
						String url = "http://www.gsdzj.gov.cn"+element.attr("href");
						if(urlSet.contains(url)){
							continue;
						}
						Document docdetail = Jsoup.connect("http://www.gsdzj.gov.cn"+element.attr("href"))
												  .header("User-Agent", Constants.HEAD)
												  .timeout(Constants.TIMEOUT)
												  .get();
						Element telement = docdetail.select("div.infoBox").first();
						String time = telement.text().replace("\u00A0","");
						String ssource = time.substring(time.indexOf("来源")+3,time.indexOf("|"));
						String stime = time.substring(time.indexOf("发布日期")+5,time.indexOf("发布日期")+16);
						stime = stime.replace("年", "-").replace("月", "-").replace("日", "");
						System.out.println(stime);
						Element content =docdetail.getElementById("wwkjArticleDetail");
						Elements imgs = content.select("img");
						for (Element img : imgs) {
							String src = img.attr("src");
							img.attr("src", "http://www.gsdzj.gov.cn"+src);
						}
						//System.out.println(ssource+"--"+title+"--"+stime);
						String html = content.html();
						String sauthor="";
						LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.gansu);
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
		//linksService.insertLinksWithBLOBsList(list);
		return "redirect:/";
	}
}
