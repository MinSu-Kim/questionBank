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
import com.yi.domain.RateDTO;
import com.yi.domain.ResultTestVO;
import com.yi.domain.TestVO;
import com.yi.service.CustomerService;
import com.yi.service.ResultTestService;
import com.yi.service.TestService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ResultTestDaoTest {

	@Autowired
	private ResultTestService service;
	@Autowired
	private CustomerService cService;
	@Autowired
	private TestService tService;
	
	//@Test
	public void test01insert() {
		ResultTestVO vo = new ResultTestVO();
		vo.setResultTestCode(0);
		
		CustomerVO cVo = new CustomerVO();
		cVo.setCustomerCode("C001");
		cVo = cService.selectByNo(cVo);
		
		vo.setCustomer(cVo);
		vo.setAnswer(1);
		vo.setCorrect(1);
		vo.setSpendTime(120);
		vo.setPass(true);
		
		TestVO tVo = new TestVO();
		tVo.setTestCode("T001");
		tVo = tService.selectByNo(tVo);
		
		service.insertResultTest(vo);
	}
	
	//@Test
	public void test02selectByAll() {
		List<ResultTestVO> list = service.selectByAll();
		Assert.assertNotNull(list);
	}
	
	//@Test
	public void test03selectByNo() {
		ResultTestVO vo = new ResultTestVO();
		vo.setResultTestCode(1);
		vo = service.selectByNo(vo);
		System.out.println(vo);
		Assert.assertNotNull(vo);
	}
	
	//@Test
	public void test04selectByCustomer() {/////////////////////////////////////?customer�� �ڲ� null?
		CustomerVO cVo = new CustomerVO();
		cVo.setCustomerCode("C001");
		cVo = cService.selectByNo(cVo);
		System.err.println(cVo); //CustomerVO [customerCode=C001, customerName=ȫ�浿, id=hong3, password=11112222, email=hong@test.com, employee=false] ���
		List<ResultTestVO> list = service.selectByCustomerCode(cVo);
		System.err.println("Ȯ�ο�"+list);//ResultTestVO [resultTestCode=R001, customer=null, score=99, pass=true, test=null]
		Assert.assertNotNull(list);
	}
	
	//@Test
	public void test05update() { /////////////////////////////////////?�Ӿ� �־ȵ�
		ResultTestVO vo = new ResultTestVO();
		vo.setResultTestCode(1);
		vo = service.selectByNo(vo);
		System.err.println("������Ʈ vo"+vo);//test�� customer null
		service.updateResultTest(vo);
	}
	
	//@Test
	public void test06delete() {
		ResultTestVO vo = new ResultTestVO();
		vo.setResultTestCode(2);
		service.deleteResultTest(vo);
	}
	
	//@Test
	public void test07selectIncorrect() {
		List<String> list = service.selectIncorrectQuestionBySubject("C002", "D");
		System.out.println(list);
	}
	
	@Test
	public void test07selectCorrectRate() {
		RateDTO dto = service.selectCorrectRateBySubject("C002", "D");
		System.out.println(dto);
	}
}























