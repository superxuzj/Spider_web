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
public class QinghaiController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/qinghai")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI= qinghai " +" "+new Date());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.qinghai);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try {
			Document doc = Jsoup.connect("http://www.qhdzj.gov.cn/3430372.html")
								.header("User-Agent", Constants.HEAD)
								.timeout(Constants.TIMEOUT)
								.get();
			Element page = doc.select("div.navjz").first();
			String pageStringAll = page.html();
			
			String pageString  = pageStringAll.substring(pageStringAll.indexOf("pageCount")+10, pageStringAll.indexOf("pageCount")+12);
			int pageSize = Integer.valueOf(pageString);
			//System.out.println(pageSize);
			for(int i=1;i<=pageSize;i++){
				Document docpage ;
				if(i==1){
					docpage = Jsoup.connect("http://www.qhdzj.gov.cn/content/column/3430372?pageIndex="+i)
									.header("User-Agent", Constants.HEAD)
									.timeout(Constants.TIMEOUT)
									.get();
				}else{
					docpage = Jsoup.connect("http://www.qhdzj.gov.cn/content/column/3430372?pageIndex="+i)
									.header("User-Agent", Constants.HEAD)
									.timeout(Constants.TIMEOUT)
									.get();
				}
				Elements lis = docpage.select("a.title");
				
				for (Element element : lis) {
					String title = element.text();
					String url = element.attr("href");
					if(urlSet.contains(url)){
						continue;
					}
					Document docdetail = Jsoup.connect(url)
												.header("User-Agent", Constants.HEAD)
												.timeout(Constants.TIMEOUT)
												.get();
					Element telement = docdetail.select("div.wzbjxx").first();
					String time = telement.text();
//					System.out.println(time);
					String ssource  = time.substring(time.indexOf("来源")+3,time.indexOf("阅读")).trim();
					String stime = time.substring(time.indexOf("发布日期")+5,time.indexOf("作者")).trim();
					String sauthor = time.substring(time.indexOf("作者")+3,time.indexOf("来源")).trim();
					Element content =docdetail.getElementById("J_content");
					Elements imgs = content.select("img");
					  for (Element img : imgs) {
						 String src = img.attr("src");
						 img.attr("src", "http://www.qhdzj.gov.cn"+src);
						 //System.out.println(img.attr("src"));
					  }
					String html = content.html();
					//String sauthor="";
					LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
					linksWithBLOBs.setWebId(Constants.qinghai);
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
					System.out.println(++count+" -  72");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Document doc = Jsoup.connect("http://www.qhdzj.gov.cn/3430376.html")
								.header("User-Agent", Constants.HEAD)
								.timeout(Constants.TIMEOUT)
								.get();
			Element page = doc.select("div.navjz").first();
			String pageStringAll = page.html();
			
			String pageString  = pageStringAll.substring(pageStringAll.indexOf("pageCount")+10, pageStringAll.indexOf("pageCount")+12);
			int pageSize = Integer.valueOf(pageString);
			for(int i=1;i<=pageSize;i++){
				Document docpage ;
				if(i==1){
					docpage = Jsoup.connect("http://www.qhdzj.gov.cn/content/column/3430376?pageIndex="+i)
									.header("User-Agent", Constants.HEAD)
									.timeout(Constants.TIMEOUT)
									.get();
				}else{
					docpage = Jsoup.connect("http://www.qhdzj.gov.cn/content/column/3430376?pageIndex="+i)
									.header("User-Agent", Constants.HEAD)
									.timeout(Constants.TIMEOUT)
									.get();
				}
				
				Elements lis = docpage.select("a.title");
				
				for (Element element : lis) {
					String title = element.text();
					String url = element.attr("href");
					if(urlSet.contains(url)){
						continue;
					}
					Document docdetail = Jsoup.connect(url)
											  .header("User-Agent", Constants.HEAD)
											  .timeout(Constants.TIMEOUT)
											  .get();
					Element telement = docdetail.select("div.wzbjxx").first();
					String time = telement.text();
//					System.out.println(time);
					String ssource;
					ssource = time.substring(time.indexOf("来源")+3,time.indexOf("阅读")).trim();
					String stime = time.substring(time.indexOf("发布日期")+5,time.indexOf("来源")).trim();
					Element content =docdetail.getElementById("J_content");
					Elements imgs = content.select("img");
					  for (Element img : imgs) {
						 String src = img.attr("src");
						 img.attr("src", "http://www.qhdzj.gov.cn"+src);
						//System.out.println(img.attr("src"));
					  }
					String html = content.html();
					String sauthor="";
					LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
					linksWithBLOBs.setWebId(Constants.qinghai);
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
					System.out.println(++count+"");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Document doc = Jsoup.connect("http://www.qhdzj.gov.cn/3430380.html")
								.header("User-Agent", Constants.HEAD)
								.timeout(Constants.TIMEOUT)
								.get();
			Elements lis = doc.select("a.title");
			
			for (Element element : lis) {
				String title = element.text();
				String url = element.attr("href");
				if(urlSet.contains(url)){
					continue;
				}
				Document docdetail = Jsoup.connect(url)
											.header("User-Agent", Constants.HEAD)
											.timeout(Constants.TIMEOUT)
											.get();
				Element telement = docdetail.select("div.wzbjxx").first();
				String time = telement.text();
				String ssource;
				ssource = time.substring(time.indexOf("来源")+3,time.indexOf("阅读")).trim();
				String stime = time.substring(time.indexOf("发布日期")+5,time.indexOf("来源")).trim();
				Element content =docdetail.getElementById("J_content");
				Elements imgs = content.select("img");
				  for (Element img : imgs) {
					 String src = img.attr("src");
					 img.attr("src", "http://www.qhdzj.gov.cn"+src);
					 //System.out.println(img.attr("src"));
				  }
				String html = content.html();
				String sauthor="";
				LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
				linksWithBLOBs.setWebId(Constants.qinghai);
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
				System.out.println(++count+" -  80");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		linksService.insertLinksWithBLOBsList(list);
		return "redirect:/";
	}
}
