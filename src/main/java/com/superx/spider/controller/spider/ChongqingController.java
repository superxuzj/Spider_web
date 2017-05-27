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
public class ChongqingController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/chongqing")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI= chongqing "+request.getRequestURI());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.chongqing);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>();
		try {
			Document doc = Jsoup.connect("http://www.cqdzj.gov.cn/list.php?id=11&page=1")
								.header("User-Agent", Constants.HEAD)
								.timeout(Constants.TIMEOUT)
								.get();
			Element page = doc.select("div.page > a").get(7);
			String pagehref = page.attr("href");
			int pageSize = Integer.valueOf(pagehref.substring(pagehref.indexOf("page=")+5, pagehref.length()));
			//System.out.println(pageSize);
			for(int i=1;i<=pageSize;i++){
				Document docpage = Jsoup.connect("http://www.cqdzj.gov.cn/list.php?id=11&page="+i)
										.header("User-Agent", Constants.HEAD)
										.timeout(20000)
										.get();
				Elements lis = docpage.select("div.sortlist>dl>dd>ul a");
				for (Element element : lis) {
					String title = element.text();
					if(title.indexOf("应急")!=-1 || title.indexOf("救援")!=-1){
						String temp = element.attr("href");
						temp = temp.substring(0, temp.indexOf("&"));
						String url = "http://www.cqdzj.gov.cn/"+temp;
						if(urlSet.contains(url)){
							continue;
						}
						Document docdetail = Jsoup.connect(url)
												.header("User-Agent", Constants.HEAD)
												.timeout(Constants.TIMEOUT).get();
						String time = docdetail.select("div.cen h3").first().text();
//						String ssource = time.substring(time.indexOf("来源")+3, time.indexOf("作者"));
                        String stime = time.substring(time.indexOf("发布日期")+5,time.length()-9);
						String ssource = time.substring(time.indexOf("发布人")+4,time.indexOf("浏览量")).trim();
						Element content = docdetail.getElementById("showcontent");
						Elements imgs = content.select("img");
						for (Element img : imgs) {
							String src = img.attr("src");
							img.attr("src", "http://www.cqdzj.gov.cn"+src.substring(2, src.length()));
							//System.out.println(img.attr("src"));
						}
						String html = content.html();
						LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.chongqing);
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
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		linksService.insertLinksWithBLOBsList(list);
		return "redirect:/";
	}
}
