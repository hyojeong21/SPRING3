package com.example.demo.service;

import com.example.demo.dtos.GuestbookDTO;
import com.example.demo.dtos.PageRequestDTO;
import com.example.demo.dtos.PageResultDTO;
import com.example.demo.entity.Guestbook;

// Controller  →  Service  →  Repository  →  DB
// Service는 비즈니스 로직 담당, Controller는 서비스에게 일을 요청함
// Service : 기능 정의, ServiceImpl : 실제 코드
public interface GuestbookService {
   // 글 삭제
   void remove(Long bno);
   
   // 글 수정
   void modify(GuestbookDTO dto);
   
   // 글상세
   GuestbookDTO read(Long bno);
   
   // 하나의 방명록 글이 insert 되는 기능 선언
   Long register(GuestbookDTO guestbookDTO);
   
   // 요청된 페이지의 결과를 리턴하는 메서드 선언
   PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO);
   
   // DTO --> Entity로 변환하는 기본 메서드를 정의한다
   // 이렇게 하면 얘를 상속받은 클래스는 이 메서드를 사용할 수 있음
   // GuestbookDTO → Guestbook으로 변환하는 함수
   default Guestbook dtoToEntity(GuestbookDTO dto) {
      Guestbook entity = Guestbook.builder()
            // DTO의 bno 값을 가져와서 Entity의 bno에 넣는다
            .bno(dto.getBno())
            .title(dto.getTitle())
            .content(dto.getContent())
            .writer(dto.getWriter())
            .build();
      return entity;
   }
   
   // Entity -> DTO로 변환해주는 메서드 정의
   default GuestbookDTO entityToDto(Guestbook entity) {
      GuestbookDTO dto = GuestbookDTO.builder()
            .bno(entity.getBno())
            .title(entity.getTitle())
            .content(entity.getContent())
            .writer(entity.getWriter())
            .regDate(entity.getRegDate())
            .modDate(entity.getModDate())
            .build();
      return dto;
   }
}