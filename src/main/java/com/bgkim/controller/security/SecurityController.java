package com.bgkim.controller.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j

@RequestMapping("/security")
/**
 * 
 * @author 김병구
 * 
 * login, logout 처리(post) 위한 정의는 controller에 정의 하지 않아도 된다.
 * ex) post /login <-- 정의된 곳을 모름, post /logout <--security-context정의
 */
public class SecurityController {
	/*
	 * security-context.xml에서 get /security/login call 함
	 * login page -> post-> authentication-manager -> login fail
	 * post /login 시행 후  실패시 묵시적으로 해당 페이지 호출 됨.
	 * logout 진행 후에도 묵시적으로 해당 페이지 콜
	 */
	@GetMapping("/login")
	public void login(String error, String logout, Model model) {
		log.info("error : " + error);
		log.info("logout : " + logout);
		
		if(error != null) {
			model.addAttribute("error", "로그인 실패");
		}
		
		if(logout != null) {
			model.addAttribute("logout", "로그아웃 함.");
		}
	}
	
	@GetMapping("/accessDenied")
	public String accessDenied(Authentication authentication) {
		log.info("Access Denied : "  + authentication);
		
		return "/security/accessDenied_page";
	}
	

	@GetMapping("/mypage/admin")
	public void doAdmin() {
		log.info("admin page ... ");
	}
	
	/*
	 * logout을 위한  get 방식의 로그아웃  page 호출
	 */
	@GetMapping("/logout")
	public void doLogoutPage() {
		log.info("logout page call...");
	}
}
