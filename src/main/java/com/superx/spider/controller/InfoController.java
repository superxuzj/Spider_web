package com.superx.spider.controller;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.superx.spider.entity.InfoWithBLOBs;
import com.superx.spider.entity.Links;
import com.superx.spider.entity.LinksWithBLOBs;
import com.superx.spider.entity.WebSite;
import com.superx.spider.service.InfoService;
import com.superx.spider.service.WebSiteService;

@Controller
public class InfoController {
	@Autowired
	private InfoService infoService;
	
	@Autowired
	private WebSiteService webSiteService;
	
	
	@RequestMapping("info")
    public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model,WebSite record) {
        return "info";
    }
	
	/**
	 * 把t_links表中的数据
	 * @param request
	 * @param response
	 * @param model
	 * @param links
	 * @return
	 */
	@RequestMapping("spider/data")
    public String data(HttpServletRequest request, 
    		HttpServletResponse response,Model model,Links links) {
		int count = 0;
		System.out.println(new Date()+" spider/data");
		Set<String> urlSet = infoService.selectInfolinksList();
		List<LinksWithBLOBs>  list = infoService.selectLinksWithBLOBsList(new Links());//我自己爬出来的数据
		for (LinksWithBLOBs linksWithBLOBs : list) {
			if(urlSet.contains(linksWithBLOBs.getLink())){
				continue;
			}
			WebSite webSite = webSiteService.selectByPrimaryKey(linksWithBLOBs.getWebId());
			InfoWithBLOBs infoWithBLOBs = new InfoWithBLOBs();
			infoWithBLOBs.setProvince(webSite.getName());
			infoWithBLOBs.setProvinceCode(webSite.getCode());
			infoWithBLOBs.setAuthor(linksWithBLOBs.getAuthor());
			infoWithBLOBs.setContent(linksWithBLOBs.getSendcontent());
			infoWithBLOBs.setCreatorName(linksWithBLOBs.getCreatorName());
			infoWithBLOBs.setCreatorTime(new Date());
			infoWithBLOBs.setIdent("0");
			infoWithBLOBs.setLink(linksWithBLOBs.getLink());
			infoWithBLOBs.setSource(linksWithBLOBs.getSource());
			infoWithBLOBs.setStatus("0");
			infoWithBLOBs.setTime(linksWithBLOBs.getTime());
			infoWithBLOBs.setTitle(linksWithBLOBs.getTitle());
			
			infoService.insertSelective(infoWithBLOBs);//数据写到提供接口数据的表中
			System.out.println(count++);
		}
		System.out.println(new Date());
		model.addAttribute("list", list);
        return "info";
    }
}
