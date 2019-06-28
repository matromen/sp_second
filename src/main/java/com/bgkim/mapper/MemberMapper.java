package com.bgkim.mapper;

import java.util.List;

import com.bgkim.domain.security.MemberVO;

public interface MemberMapper {
	public MemberVO memberSelect(String userid);
	
	public List<String> useridSelect(String userid);
}
