package com.yi.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yi.domain.ReqUpdateVO;
import com.yi.service.ReqUpdateService;

@Controller
@RequestMapping("/reqUpdate/*")
public class ReqUpdateController {

	private static final Logger logger = LoggerFactory.getLogger(ReqUpdateController.class);
	
	@Autowired
	private ReqUpdateService service;
	
	//��ü ����Ʈ����(�ܵ��޴���)
	@RequestMapping(value="list", method=RequestMethod.GET)
	public void list(Model model) {
		logger.info("list================== controller");
		List<ReqUpdateVO> list = service.selectByAll();
		model.addAttribute("list", list);
		logger.info("list================== controller list :"+list);
	}
	
	//���������޴� �Ʒ��� ���̴� ����Ʈ
	@ResponseBody
	@RequestMapping(value="/{question}", method=RequestMethod.GET)
	public ResponseEntity<List<ReqUpdateVO>> listByQuestion(@PathVariable("question") String question){
		logger.info("list==================������û controller");
		ResponseEntity<List<ReqUpdateVO>> entity = null;
		
		try {
			List<ReqUpdateVO> list = service.selectByQuestionCode(question);
			logger.info("list==================������û controller :::"+list);
			entity = new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);//List<ReqUpdateVO>�� ������ �ϳ�, ��������������HttpStatus�� ����
		}
		return entity;
	}
	
	//�������� �Ʒ��� �߰��ϴ� �޼ҵ�
	@ResponseBody
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public ResponseEntity<String> register(@RequestBody ReqUpdateVO vo){
		logger.info("list==================������û��� controller"+vo);
		ResponseEntity<String> entity = null;
		
		try {
			service.insert(vo);
			logger.info("list==================������û��� controller :::");
			entity = new ResponseEntity<>("success", HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);//List<ReqUpdateVO>�� ������ �ϳ�, ��������������HttpStatus�� ����
		}
		return entity;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

