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
public class HenanController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/henan")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI= henan " +" "+new Date());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.henan);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		
		try { //设置超时时间，同时Document对象中封装了返回的html文档
      	    String csdnUrl="http://www.eqha.gov.cn/WebPub/html/ff808181126bebda01126bec4dd00001/yjjy/index.html";
            Connection conn = Jsoup.connect(csdnUrl);//获取连接
             //设置请求头，伪装成浏览器(否则会报403)
            conn.header("User-Agent", Constants.HEAD);
            Document doc=conn.timeout(Constants.TIMEOUT).get();
            Element options = doc.select("span a").get(3);
            int pagesize = Integer.valueOf(options.attr("href").substring(6, 7));
//            System.out.println(pagesize);
            for(int i = 1 ; i<=pagesize;i++){
          	  Connection  connPage;
          	  if(i ==1 ){
          		  connPage = Jsoup.connect("http://www.eqha.gov.cn/WebPub/html/ff808181126bebda01126bec4dd00001/yjjy/index.html");
          	  }else{
          		  connPage = Jsoup.connect("http://www.eqha.gov.cn/WebPub/html/ff808181126bebda01126bec4dd00001/yjjy/index_"+i+".html");
          	  }
          	  Document docPage=connPage.header("User-Agent", Constants.HEAD)
          			  					.timeout(Constants.TIMEOUT).get();
          	  Elements links = docPage.select("a.STYLE2");
                for(Element link : links){
              	  String linkHref = link.attr("href");
	              if(urlSet.contains(linkHref)){
	          		  continue;
	          	  }
              	  String linkText = link.text();
              	//  System.out.println(linkHref+linkText+(++count));
              	  Connection  connDetail = Jsoup.connect(linkHref);
              	  Document docDetail=connDetail.header("User-Agent", Constants.HEAD)
              			  						.timeout(Constants.TIMEOUT).get();
              	  Element content =docDetail.select("table.TableFont").first();
              	  
              	  Elements imgs = content.select("img");
					  for (Element img : imgs) {
						 String src = img.attr("src");
						 img.attr("src", "http://www.eqha.gov.cn"+src);
						// System.out.println(img.attr("src"));
					  }
              	  
              	  String htmls = docDetail.html();
              	  String stime = htmls.substring(htmls.indexOf("<!--starttime-->")+16, htmls.indexOf("<!--starttime-->")+26);
              	  String ssource = htmls.substring(htmls.indexOf("<!--startlaiyuan-->")+19, htmls.indexOf("<!--endlaiyuan-->"));
              	  String html = content.html();
	              	LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
					linksWithBLOBs.setWebId(Constants.henan);
				    linksWithBLOBs.setTitle(linkText);
					linksWithBLOBs.setLink(linkHref);
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
