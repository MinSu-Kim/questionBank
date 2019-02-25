package com.yi.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yi.domain.ReqUpdateVO;
import com.yi.service.ReqUpdateService;

@RestController
@RequestMapping("/reqUpdate/*")
public class ReqUpdateController {

	private static final Logger logger = LoggerFactory.getLogger(ReqUpdateController.class);
	
	@Autowired
	private ReqUpdateService service;
	
	@RequestMapping(value="/{question}", method=RequestMethod.GET)
	public ResponseEntity<List<ReqUpdateVO>> list(@PathVariable("question") String question){
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

