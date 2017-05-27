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
public class ShandongController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/shandong")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI= shandong "+request.getRequestURI());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.shandong);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		
        try { //设置超时时间，同时Document对象中封装了返回的html文档
        	  String csdnUrl="http://www.sddzj.gov.cn/channel/42/";
              Connection conn = Jsoup.connect(csdnUrl);//获取连接
              //设置请求头，伪装成浏览器(否则会报403)
              conn.header("User-Agent", Constants.HEAD);
              Document doc=conn.timeout(Constants.TIMEOUT).get();
              Elements lists = doc.select("h3");
              for (Element element : lists) {
            	  Element elementa = element.select("a").first();
            	  String title = elementa.text();
            	  /*System.out.println(elementa.text());
            	  System.out.println(elementa.attr("href"));*/
            	  String url = "http://www.sddzj.gov.cn/"+elementa.attr("href");
            	  if(urlSet.contains(url)){
            		  continue;
            	  }
            	  Connection  connDetail = Jsoup.connect(url);
            	  Document docDetail=connDetail.header("User-Agent", Constants.HEAD)
            			  						.timeout(Constants.TIMEOUT).get();
            	  Element timeAndSource = docDetail.select("div[align=center]").first();
            	  
            	  String atimesource = timeAndSource.text();
//            	  System.out.println(atimesource);
            	  String stime = atimesource.substring(atimesource.indexOf("时间")+3, atimesource.indexOf("时间")+13);
            	  String ssource = "";
            	  String sauthor="";
            	  if(atimesource.indexOf("来源")!=-1){
            		  ssource = atimesource.substring(atimesource.indexOf("来源")+3, atimesource.indexOf("字体"));
            		  if(atimesource.indexOf("作者")!=-1){
                		  sauthor = atimesource.substring(atimesource.indexOf("作者")+3, atimesource.indexOf("信息来源")).trim();
                	  }
            	  }
            	  Element content = docDetail.select("td.content").first();
            	  Elements imgs = content.select("img");
				  for (Element img : imgs) {
					 String src = img.attr("src");
					// System.out.println(src);
					 img.attr("src", "http://www.sddzj.gov.cn"+src);
					 //System.out.println(img.attr("src"));
				  }
				  String html = content.html();
				  	LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
					linksWithBLOBs.setWebId(Constants.shandong);
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
            	  System.out.println(++count+" sd");
              }
             
              System.out.println(new Date());
  
      } catch (IOException e) {
          e.printStackTrace();
      }
		
        linksService.insertLinksWithBLOBsList(list);
		return "redirect:/";
	}
}
