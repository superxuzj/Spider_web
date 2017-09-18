package com.superx.spider.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.superx.spider.entity.InfoWithBLOBs;
import com.superx.spider.service.InfoService;
import com.superx.spider.util.TextUtil;
import com.superx.spider.util.WordGenerator;

@Controller
public class MakeDateController {

	@Autowired
	private InfoService infoService;

	@RequestMapping("/report")
	public String monReport(HttpServletRequest request,
			HttpServletResponse response) {
		return "report";
	}
	
	@RequestMapping("/down")
	public String mondownload(HttpServletRequest request,
			HttpServletResponse response) {
		return "download";
	}

	@RequestMapping("/makeDate")
	@ResponseBody
	public void makeDate(HttpServletRequest request,
			HttpServletResponse response, Model model, InfoWithBLOBs info,
			String datatime) {

		String[] datas = datatime.split("-");
		info.setFormattime1("%"+datas[0]+"年"+datas[1]+"月"+"%");
		info.setFormattime2("%"+datatime+"%");
		info.setProvinceCode("dizhenju");
		List<InfoWithBLOBs> listfirst = infoService
				.selectInfoWithBLOBsListByMakeDateFirst(info);
		for (InfoWithBLOBs infoWithBLOBs : listfirst) {
			String content = infoWithBLOBs.getContent();
			String text = Jsoup.parse(
					content.replaceAll("(?i)<br[^>]*>", "br2nl").replaceAll(
							"\n", "br2nl")).text().trim();
			text = text.replaceAll("br2nl ", "<w:br />").replaceAll("br2nl", "<w:br />").trim();
			/*Document ducument = Jsoup.parse(content);
			String text1 = ducument.text();*/
			infoWithBLOBs.setContent(text);
			// System.out.println(text1);
		}
		
		List<InfoWithBLOBs> listsecond = infoService
				.selectInfoWithBLOBsListByMakeDateSecond(info);
		for (InfoWithBLOBs infoWithBLOBs : listsecond) {
			String content = infoWithBLOBs.getContent();
			String text = Jsoup.parse(
					content.replaceAll("(?i)<br[^>]*>", "br2nl").replaceAll(
							"\n", "br2nl")).text().trim();
			text = text.replaceAll("br2nl ", "<w:br />").replaceAll("br2nl", "<w:br />").trim();
			/*Document ducument = Jsoup.parse(content);
			String text1 = ducument.text();*/
			infoWithBLOBs.setContent(text);
			// System.out.println(text1);
		}
		//listfirst= listsecond;
		listfirst.addAll(listsecond);
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException c1) {
			// TODO Auto-generated catch block
			c1.printStackTrace();
		}

		// 构造数据
		Map<String, Object> dataMap = new HashMap<String, Object>();

		dataMap.put("infoList", listfirst);
		dataMap.put("nianyue", datatime);
		 Calendar now = Calendar.getInstance();  
	      
		dataMap.put("today", now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH) + 1)+"-"+now.get(Calendar.DAY_OF_MONTH));
		// 提示：在调用工具类生成Word文档之前应当检查所有字段是否完整
		// 否则Freemarker的模板殷勤在处理时可能会因为找不到值而报错 这里暂时忽略这个步骤了
		File file = null;
		InputStream fin = null;
		ServletOutputStream out = null;
		try {
			// 调用工具类WordGenerator的createDoc方法生成Word文档
			file = WordGenerator.createDoc(dataMap, "report");
			fin = new FileInputStream(file);

			response.setCharacterEncoding("utf-8");
			response.setContentType("application/msword");
			// 设置浏览器以下载的方式处理该文件默认名为resume.doc
			response.addHeader("Content-Disposition",
					"attachment;filename=report.doc");

			out = response.getOutputStream();
			byte[] buffer = new byte[512]; // 缓冲区
			int bytesToRead = -1;
			// 通过循环将读入的Word文件的内容输出到浏览器中
			while ((bytesToRead = fin.read(buffer)) != -1) {
				out.write(buffer, 0, bytesToRead);
			}
		} catch (Exception c) {
			// TODO: handle exception
		} finally {
			try {
				if (fin != null)
					fin.close();
				if (out != null)
					out.close();
				if (file != null)
					file.delete(); // 删除临时文件
			} catch (Exception c) {
				// TODO: handle exception
			}
		}
	}

	@RequestMapping("download")
	@ResponseBody
    public void exportWord( HttpServletRequest request, HttpServletResponse response, Model model, InfoWithBLOBs info,
			String datatime) 
                throws Exception {
		String[] datas = datatime.split("-");
		info.setFormattime1("%"+datas[0]+"年"+datas[1]+"月"+"%");
		info.setFormattime2("%"+datatime+"%");
		info.setProvinceCode("dizhenju");
		List<InfoWithBLOBs> listfirst = infoService
				.selectInfoWithBLOBsListByMakeDateFirst(info);
		
		StringBuffer sb = new StringBuffer();
		sb.append("<html><body>");
		for (InfoWithBLOBs infoWithBLOBs : listfirst) {
			String content = infoWithBLOBs.getContent();
			sb.append(infoWithBLOBs.getTitle()+"<br/>");
			sb.append(content+"<br/>");
			sb.append("（信息来源："+infoWithBLOBs.getProvince()+"）"+"<br/>");
			sb.append("（发布日期："+infoWithBLOBs.getTime()+"）"+"<p/>");
		}
		
		List<InfoWithBLOBs> listsecond = infoService
				.selectInfoWithBLOBsListByMakeDateSecond(info);
		for (InfoWithBLOBs infoWithBLOBs : listsecond) {
			String content = infoWithBLOBs.getContent();
			sb.append(infoWithBLOBs.getTitle()+"<br/>");
			sb.append(content+"<br/>");
			sb.append("（信息来源："+infoWithBLOBs.getProvince()+"）"+"<br/>");
			sb.append("（发布日期："+infoWithBLOBs.getTime()+"）"+"<p/>");
			
		}
		sb.append("</body></html>");
        try {
                //word内容
                //String content="<html><body><p>adf</p><p>adf</p><p>adf</p><p>adf</p><p>adf</p><p>adf</p></body></html>";
                byte b[] = sb.toString().getBytes("utf-8");  //这里是必须要设置编码的，不然导出中文就会乱码。
                ByteArrayInputStream bais = new ByteArrayInputStream(b);//将字节数组包装到流中  
                /*
                * 关键地方
                * 生成word格式
                */
                POIFSFileSystem poifs = new POIFSFileSystem();  
                DirectoryEntry directory = poifs.getRoot();  
                DocumentEntry documentEntry = directory.createDocument("WordDocument", bais); 
                //输出文件
                String fileName="月报";
                request.setCharacterEncoding("utf-8");  
                response.setContentType("application/msword");//导出word格式
                response.addHeader("Content-Disposition", "attachment;filename=" +
                         new String( (fileName + ".doc").getBytes(),  
                                 "iso-8859-1"));
                 
                OutputStream ostream = response.getOutputStream(); 
                poifs.writeFilesystem(ostream);  
                bais.close();  
                ostream.close(); 
            }catch(Exception e){
               // AppUtils.logError("导出出错：%s", e.getMessage());
            }  
    }
	
}
