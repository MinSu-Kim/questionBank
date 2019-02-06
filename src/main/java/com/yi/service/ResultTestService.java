package com.yi.service;

import java.util.List;

import com.yi.domain.CustomerVO;
import com.yi.domain.ResultTestVO;

public interface ResultTestService {

	public List<ResultTestVO> selectByAll();
	public ResultTestVO selectByNo(ResultTestVO resultTestVo);
	public List<ResultTestVO> selectByCustomerCode(CustomerVO customerVo);
	public void insertResultTest(ResultTestVO resultTestVo);
	public void deleteResultTest(ResultTestVO resultTestVo);
	public void updateResultTest(ResultTestVO resultTestVo);
}
