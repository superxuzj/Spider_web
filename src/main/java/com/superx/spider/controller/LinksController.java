package com.superx.spider.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.superx.spider.controller.spider.AnhuiController;
import com.superx.spider.controller.spider.BeijingController;
import com.superx.spider.controller.spider.ChongqingController;
import com.superx.spider.controller.spider.DizhenjuController;
import com.superx.spider.controller.spider.FujianController;
import com.superx.spider.controller.spider.GansuController;
import com.superx.spider.controller.spider.GuangdongController;
import com.superx.spider.controller.spider.GuangxiController;
import com.superx.spider.controller.spider.GuizhouController;
import com.superx.spider.controller.spider.HainanController;
import com.superx.spider.controller.spider.HebeiController;
import com.superx.spider.controller.spider.HeilongjiangController;
import com.superx.spider.controller.spider.HenanController;
import com.superx.spider.controller.spider.HubeiController;
import com.superx.spider.controller.spider.HunanController;
import com.superx.spider.controller.spider.JiangsuController;
import com.superx.spider.controller.spider.JiangxiController;
import com.superx.spider.controller.spider.JilinController;
import com.superx.spider.controller.spider.LiaoningController;
import com.superx.spider.controller.spider.NeimengguController;
import com.superx.spider.controller.spider.NingxiaController;
import com.superx.spider.controller.spider.QinghaiController;
import com.superx.spider.controller.spider.ShandongController;
import com.superx.spider.controller.spider.ShanghaiController;
import com.superx.spider.controller.spider.ShanxiController;
import com.superx.spider.controller.spider.ShxiController;
import com.superx.spider.controller.spider.SichuanController;
import com.superx.spider.controller.spider.TaiwangController;
import com.superx.spider.controller.spider.TianjinController;
import com.superx.spider.controller.spider.XinjiangController;
import com.superx.spider.controller.spider.XizangController;
import com.superx.spider.controller.spider.YingjisoujiuController;
import com.superx.spider.controller.spider.YunnanController;
import com.superx.spider.controller.spider.ZaihaifangyuController;
import com.superx.spider.controller.spider.ZhejiangController;

@Controller
public class LinksController {

	@Autowired
	private BeijingController beijingController;

	@Autowired
	private TianjinController tianjinController;

	@Autowired
	private HebeiController hebeiController;

	@Autowired
	private ShanxiController shanxiController;

	@Autowired
	private LiaoningController liaoningController;

	@Autowired
	private JilinController jilinController;

	@Autowired
	private ShanghaiController shanghaiController;

	@Autowired
	private JiangsuController jiangsuController;

	@Autowired
	private ZhejiangController zhejiangController;

	@Autowired
	private AnhuiController anhuiController;

	@Autowired
	private FujianController fujianController;

	@Autowired
	private JiangxiController jiangxiController;

	@Autowired
	private ShandongController shandongController;

	@Autowired
	private HenanController henanController;

	@Autowired
	private HubeiController hubeiController;

	@Autowired
	private HunanController hunanController;

	@Autowired
	private GuangdongController guangdongController;

	@Autowired
	private GuangxiController guangxiController;

	@Autowired
	private HainanController hainanController;

	@Autowired
	private ChongqingController chongqingController;

	@Autowired
	private SichuanController sichuanController;

	@Autowired
	private GuizhouController guizhouController;

	@Autowired
	private YunnanController yunnanController;

	@Autowired
	private XizangController xizangController;

	@Autowired
	private ShxiController shxiController;

	@Autowired
	private GansuController gansuController;

	@Autowired
	private QinghaiController qinghaiController;

	@Autowired
	private NingxiaController ningxiaController;

	@Autowired
	private XinjiangController xinjiangController;

	@Autowired
	private NeimengguController neimengguController;

	@Autowired
	private HeilongjiangController heilongjiangController;

	@Autowired
	private YingjisoujiuController yingjisoujiuController;

	@Autowired
	private TaiwangController taiwangController;

	@Autowired
	private ZaihaifangyuController zaihaifangyuController;

	@Autowired
	private DizhenjuController dizhenjuController;

	@RequestMapping("spider/all")
	public String index(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		System.out.println(new Date() + " spider all");
		try {
			beijingController.index(request, response, model);
		} catch (Exception c) {
			// TODO: handle exception
			c.printStackTrace();
		}

		try {
			tianjinController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			hebeiController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			shanxiController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			liaoningController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			jilinController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			shanghaiController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			jiangsuController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			zhejiangController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			anhuiController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			fujianController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			jiangxiController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			shandongController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			henanController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			hubeiController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			hunanController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			guangdongController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			guangxiController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			hainanController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			chongqingController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			sichuanController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			guizhouController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			yunnanController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			xizangController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			shxiController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			gansuController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			qinghaiController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			ningxiaController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			xinjiangController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			neimengguController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			heilongjiangController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			yingjisoujiuController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			taiwangController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			zaihaifangyuController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		try {
			dizhenjuController.index(request, response, model);
		} catch (Exception c) {
			// TODO Auto-generated catch block
			c.printStackTrace();
		}
		System.out.println(new Date());
		return "redirect:/";
	}

	// 定时任务 判断结束时间业务状态
	public void runTask() {

		System.out.println("11111111");
	}

}
