package com.bgkim.domain.board;

import lombok.Data;

@Data

public class SpBoardAttachVO {
	private String uuid;
	private long seq;
	private String uploadPath;
	private String fileName;
	private String fileType;
	
}
