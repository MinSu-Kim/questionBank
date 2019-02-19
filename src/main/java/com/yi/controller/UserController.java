package com.yi.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yi.domain.CustomerVO;
import com.yi.domain.LoginDTO;
import com.yi.service.CustomerService;


@Controller
@RequestMapping("/user/*")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private CustomerService service;
	
	@RequestMapping(value="login", method=RequestMethod.GET)
	public void loginGet() {
		logger.info("login GET -------------- ");
	}
	
	@RequestMapping(value="loginPost", method=RequestMethod.POST)
	public void loginPost(String id, String password, Model model) {
		logger.info("loginPost  -------------- ");
		CustomerVO vo = service.read(id, password);
		logger.info("loginPost  -------------- " +vo);
		if(vo == null) {//ȸ���� ������ �α��� ȭ������ ���ư���
			logger.info("loginPost return -------------- ");
			return;
		}
		LoginDTO dto = new LoginDTO();
		dto.setCustomerName(vo.getCustomerName());
		dto.setId(vo.getId());
		dto.setEmployee(vo.isEmployee());
		dto.setCustomerCode(vo.getCustomerCode());
		System.out.println("========dtoȮ�ο� : "+dto);
		model.addAttribute("customerVO", dto);
	}
	
	@RequestMapping(value="logout", method=RequestMethod.GET)
	public String logOutGET(HttpSession session) {
		logger.info("logout GET ------------");
		session.invalidate();//���ǳ����� �α׾ƿ� �Ϸ�
		return "redirect:/";
	}
	
	@RequestMapping(value="mypage", method=RequestMethod.GET)
	public void mypageGET(HttpSession session) {
		logger.info("mypageGET GET ------------");
	}
}






















