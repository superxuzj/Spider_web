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
public class GuangxiController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/guangxi")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI=guangxi " +" "+new Date());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.guangxi);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try {
			Document doc = Jsoup.connect("http://www.gxdzj.gov.cn/html/zhxx/")
								.header("User-Agent", Constants.HEAD)
								.timeout(Constants.TIMEOUT)
								.get();
//			Element page = doc.select("div.page02").first();
//			int pageSize = Integer.valueOf(page.text().substring(page.text().indexOf("/")+1, page.text().indexOf("页)")));
			Element page = doc.select("div.page").first();
			int from = page.text().indexOf("/")+1;
			int to  = page.text().indexOf("页");
			int pageSize = Integer.valueOf(page.text().substring(from, to));
			for(int i=1;i<=pageSize;i++){
				Document docpage ;
				if(i==1){
					docpage = Jsoup.connect("http://www.gxdzj.gov.cn/html/zhxx/index.html")
									.header("User-Agent", Constants.HEAD)	
									.timeout(Constants.TIMEOUT).get();
				}else{
					docpage = Jsoup.connect("http://www.gxdzj.gov.cn/html/zhxx/index_"+i+".html")
									.header("User-Agent", Constants.HEAD)
									.timeout(Constants.TIMEOUT).get();
				}
				
				Elements lis = docpage.select("ul.list li a");
				for (Element element : lis) {
					String title = element.attr("title");
					if(title.indexOf("应急")!=-1 || title.indexOf("救援")!=-1){
						String url = "http://www.gxdzj.gov.cn"+element.attr("href");
						if(urlSet.contains(url)){
							continue;
						}
						Document docdetail = Jsoup.connect(url)
													.header("User-Agent", Constants.HEAD)
													.timeout(Constants.TIMEOUT).get();
						String time = docdetail.select("div.lf").first().text();
						//System.out.println(time);
						String sauthor = time.substring(time.indexOf("作者")+3, time.length());
						String ssource = time.substring(time.indexOf("来源")+3, time.indexOf("作者"));
                        //String stime = time.substring(0, time.indexOf("来源"));
						String stime = time.substring(0, 10);
						//System.out.println(stime+"  "+ssource +"   -- "+title);
						Element content = docdetail.getElementById("UCAP-CONTENT");
						Elements imgs = content.select("img");
						for (Element img : imgs) {
							String src = img.attr("src");
							img.attr("src", "http://www.gxdzj.gov.cn"+src);
							//System.out.println(img.attr("src"));
						}
						String html = content.html();
						LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.guangxi);
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
