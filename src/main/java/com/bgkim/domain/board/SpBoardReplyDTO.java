package com.bgkim.domain.board;

import java.util.List;

import com.bgkim.domain.ListPageConfig;

import lombok.Data;

@Data

public class SpBoardReplyDTO {
	private List<SpBoardReplyVO> listReply;
	private ListPageConfig listReplyPageConfig;
}
