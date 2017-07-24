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
public class YingjisoujiuController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/yingjisoujiu")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI=yingjisoujiu " +" "+new Date());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.yingjisoujiu);
		//List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try {
			Document doc = Jsoup.connect("http://www.nerss.cn/list.php?fid=84")
								.header("User-Agent", Constants.HEAD)		
								.timeout(Constants.TIMEOUT)
								.get();
			Element page = doc.select("div.page").first();
			String pageStringAll = page.text();
			String pageString  = pageStringAll.split("/")[1];
			int pageSize = Integer.valueOf(pageString.trim());
			//System.out.println(pageSize);
			for(int i=1;i<=pageSize;i++){
				Document docpage ;
					docpage = Jsoup.connect("http://www.nerss.cn/list.php?fid=84&page="+i)
									.header("User-Agent", Constants.HEAD)		
									.timeout(Constants.TIMEOUT)
									.get();
				Elements lis = docpage.select("td.middle>table td span a");
				urlSet = linksService.selectLinksListByWebId(Constants.yingjisoujiu);
				for (Element element : lis) {
					    String url = "http://www.nerss.cn/"+element.attr("href");
					    if(urlSet.contains(url)){
					    	continue;
					    }
						Document docdetail = Jsoup.connect(url)
												  .header("User-Agent", Constants.HEAD)		
												  .timeout(Constants.TIMEOUT)
												  .get();
						Element telement = docdetail.select("div.top_about").first();
						Element titleElement = docdetail.select("div.main_title").first();
						String title = titleElement.text();
						String time = telement.text();
//						System.out.println(time);
						String ssource;
						ssource = time.substring(time.indexOf("来源")+3,time.indexOf("作者")).trim();
						String sauthor = time.substring(time.indexOf("作者")+3,time.indexOf("【")).trim();
						String stime = time.substring(0,10);
						Element content =docdetail.getElementById("read_tpc");
						Elements imgs = content.select("img");
						  for (Element img : imgs) {
							 String src = img.attr("src");
							 if(src.indexOf("http")==-1){
								 src = "http://www.nerss.cn/"+src;
							 }
							 img.attr("src",src);
							//System.out.println(img.attr("src"));
						  }
						String html = content.html();
						LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.yingjisoujiu);
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
						//list.add(linksWithBLOBs);
						int result = linksService.insertSelective(linksWithBLOBs);
						System.out.println(result+"   result");
						System.out.println(++count+" -  ");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//linksService.insertLinksWithBLOBsList(list);
		return "redirect:/";
	}
}
