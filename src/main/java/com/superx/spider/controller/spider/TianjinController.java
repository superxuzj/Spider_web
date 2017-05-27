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
public class TianjinController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/tianjin")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI=tianjin "+request.getRequestURI());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.tianjin);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		
		try { //设置超时时间，同时Document对象中封装了返回的html文档
      	  	String csdnUrl="http://www.tjdzj.gov.cn/yjjy/";
            Connection conn = Jsoup.connect(csdnUrl);//获取连接
            conn.header("User-Agent", Constants.HEAD);
            Document doc=conn.timeout(Constants.TIMEOUT).get();
            //得到博客列表article_list_3 article_list_2 article_list_1
            Element element=doc.getElementById("list");
            Elements links = element.getElementsByTag("a");
        for(Element link : links){
      	  String linkHref = link.attr("href");
      	  String linkText = link.text();
      	  if(linkHref.indexOf("shtml")!=-1){
      		 // System.out.println(linkHref+" "+linkText);
      		  String url = "http://www.tjdzj.gov.cn"+linkHref;
      		  if(urlSet.contains(url)){
      			  continue;
      		  }
      		  Connection conndetail = Jsoup.connect(url);//获取连接
      		  conndetail.header("User-Agent", Constants.HEAD);
          	  Document docdetail=conndetail.timeout(Constants.TIMEOUT).get();
          	  Element n_from=docdetail.getElementById("n_from");
          	  Element n_author = docdetail.getElementById("n_author");
          	  Element n_time=docdetail.getElementById("n_time");
          	  String ssource = n_from.text().substring(3, n_from.text().length()).trim();
          	  String sauthor = n_author.text().substring(3, n_author.text().length()).trim();
          	  String stime = n_time.text().substring(3, n_time.text().length()).trim();
//          	  System.out.println(ssource+" "+sauthor+" "+stime+" ");
          	  Element content=docdetail.getElementById("content");
          	  Elements imgs = content.select("img");
			  for (Element img : imgs) {
				 String src = img.attr("src");
				 img.attr("src", "http://www.tjdzj.gov.cn"+src);
				// System.out.println(img.attr("src"));
			  }
				String html = content.html();
				LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
				linksWithBLOBs.setWebId(Constants.tianjin);
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
          	  System.out.println(++count+" -  ");
      	  }
      	
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
		linksService.insertLinksWithBLOBsList(list);
		return "redirect:/";
	}
}
