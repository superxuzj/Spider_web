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
public class XinjiangController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/xinjiang")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI=xinjiang " +" "+new Date());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.xinjiang);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try {
			Document doc = Jsoup.connect("http://www.xjdzj.gov.cn/info/iList.jsp?cat_id=10017")
								.header("User-Agent", Constants.HEAD)		
								.timeout(Constants.TIMEOUT)
								.get();
			Element page = doc.select("div.page_list").first();
			String pageStringAll = page.text();
			String pageString  = pageStringAll.substring(pageStringAll.indexOf("/")+1, pageStringAll.indexOf("页"));
			int pageSize = Integer.valueOf(pageString);
			for(int i=1;i<=pageSize;i++){
				Document docpage ;
				docpage = Jsoup.connect("http://www.xjdzj.gov.cn/info/iList.jsp?cat_id=10017&cur_page="+i)
								.header("User-Agent", Constants.HEAD)		
								.timeout(Constants.TIMEOUT)
								.get();
				Elements lis = docpage.select("div.page-news td a");
				
				for (Element element : lis) {
					String title = element.text();
						String url = "http://www.xjdzj.gov.cn"+element.attr("href");
						if(urlSet.contains(url)){
							continue;
						}
						Document docdetail = Jsoup.connect(url)
												.header("User-Agent", Constants.HEAD)		
												.timeout(Constants.TIMEOUT)
												.get();
						String ssource;
						ssource = docdetail.getElementById("info_source").text();
						String sauthor;
						sauthor = docdetail.getElementById("info_author").text();
						String stime = docdetail.getElementById("info_released_dtime").text();
						Element content =docdetail.getElementById("info_content");
						/*Elements imgs = content.select("img"); 图片链接不用更改
						  for (Element img : imgs) {
							 String src = img.attr("src");
							 System.out.println(src);
							 //img.attr("src", "http://www.eqha.gov.cn"+src);
							 //System.out.println(img.attr("src"));
						  }*/
						String html = content.html();
						LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.xinjiang);
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
		linksService.insertLinksWithBLOBsList(list);
		return "redirect:/";
	}
}
