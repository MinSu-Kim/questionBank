package com.yi.service;

import java.util.List;
import java.util.Map;

import com.yi.domain.CustomerVO;
import com.yi.domain.ResultTestVO;
import com.yi.domain.TestScroeDTO;

public interface ResultTestService {

	public List<ResultTestVO> selectByAll();
	public ResultTestVO selectByNo(ResultTestVO resultTestVo);
	public List<ResultTestVO> selectByCustomerCode(CustomerVO customerVo);
	public void insertResultTest(ResultTestVO resultTestVo);
	public void deleteResultTest(ResultTestVO resultTestVo);
	public void updateResultTest(ResultTestVO resultTestVo);
	//����, ������, ȸ���� Ǭ �ѹ������� ���� ���� ���ϱ�
	public TestScroeDTO selectScore(String customerCode, int year, int round);
	//maxNum���ϱ�
	public int selectMaxCode();
	//����ȣ�� ������ȣ�� �̿��Ͽ� update
	public void updateByCustomerAndQuestion(ResultTestVO resultTestVo);
	//���� �����Ʈ ����Ʈ
	public List<ResultTestVO> selectIncorrectQuestionByCustomer(String customerCode);
	//batch insert
	public void insertBatchResultTest(Map<String, Object> map);
}
