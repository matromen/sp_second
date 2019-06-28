package com.bgkim.security;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.bgkim.domain.security.MemberVO;

import lombok.Getter;
import lombok.extern.log4j.Log4j;

@Getter
@Log4j
public class CustomUser extends User{


	private static final long serialVersionUID = 1L;
	
	private MemberVO memberVO;
	
	public CustomUser(MemberVO vo) {

		
		super(vo.getUserid(), vo.getUserpw(), 
				vo.getAuthList().stream().map(auth -> new SimpleGrantedAuthority(auth.getAuth())).collect(Collectors.toList()));
		
		
		this.memberVO = vo;
		log.info(vo.getUserpw());
		log.info(vo.getUserid());
	}
}
