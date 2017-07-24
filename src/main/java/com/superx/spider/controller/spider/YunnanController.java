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
public class YunnanController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/yunnan")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI=yunnan " +" "+new Date());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.yunnan);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try {
			Document doc = Jsoup.connect("http://yndzj.gov.cn/yndzj/_300559/_300651/index.html")
								.header("User-Agent", Constants.HEAD)
								.timeout(Constants.TIMEOUT)
								.get();
			
			Element page = doc.select("td.Normal").first();
			String pageStringAll = page.text();
			String pageString  = pageStringAll.substring(pageStringAll.indexOf("/")+1, pageStringAll.indexOf("跳转"));
			int pageSize = Integer.valueOf(pageString.trim());
			for(int i=1;i<=pageSize;i++){
				Document docpage ;
				docpage = Jsoup.connect("http://yndzj.gov.cn/eportal/ui?pageId=300653&currentPage="+i+"&moduleId=b4ed2f132d90443abb869465186e9283&staticRequest=yes")
								.header("User-Agent", Constants.HEAD)
								.timeout(Constants.TIMEOUT).get();
				
				Elements lis = docpage.select("div.m_cont>table>tbody tbody td a");
				
				for (Element element : lis) {
					String title = element.attr("title");
					if(title.indexOf("应急")!=-1 || title.indexOf("救援")!=-1){
						String url = "http://yndzj.gov.cn"+element.attr("href");
						if(urlSet.contains(url)){
							continue;
						}
						Document docdetail = Jsoup.connect(url)
												  .header("User-Agent", Constants.HEAD)	
												  .timeout(Constants.TIMEOUT).get();
						Element telement = docdetail.getElementById("jiuctit").parent().parent().nextElementSibling().nextElementSibling();
						String time = telement.text();
						String ssource = time.substring(time.indexOf("来源")+3,time.indexOf("作者")).trim();
						String stime = time.substring(time.indexOf("时间")+3,time.indexOf("阅读次数")).trim();
						String sauthor = time.substring(time.indexOf("作者")+3, time.indexOf("时间")).trim();
						Element content = docdetail.getElementById("xilan_cont");
						Elements imgs = content.select("img");
						  for (Element img : imgs) {
							 String src = img.attr("src");
							 img.attr("src", "http://yndzj.gov.cn"+src);
							 //System.out.println(img.attr("src"));
						  }
						String html = content.html();
						LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.yunnan);
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
						System.out.println(++count);
						
					}
						
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
