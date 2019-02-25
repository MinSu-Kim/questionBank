package com.yi.questionBank;

import java.util.List;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yi.domain.CustomerVO;
import com.yi.domain.QuestionVO;
import com.yi.domain.ReqUpdateVO;
import com.yi.service.ReqUpdateService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReqUpdateDaoTest {
	
	@Autowired
	private ReqUpdateService service;
	
	@Test
	public void test01selectByQuestionCode() {
		List<ReqUpdateVO> list = service.selectByQuestionCode("QD20183001");
		Assert.assertNotNull(list);
	}
	
	@Test
	public void test02insert() {
		ReqUpdateVO vo = new ReqUpdateVO();
		
		QuestionVO qvo = new QuestionVO();
		qvo.setQuestionCode("QD20183010");
		
		vo.setQuestion(qvo);
		vo.setContent("���� Ʋ�� �� ���ƿ�");
		vo.setReqCorrect(1);
		vo.setOriCorrect(2);
		
		CustomerVO cvo = new CustomerVO();
		cvo.setCustomerCode("C001");
		vo.setWriter(cvo);
		vo.setState("��û");
		
		service.insert(vo);
	}
	
	//@Test
	public void test03update() {
		ReqUpdateVO vo = new ReqUpdateVO();
		
		vo.setContent("���� Ʋ�� �� ���ƿ�");
		vo.setReqCorrect(1);
		vo.setOriCorrect(2);
		
		vo.setState("����");
		vo.setReqCode(2);
		
		service.update(vo);
	}
	
	//@Test
	public void test04selectByCode() {
		ReqUpdateVO vo = service.selectByCode(2);
		Assert.assertNotNull(vo);
	}
	
	//@Test
	public void test05delete() {
		service.delete(2);
	}
	
}



























