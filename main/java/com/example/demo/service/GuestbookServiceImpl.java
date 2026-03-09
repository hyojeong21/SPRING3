package com.example.demo.service;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.GuestbookRepository;
import com.example.demo.dtos.GuestbookDTO;
import com.example.demo.dtos.PageRequestDTO;
import com.example.demo.dtos.PageResultDTO;
import com.example.demo.entity.Guestbook;
import com.example.demo.entity.QGuestbook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor   // final로 선언된 필드의 초기화 작업을 수행해주는 역할
public class GuestbookServiceImpl implements GuestbookService {

   private final GuestbookRepository guestbookRepository;
   
   @Override
   // DTO 받아서 DB 저장
   public Long register(GuestbookDTO guestbookDTO) {
      log.info("DTO ---------------------------------------");
      log.info(guestbookDTO);
      
      // 상속된 defult 메서드를 이용해서 DTO -> Entity 변환한다
      Guestbook entity = dtoToEntity(guestbookDTO);
      
      log.info(entity);
      
      guestbookRepository.save(entity);
      
      // 저장하면 DB가 자동으로 bno 생성
      return entity.getBno();
   }
   
   @Override
   public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {
      // 이 메서드에서는 요청된 DTO에서 Pageable 객체를 얻어내고, 필요에 따라서 Sorting 객체도 조정
      // 얻어낸 객체에서 전달될 목록을 담고 있는 Page 객체를 얻어낸다.
      // 이 얻어낸 Entity 목록들을 DTO로 변환해주는 함수객체를 만들어서 PageResult 객체에 넘긴다
      // 최신글부터 정렬
      Pageable pageable = requestDTO.getPageable(Sort.by("bno").descending());
      
      // 검색어에 관련된 BooleanBuilder 객체를 얻어낸다
      BooleanBuilder booleanBuilder = getSearch(requestDTO);
      
      Page<Guestbook> result = guestbookRepository.findAll(booleanBuilder, pageable);
      
      // Guestbook → GuestbookDTO 변환 함수
      Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDto(entity));
      
      // Page 데이터 + 변환 함수. PageResultDTO 내부에서 Entity → DTO 자동 변환함
      return new PageResultDTO<>(result, fn);
   }
   
   @Override
   public GuestbookDTO read(Long bno) {
      // DB 조회
      Optional<Guestbook> result = guestbookRepository.findById(bno);
      
      // 데이터 있으면 → DTO 변환, 없으면 → null
      return result.isPresent()?entityToDto(result.get()):null;
   }

   @Override
   public void modify(GuestbookDTO dto) {
      // 글번호에 해당하는 글이 존재하는지 확인 후, 변경될 내용을 entity에 넣고 save한다
      Optional<Guestbook> result = guestbookRepository.findById(dto.getBno());
      
      if(result.isPresent()) {
         Guestbook entity = result.get();
         
         entity.changeTitle(dto.getTitle());
         entity.changeContent(dto.getContent());
         
         guestbookRepository.save(entity);
      }
   }
   
   // 검색어 기능 추가
   private BooleanBuilder getSearch(PageRequestDTO requestDTO) {
	   String type = requestDTO.getType();
	   BooleanBuilder booleanBuilder = new BooleanBuilder();
	   QGuestbook qGuestbook = QGuestbook.guestbook;
	   
	   String keyword = requestDTO.getKeyword();
	   
	   BooleanExpression expression = qGuestbook.bno.gt(0L);
	   
	   booleanBuilder.and(expression);
	   
	   if(type == null || type.trim().length()==0) {
		   // 검색 조건이 없는 경우
		   return booleanBuilder;
	   }
	   
	   // 검색 조건 작성
	   BooleanBuilder conditionBuilder = new BooleanBuilder();
	   
	   if(type.contains("t")) {
		   conditionBuilder.or(qGuestbook.title.contains(keyword));
	   }
	   if(type.contains("c")) {
		   conditionBuilder.or(qGuestbook.content.contains(keyword));
	   }
	   if(type.contains("w")) {
		   conditionBuilder.or(qGuestbook.writer.contains(keyword));
	   }
	   
	   // 모든 조건을 통합한다
	   booleanBuilder.and(conditionBuilder);
	   
	   return booleanBuilder;
   }

   @Override
   public void remove(Long bno) {
      guestbookRepository.deleteById(bno);
   }

}