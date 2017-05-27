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

/**
 * @author xuzj
 *
 */
@Controller
public class GuangdongController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/guangdong")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI=guangdong "+request.getRequestURI());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.guangdong);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try {
			Document doc = Jsoup.connect("http://www.gddzj.gov.cn/dizhenxinwen/dzxinwenll.asp?xwid=1")
								.header("User-Agent", Constants.HEAD)
								.timeout(Constants.TIMEOUT)
								.get();
			Elements elements = doc.select("a");
			for (Element element : elements) {
//				System.out.println(element.attr("href"));
//				System.out.println(element.text());
//				System.out.println(element.attr("href"));
				if(element.attr("href").indexOf("xinwenxx.asp")!=-1){
					String title = element.text();
					if(title.indexOf("应急")!=-1 || title.indexOf("救援")!=-1){
						Element time =  element.parent().child(1);
						String url = "http://www.gddzj.gov.cn/dizhenxinwen/"+element.attr("href");
						if(urlSet.contains(url)){
							continue;
						}
						Document doca = Jsoup.connect(url)
										     .header("User-Agent", Constants.HEAD)
											 .timeout(Constants.TIMEOUT).get();
						Element content = doca.select("td.text1").first();
//						System.out.println(content);
						Elements imgs = content.select("img");
						for (Element img : imgs) {
							String src = img.attr("src");
							img.attr("src", "http://www.gddzj.gov.cn/dizhenxinwen/"+src);
//							System.out.println(img.attr("src"));
						}
						
						Element titlea = doca.select("div.bt").first();
						String stitle = titlea.text();
						String stime = time.text().substring(1, time.text().length()-1);
						String html = content.html();
						String sauthor="";
						LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.guangdong);
						linksWithBLOBs.setTitle(stitle);
						linksWithBLOBs.setLink(url);
						linksWithBLOBs.setSource("");
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
