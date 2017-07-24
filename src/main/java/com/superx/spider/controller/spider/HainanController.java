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
public class HainanController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/hainan")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI= hainan " +" "+new Date());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.hainan);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		try { //设置超时时间，同时Document对象中封装了返回的html文档
	          String csdnUrl="http://dzj.hainan.gov.cn/yjjy/gzdt/";//工作动态
	          Connection conn = Jsoup.connect(csdnUrl);//获取连接
	          //设置请求头，伪装成浏览器(否则会报403)
	          conn.header("User-Agent", Constants.HEAD);
	          Document doc=conn.timeout(Constants.TIMEOUT).get();
	          Element pagea = doc.select("dt.ny_my ul li script").first();
	          String allString = pagea.html();
	          allString= allString .substring(15, allString.indexOf(","));
	          //System.out.println(allString);
	          int pagesize = Integer.parseInt(allString);
	          for(int i = 0 ; i<=pagesize-1;i++){
	        	  Connection  connPage ;
	        	  if(i==0){
	        		  connPage = Jsoup.connect("http://dzj.hainan.gov.cn/yjjy/gzdt/index.html");
	        	  }else{
	        		  connPage = Jsoup.connect("http://dzj.hainan.gov.cn/yjjy/gzdt/index_"+i+".html"); 
	        	  }
	        	  Document docPage=connPage.header("User-Agent", Constants.HEAD)
	        			  					.timeout(Constants.TIMEOUT).get();
	        	  
	        	  Elements lis = docPage.select("dt.ny_news ul li a");
	        	  
	        	  urlSet = linksService.selectLinksListByWebId(Constants.hainan);
	        	  for (Element element : lis) {
	        		  String href = element.attr("href");
	        		  String title = element.text();
	        		  Document docdetail ;
						String ssource;
						String stime="";
						String content;
						String url = "http://dzj.hainan.gov.cn"+href.substring(5, href.length());
						if(urlSet.contains(url)){
							continue;
						}
						docdetail = Jsoup.connect(url)
										.header("User-Agent", Constants.HEAD)
										.timeout(Constants.TIMEOUT).get();
						Element telement = docdetail.select("div.ny_ly").first();
						String time = telement.html();
						//System.out.println(time);
						ssource = time.substring(time.indexOf("laiyuan")+11,time.indexOf("if")).trim().replace("\u00A0","");
						String timeString = telement.text().trim().replace("\u00A0","");
						stime = timeString.substring(5, timeString.length());
						Element contente = docdetail.select("div.TRS_Editor").first();
						if(contente==null){
							contente = docdetail.select("div.ny_wz").first();
						}
						Elements imgs = contente.select("img");
						for (Element img : imgs) {
							String src = img.attr("src");
							String hreftemp = href;
							hreftemp = hreftemp.substring(5, hreftemp.lastIndexOf("/"));
							String allSrc = "http://dzj.hainan.gov.cn"+hreftemp+src.substring(1, src.length());
							img.attr("src", allSrc);
						}
						content = contente.text();
						String html = contente.html();
						String sauthor="";
						LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.hainan);
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
						//list.add(linksWithBLOBs);
						int result = linksService.insertSelective(linksWithBLOBs);
						System.out.println(result+"   result");
						System.out.println(++count);
	        	  }
	          }
	      } catch (IOException e) {
	          e.printStackTrace();
	      }
	        
	        try { //设置超时时间，同时Document对象中封装了返回的html文档
	            String csdnUrl="http://dzj.hainan.gov.cn/yjjy/dzyjya/";//地震应急预案
	            Connection conn = Jsoup.connect(csdnUrl);//获取连接
	            //设置请求头，伪装成浏览器(否则会报403)
	            conn.header("User-Agent", Constants.HEAD);
	            Document doc=conn.timeout(Constants.TIMEOUT).get();
	      	  Elements lis = doc.select("dt.ny_news ul li a");
	      	urlSet = linksService.selectLinksListByWebId(Constants.hainan);
	      	  for (Element element : lis) {
	      		  String href = element.attr("href");
	      		  String title = element.text();
	      		  Document docdetail ;
					String ssource;
					String stime="";
					String content;
					String url;
					url ="http://dzj.hainan.gov.cn/yjjy/dzyjya"+href.substring(1, href.length());
					if(url.indexOf("..")!=-1){
						url ="http://dzj.hainan.gov.cn"+href.substring(5, href.length());
					}
					if(urlSet.contains(url)){
						continue;
					}
					docdetail = Jsoup.connect(url)
									.header("User-Agent", Constants.HEAD)
									.timeout(Constants.TIMEOUT).get();
					Element telement = docdetail.select("div.ny_ly").first();
					String time = telement.html();
					//System.out.println(time);
					ssource = time.substring(time.indexOf("laiyuan")+11,time.indexOf("if")).trim().replace("\u00A0","");
					String timeString = telement.text().trim().replace("\u00A0","");
					stime = timeString.substring(5, timeString.length());
					Element contente = docdetail.select("div.TRS_Editor").first();
					if(contente==null){
						contente = docdetail.select("div.ny_wz").first();
					}
					/*Elements imgs = contente.select("img");meiyou 
					for (Element img : imgs) {
						String src = img.attr("src");
						String hreftemp = href;
						hreftemp = hreftemp.substring(5, hreftemp.lastIndexOf("/"));
						String allSrc = "http://dzj.hainan.gov.cn"+hreftemp+src.substring(1, src.length());
						img.attr("src", allSrc);
					}*/
					content = contente.text();
					String html = contente.html();
					String sauthor="";
					LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
					linksWithBLOBs.setWebId(Constants.hainan);
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
					System.out.println(++count);
	      	  }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        try { //设置超时时间，同时Document对象中封装了返回的html文档
	            String csdnUrl="http://dzj.hainan.gov.cn/yjjy/dzyjjydw/";//救援队伍
	            Connection conn = Jsoup.connect(csdnUrl);//获取连接
	            //设置请求头，伪装成浏览器(否则会报403)
	            conn.header("User-Agent", Constants.HEAD);
	            Document doc=conn.timeout(Constants.TIMEOUT).get();
	      	  Elements lis = doc.select("dt.ny_news ul li a");
	      	  urlSet = linksService.selectLinksListByWebId(Constants.hainan);
	      	  for (Element element : lis) {
	      		  String href = element.attr("href");
	      		  String title = element.text();
	      		  Document docdetail ;
					String ssource;
					String stime="";
					String content;
					String url ="http://dzj.hainan.gov.cn"+href.substring(5, href.length());
					if(urlSet.contains(url)){
						continue;
					}
					docdetail = Jsoup.connect(url)
									.header("User-Agent", Constants.HEAD)
									.timeout(Constants.TIMEOUT).get();
					Element telement = docdetail.select("div.ny_ly").first();
					String time = telement.html();
					//System.out.println(time);
					ssource = time.substring(time.indexOf("laiyuan")+11,time.indexOf("if")).trim().replace("\u00A0","");
					String timeString = telement.text().trim().replace("\u00A0","");
					stime = timeString.substring(5, timeString.length());
					Element contente = docdetail.select("div.TRS_Editor").first();
					if(contente==null){
						contente = docdetail.select("div.ny_wz").first();
					}
					Elements imgs = contente.select("img"); 
					for (Element img : imgs) {
						String src = img.attr("src");
						String hreftemp = href;
						hreftemp = hreftemp.substring(5, hreftemp.lastIndexOf("/"));
						String allSrc = "http://dzj.hainan.gov.cn"+hreftemp+src.substring(1, src.length());
						img.attr("src", allSrc);
					}
					content = contente.text();
					String html = contente.html();
					String sauthor="";
					LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
					linksWithBLOBs.setWebId(Constants.hainan);
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
					System.out.println(++count);
	      	  }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
	        try { //设置超时时间，同时Document对象中封装了返回的html文档
	            String csdnUrl="http://dzj.hainan.gov.cn/yjjy/dzbncsjs/";//地震避难场所建设
	            Connection conn = Jsoup.connect(csdnUrl);//获取连接
	            //设置请求头，伪装成浏览器(否则会报403)
	            conn.header("User-Agent", Constants.HEAD);
	            Document doc=conn.timeout(Constants.TIMEOUT).get();
	      	  Elements lis = doc.select("dt.ny_news ul li a");
	      	urlSet = linksService.selectLinksListByWebId(Constants.hainan);
	      	  for (Element element : lis) {
	      		  String href = element.attr("href");
	      		  String title = element.text();
	      		  Document docdetail ;
					String ssource;
					String stime="";
					String content;
					String url ="http://dzj.hainan.gov.cn"+href.substring(5, href.length());
					if(urlSet.contains(url)){
						continue;
					}
					docdetail = Jsoup.connect(url)
									.header("User-Agent", Constants.HEAD)
									.timeout(Constants.TIMEOUT).get();
					Element telement = docdetail.select("div.ny_ly").first();
					String time = telement.html();
					//System.out.println(time);
					ssource = time.substring(time.indexOf("laiyuan")+11,time.indexOf("if")).trim().replace("\u00A0","");
					String timeString = telement.text().trim().replace("\u00A0","");
					stime = timeString.substring(5, timeString.length());
					Element contente = docdetail.select("div.TRS_Editor").first();
					if(contente==null){
						contente = docdetail.select("div.ny_wz").first();
					}
					Elements imgs = contente.select("img"); 
					for (Element img : imgs) {
						String src = img.attr("src");
						String hreftemp = href;
						hreftemp = hreftemp.substring(5, hreftemp.lastIndexOf("/"));
						String allSrc = "http://dzj.hainan.gov.cn"+hreftemp+src.substring(1, src.length());
						img.attr("src", allSrc);
					}
					content = contente.text();
					String html = contente.html();
					String sauthor="";
					LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
					linksWithBLOBs.setWebId(Constants.hainan);
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
					System.out.println(++count);
	      	  }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        linksService.insertLinksWithBLOBsList(list);
			return "redirect:/";
	}
}
