package com.example.demo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.dtos.GuestbookDTO;
import com.example.demo.dtos.PageRequestDTO;
import com.example.demo.dtos.PageResultDTO;
import com.example.demo.entity.Guestbook;

@SpringBootTest

public class GuestbookServiceTests {

	@Autowired	// 구현객체 자동주입
	private GuestbookService guestbookService;
	
	@Test
	public void testRegister() {
		GuestbookDTO guestbookDTO = GuestbookDTO.builder()
				.title("Sample Title...")
				.content("Sample Content...")
				.writer("jason")
				.build();
		
		// service의 등록 메서드 호출
		System.out.println(guestbookService.register(guestbookDTO));
	}
	
	// 요청 페이지에 따른 글 목록 리턴하는 로직 테스트하기
	@Test
	public void testList() {
		PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();
		PageResultDTO<GuestbookDTO, Guestbook> resultDTO = guestbookService.getList(pageRequestDTO);
		
		System.out.println("PREV: " + resultDTO.isPrev());
		System.out.println("NEXT: " + resultDTO.isNext());
		System.out.println("TOTAL: " + resultDTO.getTotalPage());
		System.out.println("=====================================================================");
		
		for(GuestbookDTO guestbookDTO : resultDTO.getDtoList()) {
			System.out.println(guestbookDTO);
		}
		
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		resultDTO.getPageList().forEach(i -> System.out.println(i));
	}
	
}