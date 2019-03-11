package com.yi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yi.domain.CustomerVO;
import com.yi.domain.LoginDTO;
import com.yi.domain.QuestionVO;
import com.yi.domain.RateDTO;
import com.yi.domain.ResultTestVO;
import com.yi.service.CustomerService;
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
	@Autowired
	private CustomerService cService;
	
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
	
	// �߰� : ajax�� �����׽�Ʈ ������ư Ŭ���� �ϳ��� ����Ʈ�� ���
	@ResponseBody
	@RequestMapping(value="subjecttest/{customer}/{question}", method=RequestMethod.POST)
	public void subjecttestresult(HttpServletRequest request,@RequestBody ResultTestVO vo, @PathVariable("customer") String customer,@PathVariable("question") String question){
		
		CustomerVO cvo = new CustomerVO();
		cvo.setCustomerCode(customer);
		cvo = cService.selectByNo(cvo);
		
		QuestionVO qvo = new QuestionVO();
		qvo.setQuestionCode(question);
		qvo = qService.selectByNO(qvo);
		
		vo.setCustomer(cvo);
		vo.setQuestion(qvo);
		logger.info("subjecttestresult vo ------------"+vo);
		
		List<ResultTestVO> list = (List<ResultTestVO>) request.getSession().getAttribute("list");
		if(list==null) {
			list = new ArrayList<>();
		}
		list.add(vo);
		logger.info("list ������------------"+list.size());
	}
	
	// ���� : ajax�� �����׽�Ʈ ������ư Ŭ���� �ϳ��� ����Ʈ�� ���
		@ResponseBody
		@RequestMapping(value="subjecttest/{customer}/{question}", method=RequestMethod.PUT)
		public void subjecttestModify(HttpServletRequest request,@RequestBody ResultTestVO vo, @PathVariable("customer") String customer,@PathVariable("question") String question){
			
			CustomerVO cvo = new CustomerVO();
			cvo.setCustomerCode(customer);
			cvo = cService.selectByNo(cvo);
			
			QuestionVO qvo = new QuestionVO();
			qvo.setQuestionCode(question);
			qvo = qService.selectByNO(qvo);
			
			vo.setCustomer(cvo);
			vo.setQuestion(qvo);
			logger.info("���� vo ------------"+vo);
			
			List<ResultTestVO> list = (List<ResultTestVO>) request.getSession().getAttribute("list");
			if(list.contains(vo)) {
				logger.info("list ------------��"+list.get(list.indexOf(vo)).getAnswer());
				list.set(list.indexOf(vo), vo);
				logger.info("list ------------��"+list.get(list.indexOf(vo)).getAnswer());
			}
			logger.info("list ������------------"+list.size());
		}
	
	//���� �׽�Ʈ submitŬ���� 20���� ��� insert�ϱ�
	@ResponseBody
	@RequestMapping(value="subjecttestResult", method=RequestMethod.POST)
	public void insertSubjectTest(HttpServletRequest request,Model model) {
		//arraylist���ִ� �� insert�Ŀ� session�� �����
		List<ResultTestVO> list = (List<ResultTestVO>) request.getSession().getAttribute("list");
		logger.info("insertSubjectTest ------------list:"+list);
		for(int i=0;i<list.size();i++) {
			service.insertResultTest(list.get(i));
		}
		logger.info("insertSubjectTest �Ϸ�");
	}
	//���� ����
	@RequestMapping(value="score", method=RequestMethod.GET)
	public String subjectTestScore(HttpServletRequest request,Model model) {
		logger.info("subjectTestScore ------------");
		List<ResultTestVO> list = (List<ResultTestVO>) request.getSession().getAttribute("list");
		int score = 0;
		for(int i=0;i<20;i++) {
			if(list.get(i).getAnswer()==list.get(i).getCorrect()) {
				score += 5;
			}
		}
		logger.info("Score ------------:"+score);
		//�𵨿� ���� �Ǿ����
		model.addAttribute("score", score);
		logger.info("list ------------:"+list.size());
		//���Ǿȿ� �����
		//request.getSession().removeAttribute("list");
		//request.getSession().removeAttribute("score");
		return "redirect:/question/subjectScore";
	}
	
	@RequestMapping(value="subjectScore", method=RequestMethod.GET)
	public void subjectScorePage(HttpServletRequest request,@ModelAttribute("score") int score) {//request �Ķ���͸� ������ֱ�
		logger.info("subjectScorePage ------------"+score); 
		List<ResultTestVO> list = (List<ResultTestVO>) request.getSession().getAttribute("list");
		logger.info("list ------------:"+list.size());
	}
	
	//����ٽ�Ǯ�� ���� ��ü ����Ʈ
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
	
	//����ٽ�Ǯ�� ���� ���� ����Ʈ(�ߺ�����)
	@ResponseBody
	@RequestMapping(value="/incorrect/{customerCode}/{subject}", method=RequestMethod.GET)
	public ResponseEntity<List<QuestionVO>> selectIncorrectBySubject(@PathVariable("customerCode")String customerCode,@PathVariable("subject") String subject) {
		logger.info("selectIncorrect ȸ��------------"+customerCode);
		logger.info("selectIncorrect ����------------"+subject);
		ResponseEntity<List<QuestionVO>> entity = null;

		try {
			List<String> codeList = service.selectIncorrectQuestionBySubject(customerCode, subject);
			logger.info("selectIncorrect ���� list------------"+codeList);
			List<QuestionVO> list = new ArrayList<>();
			for(int i=0;i<codeList.size();i++) {
				QuestionVO vo = new QuestionVO();
				vo.setQuestionCode(codeList.get(i));
				vo = qService.selectByNO(vo);
				list.add(vo);
			}
			logger.info("selectIncorrect ���� list------------"+list);
			entity = new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);//List<QuestionVO>�� ������ �ϳ�, ��������������HttpStatus�� ����
		}
		
		return entity;
	}
	
	//���ǰ��
	@ResponseBody
	@RequestMapping(value="resultMokeTest", method=RequestMethod.POST)
	public void insertResultMokeTest(HttpServletRequest request,@RequestParam(value="aArray[]") List<String> aArray, @RequestParam(value="qArray[]")List<String> qArray,@RequestParam(value="customerCode") String customerCode) {
		
		//��ã��
		CustomerVO cvo = new CustomerVO();
		cvo.setCustomerCode(customerCode);
		CustomerVO newCvo = cService.selectByNo(cvo);
		//����ã��
		QuestionVO qvo = new QuestionVO();
		
		List<ResultTestVO> list = new ArrayList<>();//batch�� ����� �迭
		//���� ���ϴ� ��
		int score = 0;
		//���� ���� ���ϴ� ��
		int dScore = 0;
		int aScore = 0;
		int oScore = 0;
		int sScore = 0;
		int cScore = 0;
		for(int i=0;i<100;i++) {
			ResultTestVO vo = new ResultTestVO();
			//�ش繮������ã��
			qvo.setQuestionCode(qArray.get(i));
			QuestionVO newQvo = qService.selectByNO(qvo);
			
			//�� �Է�
			vo.setCustomer(newCvo);
			vo.setQuestion(newQvo);
			vo.setAnswer(Integer.parseInt(aArray.get(i)));
			vo.setSpendTime(0);//�����ذ�X
			
			vo.setCorrect(newQvo.getCorrect());
			boolean pass = false;
			if(vo.getAnswer()==vo.getCorrect()) {
				pass=true;
				score += 1;
				logger.info("���� Score�� ����:"+newQvo);
				logger.info("���� Score�� ����:"+newQvo.getQuestionCode().substring(0, 2));
				//���� ���� ���ϱ�
				switch(newQvo.getQuestionCode().substring(0, 2)){
		        case "QD": 
		        	dScore += 1;
		            break;
		        case "QA":
		        	aScore += 1;
		            break;
		        case "QO" :
		        	oScore += 1;
		            break;
		        case "QS":
		        	sScore += 1;
		            break;
		        case "QC" :
		        	cScore += 1;
		            break;
				}
			}
			vo.setPass(pass);
			logger.info("insertResultMokeTest ------------vo "+vo);
			list.add(vo);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		
		//���ǿ� ����Ʈ�� �������
		List<Integer> scoreList = new ArrayList<>();
		scoreList.add(score);
		scoreList.add(dScore);
		scoreList.add(aScore);
		scoreList.add(oScore);
		scoreList.add(sScore);
		scoreList.add(cScore);
		logger.info("Score ------------list "+scoreList);
		request.getSession().setAttribute("list", list);
		request.getSession().setAttribute("scoreList", scoreList);
		
		service.insertBatchResultTest(map);
		
	}

	@RequestMapping(value="mokeScore", method=RequestMethod.GET)
	public void moketestScore(HttpServletRequest request, Model model) {
		logger.info("mokeScore ------------");
		
		List<Integer> scoreList = (List<Integer>) request.getSession().getAttribute("scoreList");
		List<ResultTestVO> list = (List<ResultTestVO>) request.getSession().getAttribute("list");
		model.addAttribute("list", list);
		model.addAttribute("scoreList", scoreList);
		//���� ���� ���ϱ�		
		
		//���ǿ� ��� �� �����(������ ���ΰ�ħ�� �����߻�)
		//request.getSession().removeAttribute("score");
		//request.getSession().removeAttribute("list");
	}
	
	//���䷩ŷ������ ���� ����Ʈ
	@RequestMapping(value="/rankIncorrect", method=RequestMethod.GET)
	public void selectIncorrectByRank(Model model){
		logger.info("selectIncorrect ------------");
		List<String> resultList = service.selectIncorrectTopRank("D");
		System.out.println(resultList);
		List<QuestionVO> list = new ArrayList<>();
			
		for(int i=0;i<resultList.size();i++) {
			QuestionVO qvo = new QuestionVO();
			qvo.setQuestionCode(resultList.get(i));
			qvo = qService.selectByNO(qvo);
			list.add(qvo);
		}
		System.out.println(list);
		model.addAttribute("list", list);
	}
	
	//����Ʋ������ ���� ���� ����Ʈ(�ߺ�����)
	@ResponseBody
	@RequestMapping(value="/rankIncorrect/{subject}", method=RequestMethod.GET)
	public ResponseEntity<List<QuestionVO>> selectIncorrectRankBySubject(@PathVariable("subject") String subject) {
		logger.info("selectIncorrect ����------------"+subject);
		ResponseEntity<List<QuestionVO>> entity = null;
		
		try {
			List<String> codeList = service.selectIncorrectTopRank(subject);
			logger.info("selectIncorrect ���� list------------"+codeList);
			List<QuestionVO> list = new ArrayList<>();
			for(int i=0;i<codeList.size();i++) {
				QuestionVO vo = new QuestionVO();
				vo.setQuestionCode(codeList.get(i));
				vo = qService.selectByNO(vo);
				list.add(vo);
			}
			logger.info("selectIncorrect ���� list------------"+list);
			entity = new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);//List<QuestionVO>�� ������ �ϳ�, ��������������HttpStatus�� ����
		}
			
		return entity;
	}
	
	//���κ�, ���� rate �������� �̵��ϴ� get�Լ�
	@RequestMapping(value="correctRate", method=RequestMethod.GET)
	public void correctRateGet(HttpServletRequest request, Model model) {
		LoginDTO vo = (LoginDTO) request.getSession().getAttribute("login");
		logger.info("selectIncorrect �α����� ���޾ƿ���------------"+vo);
		
		List<RateDTO> list = new ArrayList<>();
		//���� rate��� �޾ƿ���
		RateDTO dRate = service.selectCorrectRateBySubject(vo.getCustomerCode(), "D");
		RateDTO aRate = service.selectCorrectRateBySubject(vo.getCustomerCode(), "A");
		RateDTO oRate = service.selectCorrectRateBySubject(vo.getCustomerCode(), "O");
		RateDTO sRate = service.selectCorrectRateBySubject(vo.getCustomerCode(), "S");
		RateDTO cRate = service.selectCorrectRateBySubject(vo.getCustomerCode(), "C");
		list.add(dRate);
		list.add(aRate);
		list.add(oRate);
		list.add(sRate);
		list.add(cRate);
		logger.info("selectIncorrect list------------"+list);
		model.addAttribute("list", list);
	}

}




















