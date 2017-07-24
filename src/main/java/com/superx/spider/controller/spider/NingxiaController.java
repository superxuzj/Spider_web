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
public class NingxiaController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/ningxia")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI=ningxia " +" "+new Date());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.ningxia);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try {
			Document doc = Jsoup.connect("http://www.nxdzj.gov.cn/manage/html/ff808181126bebda01126bec4dd00001/yjjy/index.html")
								.header("User-Agent", Constants.HEAD)		
								.timeout(Constants.TIMEOUT)
								.get();
			
			String pageStringAll = doc.html();
			String pageString  = pageStringAll.substring(pageStringAll.indexOf("下一页")+3, pageStringAll.indexOf("末页"));
			String shortString = pageString.substring(pageString.length()-8, pageString.length()-7);
			int pageSize = Integer.valueOf(shortString);
			for(int i=1;i<=pageSize;i++){
				Document docpage ;
				if(i==1){
					docpage = Jsoup.connect("http://www.nxdzj.gov.cn/manage/html/ff808181126bebda01126bec4dd00001/yjjy/index.html")
								    .header("User-Agent", Constants.HEAD)		
									.timeout(Constants.TIMEOUT)
									.get();
				}else{
					docpage = Jsoup.connect("http://www.nxdzj.gov.cn/manage/html/ff808181126bebda01126bec4dd00001/yjjy/index_"+i+".html")
									.header("User-Agent", Constants.HEAD)		
									.timeout(Constants.TIMEOUT)
									.get();
				}
				
				Elements lis = docpage.select("div.STYLE1 div span a");
				
				for (Element element : lis) {
					String title = element.text();
						String url = "http://www.nxdzj.gov.cn/manage/html/ff808181126bebda01126bec4dd00001"
								+element.attr("href").substring(2, element.attr("href").length());
						if(urlSet.contains(url)){
							continue;
						}
						Document docdetail = Jsoup.connect(url)
												   .header("User-Agent", Constants.HEAD)		
												   .timeout(Constants.TIMEOUT).get();
						Element telement = docdetail.select("div.linkstyle").get(1);
						String time = telement.text();
						String ssource;
						ssource = time.substring(time.indexOf("来源")+3,time.indexOf("作者")).trim();
						String sauthor = time.substring(time.indexOf("作者")+3,time.indexOf("【")).trim();
						String stime = time.substring(0,10).trim();
						Element content =docdetail.getElementById("artical_real");
						Elements imgs = content.select("img");
						  for (Element img : imgs) {
							 String src = img.attr("src");
							 img.attr("src", "http://www.nxdzj.gov.cn"+src);
							 //System.out.println(img.attr("src"));
						  }
						String html = content.html();
						LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.ningxia);
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
