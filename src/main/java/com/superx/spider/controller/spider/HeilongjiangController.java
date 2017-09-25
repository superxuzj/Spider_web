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
public class HeilongjiangController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/heilongjiang")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI=heilongjiang " +" "+new Date());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.heilongjiang);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try {
			Document doc = Jsoup.connect("http://www.hea.gov.cn/xwzx/fzjzyw/index.html")
								.header("User-Agent", Constants.HEAD)		
								.timeout(Constants.TIMEOUT)
								.get();
			Element page = doc.select("div.listmain").first();
			String pageStringAll = page.text();
			String pageString  = pageStringAll.substring(pageStringAll.indexOf("/共")+2, pageStringAll.indexOf("页 ["));
			int pageSize = Integer.valueOf(pageString.trim());
			for(int i=0;i<=pageSize-1;i++){
				Document docpage ;
				if(i==0){
					docpage = Jsoup.connect("http://www.hea.gov.cn/xwzx/fzjzyw/index.html")
									.header("User-Agent", Constants.HEAD)		
									.timeout(Constants.TIMEOUT)
									.get();
				}else if(i<10 &&i >0){
					docpage = Jsoup.connect("http://www.hea.gov.cn/system/more/xwzx/fzjzyw/index/page_0"+i+".html")
									.header("User-Agent", Constants.HEAD)		
									.timeout(Constants.TIMEOUT)
									.get();
				}else{
					docpage = Jsoup.connect("http://www.hea.gov.cn/system/more/xwzx/fzjzyw/index/page_"+i+".html")
									.header("User-Agent", Constants.HEAD)		
									.timeout(Constants.TIMEOUT)
									.get();
				}
				Elements lis = docpage.select("div.listmain>ul>li  a");
				
				for (Element element : lis) {
					String title = element.text();
					if(title.indexOf("应急")!=-1 || title.indexOf("救援")!=-1){
						String url = "http://www.hea.gov.cn"+element.attr("href");
						if(urlSet.contains(url)){
							continue;
						}
						Document docdetail = Jsoup.connect(url)
												  .header("User-Agent", Constants.HEAD)		
												  .timeout(Constants.TIMEOUT)
												  .get();
						Element telement = docdetail.select("div.maintittwo").first();
						String time = telement.text().trim();
						//System.out.println(time);
						String ssource = time.substring(time.indexOf("来源")+3,time.length()).trim();
						String stime = time.substring(time.indexOf("时间")+4,time.indexOf("时间")+14).trim();
						System.out.println(stime);
						Element content =docdetail.select("div.mainnews").first();
						Elements imgs = content.select("img");
						for (Element img : imgs) {
							String src = img.attr("src");
							if(src.indexOf("http")==-1){
								img.attr("src", "http://www.hea.gov.cn"+src);
							}
							//System.out.println(img.attr("src"));
						}
						String html = content.html();
						LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.heilongjiang);
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
