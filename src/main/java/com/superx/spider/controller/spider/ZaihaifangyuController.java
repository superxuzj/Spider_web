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
public class ZaihaifangyuController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/zaihaifangyu")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI=zaihaifangyu " +" "+new Date());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.zaihaifangyu);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try {
			Document doc = Jsoup.connect("http://www.eq-cedpc.cn/dynamic.aspx?ffid=12&page=1")
								.header("User-Agent", Constants.HEAD)		
								.timeout(Constants.TIMEOUT)
								.get();
			
			Element page = doc.getElementById("AspNetPager1").select("a").get(6);
			String pageStringAll = page.attr("href");
			String pageString  = pageStringAll.substring(pageStringAll.indexOf("page=")+5, pageStringAll.length());
			int pageSize = Integer.valueOf(pageString.trim());
			for(int i=1;i<=pageSize;i++){
				Document docpage ;
				docpage = Jsoup.connect("http://www.eq-cedpc.cn/dynamic.aspx?ffid=12&page="+i)
								.header("User-Agent", Constants.HEAD)		
								.timeout(Constants.TIMEOUT)
								.get();
				Elements lis = docpage.select("ul.newsul li a");
				
				for (Element element : lis) {
					String title = element.text();
					if(title.indexOf("应急")!=-1 || title.indexOf("救援")!=-1){
						String url = "http://www.eq-cedpc.cn/"+element.attr("href");
						if(urlSet.contains(url)){
							continue;
						}
						Document docdetail = Jsoup.connect(url)
													.header("User-Agent", Constants.HEAD)		
													.timeout(Constants.TIMEOUT)
													.get();
						Element telement = docdetail.select("div.nrtitle span").first();
						String time = telement.text().trim();
						String ssource = time.substring(time.indexOf("来源")+3,time.length()).trim();
						String stime = time.substring(time.indexOf("发布日期")+5,time.indexOf("来源")).trim();
						Element content =docdetail.select("div.newsinfo").first();
						Elements imgs = content.select("img");
						for (Element img : imgs) {
							String src = img.attr("src");
							img.attr("src", "http://www.eq-cedpc.cn"+src);
						}
						String html = content.html();
						LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.zaihaifangyu);
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
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		linksService.insertLinksWithBLOBsList(list);
		return "redirect:/";
	}
}
