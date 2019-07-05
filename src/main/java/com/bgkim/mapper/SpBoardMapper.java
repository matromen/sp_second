package com.bgkim.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bgkim.domain.board.SpBoardParam;
import com.bgkim.domain.board.SpBoardVO;

public interface SpBoardMapper {
	public int totalSelect(SpBoardParam pbean);
	
	public List<SpBoardVO> listSelect(SpBoardParam pbean);
	
	public int writeInsert(SpBoardVO vo);
	
	public SpBoardVO detailSelect(long seq);	
	
	public int updateUpdate(SpBoardVO vo);
	
	public int deleteDelete(long seq);
	
	public String registerSelect(long seq);
	
	public int getCountRegisterSeq(@Param("seq") long seq, @Param("regiseter") String register);
	
	public void writeSpBoardCountInsert(@Param("seq") long seq, @Param("register") String register);
	
	public void modSpBoardCountUpdate(long seq);
	
	public int remSpBoardCountAllDelete(long seq);
}
