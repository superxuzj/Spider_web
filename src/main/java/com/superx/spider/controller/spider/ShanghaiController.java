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
 * 上海暂时还没有分页
 * @author xuzj
 *
 */
@Controller
public class ShanghaiController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/shanghai")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI= shanghai " +" "+new Date());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.shanghai);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try {
			Document doc = Jsoup.connect("http://www.shdzj.gov.cn/gb/dzj/gzdt/yjjy/index.html")
								.header("User-Agent", Constants.HEAD)
					   			.timeout(Constants.TIMEOUT).get();
			Elements elements = doc.select("ul.listTxt > li > a");
			for (Element element : elements) {
//				System.out.println(element.attr("href"));
//				System.out.println(element.text());
				String url = "http://www.shdzj.gov.cn"+element.attr("href");
				if(urlSet.contains(url)){
					continue;
				}
				Document docli = Jsoup.connect(url)
										.header("User-Agent", Constants.HEAD)
										.timeout(Constants.TIMEOUT).get();
				String title = element.attr("title");
				
				String time =docli.select("div.viewTitle").first().text();
				String stime= time.substring(time.indexOf("发布时间")+5, time.indexOf("信息来源")).trim();
				String ssource = time.substring(time.indexOf("信息来源")+5, time.length());
				
				Element content = docli.getElementById("content");
				Elements imgs = content.select("img");
				  for (Element img : imgs) {
					 String src = img.attr("src");
					 img.attr("src", "http://www.shdzj.gov.cn"+src);
					// System.out.println(img.attr("src"));
				  }
				 String html = content.html();
				    LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
					linksWithBLOBs.setWebId(Constants.shanghai);
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
				//System.out.println(title.replace("\u00A0",""));
			}
//			System.out.println(doc.html());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		linksService.insertLinksWithBLOBsList(list);
		
		return "redirect:/";
	}
}
