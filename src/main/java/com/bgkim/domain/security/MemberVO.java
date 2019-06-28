package com.bgkim.domain.security;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class MemberVO {
	private String userid;
	private String userpw;
	private String username;
	private Date regdate;
	private Date updatedate;
	private String enabled;
	
	private List<MemberAuthVO> authList;
}
