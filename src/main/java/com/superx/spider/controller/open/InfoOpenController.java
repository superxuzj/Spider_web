package com.superx.spider.controller.open;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.superx.spider.entity.InfoWithBLOBs;
import com.superx.spider.entity.Record;
import com.superx.spider.service.InfoService;
import com.superx.spider.service.RecordService;

@Controller
public class InfoOpenController extends BaseOpen{
	@Autowired
	private InfoService infoService;
	@Autowired
	private RecordService recordService;
	/**
	 * 获取所有的数据(一次性)
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getAllInfos")
	public String getNewInfos(HttpServletRequest request, HttpServletResponse response,String tokenId) {
		OpenResult open = new OpenResult();
		if(null == tokenId || !tokenId.equals(TOKEN)){
			open.setStatusCode(VAL_STATUS_CODE);
			return responseWrite(request, response,open);
		}
		try {
			InfoWithBLOBs infoBlOB = new InfoWithBLOBs();
			List<InfoWithBLOBs> list = infoService.selectInfoWithBLOBsList(infoBlOB);
			open.setResult(list);
			open.setTotalSize(list.size());
			if(list==null || list.size()==0){
				open.setStatusCode(NODATA_STATUS_CODE);
			}else{
				open.setStatusCode(OK_STATUS_CODE);
			}
			Record record = new Record();
			record.setCreatorTime(new Date());
			record.setMethodName("getAllInfos");
			record.setGetIp(getRemortIP(request));
			record.setTotalSize(list.size()+"");
			recordService.insertSelective(record);
		} catch (Exception e) {
			open.setStatusCode(DATA_ERROR_STATUS_CODE);
		}
		return responseWrite(request, response,open);
	}
	
	/**
	 * 根据省份code获取该省份下面所有数据
	 * @param request
	 * @param response
	 * @param code
	 * @return
	 */
	@RequestMapping("/getInfosByCode")
	public String getInfosByCode(HttpServletRequest request, HttpServletResponse response,
								 String code,String tokenId) {
		//System.out.println("ssssssss");
		OpenResult open = new OpenResult();
		if(null == tokenId || !tokenId.equals(TOKEN)){
			open.setStatusCode(VAL_STATUS_CODE);
			return responseWrite(request, response,open);
		}
		if(null == code || code=="" || code.equals("")){
			open.setStatusCode(PARAM_ERROR_STATUS_CODE);
			return responseWrite(request, response,open);
		}
		System.out.println(code);
		
		try {
			InfoWithBLOBs infoBlOB = new InfoWithBLOBs();
			infoBlOB.setProvinceCode(code);
			List<InfoWithBLOBs> list = infoService.selectInfoWithBLOBsList(infoBlOB);
			open.setResult(list);
			open.setTotalSize(list.size());
			if(list==null || list.size()==0){
				open.setStatusCode(NODATA_STATUS_CODE);
			}else{
				open.setStatusCode(OK_STATUS_CODE);
			}
			
			Record record = new Record();
			record.setCreatorTime(new Date());
			record.setMethodName("getInfosByCode");
			record.setGetIp(getRemortIP(request));
			record.setTotalSize(list.size()+"_"+code);
			recordService.insertSelective(record);
		} catch (Exception e) {
			open.setStatusCode(DATA_ERROR_STATUS_CODE);
		}
		
		return responseWrite(request, response, open);
	}
	
	/**
	 * 获取增量数据(该方法每条数据只会传一次)
	 * 该方法需要与 updateInfosStatus方法同步调用
	 * 
	 * 只获取status为0的数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/getUpdateInfos")
	public String getUpdateInfos(HttpServletRequest request, HttpServletResponse response,String tokenId) {
		OpenResult open = new OpenResult();
		if(null == tokenId || !tokenId.equals(TOKEN)){
			open.setStatusCode(VAL_STATUS_CODE);
			return responseWrite(request, response,open);
		}
		System.out.println(new Date());
		try {
			InfoWithBLOBs infoBlOB = new InfoWithBLOBs();
			infoBlOB.setStatus("0");//增量获取 只获取数据未被接受的数据
			List<InfoWithBLOBs> list = infoService.selectInfoWithBLOBsList(infoBlOB);
			open.setResult(list);
			open.setTotalSize(list.size());
			if(list==null || list.size()==0){
				open.setStatusCode(NODATA_STATUS_CODE);
			}else{
				/*for (InfoWithBLOBs infoWithBLOBs : list) {
					infoWithBLOBs.setIdent("1");//数据已发送
					infoService.updateByPrimaryKeySelective(infoWithBLOBs);
				}*/
				infoService.updateInfoIden();
				open.setStatusCode(OK_STATUS_CODE);
			}
			Record record = new Record();
			record.setCreatorTime(new Date());
			record.setMethodName("getUpdateInfos");
			record.setGetIp(getRemortIP(request));
			record.setTotalSize(list.size()+"");
			recordService.insertSelective(record);
		} catch (Exception e) {
			open.setStatusCode(DATA_ERROR_STATUS_CODE);
		}
		System.out.println(new Date());
		return responseWrite(request, response,open);
	}
	
	/**
	 * 更改把t_info表中所有的status设置为1
	 * @return
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateInfosStatus")
	public String updateInfosStatus(HttpServletRequest request, HttpServletResponse response,String tokenId){
		OpenResult open = new OpenResult();
		if(null == tokenId || !tokenId.equals(TOKEN)){
			open.setStatusCode(VAL_STATUS_CODE);
			return responseWrite(request, response,open);
		}
		Integer sise = infoService.updateInfoStatus();
		open.setTotalSize(sise);
		open.setStatusCode(OK_STATUS_CODE);
		Record record = new Record();
		record.setCreatorTime(new Date());
		record.setMethodName("updateInfosStatus");
		record.setGetIp(getRemortIP(request));
		record.setTotalSize(sise+"");
		recordService.insertSelective(record);
		
		return responseWrite(request, response,open);
	}
	
	/**
	 * 根据id把info表中信息的status设置为1
	 * @return
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateInfoStatusById")
	public String updateInfoStatusById(HttpServletRequest request, HttpServletResponse response,
			Integer id,String tokenId){
		OpenResult open = new OpenResult();
		if(null == tokenId || !tokenId.equals(TOKEN)){
			open.setStatusCode(VAL_STATUS_CODE);
			return responseWrite(request, response,open);
		}
		if(null==id){
			open.setStatusCode(PARAM_ERROR_STATUS_CODE);
			return responseWrite(request, response,open);
		}
		InfoWithBLOBs info = new InfoWithBLOBs();
		info.setId(id);
		info.setStatus("1");
		Integer size  = infoService.updateByPrimaryKeySelective(info);//
		open.setTotalSize(size);
		if(size==0){
			//传入id没有找到该条记录
			open.setStatusCode(PARAM_ERROR_STATUS_CODE);
			return responseWrite(request, response,open);
		}
		open.setStatusCode(OK_STATUS_CODE);
		/*Integer sise = infoService.updateInfoStatus();
		*/
		Record record = new Record();
		record.setCreatorTime(new Date());
		record.setMethodName("updateInfoStatusById");
		record.setGetIp(getRemortIP(request));
		record.setTotalSize(size+"_"+id);
		recordService.insertSelective(record);
		return responseWrite(request, response,open);
	}
	
	public String getRemortIP(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getRemoteAddr();
	    }
	    return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}
}
