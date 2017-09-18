package com.superx.spider.controller.spider;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
public class DizhenjuController {

	@Autowired
	private LinksService linksService;
	
	private static final String standTime = "[2017-06-30 23:59:59]";
	
	@RequestMapping("/spider/dizhenju")
	public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model){
		System.out.println("URI=dizhenju " +" "+new Date());
		/**http://www.cea.gov.cn/publish/dizhenj/124/196/index.html图片新闻
		http://www.cea.gov.cn/publish/dizhenj/124/197/index.html 部门动态
		http://www.cea.gov.cn/publish/dizhenj/124/198/index.html 业务动态
		http://www.cea.gov.cn/publish/dizhenj/124/199/index.html 应急救援科技
		http://www.cea.gov.cn/publish/dizhenj/124/201/index.html 灾评通告
		http://www.cea.gov.cn/publish/dizhenj/124/202/203/index.html 应急响应
		http://www.cea.gov.cn/publish/dizhenj/466/498/index.html 我要订阅地震速报信息
		http://www.cea.gov.cn/publish/dizhenj/124/202/206/index.html 应急处置行动
		http://www.cea.gov.cn/publish/dizhenj/124/207/208/index.html 紧急救援
		http://www.cea.gov.cn/publish/dizhenj/124/207/209/index.html 救援行动
		http://www.cea.gov.cn/publish/dizhenj/124/207/210/index.html 救援装备
		http://www.cea.gov.cn/publish/dizhenj/124/215/216/index.html 应急预案和演练
		http://www.cea.gov.cn/publish/dizhenj/124/215/218/index.html 规范文件
		http://www.cea.gov.cn/publish/dizhenj/124/219/index.html 国际救援
		http://www.cea.gov.cn/publish/dizhenj/124/220/index.html 救援链接
		http://www.cea.gov.cn/publish/dizhenj/124/1051/211/212/index.html 现场应急队伍
		http://www.cea.gov.cn/publish/dizhenj/124/1051/211/213/index.html 紧急救援队伍
		http://www.cea.gov.cn/publish/dizhenj/124/1051/211/214/index.html 志愿者
		http://www.cea.gov.cn/publish/dizhenj/124/1051/200/index.html 应急避险场所
		**/
		String[] columnCount = {"124/196","124/197","124/198","124/199","124/201",
				"124/202/203","466/498","124/202/206","124/207/208","124/207/209","124/207/210"
				,"124/215/216","124/215/218","124/219","124/220","124/1051/211/212","124/1051/211/213"
				,"124/1051/211/214","124/1051/200"};
		int count = 0;
		Set<String> urlSet = linksService.selectLinksListByWebId(Constants.dizhenju);
		//List<LinksWithBLOBs> list = new ArrayList<LinksWithBLOBs>(); 
		for (String urlcolumn : columnCount) {
			try {
				Document doc = Jsoup.connect("http://www.cea.gov.cn/publish/dizhenj/"+urlcolumn+"/index.html")
									.header("User-Agent", Constants.HEAD)		
									.timeout(Constants.TIMEOUT)
									.get();
				Element page = doc.select("span.page_right").first();
				String pages = page.text();
				String pagesizes = pages.substring(
						pages.indexOf("共", pages.indexOf("共")+1)+1,
						pages.indexOf("页", pages.indexOf("页")+1));
				//System.out.println(pagesizes);
				int pageSize = Integer.valueOf(pagesizes);
				for(int i=1;i<=pageSize;i++){
					Document docpage ;
					if(i==1){
						docpage = Jsoup.connect("http://www.cea.gov.cn/publish/dizhenj/"+urlcolumn+"/index.html")
									   .header("User-Agent", Constants.HEAD)	
									   .timeout(Constants.TIMEOUT).get();
					}else{
						docpage = Jsoup.connect("http://www.cea.gov.cn/publish/dizhenj/"+urlcolumn+"/index_"+i+".html")
									   .header("User-Agent", Constants.HEAD)	
								       .timeout(Constants.TIMEOUT).get();
					}
					urlSet = linksService.selectLinksListByWebId(Constants.dizhenju);
					Elements lis = docpage.select("div.list_main_right_conbg_con ul a");
					for (Element element : lis) {
						String spanttime = element.siblingElements().text();
						if(!(spanttime.compareTo(standTime)>0)){
							continue;// 比较时间
						}
						String url = "http://www.cea.gov.cn"+element.attr("href");
					    if(urlSet.contains(url)){
					    	continue;
					    }
						String title = element.text();
						Document docdetail = Jsoup.connect("http://www.cea.gov.cn"+element.attr("href"))
												  .timeout(50000).get();
						Element telementtitle = docdetail.select("div.detail_main_right_conbg_tit").first();
						String time = telementtitle.text();
//						System.out.println(time);
						String ssource;
						ssource = time.substring(time.indexOf("信息来源")+5,time.length()).trim();
						if(!ssource.equals("震灾应急救援司")){
							continue;
						}
						String stime = time.substring(time.indexOf("发布时间")+5,time.indexOf("信息来源")).trim();
						Element telementcontent = docdetail.select("div.detail_main_right_conbg_con").first();//内容
						Elements imgs = telementcontent.select("img");
						  for (Element img : imgs) {
							 String src = img.attr("src");
							 img.attr("src", "http://www.cea.gov.cn"+src);
//							 System.out.println(img.attr("src"));
						  }
						String html = telementcontent.html();
						LinksWithBLOBs linksWithBLOBs = new LinksWithBLOBs();
						linksWithBLOBs.setWebId(Constants.dizhenju);
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
						//list.add(linksWithBLOBs);
						int result = linksService.insertSelective(linksWithBLOBs);
						//System.out.println(result+"   result");
						System.out.println(++count+" -  ");
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//linksService.insertLinksWithBLOBsList(list);
		System.out.println("dizhenju end");
		return "redirect:/";
	}
}
