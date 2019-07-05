package com.bgkim.service.board;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.bgkim.domain.Param;
import com.bgkim.domain.board.SpBoardReplyVO;
import com.bgkim.mapper.SpBoardReplyMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service
@Log4j

public class SpBoardReplyServiceImpl implements SpBoardReplyService {

	@Setter(onMethod_ = @Autowired)
	SpBoardReplyMapper replyMapper;
	
	@Override
	public int getSeqReplyTotal(long seq) {
		return replyMapper.replyTotalSelect(seq);
	}

	@Override
	public List<SpBoardReplyVO> getSeqReplyList(Param rParam, long seq) {
		return replyMapper.replyListSelect(rParam, seq);
	}

	@Override
	public int writePro(SpBoardReplyVO boardReplyVO) {
		return replyMapper.writeInsert(boardReplyVO);
	}

	@Override
	public SpBoardReplyVO getReplyUpdateForm(long rseq) {
		return replyMapper.replyDetailSelect(rseq);
	}

	@Override
	public int replyUpdatePro(SpBoardReplyVO boardReplyVO) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(!authentication.getAuthorities().contains(new SimpleGrantedAuthority("admin"))) {
			log.info("no admin");
			
			String register = replyMapper.registerSelect(boardReplyVO.getRseq());
			if(!register.equals(authentication.getName())) {
				throw new AccessDeniedException(authentication.getName());
			}
			
		}
		
		return replyMapper.modReplyUpdate(boardReplyVO);
	}

	@Override
	public int replyDeletePro(SpBoardReplyVO boardReplyVO) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if(!authentication.getAuthorities().contains(new SimpleGrantedAuthority("admin"))) {
			log.info("no admin");
			
			String register = replyMapper.registerSelect(boardReplyVO.getRseq());
			if(!register.equals(authentication.getName())) {
				throw new AccessDeniedException(authentication.getName());
			}
			
		}
		
		
		return replyMapper.remReplyDelete(boardReplyVO);
	}
	
}
