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
public class BeijingController {

	@Autowired
	private LinksService linksService;
	@RequestMapping("/spider/beijing")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI= beijing "+request.getRequestURI());
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.beijing);
		List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>();
		try {
			Connection conn = Jsoup.connect("http://www.bjdzj.gov.cn/bjdzj/zwxx/gzdt/xydt/index.html");//获取连接
            //设置请求头，伪装成浏览器(否则会报403)
            conn.header("User-Agent", Constants.HEAD);
            Document doc=conn.timeout(Constants.TIMEOUT).get();
			Element page = doc.select("div.pagination").first().nextElementSibling();
			int pageSize = Integer.valueOf(page.attr("totalpage"));
			System.out.println(pageSize);
			for(int i=1;i<=pageSize;i++){
				Document docpage ;
				docpage = Jsoup.connect("http://www.bjdzj.gov.cn/bjdzj/zwxx/gzdt/xydt/9c91a19f-"+i+".html")
								.timeout(Constants.TIMEOUT).get();
				
				Elements lis = docpage.select("ul.list_mod li a");
				
				for (Element element : lis) {
					String title = element.text();
					if(title.indexOf("应急")!=-1 || title.indexOf("救援")!=-1){
						Document docdetail ;
						String ssource;
						String stime;
						String content;
						String html;
						String url;
						
						if(element.attr("href").indexOf("http")==-1){
							url = "http://www.bjdzj.gov.cn/"+element.attr("href");
							if(urlSet.contains(url)){
								continue;
							}
							Connection conndetail = Jsoup.connect(url);//获取连接
				      		conndetail.header("User-Agent", Constants.HEAD);
				          	docdetail=conndetail.timeout(Constants.TIMEOUT).get();
							String time = docdetail.select("div.detail_about").first().text();
							ssource = time.substring(time.indexOf("来源")+3,time.indexOf("发布者")).trim().replace("\u00A0","");
							stime = time.substring(time.indexOf("发布时间")+5,time.indexOf("浏览量")).trim().replace("\u00A0","");
							
							Element contentElement =  docdetail.select("div.detail_inner").first();
							/*Elements imgs = contentElement.select("img");没有图片
							for (Element img : imgs) {
								String src = img.attr("src");
								//img.attr("src", "http://www.cea.gov.cn"+src);
							}*/
							
							html = contentElement.html();
							content = contentElement.text().replace("\u00A0","");
						}else{
							url = element.attr("href");
							if(urlSet.contains(url)){
								//System.out.println(url);
								continue;
							}
							Connection conndetail = Jsoup.connect(url);//获取连接
				      		conndetail.header("User-Agent", Constants.HEAD);
				          	docdetail=conndetail.timeout(Constants.TIMEOUT).get();
							String time = docdetail.select("div.detail_main_right_conbg_tit").first().text();
							ssource = time.substring(time.indexOf("信息来源")+5,time.length()).trim().replace("\u00A0","");
							stime = time.substring(time.indexOf("发布时间")+5,time.indexOf("信息来源")).trim().replace("\u00A0","");
							Element contentElement = docdetail.select("div.detail_main_right_conbg_con").first();
							Elements imgs = contentElement.select("img");
							for (Element img : imgs) {
								String src = img.attr("src");
								img.attr("src", "http://www.cea.gov.cn"+src);
							}
							content = contentElement.text().replace("\u00A0","");
							html = docdetail.select("div.detail_main_right_conbg_con").first().html();
						}
						LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.beijing);//北京
						linksWithBLOBs.setTitle(title);
						linksWithBLOBs.setLink(url);
						linksWithBLOBs.setAuthor("");
						linksWithBLOBs.setSource(ssource);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		linksService.insertLinksWithBLOBsList(list);
		return "redirect:/";
	}
}
