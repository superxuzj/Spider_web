package com.superx.spider.repository;

import java.util.List;

import com.superx.spider.entity.WebSite;

public interface WebSiteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WebSite record);

    int insertSelective(WebSite record);

    WebSite selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WebSite record);

    int updateByPrimaryKey(WebSite record);
    
    List<WebSite> selectWebsiteList(WebSite record);
}