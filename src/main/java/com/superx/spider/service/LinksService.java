package com.superx.spider.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superx.spider.entity.Links;
import com.superx.spider.entity.LinksWithBLOBs;
import com.superx.spider.repository.LinksMapper;
/**
 * 从网上爬取数据存到t_links表中
 * @author xuzj
 *
 */
@Service
public class LinksService {

	@Autowired
	private LinksMapper linksMapper;
	
	public int insertSelective(LinksWithBLOBs record){
		return linksMapper.insertSelective(record);
	}
	
	public int insertLinksWithBLOBsList(List<LinksWithBLOBs> list){
		if(null!=list && list.size()>0){
			for (LinksWithBLOBs linksWithBLOBs : list) {
				linksMapper.insertSelective(linksWithBLOBs);
			}
			System.out.println(list.size());
			return list.size();
		}else{
			return 0;
		}
	}
	
	public Set<String> selectLinksListByWebId(Integer webId){
		Set<String> set=new HashSet<String>();
		Links record = new Links();
		record.setWebId(webId);
		List<Links> linksList = linksMapper.selectLinksList(record);
		for (Links links : linksList) {
			set.add(links.getLink());
		}
		return set;
	}
}
