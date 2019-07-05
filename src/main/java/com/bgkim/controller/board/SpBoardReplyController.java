package com.bgkim.controller.board;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bgkim.domain.ListPageConfig;
import com.bgkim.domain.Param;
import com.bgkim.domain.board.SpBoardReplyDTO;
import com.bgkim.domain.board.SpBoardReplyVO;
import com.bgkim.service.board.SpBoardReplyService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@RestController

@PreAuthorize("isAuthenticated()")
@RequestMapping("/reply")

@Log4j

@AllArgsConstructor

public class SpBoardReplyController {
	
	SpBoardReplyService replyService;
	
	
	@GetMapping(value="/replyList/{seq}/{page}", produces= {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<SpBoardReplyDTO> getList(@PathVariable("seq") long seq, @PathVariable("page") int page){
		
		if(page <= 0) page = 1;
		Param rParam = new Param(page, 10, "", "");
		
		SpBoardReplyDTO spBoardReplyDTO = new SpBoardReplyDTO();
		
		spBoardReplyDTO.setListReply(replyService.getSeqReplyList(rParam, seq));
		spBoardReplyDTO.setListReplyPageConfig(new ListPageConfig(rParam, replyService.getSeqReplyTotal(seq)));
		
		return new ResponseEntity<SpBoardReplyDTO>(spBoardReplyDTO, HttpStatus.OK);
	}
	
	
	@PostMapping(value="/replyWritePro", consumes= "application/json", produces= {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> writePro(@RequestBody SpBoardReplyVO boardReplyVO){
		int rflag = replyService.writePro(boardReplyVO);
		
		return (rflag > 0) ? new ResponseEntity<>("SUCCESS", HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@GetMapping(value="/{rseq}", produces= {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<SpBoardReplyVO> getUpdateForm(@PathVariable("rseq") Long rseq){
		return new ResponseEntity<>(replyService.getReplyUpdateForm(rseq), HttpStatus.OK);
	}
	
	
	@RequestMapping(value="/{rseq}", method= {RequestMethod.PUT, RequestMethod.PATCH }, consumes="application/json")
	@PreAuthorize("principal.username == #boardReplyVO.register || hasAuthority('admin')") //hasAnyRole 차이 확인 "메모"
	public ResponseEntity<String> updatePro(@RequestBody SpBoardReplyVO boardReplyVO, Authentication authentication){
		
		boardReplyVO.setRegister(authentication.getName());
		
		int rflag = replyService.replyUpdatePro(boardReplyVO);
		
		return rflag> 0 ? new ResponseEntity<>("SUCCESS", HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@DeleteMapping(value="/{rseq}", consumes="application/json")
	@PreAuthorize("principal.username == #boardReplyVO.register || hasAuthority('admin')") //hasAnyRole 차이 확인 "메모"
	public ResponseEntity<String> deletePro(@RequestBody SpBoardReplyVO boardReplyVO){
	
		int rflag = replyService.replyDeletePro(boardReplyVO);
		
		return rflag>0 ? new ResponseEntity<>("SUCCESS", HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	
	}
}
