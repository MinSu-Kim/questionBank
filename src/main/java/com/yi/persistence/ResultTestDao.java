package com.yi.persistence;

import java.util.List;
import java.util.Map;

import com.yi.domain.CustomerVO;
import com.yi.domain.RateDTO;
import com.yi.domain.ResultTestVO;
import com.yi.domain.TestScroeDTO;

public interface ResultTestDao {
	
	public List<ResultTestVO> selectByAll();
	public ResultTestVO selectByNo(ResultTestVO resultTestVo);
	public List<ResultTestVO> selectByCustomerCode(CustomerVO customerVo);
	public void insertResultTest(ResultTestVO resultTestVo);
	public void deleteResultTest(ResultTestVO resultTestVo);
	public void updateResultTest(ResultTestVO resultTestVo);
	
	//maxNum���ϱ�
	public int selectMaxCode();
	//����ȣ�� ������ȣ�� �̿��Ͽ� update
	public void updateByCustomerAndQuestion(ResultTestVO resultTestVo);
	//���� �����Ʈ ����Ʈ
	public List<ResultTestVO> selectIncorrectQuestionByCustomer(String customerCode);
	public List<String> selectIncorrectQuestionBySubject(String customerCode, String subject);
	//batch insert
	public void insertBatchResultTest(Map<String, Object> map);
	//���� Ʋ�� ���� select
	public List<String> selectIncorrectTopRank(String subject);
	//���� ���� �����
	public RateDTO selectCorrectRateBySubject(String customerCode, String subject);
}
