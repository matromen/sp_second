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
 * @author �躴��
 * 
 * login, logout ó��(post) ���� ���Ǵ� controller�� ���� ���� �ʾƵ� �ȴ�.
 * ex) post /login <-- ���ǵ� ���� ��, post /logout <--security-context����
 */
public class SecurityController {
	/*
	 * security-context.xml���� get /security/login call ��
	 * login page -> post-> authentication-manager -> login fail
	 * post /login ���� ��  ���н� ���������� �ش� ������ ȣ�� ��.
	 * logout ���� �Ŀ��� ���������� �ش� ������ ��
	 */
	@GetMapping("/login")
	public void login(String error, String logout, Model model) {
		log.info("error : " + error);
		log.info("logout : " + logout);
		
		if(error != null) {
			model.addAttribute("error", "�α��� ����");
		}
		
		if(logout != null) {
			model.addAttribute("logout", "�α׾ƿ� ��.");
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
	 * logout�� ����  get ����� �α׾ƿ�  page ȣ��
	 */
	@GetMapping("/logout")
	public void doLogoutPage() {
		log.info("logout page call...");
	}
}
