package com.yi.persistence;

import java.util.List;

import com.yi.domain.Criteria;
import com.yi.domain.ReplyVO;

	public interface ReplyDao {
	public List<ReplyVO> list(int bno);
	public void create(ReplyVO vo);
	public void update(ReplyVO vo);
	public void delete(int rno);
	public List<ReplyVO> listPage(int bno);//bno를 보내기 위해서 매개변수2개
	public int totalCount(int bno);
}
