package com.bgkim.domain.board;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class SpBoardVO {
	private long seq;
	private String title;
	private String content;
	private String register;
	private Date rdate;
	private Date udate;
	
	private String userName;
	
	private List<SpBoardAttachVO> attachList;
	
	//private SpBoardAttachVO[] attachList2;
}
