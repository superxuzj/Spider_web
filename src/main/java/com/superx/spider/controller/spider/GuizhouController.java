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
public class GuizhouController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/guizhou")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI=guizhou "+request.getRequestURI());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.guizhou);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try {
			Document doc = Jsoup.connect("http://www.gzsdzj.gov.cn/Part/page/symbol/meet.htm")
								.header("User-Agent", Constants.HEAD)
								.timeout(Constants.TIMEOUT)
								.get();
			
			Element page = doc.select("div.page").first();
			String pageStringAll = page.text();
			String pageString  = pageStringAll.substring(pageStringAll.indexOf("/")+1, pageStringAll.indexOf("页"));
			int pageSize = Integer.valueOf(pageString.trim());
			for(int i=1;i<=pageSize;i++){
				Document docpage ;
				docpage = Jsoup.connect("http://www.gzsdzj.gov.cn/Part/page/symbol/meet/p/"+i)
								.header("User-Agent", Constants.HEAD)
							   .timeout(Constants.TIMEOUT).get();
				
				Elements lis = docpage.select("div.list_ls_li ul li a");
				
				for (Element element : lis) {
					String title = element.text();
						String url = "http://www.gzsdzj.gov.cn"+element.attr("href");
						if(urlSet.contains(url)){
							continue;
						}
						Document docdetail = Jsoup.connect(url)
												  .header("User-Agent", Constants.HEAD)
												  .timeout(Constants.TIMEOUT)
												  .get();
						Element telement = docdetail.select("div.view_tr").first();
						String time = telement.text();
						time = time.replace("；", "");
						//System.out.println(time);
						String ssource;
						String sauthor="";
						if(time.indexOf("作者")!=-1){
							ssource = time.substring(time.indexOf("来源")+3,time.indexOf("作者")).trim().replace("\u00A0","");
							sauthor = time.substring(time.indexOf("作者")+3,time.indexOf("发布时间")).trim().replace("\u00A0","");
						}else{
							ssource = time.substring(time.indexOf("来源")+3,time.indexOf("发布")).trim().replace("\u00A0","");
						}
						
						String stime = time.substring(time.indexOf("时间")+3,time.length()).trim();
						
						Element content =docdetail.select("div.view_html").first();
						Elements imgs = content.select("img");
						for (Element img : imgs) {
							String src = img.attr("src");
							img.attr("src", "http://www.gzsdzj.gov.cn"+src);
							//System.out.println(img.attr("src"));
						}
						//System.out.println(ssource+"--"+title+"--"+stime+"sauthor" +sauthor);
						String html = content.html();
						LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.guizhou);
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
