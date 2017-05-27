package com.superx.spider.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superx.spider.entity.WebSite;
import com.superx.spider.repository.WebSiteMapper;

@Service
public class WebSiteService {

	@Autowired
	private WebSiteMapper webSiteMapper;
	
	public List<WebSite> selectWebsiteList(WebSite record){
		List<WebSite> list = webSiteMapper.selectWebsiteList(record);
		
		/*for (WebSite webSite : list) {
			System.out.println("@Autowired");
			String code = webSite.getCode();
			code=  code.substring(0, 1).toUpperCase() + code.substring(1);
			System.out.println("private "+code+"Controller "+webSite.getCode()+"Controller;");
			System.out.println("");
		}*/
		
		/*for (WebSite webSite : list) {
			System.out.println(webSite.getName()+":"+webSite.getCode());
		}*/
		return list;
	}
	
	public WebSite selectByPrimaryKey(Integer id){
		return webSiteMapper.selectByPrimaryKey(id);
	}
	
	public int insertSelective(WebSite record){
		return webSiteMapper.insertSelective(record);
	}
}
