package com.superx.spider.repository;

import java.util.List;

import com.superx.spider.entity.Links;
import com.superx.spider.entity.LinksWithBLOBs;

public interface LinksMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LinksWithBLOBs record);

    int insertSelective(LinksWithBLOBs record);

    LinksWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LinksWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(LinksWithBLOBs record);

    int updateByPrimaryKey(Links record);
    
    List<Links> selectLinksList(Links record);
    
    List<LinksWithBLOBs> selectLinksWithBLOBsList(Links record);
}