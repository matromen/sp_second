package com.bgkim.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bgkim.domain.board.SpBoardReplyVO;

public interface SpBoardReplyMapper {
	public int replyTotalSelect(long seq);
	
	public List<SpBoardReplyVO> replyListSelect(@Param("rParam") com.bgkim.domain.Param rParam, @Param("seq") long seq);
	
	public int writeInsert(SpBoardReplyVO boardReplyVO);
	
	public SpBoardReplyVO replyDetailSelect(long rseq);
	
	public int modReplyUpdate(SpBoardReplyVO boardReplyVO);
	
	public int remReplyDelete(SpBoardReplyVO boardReplyVO);
	
	public String registerSelect(long rseq);
	
	public int remReplyAllDelete(long seq);
}
