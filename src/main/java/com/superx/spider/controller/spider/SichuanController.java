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
public class SichuanController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/sichuan")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI=sichuan "+request.getRequestURI());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.sichuan);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try {
			Document doc = Jsoup.connect("http://www.scdzj.gov.cn/xwzx/bmdt/")
								.header("User-Agent", Constants.HEAD)
								.timeout(Constants.TIMEOUT)
								.get();
			
			Element page = doc.select("div.p_next script").first();
			String pageString = page.html();
			int pageSize = Integer.valueOf(pageString.substring(page.text().indexOf("createPageHTML")+16,pageString.indexOf(",")));
			for(int i=0;i<=pageSize-1;i++){
				Document docpage ;
				if(i==0){
					docpage = Jsoup.connect("http://www.scdzj.gov.cn/xwzx/bmdt/index.html")
									.header("User-Agent", Constants.HEAD)
									.timeout(Constants.TIMEOUT).get();
				}else{
					docpage = Jsoup.connect("http://www.scdzj.gov.cn/xwzx/bmdt/index_"+i+".html")
									.header("User-Agent", Constants.HEAD)
									.timeout(Constants.TIMEOUT).get();
				}
				Elements lis = docpage.select("div.p_right ul li a");
				for (Element element : lis) {
					String title = element.text();
					if(title.indexOf("应急")!=-1 || title.indexOf("救援")!=-1){
						String url = "http://www.scdzj.gov.cn/xwzx/bmdt/"+element.attr("href").substring(1, element.attr("href").length());
						if(urlSet.contains(url)){
							continue;
						}
						Document docdetail = Jsoup.connect(url)
													.header("User-Agent", Constants.HEAD)				 
													.timeout(Constants.TIMEOUT).get();
						String time = docdetail.getElementById("title").html();
						//System.out.println(time);
						String ssource = time.substring(time.indexOf("sourcecontent")+16,time.indexOf("sourcecontent")+28).trim().replace("？", "");
						String stime = time.substring(time.indexOf("时间")+3,time.indexOf("时间")+13);
						//System.out.println(ssource+" "+element.attr("href").substring(1, element.attr("href").length())+"--"+stime);
						Element content = docdetail.getElementById("content");
						Elements imgs = content.select("img");
						  for (Element img : imgs) {
							 String src = img.attr("src");
							 String hreftemp = element.attr("href");
							 hreftemp = hreftemp.substring(1, 8);
							 String allSrc = "http://www.scdzj.gov.cn/xwzx/bmdt"+hreftemp+src.substring(1, src.length());
							 img.attr("src", allSrc);
							// System.out.println(img.attr("src"));
						  }
						String html = content.html();
						LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.sichuan);
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
