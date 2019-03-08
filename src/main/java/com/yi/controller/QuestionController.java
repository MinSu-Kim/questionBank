package com.yi.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yi.domain.Criteria;
import com.yi.domain.PageMaker;
import com.yi.domain.QuestionVO;
import com.yi.service.QuestionService;
import com.yi.util.MediaUtils;

@Controller
@RequestMapping("/question/*")
public class QuestionController {
	
private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
	
	@Autowired
	private QuestionService service;
	@Autowired
	private DataSource ds;
	//�⺻���� ���Թ������� servlet-context�� ��ϵ� �̸����� ����
	@Resource(name="uploadPath")
	private String uploadPath;
	
	@RequestMapping(value="register", method=RequestMethod.GET)
	public void registerGet(){
		logger.info("QuestionVO create------------GET");
	}
	
	//�����߰�
	@RequestMapping(value="register", method=RequestMethod.POST)
	public String registerPost(QuestionVO vo, String number,MultipartFile pictureFile){
		logger.info("QuestionVO create------------POST");
		//�����, ���´� �Է¾��� �⺻ 0��/����ó��
		vo.setState("����");
		vo.setCorrectRate(0);
		//�������� ��� ����
		String picture = pictureFile.getOriginalFilename();
		if(picture.equals("")==false) {
			System.out.println("=============pictureFile.getOriginalFilename()"+picture);
			vo.setPicture(uploadPath+"/"+picture);
			logger.info("QuestionVO create------------"+vo);
		}
		//����/ȸ��/��ȣ�� �̿��Ͽ� Code�Է��ϱ�		
		String threeNum = String.format("%03d",Integer.parseInt(number));
		System.out.println("Q"+vo.getSubject()+vo.getYear()+vo.getRound()+threeNum);
		vo.setQuestionCode("Q"+vo.getSubject()+vo.getYear()+vo.getRound()+threeNum);
		//insert����
		service.insert(vo);
		System.out.println(vo);
		
		return "redirect:/question/register";
	}
	
	//�����߰�
	@RequestMapping(value="registerfile", method=RequestMethod.GET)
	public void registerfileGet(){
		logger.info("registerfileGet------------GET");
	}	
	
