package com.yi.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yi.domain.CustomerVO;
import com.yi.domain.QuestionVO;
import com.yi.domain.ResultTestVO;
import com.yi.service.QuestionService;
import com.yi.service.ResultTestService;

@Controller
@RequestMapping("/question/*")
public class ResultTestController {
	private static final Logger logger = LoggerFactory.getLogger(ResultTestController.class);
	
	@Autowired
	private ResultTestService service;
	@Autowired
	private QuestionService qService;
	
	@RequestMapping(value="result", method=RequestMethod.POST)
	public String addAnswer(ResultTestVO vo) {
		logger.info("registerResultTest create------------POST");
		
		return "redirect:/resultTest/list"; 
	}
	
	//�������� �ѹ����� post��, insert��Ű��
	@RequestMapping(value="singletest", method=RequestMethod.POST)
	public String singletestresult(ResultTestVO vo, CustomerVO customerCode, QuestionVO questionCode , Model model){
		logger.info("singletestresult===========:"+vo);
		vo.setCustomer(customerCode);
		vo.setQuestion(questionCode);
		service.insertResultTest(vo);
		return "redirect:/question/singletest";
	}	
	
	//ajax�� ���� insert
	@ResponseBody
	@RequestMapping(value="subjecttest/{customer}/{question}", method=RequestMethod.POST)
	public ResponseEntity<String> subjecttestresult(@RequestBody ResultTestVO vo, @PathVariable("customer") String customer,@PathVariable("question") String question){
		logger.info("subjecttestresult �� ------------"+vo);
		ResponseEntity<String> entity = null;
		
		CustomerVO cvo = new CustomerVO();
		cvo.setCustomerCode(customer);
		
		QuestionVO qvo = new QuestionVO();
		qvo.setQuestionCode(question);
		
		vo.setCustomer(cvo);
		vo.setQuestion(qvo);
		logger.info("subjecttestresult �� ------------"+vo);
		try {
			service.insertResultTest(vo);
			int code = service.selectMaxCode();
			entity = new ResponseEntity<String>(code+"", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);//400����
		}
		return entity;
	}
	
	//ajax�� ���� update
	@ResponseBody
	@RequestMapping(value="subjecttest/{customer}/{question}", method=RequestMethod.PUT)
	public ResponseEntity<String> subjecttestupdate(@RequestBody ResultTestVO vo, @PathVariable("customer") String customer,@PathVariable("question") String question){
		logger.info("subjecttestupdate �� ------------"+vo);
		ResponseEntity<String> entity = null;
		
		CustomerVO cvo = new CustomerVO();
		cvo.setCustomerCode(customer);
		
		QuestionVO qvo = new QuestionVO();
		qvo.setQuestionCode(question);
		
		vo.setCustomer(cvo);
		vo.setQuestion(qvo);
		
		logger.info("subjecttestupdate �� ------------"+vo);
		try {
			service.updateByCustomerAndQuestion(vo);
			entity = new ResponseEntity<String>("success", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);//400����
		}
		return entity;
	}
	
	//����ٽ�Ǯ�� ���� ����Ʈ
	@RequestMapping(value="/incorrect", method=RequestMethod.GET)
	public void selectIncorrect(String customerCode, Model model){
		logger.info("selectIncorrect ------------"+customerCode);
		List<ResultTestVO> resultList = service.selectIncorrectQuestionByCustomer(customerCode);
		System.out.println(resultList);
		List<QuestionVO> list = new ArrayList<>();
		
		for(int i=0;i<resultList.size();i++) {
			QuestionVO qvo = qService.selectByNO(resultList.get(i).getQuestion());
			list.add(qvo);
		}
		
		System.out.println(list);
		model.addAttribute("list", list);
	}
	
	//���ǰ��
	@ResponseBody
	@RequestMapping(value="resultMokeTest", method=RequestMethod.POST)
	public void insertResultMokeTest(@RequestParam(value="aArray[]") List<String> aArray, @RequestParam(value="qArray[]")List<String> qArray,@RequestParam(value="customerCode") String customerCode) {
		logger.info("insertResultMokeTest ------------aArray: "+aArray);
		logger.info("insertResultMokeTest ------------qArray: "+qArray);
		logger.info("insertResultMokeTest ------------customerCode "+customerCode);
		
		
	}
}




















