package com.superx.spider.controller.spider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Connection;
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
public class FujianController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/fujian")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI=fujian "+request.getRequestURI());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.fujian);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try { //设置超时时间，同时Document对象中封装了返回的html文档   --------应急与救援
        	  String csdnUrl="http://www.fjdzj.gov.cn/NewsList.aspx?ClassID=192&Page=1";
              Connection conn = Jsoup.connect(csdnUrl);//获取连接
              //设置请求头，伪装成浏览器(否则会报403)
              conn.header("User-Agent", Constants.HEAD);
              Document doc=conn.timeout(Constants.TIMEOUT).get();
              Elements options = doc.select("select[name=page] option");
             // System.out.println(options.size());
              int pagesize = options.size();
              for(int i = 1 ; i<=pagesize;i++){
            	  Connection  connPage = Jsoup.connect("http://www.fjdzj.gov.cn/NewsList.aspx?ClassID=192&Page="+i);
            	  Document docPage=connPage.header("User-Agent", Constants.HEAD).timeout(Constants.TIMEOUT).get();
            	  Elements links = docPage.select("a.AMoreByNewsList");
                  for(Element link : links){
                	  String linkHref = link.attr("href");
//                	  String linkText = link.text();
                	  //  System.out.println(linkHref+linkText+(++count));
                	  String url = "http://www.fjdzj.gov.cn/"+linkHref;
                	  if(urlSet.contains(url)){
							continue;
						}
                	  Connection  connDetail = Jsoup.connect(url);
                	  Document docDetail=connDetail.header("User-Agent", Constants.HEAD)
                			  						.timeout(Constants.TIMEOUT).get();
                	  Element table =docDetail.select("table.TableByInByNewsView").first();
                	  Element trTitle =  table.select("tr").get(0);//标题
                	  Element trTime =  table.select("tr").get(1);//时间
                	  Element trContent =  table.select("tr").get(2);//
                	  
                	  String html;
                	  if(trContent.html().contains("【上 一 篇】")){
                		  html = Jsoup.parse(trContent.html().split("【上 一 篇】")[0]).html();
                	  }else{
                		  html = Jsoup.parse(trContent.html().split("【下 一 篇】")[0]).html();
                	  }
                	  	LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.fujian);
					    linksWithBLOBs.setTitle(trTitle.text());
						linksWithBLOBs.setLink(url);
						linksWithBLOBs.setSource("");
						linksWithBLOBs.setAuthor("");
						linksWithBLOBs.setTime(trTime.text().substring(6, 16));
						linksWithBLOBs.setSendcontent(html);
						linksWithBLOBs.setIdent("0");
						linksWithBLOBs.setStatus("0");
						linksWithBLOBs.setCreatorName("superxu");
						linksWithBLOBs.setCreatorTime(new Date());
						list.add(linksWithBLOBs);
						//int result = linksService.insertSelective(linksWithBLOBs);
						//System.out.println(result+"   result");
						System.out.println((++count));
                  }
              }
      } catch (IOException e) {
          e.printStackTrace();
      }
		 try { //设置超时时间，同时Document对象中封装了返回的html文档 ----------日常工作
       	  String csdnUrl="http://www.fjdzj.gov.cn/NewsList.aspx?ClassID=231&Page=1";
             Connection conn = Jsoup.connect(csdnUrl);//获取连接
             //设置请求头，伪装成浏览器(否则会报403)
             conn.header("User-Agent", Constants.HEAD);
             Document doc=conn.timeout(Constants.TIMEOUT).get();
             Elements options = doc.select("select[name=page] option");
            // System.out.println(options.size());
             int pagesize = options.size();
             for(int i = 1 ; i<=pagesize;i++){
           	  Connection  connPage = Jsoup.connect("http://www.fjdzj.gov.cn/NewsList.aspx?ClassID=231&Page="+i);
           	  Document docPage=connPage.header("User-Agent", Constants.HEAD).timeout(Constants.TIMEOUT).get();
           	  Elements links = docPage.select("a.AMoreByNewsList");
                 for(Element link : links){
               	  String linkHref = link.attr("href");
               	  String url = "http://www.fjdzj.gov.cn/"+linkHref;
               	  if(urlSet.contains(url)){
							continue;
						}
               	  Connection  connDetail = Jsoup.connect(url);
               	  Document docDetail=connDetail.header("User-Agent", Constants.HEAD)
               			  						.timeout(Constants.TIMEOUT).get();
               	  Element table =docDetail.select("table.TableByInByNewsView").first();
               	  Element trTitle =  table.select("tr").get(0);//标题
               	  Element trTime =  table.select("tr").get(1);//时间
               	  Element trContent =  table.select("tr").get(2);//
               	  
               	  String html;
               	  if(trContent.html().contains("【上 一 篇】")){
               		  html = Jsoup.parse(trContent.html().split("【上 一 篇】")[0]).html();
               	  }else{
               		  html = Jsoup.parse(trContent.html().split("【下 一 篇】")[0]).html();
               	  }
               	  	LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.fujian);
					    linksWithBLOBs.setTitle(trTitle.text());
						linksWithBLOBs.setLink(url);
						linksWithBLOBs.setSource("");
						linksWithBLOBs.setAuthor("");
						linksWithBLOBs.setTime(trTime.text().substring(6, 16));
						linksWithBLOBs.setSendcontent(html);
						linksWithBLOBs.setIdent("0");
						linksWithBLOBs.setStatus("0");
						linksWithBLOBs.setCreatorName("superxu");
						linksWithBLOBs.setCreatorTime(new Date());
						list.add(linksWithBLOBs);
						//int result = linksService.insertSelective(linksWithBLOBs);
						//System.out.println(result+"   result");
						System.out.println((++count));
               	 // System.out.println((++count));
                 }
             }
     } catch (IOException e) {
         e.printStackTrace();
     }
		 linksService.insertLinksWithBLOBsList(list);
			return "redirect:/";
	}
}
