package com.superx.spider.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superx.spider.entity.Info;
import com.superx.spider.entity.InfoWithBLOBs;
import com.superx.spider.entity.Links;
import com.superx.spider.entity.LinksWithBLOBs;
import com.superx.spider.repository.InfoMapper;
import com.superx.spider.repository.LinksMapper;
/**
 * 从t_links表中 转移数据到t_info表
 * t_info表只提供接口数据
 * @author xuzj
 *
 */
@Service
public class InfoService {
	@Autowired
	private InfoMapper infoMapper;
	@Autowired
	private LinksMapper linksMapper;
	
	public int insertSelective(InfoWithBLOBs record){
		return infoMapper.insertSelective(record);
	}
	
	public List<LinksWithBLOBs> selectLinksWithBLOBsList(Links links){
		return  linksMapper.selectLinksWithBLOBsList(links);
	}
	
	public List<InfoWithBLOBs> selectInfoWithBLOBsList(InfoWithBLOBs info){
		return  infoMapper.selectInfoWithBLOBsList(info);
	}
	
	//接口数据表所有的链接  一个链接只传一次
	public Set<String> selectInfolinksList(){
		Set<String> set=new HashSet<String>();
		List<Info> linksList = infoMapper.selectInfoList(new Info());
		for (Info info : linksList) {
			boolean add = set.add(info.getLink());
			if(!add){
				System.out.println(info.getLink());
			}
			
		}
		return set;
	}
	
	public int updateByPrimaryKeySelective(InfoWithBLOBs record){
		return infoMapper.updateByPrimaryKeySelective(record);
	}
	/**
	 * 把t_info表中所有的status设置为1
	 * @return
	 */
	public int updateInfoStatus(){
		return infoMapper.updateInfoStatus();
	}
	
	/**
	 * 把t_info表中所有的status为0的iden设置为1
	 * @return
	 */
	public int updateInfoIden(){
		return infoMapper.updateInfoIden();
	}
}
