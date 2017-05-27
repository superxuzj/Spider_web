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

@Controller
public class JilinController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/jilin")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI= jilin "+request.getRequestURI());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.jilin);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try { //设置超时时间，同时Document对象中封装了返回的html文档
      	    String csdnUrl="http://www.jldzj.gov.cn/yjgzdt/";//工作动态
            Connection conn = Jsoup.connect(csdnUrl);//获取连接
            //设置请求头，伪装成浏览器(否则会报403)
            conn.header("User-Agent", Constants.HEAD);
            Document doc=conn.timeout(Constants.TIMEOUT).get();
            Elements pagea = doc.select("div.pg-3 select option");
            int pagesize = pagea.size()-4;
            for(int i = 1 ; i<=pagesize;i++){
          	  Connection  connPage;
          	  if(i==1){
          		  connPage = Jsoup.connect("http://www.jldzj.gov.cn/yjgzdt/index.html");
          	  }else{
          		  connPage = Jsoup.connect("http://www.jldzj.gov.cn/yjgzdt/index_"+i+".html");
          	  }
          	  
          	  Document docPage=connPage.header("User-Agent", Constants.HEAD)
          			  					.timeout(Constants.TIMEOUT).get();
          	  Elements links = docPage.select("td.fontbl a");
          	  for(int n =0 ;n<links.size();n=n+2){
          		  Element li = links.get(n);
          		  String title = li.text();
          		  Document docdetail ;
					  String ssource="";
					  String stime;
					  String contents;
					  String url = "http://www.jldzj.gov.cn"+li.attr("href");
					  if(urlSet.contains(url)){
							continue;
					  }
					  docdetail = Jsoup.connect(url)
							  			.header("User-Agent", Constants.HEAD)
							  		   .timeout(Constants.TIMEOUT).get();
					  String time = docdetail.select("div.msgbar").first().text();
					  String sauthor = time.substring(time.indexOf("作者")+3,time.indexOf("来源")).trim();
					  stime = time.substring(time.indexOf("发布时间")+5,time.indexOf("作者")).trim();
					  Element content = docdetail.select("div.newsCon").first();
					  Elements imgs = content.select("img");
					  for (Element img : imgs) {
						 String src = img.attr("src");
						 if(src!=""|| !src.equals(""))
							 img.attr("src", "http://www.jldzj.gov.cn"+src);
					  }
					  String html = content.html();
					  	LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.jilin);
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
					 // System.out.println(sauthor+"  "+stime);
          		  System.out.println(count++);
          	  }
                
            }
      

    } catch (IOException e) {
        e.printStackTrace();
    }
      
      try { //设置超时时间，同时Document对象中封装了返回的html文档
    	  String csdnUrl="http://www.jldzj.gov.cn/yjya/";//应急预案
          Connection conn = Jsoup.connect(csdnUrl);//获取连接
          //设置请求头，伪装成浏览器(否则会报403)
          conn.header("User-Agent", Constants.HEAD);
          Document doc=conn.timeout(Constants.TIMEOUT).get();
        
        	  Elements links = doc.select("td.fontbl a");
        	  for(int n =0 ;n<links.size();n=n+2){
        		  Element li = links.get(n);
        		  String title = li.text();
        		  Document docdetail ;
					  String ssource="";
					  String stime;
					  String contents;
					  String url = "http://www.jldzj.gov.cn"+li.attr("href");
					  if(urlSet.contains(url)){
							continue;
					  }
					  docdetail = Jsoup.connect(url)
							  			.header("User-Agent", Constants.HEAD)
							  		   .timeout(Constants.TIMEOUT).get();
					  String time = docdetail.select("div.msgbar").first().text();
					  String sauthor = time.substring(time.indexOf("作者")+3,time.indexOf("来源")).trim();
					  stime = time.substring(time.indexOf("发布时间")+5,time.indexOf("作者")).trim();
					  Element content = docdetail.select("div.newsCon").first();
					  Elements imgs = content.select("img");
					  for (Element img : imgs) {
						 String src = img.attr("src");
						 if(src!=""|| !src.equals(""))
							 img.attr("src", "http://www.jldzj.gov.cn"+src);
//						 System.out.println(img.attr("src"));
					  }
					  String html = content.html();
					  LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.jilin);
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
					 // System.out.println(sauthor+"  "+stime);
        		  System.out.println(count++);
        	  }
              
  } catch (IOException e) {
      e.printStackTrace();
  }
      
      try { //设置超时时间，同时Document对象中封装了返回的html文档
      	  String csdnUrl="http://www.jldzj.gov.cn/yjcz/";//应急预案
            Connection conn = Jsoup.connect(csdnUrl);//获取连接
            //设置请求头，伪装成浏览器(否则会报403)
            conn.header("User-Agent", Constants.HEAD);
            Document doc=conn.timeout(Constants.TIMEOUT).get();
          
          	  Elements links = doc.select("td.fontbl a");
          	  for(int n =0 ;n<links.size();n=n+2){
          		  Element li = links.get(n);
          		  String title = li.text();
          		  Document docdetail ;
					  String ssource="";
					  String stime;
					  String contents;
					  String url = "http://www.jldzj.gov.cn"+li.attr("href");
					  if(urlSet.contains(url)){
							continue;
					  }
					  docdetail = Jsoup.connect(url)
							  			.header("User-Agent", Constants.HEAD)
							  		   .timeout(Constants.TIMEOUT).get();
					  String time = docdetail.select("div.msgbar").first().text();
					  String sauthor = time.substring(time.indexOf("作者")+3,time.indexOf("来源")).trim();
					  stime = time.substring(time.indexOf("发布时间")+5,time.indexOf("作者")).trim();
					  Element content = docdetail.select("div.newsCon").first();
					  Elements imgs = content.select("img");
					  for (Element img : imgs) {
						 String src = img.attr("src");
						 if(src!=""|| !src.equals(""))
							 img.attr("src", "http://www.jldzj.gov.cn"+src);
						 System.out.println(img.attr("src"));
					  }
					  
					  String html = content.html();
					  LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.jilin);
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
					 // System.out.println(sauthor+"  "+stime);
          		  System.out.println(count++);
          	  }
                
    } catch (IOException e) {
        e.printStackTrace();
    }
		
      linksService.insertLinksWithBLOBsList(list);
		return "redirect:/";
	}
}
