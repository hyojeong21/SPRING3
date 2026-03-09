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
// @RequestMapping: 기본 URL
@RequestMapping("/guestbook")
// 생성자 자동 생성
@RequiredArgsConstructor

public class GuestbookController {

   private final GuestbookService guestbookService;
   
   // @PostMapping: POST 요청 처리
   @PostMapping("/remove")
   // @RequestParam: URL 파라미터 받기
   // @RequestParam("bno") Long bno: URL 파라미터 값을 가져온다
   public String remove(@RequestParam("bno") Long bno,
      // 페이지 정보 유지
           @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO,
           RedirectAttributes redirectAttributes) {
      
      guestbookService.remove(bno);
      
       // redirect 정보 전달
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
   
   // @GetMapping: GET 요청 처리
   @GetMapping({"/read","/modify"})
   public void read(@RequestParam("bno") long bno, 
         @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {
      // DB 조회
      GuestbookDTO dto = guestbookService.read(bno);
      // Model에 데이터 전달
      model.addAttribute("dto",dto);
   }
   
   // 등록폼매핑
   @GetMapping("/register")
   public void register() {      // 글 작성 화면을 보여주는 역할

   }

   @PostMapping("/register") // 등록된 이후엔 List 로 리다이랙트한다
   public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttributes) {
      Long bno = guestbookService.register(dto);

      // Flash Attribute: redirect 후 한번만 전달되는 데이터
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