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
public class TaiwangController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/taiwang")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI=taiwang "+request.getRequestURI());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.taiwang);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try {
			Document doc = Jsoup.connect("http://www.cenc.ac.cn/publish/cenc/911/index.html")
								.header("User-Agent", Constants.HEAD)		
								.timeout(Constants.TIMEOUT)
								.get();
			Element page = doc.select("div.page").first();
			String pageStringAll = page.text().split("共")[2];
//			System.out.println(pageStringAll);
			
			String pageString  = pageStringAll.substring(0, pageStringAll.length()-1);
			int pageSize = Integer.valueOf(pageString);
			System.out.println(pageSize);
			for(int i=1;i<=pageSize;i++){
				Document docpage ;
				if(i==1){
					docpage = Jsoup.connect("http://www.cenc.ac.cn/publish/cenc/911/index.html")
									.header("User-Agent", Constants.HEAD)		
									.timeout(Constants.TIMEOUT)
									.get();
				}else{
					docpage = Jsoup.connect("http://www.cenc.ac.cn/publish/cenc/911/index_"+i+".html")
									.header("User-Agent", Constants.HEAD)		
									.timeout(Constants.TIMEOUT)
									.get();
				}
				Elements lis = docpage.select("div.listmain_l_rcon li a");
				
				for (Element element : lis) {
					String title = element.text();
					String url = "http://www.cenc.ac.cn"+element.attr("href");
					if(urlSet.contains(url)){
						continue;
					}
					Document docdetail = Jsoup.connect(url)
												.header("User-Agent", Constants.HEAD)		
												.timeout(Constants.TIMEOUT)
												.get();
					Element telement = docdetail.select("div.detailmain_l>div").get(2);
					String time = telement.text();
					//System.out.println(time);
					String ssource;
					ssource = time.substring(time.indexOf("信息来源")+5,time.length()).trim();
					String stime = time.substring(time.indexOf("发布时间")+5,time.indexOf("信息来源")).trim();
					Element content =docdetail.select("div.detailmain_lcon").first();
					Elements imgs = content.select("img");
					  for (Element img : imgs) {
						 String src = img.attr("src");
						 img.attr("src", "http://www.cenc.ac.cn"+src);
						 //System.out.println(img.attr("src"));
					  }
					String html = content.html();
					LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
					linksWithBLOBs.setWebId(Constants.taiwang);
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		linksService.insertLinksWithBLOBsList(list);
		return "redirect:/";
	}
}
