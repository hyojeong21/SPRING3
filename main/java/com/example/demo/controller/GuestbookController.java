package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dtos.GuestbookDTO;
import com.example.demo.dtos.PageRequestDTO;
import com.example.demo.service.GuestbookService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/guestbook")
@RequiredArgsConstructor

public class GuestbookController {

	private final GuestbookService guestbookService;
	
	@PostMapping("/remove")
	public String remove(@RequestParam("bno") Long bno,
	        @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO,
	        RedirectAttributes redirectAttributes) {
		
		guestbookService.remove(bno);

	    redirectAttributes.addAttribute("page", pageRequestDTO.getPage());

	    return "redirect:/guestbook/list";
	}
	
	@PostMapping("/modify")
	public String modify(GuestbookDTO dto, 
			@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO,
			RedirectAttributes redirectAttributes) {
		guestbookService.modify(dto);
		
		redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
		redirectAttributes.addAttribute("bno", dto.getBno());
		
		return "redirect:/guestbook/list";
	}

	@GetMapping({"/read","/modify"})
	public void read(@RequestParam("bno") long bno, 
			@ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {
		GuestbookDTO dto = guestbookService.read(bno);
		model.addAttribute("dto",dto);
	}
	
	// 등록폼매핑
	@GetMapping("/register")
	public void register() {

	}

	@PostMapping("/register") // 등록된 이후엔 List 로 리다이랙트한다
	public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttributes) {
		Long bno = guestbookService.register(dto);

		redirectAttributes.addFlashAttribute("msg", bno);
		return "redirect:/guestbook/list";
	}

	@GetMapping("/")
	public String index() {
		return "redirect:/guestbook/list";
	}

	@GetMapping({ "/", "/list" })
	public void list(PageRequestDTO pageRequestDTO, Model model) {
		model.addAttribute("result", guestbookService.getList(pageRequestDTO));
	}

}