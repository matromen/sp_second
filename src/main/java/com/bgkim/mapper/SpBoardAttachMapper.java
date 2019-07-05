package com.bgkim.mapper;

import java.util.List;

import com.bgkim.domain.board.SpBoardAttachVO;

public interface SpBoardAttachMapper {
	public int attachInsert(SpBoardAttachVO vo);
	
	public List<SpBoardAttachVO> attachViewListSelect(long seq);
	
	public void seqAllAttachDelete(long seq);
	
	public List<SpBoardAttachVO> getYesterDayFiles();
}