	//�����߰� ���� ���ε�, upload���Ͽ� ���ϸ��� �ø� �� �ø����Ϸ� insert ����, ���� ���� ����
	@RequestMapping(value="registerfile", method=RequestMethod.POST)
	public void registerfilePost(String filePath, HttpServletRequest req) throws IOException{
		logger.info("registerfilePost------------Post");
		//���� upload������ ����
		//���ϻ����׽�Ʈ
		String uploadPath2 = req.getRealPath("upload");
		logger.info("req.getRealPath(upload)------------"+uploadPath2);
		logger.info("uploadPath------------"+uploadPath);
		logger.info("������1------------"+new File(".").getCanonicalPath());
		logger.info("������2------------"+new File(".").getPath());
		String upload = uploadPath + "/new";
		File dir = new File(upload);
		dir.mkdirs();
		
		String current;
		try {
			current = new java.io.File( "." ).getCanonicalPath();
			 System.out.println("Current dir:"+current);
			 String currentDir = System.getProperty("user.dir");
		     System.out.println("Current dir using System:" +currentDir);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//insert�κ�
		try (Connection con = ds.getConnection();	
				Statement stmt = con.createStatement()) {
			String tableName = "question"; 
			//String filePath = "C:/ProgramData/MySQL/MySQL Server 5.7/Uploads/question201901_5.csv";
			String sql = String.format("LOAD DATA LOCAL INFILE '%s' IGNORE INTO TABLE %s "
					+ "character set 'UTF8' "
					+ "fields TERMINATED by ',' ENCLOSED BY '\"' "
					+ "LINES TERMINATED BY '\\r\\n' "
					+ "IGNORE 1 lines "
					+ "(question_code, question_title, choice1, choice2, choice3, choice4, correct, state, correct_rate, picture,`year`,round, subject)",filePath, tableName);
			
			stmt.executeQuery(sql);
			System.out.println(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}	
	
	//jsp�� ���� �޼ҵ�
	@RequestMapping(value="moketest", method=RequestMethod.GET)
	public void moketest(Criteria cri,Model model){
		System.out.println("cri===========:"+cri);
		List<QuestionVO> list = service.selectByYearAndRound(2018, 3, cri);///////������ ȸ�� �ܺο��� �ޱ�!!!!
		
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(service.totalCount(2018,3));
		
		model.addAttribute("list", list);
		model.addAttribute("pageMaker", pageMaker);
		model.addAttribute("cri", cri);
	}
	
	//jsp�� ���� �޼ҵ�
		@RequestMapping(value="list", method=RequestMethod.GET)
		public void list(Criteria cri,Model model){
			System.out.println("cri===========:"+cri);
			List<QuestionVO> list = service.selectByYearAndRound(2018, 3, cri);///////������ ȸ�� �ܺο��� �ޱ�!!!!
			
			PageMaker pageMaker = new PageMaker();
			pageMaker.setCri(cri);
			pageMaker.setTotalCount(service.totalCount(2018,3));
			
			model.addAttribute("list", list);
			model.addAttribute("pageMaker", pageMaker);
			model.addAttribute("cri", cri);
		}
	
	//�������� �ѹ�����
	@RequestMapping(value="singletest", method=RequestMethod.GET)
	public void singleTest(Criteria cri,Model model){
			System.out.println("cri===========:"+cri);
			cri.setPerPageNum(1);
			List<QuestionVO> list = service.selectByRandom();
			model.addAttribute("list", list);
			model.addAttribute("cri", cri);
	}
	
	//���� ���� : �������� �� �ٷ� insert�Ǵ� controller
	@RequestMapping(value="subjecttest", method=RequestMethod.GET)
	public void subjecttest(Criteria cri,Model model){
		logger.info("subjecttest get------------");
		List<QuestionVO> list = service.selectBySubject("D");//�ܺο��� �� �ޱ�!
		
		PageMaker pageMaker = new PageMaker();
		cri.setPerPageNum(1);
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(service.totalCount(2018,3));
		
		model.addAttribute("list", list);
		model.addAttribute("pageMaker", pageMaker);
		model.addAttribute("cri", cri);
	}
		
	//json�� ������ �޼ҵ� - list.jsp���� ���
	@ResponseBody
	@RequestMapping(value="listJson/{year}/{round}", method=RequestMethod.GET)
	public ResponseEntity<List<QuestionVO>> listJson(Criteria cri,@PathVariable("year") int year, @PathVariable("round") int round){
		ResponseEntity<List<QuestionVO>> entity = null;
		
		try {
			List<QuestionVO> list = service.selectByYearAndRound(year, round, cri);
			entity = new ResponseEntity<>(list, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);//List<QuestionVO>�� ������ �ϳ�, ��������������HttpStatus�� ����
		}
		
		return entity;
	}
	
	//page�� ajax�� ó���ϴ� list
	@ResponseBody
	@RequestMapping(value="listJson/{year}/{round}/{page}", method=RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> listJsonPage(Criteria cri,@PathVariable("year") int year, @PathVariable("round") int round, @PathVariable("page") int page){
		ResponseEntity<Map<String, Object>> entity = null;
		
		try {
			cri.setPage(page);
			List<QuestionVO> list = service.selectByYearAndRound(year, round, cri);
			//����������
			PageMaker pageMaker = new PageMaker();
			pageMaker.setCri(cri);
			//����Ʈ������ ������������ ���� ���������� map�� ����
			HashMap<String, Object> map = new HashMap<>();
			map.put("list", list);
			map.put("pageMaker", pageMaker);
			entity = new ResponseEntity<>(map, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);//List<QuestionVO>�� ������ �ϳ�, ��������������HttpStatus�� ����
		}
		
		return entity;
	}
	
	//json���� ���� ����select ������ �޼ҵ� - subjecttest.jsp���� ���
	@ResponseBody
	@RequestMapping(value="subjecttest/{subject}", method=RequestMethod.GET)
	public ResponseEntity<List<QuestionVO>> listBySubjectJson(@PathVariable("subject") String subject){
			ResponseEntity<List<QuestionVO>> entity = null;
			
			try {
				List<QuestionVO> list = service.selectBySubject(subject);
				entity = new ResponseEntity<>(list, HttpStatus.OK);
			} catch (Exception e) {
				// TODO: handle exception 
				e.printStackTrace();
				entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
	
	@ResponseBody
	@RequestMapping("/displayFile")
	public ResponseEntity<byte[]> displayFile(String filename, Model model){
		 ResponseEntity<byte[]> entity = null;
		 logger.info("displayFile : "+ filename);
		 try {
			 try {
				 String format = filename.substring(filename.lastIndexOf(".")+1);
					String picture = filename.substring(filename.lastIndexOf("/"));
					MediaType mType = MediaUtils.getMediaType(format);
					
					HttpHeaders headers = new HttpHeaders();
					InputStream in = null;
					in = new FileInputStream(uploadPath+"/"+picture);
					headers.setContentType(mType);
					
					entity = new ResponseEntity<byte[]>(IOUtils.toByteArray(in), headers, HttpStatus.CREATED);
					in.close();
			} catch (StringIndexOutOfBoundsException e) {
				return null;
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			entity = new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
		}
		 return entity;
	}

}


















