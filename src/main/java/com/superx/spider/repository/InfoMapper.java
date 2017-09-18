package com.superx.spider.repository;

import java.util.List;

import com.superx.spider.entity.Info;
import com.superx.spider.entity.InfoWithBLOBs;

public interface InfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InfoWithBLOBs record);

    int insertSelective(InfoWithBLOBs record);

    InfoWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(InfoWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(InfoWithBLOBs record);

    int updateByPrimaryKey(Info record);
    
    List<Info> selectInfoList(Info record);
    
    List<InfoWithBLOBs> selectInfoWithBLOBsList(InfoWithBLOBs record);
    
    List<InfoWithBLOBs> selectInfoWithBLOBsListByMakeDateFirst(InfoWithBLOBs record);
    
    List<InfoWithBLOBs> selectInfoWithBLOBsListByMakeDateSecond(InfoWithBLOBs record);
    
    int updateInfoStatus();
    
    int updateInfoIden();
}