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
	
	//랜덤으로 한문제씩 post용, insert시키기
	@RequestMapping(value="singletest", method=RequestMethod.POST)
	public String singletestresult(ResultTestVO vo, CustomerVO customerCode, QuestionVO questionCode , Model model){
		logger.info("singletestresult===========:"+vo);
		vo.setCustomer(customerCode);
		vo.setQuestion(questionCode);
		service.insertResultTest(vo);
		return "redirect:/question/singletest";
	}	
	
	// 추가 : ajax용 과목별테스트 라디오버튼 클릭시 하나씩 리스트에 담기
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
		logger.info("list 사이즈------------"+list.size());
	}
	
	// 수정 : ajax용 과목별테스트 라디오버튼 클릭시 하나씩 리스트에 담기
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
			logger.info("수정 vo ------------"+vo);
			
			List<ResultTestVO> list = (List<ResultTestVO>) request.getSession().getAttribute("list");
			if(list.contains(vo)) {
				logger.info("list ------------전"+list.get(list.indexOf(vo)).getAnswer());
				list.set(list.indexOf(vo), vo);
				logger.info("list ------------후"+list.get(list.indexOf(vo)).getAnswer());
			}
			logger.info("list 사이즈------------"+list.size());
		}
	
	//과목별 테스트 submit클릭시 20문제 모두 insert하기
	@ResponseBody
	@RequestMapping(value="subjecttestResult", method=RequestMethod.POST)
	public void insertSubjectTest(HttpServletRequest request,Model model) {
		//arraylist에있는 값 insert후에 session값 지우기
		List<ResultTestVO> list = (List<ResultTestVO>) request.getSession().getAttribute("list");
		logger.info("insertSubjectTest ------------list:"+list);
		for(int i=0;i<list.size();i++) {
			service.insertResultTest(list.get(i));
		}
		logger.info("insertSubjectTest 완료");
	}
	
	//과목별 점수
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
		//모델에 점수 실어보내기
		model.addAttribute("score", score);
		logger.info("list ------------:"+list.size());
		//세션안에 지우기
		//request.getSession().removeAttribute("list");
		//request.getSession().removeAttribute("score");
		return "redirect:/question/subjectScore";
	}
	
	@RequestMapping(value="subjectScore", method=RequestMethod.GET)
	public void subjectScorePage(HttpServletRequest request,@ModelAttribute("score") int score) {//request 파라미터명 명시해주기
		logger.info("subjectScorePage ------------"+score); 
		List<ResultTestVO> list = (List<ResultTestVO>) request.getSession().getAttribute("list");
		logger.info("list ------------:"+list.size());
	}
	
	//오답다시풀기 문제 전체 리스트
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
	
	//오답다시풀기 과목별 문제 리스트(중복제외)
	@ResponseBody
	@RequestMapping(value="/incorrect/{customerCode}/{subject}", method=RequestMethod.GET)
	public ResponseEntity<List<QuestionVO>> selectIncorrectBySubject(@PathVariable("customerCode")String customerCode,@PathVariable("subject") String subject) {
		logger.info("selectIncorrect 회원------------"+customerCode);
		logger.info("selectIncorrect 과목------------"+subject);
		ResponseEntity<List<QuestionVO>> entity = null;

		try {
			List<String> codeList = service.selectIncorrectQuestionBySubject(customerCode, subject);
			logger.info("selectIncorrect 문제 list------------"+codeList);
			List<QuestionVO> list = new ArrayList<>();
			for(int i=0;i<codeList.size();i++) {
				QuestionVO vo = new QuestionVO();
				vo.setQuestionCode(codeList.get(i));
				vo = qService.selectByNO(vo);
				list.add(vo);
			}
			logger.info("selectIncorrect 문제 list------------"+list);
			entity = new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);//List<QuestionVO>로 보내야 하나, 보낼수없을때는HttpStatus만 보냄
		}
		
		return entity;
	}
	
	//모의고사
	@ResponseBody
	@RequestMapping(value="resultMokeTest", method=RequestMethod.POST)
	public void insertResultMokeTest(HttpServletRequest request,@RequestParam(value="aArray[]") List<String> aArray, @RequestParam(value="qArray[]")List<String> qArray,@RequestParam(value="customerCode") String customerCode) {
		
		//고객찾기
		CustomerVO cvo = new CustomerVO();
		cvo.setCustomerCode(customerCode);
		CustomerVO newCvo = cService.selectByNo(cvo);
		//문제찾기
		QuestionVO qvo = new QuestionVO();
		
		List<ResultTestVO> list = new ArrayList<>();//batch에 사용할 배열
		//점수 구하는 용
		int score = 0;
		//과목별 점수 구하는 용
		int dScore = 0;
		int aScore = 0;
		int oScore = 0;
		int sScore = 0;
		int cScore = 0;
		for(int i=0;i<100;i++) {
			ResultTestVO vo = new ResultTestVO();
			//해당문제정보찾기
			qvo.setQuestionCode(qArray.get(i));
			QuestionVO newQvo = qService.selectByNO(qvo);
			
			//값 입력
			vo.setCustomer(newCvo);
			vo.setQuestion(newQvo);
			vo.setAnswer(Integer.parseInt(aArray.get(i)));
			vo.setSpendTime(0);//아직해결X
			
			vo.setCorrect(newQvo.getCorrect());
			boolean pass = false;
			if(vo.getAnswer()==vo.getCorrect()) {
				pass=true;
				score += 1;
				logger.info("과목별 Score용 구분:"+newQvo);
				logger.info("과목별 Score용 구분:"+newQvo.getQuestionCode().substring(0, 2));
				//과목별 점수 구하기
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
		
		//세션에 리스트와 점수담기
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
		//과목별 점수 구하기		
		
		//세션에 담긴 값 지우기(페이지 새로고침시 에러발생)
		//request.getSession().removeAttribute("score");
		//request.getSession().removeAttribute("list");
	}
	
	//오답랭킹순으로 문제 리스트
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
	
	//많이틀린문제 과목별 문제 리스트(중복제외)
	@ResponseBody
	@RequestMapping(value="/rankIncorrect/{subject}", method=RequestMethod.GET)
	public ResponseEntity<List<QuestionVO>> selectIncorrectRankBySubject(@PathVariable("subject") String subject) {
		logger.info("selectIncorrect 과목------------"+subject);
		ResponseEntity<List<QuestionVO>> entity = null;
		
		try {
			List<String> codeList = service.selectIncorrectTopRank(subject);
			logger.info("selectIncorrect 문제 list------------"+codeList);
			List<QuestionVO> list = new ArrayList<>();
			for(int i=0;i<codeList.size();i++) {
				QuestionVO vo = new QuestionVO();
				vo.setQuestionCode(codeList.get(i));
				vo = qService.selectByNO(vo);
				list.add(vo);
			}
			logger.info("selectIncorrect 문제 list------------"+list);
			entity = new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);//List<QuestionVO>로 보내야 하나, 보낼수없을때는HttpStatus만 보냄
		}
			
		return entity;
	}
	
	//개인별, 과목별 rate 페이지로 이동하는 get함수
	@RequestMapping(value="correctRate", method=RequestMethod.GET)
	public void correctRateGet(HttpServletRequest request, Model model) {
		LoginDTO vo = (LoginDTO) request.getSession().getAttribute("login");
		logger.info("selectIncorrect 로그인한 고객받아오기------------"+vo);
		
		List<RateDTO> list = new ArrayList<>();
		//과목별 rate모두 받아오기
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




















