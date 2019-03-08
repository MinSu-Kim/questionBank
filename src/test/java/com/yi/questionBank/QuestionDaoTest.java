package com.yi.questionBank;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yi.domain.QuestionVO;
import com.yi.service.QuestionService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QuestionDaoTest {
	
	@Autowired
	private QuestionService service;
	@Autowired
	private DataSource ds;
	
	//@Test
	public void test01selectByAll() {
		List<QuestionVO> list = service.selectByAll();
		System.out.println(list);
		Assert.assertNotNull(list);
	}
	
	//@Test
	public void test02selectByNo() { 
		QuestionVO vo = new QuestionVO();
		vo.setQuestionCode("QA20183001");
		vo = service.selectByNO(vo);
		System.out.println(vo);
		Assert.assertNotNull(vo);
	} 
	
	//@Test
	public void test03insert() {
		QuestionVO vo = new QuestionVO();
		vo.setQuestionCode("QA20183002");
		vo.setQuestionTitle("���� �����ͺ��̽� �������� �� �� �����̼��� �⺸Ű�� �����ϴ� ��� �Ӽ� ���� ��(NULL) ���̳� �ߺ� ���� ���� �� ���ٴ� ������?");
		vo.setChoice1("Ű ���� ����");
		vo.setChoice2("�� �� ���Ἲ ���� ����");
		vo.setChoice3("���� ���� ����");
		vo.setChoice4("��ü ���Ἲ ���� ����");
		vo.setCorrectRate(0);
		vo.setState("����");
		vo.setCorrect(1);
		service.insert(vo);
	}
	
	//@Test
	public void test04update() {
		QuestionVO vo = new QuestionVO();
		vo.setQuestionCode("QA20183002");
		vo.setQuestionTitle("���� �����ͺ��̽� �������� �� �� �����̼��� �⺸Ű�� �����ϴ� ��� �Ӽ� ���� ��(NULL) ���̳� �ߺ� ���� ���� �� ���ٴ� ������?");
		vo.setChoice1("Ű ���� ����");
		vo.setChoice2("�� �� ���Ἲ ���� ����");
		vo.setChoice3("���� ���� ����");
		vo.setChoice4("��ü ���Ἲ ���� ����");
		vo.setCorrectRate(0);
		vo.setState("����");
		vo.setCorrect(4);
		vo.setRound(3);
		vo.setYear(2018);
		vo.setSubject("D");
		service.update(vo);
	}
	
	//@Test
	public void test05delete() {
		QuestionVO vo = new QuestionVO();
		vo.setQuestionCode("QA20183002");
		vo.setQuestionTitle("���� �����ͺ��̽� �������� �� �� �����̼��� �⺸Ű�� �����ϴ� ��� �Ӽ� ���� ��(NULL) ���̳� �ߺ� ���� ���� �� ���ٴ� ������?");
		vo.setChoice1("Ű ���� ����");
		vo.setChoice2("�� �� ���Ἲ ���� ����");
		vo.setChoice3("���� ���� ����");
		vo.setChoice4("��ü ���Ἲ ���� ����");
		vo.setCorrectRate(0);
		vo.setState("����");
		vo.setCorrect(4);
		service.delete(vo);
	}
	
	@Test
	public void test06upload() {
		try (Connection con = ds.getConnection();	
				Statement stmt = con.createStatement()) {
			String tableName = "question"; 
			String filePath = "C:/ProgramData/MySQL/MySQL Server 5.7/Uploads/question201901_5.csv";
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
}









