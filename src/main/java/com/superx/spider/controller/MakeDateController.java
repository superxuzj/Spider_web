package com.superx.spider.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.superx.spider.entity.InfoWithBLOBs;
import com.superx.spider.service.InfoService;
import com.superx.spider.util.TextUtil;


@Controller
public class MakeDateController {

	@Autowired
	private InfoService infoService;
	
	@RequestMapping("/makeDate")
    public String index(HttpServletRequest request, 
    		HttpServletResponse response,Model model,InfoWithBLOBs info) {
		//List<WebSite> websiteList = webSiteService.selectWebsiteList(record);
		//model.addAttribute("websiteList", websiteList);
		String filename="E:\\make.doc";
		List<InfoWithBLOBs> list = infoService.selectInfoWithBLOBsListByMakeDate(info);
		for (InfoWithBLOBs infoWithBLOBs : list) {
			String content = infoWithBLOBs.getContent();
			
			String text = Jsoup.parse(content.replaceAll("(?i)<br[^>]*>", "br2nl").replaceAll("\n", "br2nl")).text();
			 text = text.replaceAll("br2nl ", "\n").replaceAll("br2nl", "\n").trim();
			 TextUtil.addToText(filename,infoWithBLOBs.getTitle());
			 TextUtil.addToText(filename,text);
			 TextUtil.addToText(filename,"（信息来源："+infoWithBLOBs.getProvince()+"）");
			 TextUtil.addToText(filename,"");
			/*
			Document ducument = Jsoup.parse(content);
			String text = ducument.text();*/
			System.out.println(text);
		}
        return "makeDate";
    }
	
}
