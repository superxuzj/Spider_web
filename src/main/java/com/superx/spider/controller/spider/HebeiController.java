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
public class HebeiController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/hebei")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI=hebei "+request.getRequestURI());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.hebei);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		
        try { //设置超时时间，同时Document对象中封装了返回的html文档
        	  String csdnUrl="http://www.hbdzj.gov.cn/eqinfocms/ywgz/yjjymore.jsp";
              Connection conn = Jsoup.connect(csdnUrl);//获取连接
              //设置请求头，伪装成浏览器(否则会报403)
              conn.header("User-Agent", Constants.HEAD);
              Document doc=conn.timeout(Constants.TIMEOUT).get();
              
              Element pagea = doc.select("div.page > a").get(1);
              String pagesizeString = pagea.attr("href").substring(pagea.attr("href").length()-1, pagea.attr("href").length());
              int pagesize = Integer.parseInt(pagesizeString);
              for(int i = 1 ; i<=pagesize;i++){
            	  Connection  connPage = Jsoup.connect("http://www.hbdzj.gov.cn/eqinfocms/ywgz/yjjymore.jsp?pageNo="+i);
            	  connPage.header("User-Agent", Constants.HEAD);
            	  Document docPage=connPage.timeout(Constants.TIMEOUT).get();
            	  Elements lis = docPage.select("ul.list > li >a");
            	  for(int j = 0 ;j<lis.size() ;j++){
            		  
            		  Element li = lis.get(j);
            		  String title = li.text();
            		  String href = li.attr("href");
            		  href = href.substring(1, href.length());
            		  String url = "http://www.hbdzj.gov.cn/eqinfocms/ywgz"+href;
            		  if(urlSet.contains(url)){
            				continue;
            			 }
            		  Connection  connDetail = Jsoup.connect(url);
            		  connDetail.header("User-Agent", Constants.HEAD);
                	  Document docDetail=connDetail.timeout(Constants.TIMEOUT).get();
                	  Element timeAndSource = docDetail.select("div.right > .msg > .content").get(0);
                	  String time = timeAndSource.text();
                	  String stime = time.substring(time.indexOf("发布时间")+5, time.indexOf("来源")).trim();
                	  String ssource = time.substring(time.indexOf("来源")+3, time.length());
                	  Element content = docDetail.select("div.right > .msg > .content").get(1);
                	  Elements imgs = content.select("img");
						for (Element img : imgs) {
							String src = img.attr("src");
							img.attr("src", "http://www.hbdzj.gov.cn"+src);
//							System.out.println(img.attr("src"));
						}
					  String html = content.html();
					  
					    LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.hebei);//天津
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
