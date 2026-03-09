package com.example.demo.repository;

import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.demo.GuestbookRepository;
import com.example.demo.entity.Guestbook;
import com.example.demo.entity.QGuestbook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

// @SpringBootTest: Spring Boot 전체 환경을 실행해서 테스트한다는 뜻
@SpringBootTest

public class GuestbookRepositoryTests {

   // Repository 주입. Spring이 자동으로 GuestbookRepository 객체를 넣어줌
   @Autowired
   private GuestbookRepository guestbookRepository;
   
   // @Test: JUnit에서 이 메서드를 실행하라는 의미
   // @Test
   public void insertDummies() {   // 방명록 300개 생성
      IntStream.rangeClosed(1, 300)
      .forEach(i->{
         Guestbook guestbook = Guestbook.builder()
               .title("Title..."+i)
               .content("Content..."+i)
               .writer("user"+(i % 10))
               .build();
         
         // DB 저장 후 출력
         System.out.println(guestbookRepository.save(guestbook));
      });
   }
   
   // 데이터 수정
   // @Test
   public void updateTest() {   // 300번 글 수정
      // SELECT * FROM guestbook WHERE bno = 300 이런 의미
      // Optional은 값이 있을 수도 있고 없을 수도 있는 객체
      Optional<Guestbook> result = guestbookRepository.findById(300L);
      
      if(result.isPresent()) {
         Guestbook guestbook = result.get();
         
         guestbook.changeTitle("변경된 제목..");
         guestbook.changeContent("변경된 내용..");
         
         guestbookRepository.save(guestbook);
      }
   }
   
   // @Test
   public void testQuery1() {   // title에 keyword 포함된 글 검색
      // QueryDSL을 이용한 복잡한 검색 쿼리를 간단히 처리하는 방법을 알아보자
      /*
       * 이에 앞서 사용법부터 간단히 정리하면 아래와 같다
       * 1. BooleanBuilder 객체를 생성한다
       * 2. 조건에 맞는 구문은 QueryDSL에서 Predicate 타입의 함수를 생성해서 사용한다
       * 3. 1번 객체를 2번에 추가하고 실행한다
       */
      
      // 0페이지 10개씩 bno 기준 정렬
      Pageable pageable = PageRequest.of(0, 10, Sort.by("bno"));
      // 검색 키워드
      String keyword = "1";
      
      // 엔티티를 그대로 미러링한 QEntity 객체를 생성한다.. 여기를 대상으로 조회한다
      QGuestbook qGuestbook = QGuestbook.guestbook;
      
      // 불린빌더 생성
      BooleanBuilder booleanBuilder = new BooleanBuilder();
      // 제목에 keyword 포함. SQL로 변환하면 WHERE title LIKE '%1%'
      BooleanExpression booleanExpression = qGuestbook.title.contains(keyword);
      
      // 조건 추가
      booleanBuilder.and(booleanExpression);
      
      Page<Guestbook> page = guestbookRepository.findAll(booleanBuilder, pageable);
      
      // 검색 결과 하나씩 출력
      page.stream().forEach(guestbook -> {
         System.out.println(guestbook);
      });
   }
   
   @Test
   public void testQuery2() {   // title OR content 검색
      Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
      String keyword = "1";
      
      QGuestbook qguestbook = QGuestbook.guestbook;
      
      BooleanBuilder builder = new BooleanBuilder();
      
      // 제목 조건
      BooleanExpression exTitle = qguestbook.title.contains(keyword);
      // 내용 조건
      BooleanExpression exContent = qguestbook.content.contains(keyword);
      
      // OR 조건
      BooleanExpression exAll = exTitle.or(exContent);
      builder.and(exAll);
      
      // bno 조건
      builder.and(qguestbook.bno.gt(0L));
      
      Page<Guestbook> page = guestbookRepository.findAll(builder, pageable);
      
      page.stream().forEach(guestbook -> {
         System.out.println(guestbook);
      });
   }
   
}