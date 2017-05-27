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
public class HunanController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/hunan")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI=hunan "+request.getRequestURI());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.hunan);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try {
			Document doc = Jsoup.connect("http://www.hundzj.gov.cn/dzj/4/38/75/default.htm")
								.header("User-Agent", Constants.HEAD)
								.timeout(Constants.TIMEOUT)
								.get();
			Element page = doc.getElementById("pagination");
			String pageString = page.text().substring(page.text().indexOf("/")+1, page.text().indexOf("页"));
			int  pageSize = Integer.valueOf(pageString.trim());
			for(int i=0;i<=pageSize-1;i++){
				Document docpage ;
				if(i==0){
					docpage = Jsoup.connect("http://www.hundzj.gov.cn/dzj/4/38/75/default.htm")
								.header("User-Agent", Constants.HEAD)
							   .timeout(Constants.TIMEOUT).get();
				}else{
					docpage = Jsoup.connect("http://www.hundzj.gov.cn/dzj/4/38/75/default_"+i+".htm")
								.header("User-Agent", Constants.HEAD)
							   .timeout(Constants.TIMEOUT).get();
				}
				Elements lis = docpage.select("div.tob5 ul li a");
				for (Element element : lis) {
					String title = element.text();
					Document docdetail ;
					String ssource;
					String stime;
					String contents;
					String url = "http://www.hundzj.gov.cn/dzj/4/38/75/"+element.attr("href");
					if(urlSet.contains(url)){
						continue;
					}
					docdetail = Jsoup.connect(url)
										.header("User-Agent", Constants.HEAD)
										.timeout(Constants.TIMEOUT).get();
					String time = docdetail.select("span.lyym2").first().text();
					//System.out.println(time);
					if(time.indexOf("作者")!=-1){
						ssource = time.substring(time.indexOf("信息来源")+5,time.indexOf("作者")).trim();
					}else{
						ssource = time.substring(time.indexOf("信息来源")+5,time.indexOf("发布时间")).trim();
					}
					
					stime = time.substring(time.indexOf("发布时间")+5,time.indexOf("浏览量")).trim();
					Element content = docdetail.getElementById("zoom");
					contents = content.text();
					Elements imgs = content.select("img");
					  for (Element img : imgs) {
						 String src = img.attr("src");
						 //System.out.println(src);
						 img.attr("src", "http://www.hundzj.gov.cn/dzj"+src.substring(8, src.length()));
						//System.out.println(img.attr("src"));
					  }
					String html = content.html();
					  	LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.hunan);
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
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Document doc = Jsoup.connect("http://www.hundzj.gov.cn/dzj/4/38/74/default.htm")
								.header("User-Agent", Constants.HEAD)
								.timeout(Constants.TIMEOUT)
								.get();
			
				Elements lis = doc.select("div.tob5 ul li a");
				for (Element element : lis) {
					String title = element.text();
					Document docdetail ;
					String ssource;
					String stime;
					String contents;
					String url = "http://www.hundzj.gov.cn/dzj/4/38/74/"+element.attr("href");
					if(urlSet.contains(url)){
						continue;
					}
					docdetail = Jsoup.connect(url)
									.header("User-Agent", Constants.HEAD)
									.timeout(Constants.TIMEOUT).get();
					String time = docdetail.select("span.lyym2").first().text();
					//System.out.println(time);
					if(time.indexOf("作者")!=-1){
						ssource = time.substring(time.indexOf("信息来源")+5,time.indexOf("作者")).trim();
					}else{
						ssource = time.substring(time.indexOf("信息来源")+5,time.indexOf("发布时间")).trim();
					}
					stime = time.substring(time.indexOf("发布时间")+5,time.indexOf("浏览量")).trim();
					Element content = docdetail.getElementById("zoom");
					contents = content.text();
					Elements imgs = content.select("img");
					  for (Element img : imgs) {
						 String src = img.attr("src");
						 img.attr("src", "http://www.hundzj.gov.cn/dzj"+src.substring(8, src.length()));
						 System.out.println(img.attr("src"));
					  }
					String html = content.html();
				  	LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
					linksWithBLOBs.setWebId(Constants.hunan);
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
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		linksService.insertLinksWithBLOBsList(list);
		return "redirect:/";
	}
}
