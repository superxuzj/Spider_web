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
public class LiaoningController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/liaoning")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI=liaoning " +" "+new Date());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.liaoning);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try { //设置超时时间，同时Document对象中封装了返回的html文档
      	  String csdnUrl="http://www.lndzj.gov.cn/yjjy1/gzdt/";//工作动态
      	  Connection conn = Jsoup.connect(csdnUrl);//获取连接
            //设置请求头，伪装成浏览器(否则会报403)
            conn.header("User-Agent", Constants.HEAD);
            Document doc=conn.timeout(Constants.TIMEOUT).get();
            Elements pagea = doc.select("div.pagination");
            String allString = pagea.html();
            allString = allString.substring(allString.indexOf("createPageHTML")+15, allString.indexOf("createPageHTML")+16);
            int pagesize = Integer.valueOf(allString);
            for(int i = 0 ; i<=pagesize-1;i++){
          	  Connection  connPage;
          	  if(i==0){
          		  connPage = Jsoup.connect("http://www.lndzj.gov.cn/yjjy1/gzdt/index.html");
          	  }else{
          		  connPage = Jsoup.connect("http://www.lndzj.gov.cn/yjjy1/gzdt/index_"+i+".html");
          	  }
          	  Document docPage=connPage.header("User-Agent", Constants.HEAD)
          			  					.timeout(Constants.TIMEOUT).get();
          	  Elements links = docPage.select("tr.cat-list-row0 td a");
                for(Element link : links){
                  String url = "http://www.lndzj.gov.cn/yjjy1/gzdt"
  							+link.attr("href").substring(1, link.attr("href").length());
                  if(urlSet.contains(url)){
						continue;
					}
              	  Document docdetail = Jsoup.connect(url)
              			                    .header("User-Agent", Constants.HEAD)
              			  					.timeout(Constants.TIMEOUT).get();
              	  String time = docdetail.select("span.published").first().text();
              	 // System.out.println(time);
              	  //String ssource = time.substring(time.indexOf("来源")+3,time.indexOf("发布者")).trim().replace("\u00A0","");
              	  String stime = time.substring(3,time.length()).trim().replace("\u00A0","");
              	  Element content =  docdetail.select("div.TRS_Editor").first();
              	  Elements imgs = content.select("img");
					  for (Element img : imgs) {
						 String src = img.attr("src");
						 String hreftemp = link.attr("href");
						 hreftemp = hreftemp.substring(1, hreftemp.lastIndexOf("/"));
						 String allSrc = "http://www.lndzj.gov.cn/yjjy1/gzdt"+hreftemp+src.substring(1, src.length());
						 img.attr("src", allSrc);
					  }
              	  //String contents = content.text();
              	  String html = content.html();
	              	LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
					linksWithBLOBs.setWebId(Constants.liaoning);
				    linksWithBLOBs.setTitle(link.text());
					linksWithBLOBs.setLink(url);
					linksWithBLOBs.setSource("");
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
    } catch (IOException e) {
        e.printStackTrace();
    }
      
      
      try { //设置超时时间，同时Document对象中封装了返回的html文档
    	    String yuanUrl="http://www.lndzj.gov.cn/yjjy1/yjya/";//应急预案
            Connection yuanconn = Jsoup.connect(yuanUrl);//获取连接
            //设置请求头，伪装成浏览器(否则会报403)
            yuanconn.header("User-Agent",Constants.HEAD);
            Document doc=yuanconn.timeout(Constants.TIMEOUT).get();
            Elements pagea = doc.select("div.pagination");
            String allString = pagea.html();
            allString = allString.substring(allString.indexOf("createPageHTML")+15, allString.indexOf("createPageHTML")+16);
           // System.out.println(allString);
            int pagesize = Integer.valueOf(allString);
            for(int i = 0 ; i<=pagesize-1;i++){
          	  Connection  connPage;
          	  if(i==0){
          		  connPage = Jsoup.connect("http://www.lndzj.gov.cn/yjjy1/yjya/index.html");
          	  }else{
          		  connPage = Jsoup.connect("http://www.lndzj.gov.cn/yjjy1/yjya/index_"+i+".html");
          	  }
          	  Document docPage=connPage.header("User-Agent", Constants.HEAD)
          			  					.timeout(Constants.TIMEOUT).get();
          	  Elements links = docPage.select("tr.cat-list-row0 td a");
                for(Element link : links){
                  String url = "http://www.lndzj.gov.cn/yjjy1/yjya"
  							+link.attr("href").substring(1, link.attr("href").length());
                  if(urlSet.contains(url)){
						continue;
					}
              	  Document docdetail = Jsoup.connect(url)
              			  					.header("User-Agent", Constants.HEAD)
              			  					.timeout(Constants.TIMEOUT).get();
              	  String time = docdetail.select("span.published").first().text();
              	  //String ssource = time.substring(time.indexOf("来源")+3,time.indexOf("发布者")).trim().replace("\u00A0","");
              	  String stime = time.substring(3,time.length()).trim().replace("\u00A0","");
              	  Elements contents = docdetail.select("div.TRS_Editor");
              	  if(contents.size()==0){
              		  contents = docdetail.select("p.MsoNormal");
              	  }
              	  Element content = contents.first();
              	  String html = content.html();
              	LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
				linksWithBLOBs.setWebId(Constants.liaoning);
			    linksWithBLOBs.setTitle(link.text());
				linksWithBLOBs.setLink(url);
				linksWithBLOBs.setSource("");
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
              	  /*Elements imgs = contents.select("img");
					  for (Element img : imgs) {没有图片
						 String src = img.attr("src");
						 System.out.println(src+"   ------");
						 //img.attr("src", "http://www.eqha.gov.cn"+src);
						 //System.out.println(img.attr("src"));
					  }*/
              	  
                }
            }
    } catch (IOException e) {
        e.printStackTrace();
    } 
      
      
      try { //设置超时时间，同时Document对象中封装了返回的html文档\
    	    String jiuyuanUrl="http://www.lndzj.gov.cn/yjjy1/yjjyd/";//救援队伍
            Connection jiuyuanconn = Jsoup.connect(jiuyuanUrl);//获取连接
            //设置请求头，伪装成浏览器(否则会报403)
            jiuyuanconn.header("User-Agent", Constants.HEAD);
            Document doc=jiuyuanconn.timeout(Constants.TIMEOUT).get();
            Elements pagea = doc.select("div.pagination");
            String allString = pagea.html();
            allString = allString.substring(allString.indexOf("createPageHTML")+15, allString.indexOf("createPageHTML")+16);
            System.out.println(allString);
            int pagesize = Integer.valueOf(allString);
            for(int i = 0 ; i<=pagesize-1;i++){
          	  Connection  connPage;
          	  if(i==0){
          		  connPage = Jsoup.connect("http://www.lndzj.gov.cn/yjjy1/yjjyd/index.html");
          	  }else{
          		  connPage = Jsoup.connect("http://www.lndzj.gov.cn/yjjy1/yjjyd/index_"+i+".html");
          	  }
          	  Document docPage=connPage.header("User-Agent", Constants.HEAD)
          			  					.timeout(100000).get();
          	  Elements links = docPage.select("tr.cat-list-row0 td a");
                for(Element link : links){
                  String url = "http://www.lndzj.gov.cn/yjjy1/yjjyd"
  							+link.attr("href").substring(1, link.attr("href").length());
                  if(urlSet.contains(url)){
						continue;
					}
              	  Document docdetail = Jsoup.connect(url)
              			  					.header("User-Agent", Constants.HEAD)
              			  					.timeout(Constants.TIMEOUT).get();
              	  String time = docdetail.select("span.published").first().text();
              	  //String ssource = time.substring(time.indexOf("来源")+3,time.indexOf("发布者")).trim().replace("\u00A0","");
              	  String stime = time.substring(3,time.length()).trim().replace("\u00A0","");
              	  Elements contents = docdetail.select("div.TRS_Editor");
              	  if(null!=contents && null!=contents.first()){
              		  Elements imgs = contents.select("img");
  					  for (Element img : imgs) {
  						 String src = img.attr("src");
							 String hreftemp = link.attr("href");
							 hreftemp = hreftemp.substring(1, hreftemp.lastIndexOf("/"));
							 String allSrc = "http://www.lndzj.gov.cn/yjjy1/yjjyd"+hreftemp+src.substring(1, src.length());
							 img.attr("src", allSrc);
  						 //System.out.println(img.attr("src"));
  					  }
						Element content = contents.first();
//						String scontent = content.text();
						String html = content.html();
						LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.liaoning);
						linksWithBLOBs.setTitle(link.text());
						linksWithBLOBs.setLink(url);
						linksWithBLOBs.setSource("");
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
        e.printStackTrace();
    }
      
      String yanlianUrl="http://www.lndzj.gov.cn/yjjy1/yjbzyyl/";//应急保障与演练
      Connection yanlianconn = Jsoup.connect(yanlianUrl);//获取连接
      //设置请求头，伪装成浏览器(否则会报403)
      yanlianconn.header("User-Agent", Constants.HEAD);
      try { //设置超时时间，同时Document对象中封装了返回的html文档
            Document doc=yanlianconn.timeout(Constants.TIMEOUT).get();
            Elements pagea = doc.select("div.pagination");
            String allString = pagea.html();
            allString = allString.substring(allString.indexOf("createPageHTML")+15, allString.indexOf("createPageHTML")+16);
            //System.out.println(allString);
            int pagesize = Integer.valueOf(allString);
            for(int i = 0 ; i<=pagesize-1;i++){
          	  Connection  connPage;
          	  if(i==0){
          		  connPage = Jsoup.connect("http://www.lndzj.gov.cn/yjjy1/yjbzyyl/index.html");
          	  }else{
          		  connPage = Jsoup.connect("http://www.lndzj.gov.cn/yjjy1/yjbzyyl/index_"+i+".html");
          	  }
          	  Document docPage=connPage.header("User-Agent", Constants.HEAD)
          			  				   .timeout(Constants.TIMEOUT).get();
          	  Elements links = docPage.select("tr.cat-list-row0 td a");
                for(Element link : links){
                  String url = "http://www.lndzj.gov.cn/yjjy1/yjbzyyl"
  							+link.attr("href").substring(1, link.attr("href").length());
                  if(urlSet.contains(url)){
						continue;
					}
              	  Document docdetail = Jsoup.connect(url)
              			  					.header("User-Agent", Constants.HEAD)
              			  					.timeout(Constants.TIMEOUT).get();
              	  String time = docdetail.select("span.published").first().text();
              	  //String ssource = time.substring(time.indexOf("来源")+3,time.indexOf("发布者")).trim().replace("\u00A0","");
              	  String stime = time.substring(3,time.length()).trim().replace("\u00A0","");
              	  Elements contents = docdetail.select("div.TRS_Editor");
              	  if(contents.size()==0){
              		  contents = docdetail.select("p.MsoNormal");
              	  }
              	  if(null!=contents && null!=contents.first()){
              		  Elements imgs = contents.select("img");
  					  for (Element img : imgs) {
  						 String src = img.attr("src");
  						// System.out.println(src);
							 String hreftemp = link.attr("href");
							 hreftemp = hreftemp.substring(1, hreftemp.lastIndexOf("/"));
							 String allSrc = "http://www.lndzj.gov.cn/yjjy1/yjbzyyl"+hreftemp+src.substring(1, src.length());
							 img.attr("src", allSrc);
							// System.out.println(allSrc);
							 
  					  }
  					Element content = contents.first();
//					String scontent = content.text();
					String html = content.html();
					LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
					linksWithBLOBs.setWebId(Constants.liaoning);
					linksWithBLOBs.setTitle(link.text());
					linksWithBLOBs.setLink(url);
					linksWithBLOBs.setSource("");
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
        e.printStackTrace();
    }   
		
      linksService.insertLinksWithBLOBsList(list);
		return "redirect:/";
	}
}
