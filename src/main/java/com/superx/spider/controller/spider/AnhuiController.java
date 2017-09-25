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
public class AnhuiController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/anhui")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		int count = 0;
		System.out.println("URI= anhui" +" "+new Date());
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.anhui);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>();
		try {
			Document doc = Jsoup.connect("http://eq.ah.gov.cn/ny_content/?zlm=6&ty=221")
					.header("User-Agent", Constants.HEAD)
								.timeout(Constants.TIMEOUT).get();//地震应急工作
			Element page = doc.select("table.border4").first();
			String pageString = page.text();
			pageString = pageString.substring(pageString.indexOf("第 1 页 共")+7,pageString.indexOf("第 1 页 共")+9);
			int pageSize = Integer.valueOf(pageString.trim());
			//System.out.println(pageSize);
			for(int i=1;i<=pageSize;i++){
				Document docpage = Jsoup.connect("http://eq.ah.gov.cn/ny_content/?zlm=6&ty=221&page="+i)
									.header("User-Agent", Constants.HEAD)
									.timeout(Constants.TIMEOUT).get();
				Elements elements = docpage.select(".font_link");
				for (Element element : elements) {
					String href = element.attr("href");
					String title = element.text();
					if(href.indexOf("include")!=-1){
						String ssource="";
						String sauthor = "";
						String stime="";
						String content="";
						String html = "";
						href = href.substring(2, href.length());
						String url = "http://eq.ah.gov.cn"+href;
						if(urlSet.contains(url)){
							continue;
						}
						Document docli = Jsoup.connect(url)
											.header("User-Agent", Constants.HEAD)
											.timeout(Constants.TIMEOUT).get();
						//System.out.println(title);
						Element table = docli.select("table").get(13);
						Elements tds= table.select("td");
						for (Element td : tds) {
							if(td.attr("bgcolor").equals("#E7E7E7")){
								String time = td.text();
								stime = time.substring(time.indexOf("发布时间")+5, time.indexOf("发布时间")+15);
								System.out.println(stime);
								ssource= time.substring(time.indexOf("来源")+3, time.indexOf("作者")).trim();
								sauthor = time.substring(time.indexOf("作者")+3, time.length());
								//System.out.println(td.text());
							}
						}
						Element scontent = docli.getElementById("zoom");
						content = scontent.text();
						html = scontent.html();
						LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.anhui);
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
						
						System.out.println(++count);
					}
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			for(int i=156;i<=158;i++){
			Document  docpage = Jsoup.connect("http://eq.ah.gov.cn/ny_content/?zlm=6&ty="+i)
									.header("User-Agent", Constants.HEAD)
									.timeout(Constants.TIMEOUT).get();//预案体系建设与应急演练
				Elements elements = docpage.select(".font_link");
				for (Element element : elements) {
					String href = element.attr("href");
					String title = element.text();
					if(href.indexOf("include")!=-1){
						String ssource="";
						String sauthor = "";
						String stime="";
						String content="";
						href = href.substring(2, href.length());
						String url = "http://eq.ah.gov.cn"+href;
						if(urlSet.contains(url)){
							continue;
						}
						Document docli = Jsoup.connect(url)
											.header("User-Agent", Constants.HEAD)
											.timeout(Constants.TIMEOUT).get();
						//System.out.println(title);
						Element table = docli.select("table").get(13);
						Elements tds= table.select("td");
						for (Element td : tds) {
							if(td.attr("bgcolor").equals("#E7E7E7")){
								String time = td.text();
								stime = time.substring(time.indexOf("发布时间")+5, time.indexOf("发布时间")+15);
								ssource= time.substring(time.indexOf("来源")+3, time.indexOf("作者")).trim();
								sauthor = time.substring(time.indexOf("作者")+3, time.length());
							}
						}
						Element scontent = docli.getElementById("zoom");
						content = scontent.text();
						String html = scontent.html();
						LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.anhui);
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
						/*Elements imgs = scontent.select("img");
						for (Element img : imgs) {
							String src = img.attr("src");
							img.attr("src", "http://eq.ah.gov.cn"+src);
						}*/
						//System.out.println(scontent.html());
						System.out.println(++count);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*String url = "http://eq.ah.gov.cn/ny_content/?zlm=6&ty=159";
		if(!urlSet.contains(url)){
			try {
				Document  docpage = Jsoup.connect(url)
										.header("User-Agent", Constants.HEAD)
										.timeout(Constants.TIMEOUT).get();//地震事件应对
					Element element = docpage.select("table.border4").first();
					String ssource="";
					String sauthor = "";
					String stime="";
					String content="";
					Elements imgs = element.select("img");
					for (Element img : imgs) {
						String src = img.attr("src");
						img.attr("src", "http://eq.ah.gov.cn"+src);
					}
					content = element.text();
					String html = element.html();
					LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
					linksWithBLOBs.setWebId(Constants.anhui);
				    linksWithBLOBs.setTitle("地震事件应对");
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
//					System.out.println(element.html());
					
					System.out.println(++count);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		
		linksService.insertLinksWithBLOBsList(list);
		return "redirect:/";
	}
}
