package com.bgkim.controller.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bgkim.domain.ListPageConfig;
import com.bgkim.domain.board.SpBoardAttachVO;
import com.bgkim.domain.board.SpBoardParam;
import com.bgkim.domain.board.SpBoardVO;
import com.bgkim.security.CustomUser;
import com.bgkim.service.board.SpBoardService;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j

@PreAuthorize("isAuthenticated()")
@RequestMapping("/board")
public class SpBoardController {
	
	@Setter(onMethod_ = @Autowired)
	private SpBoardService service;
	
	
	@GetMapping("/list")
	public void list(@ModelAttribute("pbean") SpBoardParam pbean, Model model, RedirectAttributes rttr) {
		
		int totalCount = service.getCount(pbean);
		
		if(totalCount>0) {
			model.addAttribute("voList", service.getList(pbean));
		}
		
		model.addAttribute("listPageConfig", new ListPageConfig(pbean, totalCount));
		
		rttr.addFlashAttribute("Test", "Test");
	}
			
	@GetMapping("/writeForm")
	public void writeForm(@ModelAttribute("pbean") SpBoardParam pbean, Model model) {
		
	}
	
	@PostMapping("/writePro")
	public String writePro(@ModelAttribute("pbean") SpBoardParam pbean, SpBoardVO vo, Authentication authentication, RedirectAttributes rttr) {
		CustomUser user = (CustomUser)authentication.getPrincipal();
		vo.setRegister(user.getUsername());

		int rg = service.writePro(vo);

		rttr.addFlashAttribute("result", rg > 0);
		
		return "redirect:/board/list" + pbean.getParamUrl();
	}
	
	@GetMapping("/detailForm")
	public void detailForm(@ModelAttribute("pbean") SpBoardParam pbean, long seq, Model model) {
		model.addAttribute("vo", service.getDetail(seq));
	}
	
	@GetMapping("/updateForm")
	public void updateForm(@ModelAttribute("pbean") SpBoardParam pbean, long seq, Model model) {
		model.addAttribute("vo", service.getDetail(seq));
	}
	
	@PostMapping("/updatePro")
	@PreAuthorize("principal.username == #vo.register || hasAuthority('admin')") //hasAnyRole 차이 확인 "메모"
	public String updatePro(@ModelAttribute("pbean") SpBoardParam pbean, SpBoardVO vo, Authentication authentication, RedirectAttributes rttr) {
		
		CustomUser user = (CustomUser)authentication.getPrincipal();
		vo.setRegister(user.getUsername());
		
		int rg = service.updatePro(vo);
		
		rttr.addFlashAttribute("result", rg > 0);
		
		return "redirect:/board/detailForm" + pbean.getParamUrl() + "&seq=" + vo.getSeq();
	}
	
	@PostMapping("/deletePro")
	@PreAuthorize("principal.username == #vo.register || hasAuthority('admin')")
	public String deletePro(@ModelAttribute("pbean") SpBoardParam pbean, SpBoardVO vo, Authentication authentication, RedirectAttributes rttr) {
		int rg = service.deletePro(vo);
		
		rttr.addFlashAttribute("result", rg > 0);
		
		return "redirect:/board/list" + pbean.getParamUrl();
	}
	
}
