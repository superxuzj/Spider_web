package com.superx.spider.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superx.spider.entity.Record;
import com.superx.spider.repository.RecordMapper;
/**

 *
 */
@Service
public class RecordService {
	@Autowired
	private RecordMapper recordMapper;
	
	public int insertSelective(Record record){
		return recordMapper.insertSelective(record);
	}
	
	
}
