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
public class JiangsuController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/jiangsu")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI=jiangsu " +" "+new Date());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.jiangsu);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try {
			Document doc = Jsoup.connect("http://www.jsdzj.gov.cn/module/web/jpage/dataproxy.jsp?page=1&appid=1&appid=1&webid=1&path=/&columnid=543&unitid=286&webname=%E6%B1%9F%E8%8B%8F%E9%98%B2%E9%9C%87%E5%87%8F%E7%81%BE&permissiontype=0")
								.header("User-Agent", Constants.HEAD)
								.timeout(Constants.TIMEOUT)
								.get();
//			System.out.println(doc.text());
			Document doctext = Jsoup.parse(doc.text());
			Elements lis = doctext.select("table");
			
			for (Element a : lis) {
				Element element = a.select("tr td a").first();
				String title = element.text();
				Document docdetail ;
				String ssource;
				String stime;
				String contents;
				String url = "http://www.jsdzj.gov.cn/"+element.attr("href");
				if(urlSet.contains(url)){
					continue;
				}
				docdetail = Jsoup.connect(url)
								 .header("User-Agent", Constants.HEAD)		
								 .timeout(Constants.TIMEOUT).get();
				
				String time = docdetail.getElementById("c").text();
//				System.out.println(time);
				if(time.indexOf("信息来源")!=-1){
					ssource = time.substring(time.indexOf("信息来源")+5,time.indexOf("浏览次数")).trim();
					stime = time.substring(time.indexOf("发布时间")+5,time.indexOf("信息来源")).trim();
				}else{
					ssource = "";
					stime = time.substring(time.indexOf("发布时间")+5,time.indexOf("浏览次数")).trim();
				}
				Element content = docdetail.select("td.bt_content").first();
				Elements imgs = content.select("img");
				  for (Element img : imgs) {
					 String src = img.attr("src");
					 if(src.indexOf("http")==-1){
						 img.attr("src", "http://www.jsdzj.gov.cn"+src);
					 }
					 //System.out.println(img.attr("src"));
				  }
				contents = content.text();
				String html = content.html();
				LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
				linksWithBLOBs.setWebId(Constants.jiangsu);
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
				System.out.println(++count);
				//System.out.println(element.text());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		linksService.insertLinksWithBLOBsList(list);
		
		return "redirect:/";
	}
}
