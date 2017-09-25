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
public class JiangxiController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/jiangxi")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI=jiangxi " +" "+new Date());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.jiangxi);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		
        try { //设置超时时间，同时Document对象中封装了返回的html文档
        	  String csdnUrl="http://www.jxsdzj.gov.cn/html/zhuanlan/yingjijiuyuan/index.html";
              Connection conn = Jsoup.connect(csdnUrl);//获取连接
              //设置请求头，伪装成浏览器(否则会报403)
              conn.header("User-Agent", Constants.HEAD);
              Document doc=conn.timeout(Constants.TIMEOUT).get();
              Element option = doc.getElementById("totalpage");
              int pageSize = 0;
              pageSize = Integer.valueOf(option.text().trim());
             // System.out.println(options.size());
              for(int i = 0 ; i<=pageSize-1;i++){
            	  int page = pageSize-i;
            	  Connection  connPage;
            	  if(page ==pageSize ){
            		  connPage = Jsoup.connect("http://www.jxsdzj.gov.cn/html/zhuanlan/yingjijiuyuan/index.html");
            	  }else{
            		  connPage = Jsoup.connect("http://www.jxsdzj.gov.cn/html/zhuanlan/yingjijiuyuan/index_"+page+".html");
            	  }
            	  
            	  Document docPage=connPage.header("User-Agent", Constants.HEAD)
            			  					.timeout(Constants.TIMEOUT).get();
            	  Elements links = docPage.select("a");
                  for(Element link : links){
                	  String linkHref = link.attr("href");
                	  String linkText = link.text();//标题
                	  if(linkHref.indexOf("http://www.jxdz.gov.cn/html/zhuanlan/yingjijiuyuan/")!=-1
                			  &&linkHref.length()>59){
                		 //System.out.println(linkHref);
                		  String url = linkHref;
                		  if(urlSet.contains(url)){
                			  continue;
  							}
                		  Connection  connDetail = Jsoup.connect(url);
                    	  Document docDetail=connDetail.header("User-Agent", Constants.HEAD)
                    			  					.timeout(Constants.TIMEOUT).get();
                    	  Element trTime =  docDetail.select("td.left").get(0);//时间
                    	  String sTime = trTime.text();
                          String ssource = sTime.substring(sTime.indexOf("来源")+3, sTime.indexOf("作者")).trim();
                          String sauthor = sTime.substring(sTime.indexOf("作者")+3, sTime.indexOf("浏览次数")).trim();
                          String stime = sTime.substring(sTime.indexOf("时间")+4, sTime.indexOf("来源")).trim();
                          stime = stime.substring(0, stime.indexOf(" "));
                    	  Element content =  docDetail.select("td.ArticleContent").get(0);//内容
                    	  Elements imgs = content.select("img");
    					  /*for (Element img : imgs) {
    						 String src = img.attr("src");
    						 System.out.println(src);
    						 //img.attr("src", "http://www.eqha.gov.cn"+src);
    						 //System.out.println(img.attr("src"));
    					  }*/
                    	  String html = content.html();
                    	LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
  						linksWithBLOBs.setWebId(Constants.jiangxi);
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
  						System.out.println((++count));
                	  }
                	  
                  }
              }
              System.out.println(new Date());
  
      } catch (IOException e) {
          e.printStackTrace();
      }
		
        linksService.insertLinksWithBLOBsList(list);
		return "redirect:/";
	}
}
