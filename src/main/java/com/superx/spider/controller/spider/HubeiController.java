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

 */
@Controller
public class HubeiController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/hubei")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI= hubei" +" "+new Date());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.hubei);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		
		try { //设置超时时间，同时Document对象中封装了返回的html文档
      	  //应急救援规制建设
      	  String csdnUrl="http://www.eqhb.gov.cn/list_news2.jsp?urltype=tree.TreeTempUrl&wbtreeid=1061";
            Connection conn = Jsoup.connect(csdnUrl);//获取连接
            //设置请求头，伪装成浏览器(否则会报403)
            conn.header("User-Agent", Constants.HEAD);
            Document doc=conn.timeout(Constants.TIMEOUT).get();
            Element page = doc.select("table").first();
            String allString = page.text();
            String pageString = allString.substring(allString.indexOf("/")+1,allString.indexOf("/")+2);
            Integer pagesize = Integer.valueOf(pageString);
            for(int i = 1 ; i<=pagesize;i++){
          	  Connection  connPage;
      		  connPage = Jsoup.connect("http://www.eqhb.gov.cn/list_news2.jsp?a1052t=3&a1052p="+i
      				  +"&a1052c=15&urltype=tree.TreeTempUrl&wbtreeid=1061");
          	  Document docPage=connPage.header("User-Agent", Constants.HEAD).timeout(Constants.TIMEOUT).get();
          	  Elements links = docPage.select("ul.gover_right_ul li a");
                for(Element link : links){
              	  String linkHref = link.attr("href");
              	  String linkText = link.text();
              	//  System.out.println(linkHref+linkText+(++count));
              	  String url = "http://www.eqhb.gov.cn"+linkHref;
              	  if(urlSet.contains(url)){
              		  continue;
              	  }
              	  Connection  connDetail = Jsoup.connect(url);
              	  Document docDetail=connDetail.header("User-Agent", Constants.HEAD).timeout(Constants.TIMEOUT).get();
              	  Element content =docDetail.select("div.politics_info_box").first();//neirong 
              	  String time = docDetail.select("div.politics_info_em").first().text();
              	  String stime = time.substring(time.indexOf("发布时间")+5, time.indexOf("信息来源"));
              	  String ssource = time.substring(time.indexOf("信息来源")+5, time.indexOf("作者"));
              	  String sauthor = time.substring(time.indexOf("作者")+3, time.indexOf("浏览次数"));
              	  /*Elements imgs = content.select("img");
					  for (Element img : imgs) {没有
						 String src = img.attr("src");
						 img.attr("src", "http://www.eqhb.gov.cn"+src);
						 System.out.println(img.attr("src"));
					  }*/
              	  String html = content.html();
	              	LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
					linksWithBLOBs.setWebId(Constants.hubei);
				    linksWithBLOBs.setTitle(linkText);
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
              	  System.out.println(++count);
                }
            }
    } catch (IOException e) {
        e.printStackTrace();
    }
      
      try { //设置超时时间，同时Document对象中封装了返回的html文档
    	  //应急救援科技
    	  String csdnUrl="http://www.eqhb.gov.cn/list_news2.jsp?urltype=tree.TreeTempUrl&wbtreeid=1064";
          Connection conn = Jsoup.connect(csdnUrl);//获取连接
          //设置请求头，伪装成浏览器(否则会报403)
          conn.header("User-Agent", Constants.HEAD);
          Document doc=conn.timeout(Constants.TIMEOUT).get();
          Element page = doc.select("table").first();
          String allString = page.text();
          String pageString = allString.substring(allString.indexOf("/")+1,allString.indexOf("/")+2);
          Integer pagesize = Integer.valueOf(pageString);
          for(int i = 1 ; i<=pagesize;i++){
        	  Connection  connPage;
    		  connPage = Jsoup.connect("http://www.eqhb.gov.cn/list_news2.jsp?a1052t=2&a1052p="+i
    				  +"&a1052c=15&urltype=tree.TreeTempUrl&wbtreeid=1064");
        	  Document docPage=connPage.header("User-Agent", Constants.HEAD).timeout(Constants.TIMEOUT).get();
        	  Elements links = docPage.select("ul.gover_right_ul li a");
              for(Element link : links){
            	  String linkHref = link.attr("href");
            	  String linkText = link.text();
            	  String url ="http://www.eqhb.gov.cn"+linkHref;
              	  if(urlSet.contains(url)){
              		  continue;
              	  }
            	//  System.out.println(linkHref+linkText+(++count));
            	  Connection  connDetail = Jsoup.connect(url);
            	  Document docDetail=connDetail.header("User-Agent", Constants.HEAD).timeout(Constants.TIMEOUT).get();
            	  Element content =docDetail.select("div.politics_info_box").first();//neirong 
            	  String time = docDetail.select("div.politics_info_em").first().text();
            	  String stime = time.substring(time.indexOf("发布时间")+5, time.indexOf("信息来源"));
            	  String ssource = time.substring(time.indexOf("信息来源")+5, time.indexOf("作者"));
            	  String sauthor = time.substring(time.indexOf("作者")+3, time.indexOf("浏览次数"));
            	  Elements imgs = content.select("img");
					  for (Element img : imgs) {
						 String src = img.attr("src");
						 img.attr("src", "http://www.eqhb.gov.cn"+src.substring(5, src.length()));
						// System.out.println(img.attr("src"));
					  }
            	  String html = content.html();
            	  	LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
					linksWithBLOBs.setWebId(Constants.hubei);
				    linksWithBLOBs.setTitle(linkText);
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
            	  System.out.println(++count);
              }
          }
  } catch (IOException e) {
      e.printStackTrace();
  }
      
     try { //设置超时时间，同时Document对象中封装了返回的html文档
      	  // 应急处置
      	  String[] ids = {"1252","1253","1254","1255","1256","1257","1386"};
            for(int i = 0 ; i<=ids.length-1;i++){
          	  Connection  connPage;
      		  connPage = Jsoup.connect("http://www.eqhb.gov.cn/list_news2.jsp?urltype=tree.TreeTempUrl&wbtreeid="+ids[i]);
          	  Document docPage=connPage.header("User-Agent", Constants.HEAD).timeout(Constants.TIMEOUT).get();
          	  Elements links = docPage.select("ul.gover_right_ul li a");
                for(Element link : links){
              	  String linkHref = link.attr("href");
              	  String linkText = link.text();
              	  String url ="http://www.eqhb.gov.cn"+linkHref;
              	  if(urlSet.contains(url)){
            		  continue;
            	  }
              	//  System.out.println(linkHref+linkText+(++count));
              	  Connection  connDetail = Jsoup.connect(url);
              	  Document docDetail=connDetail.header("User-Agent", Constants.HEAD).timeout(Constants.TIMEOUT).get();
              	  Element content =docDetail.select("div.politics_info_box").first();//neirong 
              	  String time = docDetail.select("div.politics_info_em").first().text();
//              	  System.out.println(time);
              	  String stime = time.substring(time.indexOf("发布时间")+5, time.indexOf("信息来源"));
              	  String ssource = time.substring(time.indexOf("信息来源")+5, time.indexOf("作者"));
              	  String sauthor = time.substring(time.indexOf("作者")+3, time.indexOf("浏览次数"));
              	  Elements imgs = content.select("img");
					  for (Element img : imgs) {
						 String src = img.attr("src");
						 img.attr("src", "http://www.eqhb.gov.cn"+src.substring(5, src.length()));
//						 System.out.println(img.attr("src"));
					  }
              	  String html = content.html();
	              	LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
					linksWithBLOBs.setWebId(Constants.hubei);
				    linksWithBLOBs.setTitle(linkText);
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
              	  System.out.println(++count);
                }
            }
    } catch (IOException e) {
        e.printStackTrace();
    }
      
      try { //设置超时时间，同时Document对象中封装了返回的html文档
    	  //应急救援科技
    	  String[] ids = {"1062","1063"};
    	  System.out.println(ids.length);
          for(int i = 0 ; i<=ids.length-1;i++){
        	  Connection  connPage;
    		  connPage = Jsoup.connect("http://www.eqhb.gov.cn/list_news2.jsp?urltype=tree.TreeTempUrl&wbtreeid="+ids[i]);
        	  Document docPage=connPage.header("User-Agent", Constants.HEAD).timeout(Constants.TIMEOUT).get();
        	  Elements links = docPage.select("ul.gover_right_ul li a");
              for(Element link : links){
            	  String linkHref = link.attr("href");
            	  String linkText = link.text();
            	  String url ="http://www.eqhb.gov.cn"+linkHref;
              	  if(urlSet.contains(url)){
            		  continue;
            	  }
            	//  System.out.println(linkHref+linkText+(++count));
            	  Connection  connDetail = Jsoup.connect(url);
            	  Document docDetail=connDetail.header("User-Agent", Constants.HEAD).timeout(Constants.TIMEOUT).get();
            	  Element content =docDetail.select("div.politics_info_box").first();//neirong 
            	  String time = docDetail.select("div.politics_info_em").first().text();
            	  String stime = time.substring(time.indexOf("发布时间")+5, time.indexOf("信息来源"));
            	  String ssource = time.substring(time.indexOf("信息来源")+5, time.indexOf("作者"));
            	  String sauthor = time.substring(time.indexOf("作者")+3, time.indexOf("浏览次数"));
            	  Elements imgs = content.select("img");
					  for (Element img : imgs) {
						 String src = img.attr("src");
						 img.attr("src", "http://www.eqhb.gov.cn"+src.substring(5, src.length()));
						 //System.out.println(img.attr("src"));
					  }
            	  String html = content.html();
            	  	LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
					linksWithBLOBs.setWebId(Constants.hubei);
				    linksWithBLOBs.setTitle(linkText);
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
            	  System.out.println(++count);
              }
          }
  } catch (IOException e) {
      e.printStackTrace();
  }
      linksService.insertLinksWithBLOBsList(list);
		return "redirect:/";
	}
}
