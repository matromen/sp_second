package com.bgkim.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import lombok.extern.log4j.Log4j;

@Log4j

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		log.info("login success........." + authentication.getName());
//		UserDetails principal = (CustomUser)authentication.getPrincipal();
		CustomUser principal = (CustomUser)authentication.getPrincipal();
		log.info("aaaa : " + principal.getMemberVO().getUsername());
		
		List<String> roleNames = new ArrayList<>();
		authentication.getAuthorities().forEach(authroty -> roleNames.add(authroty.getAuthority()));
		
		if(roleNames.contains("admin")) {
			response.sendRedirect("/security/mypage/admin");
			
			return;
		}else {
			response.sendRedirect("/security/mypage/staff");
			
			return;
		}
		
	}

}
