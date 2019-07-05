package com.bgkim.domain.board;

import java.util.Date;

import lombok.Data;

@Data

public class SpBoardReplyVO {
	private long rseq;
	private long seq;
	private String content;
	private String register;
	private Date regDate;
	private Date updateDate;
	
	private String userName;
}
