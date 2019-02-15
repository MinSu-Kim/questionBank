package com.yi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

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
import org.springframework.web.multipart.MultipartFile;

import com.yi.domain.QuestionVO;
import com.yi.service.QuestionService;

@Controller
@RequestMapping("/question/*")
public class QuestionController {
	
private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
	
	@Autowired
	private QuestionService service;
	//�⺻���� ���Թ������� servlet-context�� ��ϵ� �̸����� ����
	@Resource(name="uploadPath")
	private String uploadPath;
	
	@RequestMapping(value="register", method=RequestMethod.GET)
	public void registerGet(){
		logger.info("QuestionVO create------------GET");
	}
	
	@RequestMapping(value="register", method=RequestMethod.POST)
	public String registerPost(QuestionVO vo, MultipartFile pictureFile){
		logger.info("QuestionVO create------------POST");
		ResponseEntity<String> entity = null;
		
			String picture = pictureFile.getOriginalFilename();
		if(picture.equals("")==false) {
			System.out.println("=============pictureFile.getOriginalFilename()"+picture);
			vo.setPicture(uploadPath+"/"+picture);
			logger.info("QuestionVO create------------"+vo);
		}
		try {
			service.insert(vo);
			System.out.println(vo);
			entity = new ResponseEntity<String>("success", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);//400����
		}
		
		return "redirect:/question/register";
		
	}
	//jsp�� ���� �޼ҵ�
	@RequestMapping(value="list", method=RequestMethod.GET)
	public void list(Model model){
		List<QuestionVO> list = service.selectByAll();
		List<String> numList = new ArrayList<>();
		for(int i=0;i<list.size();i++) {
			String num = list.get(i).getQuestionCode().substring(7);
			numList.add(num);
		}
		model.addAttribute("list", list);
		model.addAttribute("numList", numList);
	}
	
	//json�� ������ �޼ҵ�
	@ResponseBody
	@RequestMapping(value="listJson", method=RequestMethod.GET)
	public ResponseEntity<List<QuestionVO>> listJson(){
		ResponseEntity<List<QuestionVO>> entity = null;
		
		try {
			List<QuestionVO> list = service.selectByAll();
			entity = new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception 
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);//List<QuestionVO>�� ������ �ϳ�, ��������������HttpStatus�� ����
		}
		
		return entity;
	}
	
	@RequestMapping(value="{questionCode}", method=RequestMethod.PUT)
	public ResponseEntity<String> update(@PathVariable("questionCode") String questionCode,@RequestBody QuestionVO vo){
		ResponseEntity<String> entity = null;
		try {
			vo.setQuestionCode(questionCode);
			service.update(vo);
			entity = new ResponseEntity<String>("success", HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return entity;
	}
	
	@RequestMapping(value="{questionCode}", method=RequestMethod.DELETE)
	public ResponseEntity<String> remove(@PathVariable("questionCode") String questionCode){
		ResponseEntity<String> entity = null;
		
		try {
			QuestionVO vo = new QuestionVO();
			vo.setQuestionCode(questionCode);
			service.delete(vo);
			entity = new ResponseEntity<String>("success", HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			entity = new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		return entity;
	}

}


















