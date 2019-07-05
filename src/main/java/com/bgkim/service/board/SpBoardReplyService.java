package com.bgkim.service.board;

import java.util.List;

import com.bgkim.domain.Param;
import com.bgkim.domain.board.SpBoardReplyVO;

public interface SpBoardReplyService {
	public int getSeqReplyTotal(long seq);
	
	public List<SpBoardReplyVO> getSeqReplyList(Param rParam, long seq);
	
	public int writePro(SpBoardReplyVO boardReplyVO);
	
	public SpBoardReplyVO getReplyUpdateForm(long rseq);
	
	public int replyUpdatePro(SpBoardReplyVO boardReplyVO);
	
	public int replyDeletePro(SpBoardReplyVO boardReplyVO);
}
