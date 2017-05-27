package com.superx.spider.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.superx.spider.entity.WebSite;
import com.superx.spider.service.WebSiteService;
import com.superx.spider.util.JxlUtil;


@Controller
public class WebSiteController {

	@Autowired
	private WebSiteService webSiteService;
	
	@RequestMapping("")
    public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model,WebSite record) {
		List<WebSite> websiteList = webSiteService.selectWebsiteList(record);
		model.addAttribute("websiteList", websiteList);
        return "websitelist";
    }
	
	@RequestMapping("/add")
    public String add(HttpServletRequest request, 
    		HttpServletResponse response,Model model,WebSite record) {
		List<WebSite> webSiteList = JxlUtil.getWebSiteList();
		for (WebSite webSite : webSiteList) {
			webSiteService.insertSelective(webSite);
		}
        return "";
    }
}
