package com.bgkim.service.board;

import java.util.List;

import com.bgkim.domain.board.SpBoardAttachVO;
import com.bgkim.domain.board.SpBoardParam;
import com.bgkim.domain.board.SpBoardVO;

public interface SpBoardService {

	public int getCount(SpBoardParam pbean);
	
	public List<SpBoardVO> getList(SpBoardParam pbean);
	
	public int writePro(SpBoardVO vo);
	
	public SpBoardVO getDetail(long seq, char flag);
	
	public int updatePro(SpBoardVO vo);
	
	public int deletePro(SpBoardVO vo);
	
	public List<SpBoardAttachVO> getAttachViewList(long seq);

	public void deleteFiles(List<SpBoardAttachVO> seq);
}
