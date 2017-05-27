package com.superx.spider.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController{

	
	@RequestMapping("/dologin")
	public String dologin(HttpServletRequest request, 
  			HttpServletResponse response,Model model){
		System.out.println("sddddddddd");
		return "";
	}
	
	
}
