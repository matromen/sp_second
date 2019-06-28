package com.bgkim.domain.board;

import lombok.Data;

@Data
public class SpBoardAttachDTO {
	private String fileName;
	private String uploadPath;
	private String uuid;
	private boolean image;
}
