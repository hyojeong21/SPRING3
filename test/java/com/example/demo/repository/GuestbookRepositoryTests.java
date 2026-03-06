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

@SpringBootTest

public class GuestbookRepositoryTests {

	@Autowired
	private GuestbookRepository guestbookRepository;
	
	// @Test
	public void insertDummies() {
		IntStream.rangeClosed(1, 300)
		.forEach(i->{
			Guestbook guestbook = Guestbook.builder()
					.title("Title..."+i)
					.content("Content..."+i)
					.writer("user"+(i % 10))
					.build();
			
			System.out.println(guestbookRepository.save(guestbook));
		});
	}
	
	// @Test
	public void updateTest() {
		Optional<Guestbook> result = guestbookRepository.findById(300L);
		
		if(result.isPresent()) {
			Guestbook guestbook = result.get();
			
			guestbook.changeTitle("변경된 제목..");
			guestbook.changeContent("변경된 내용..");
			
			guestbookRepository.save(guestbook);
		}
	}
	
	// @Test
	public void testQuery1() {
		// QueryDSL을 이용한 복잡한 검색 쿼리를 간단히 처리하는 방법을 알아보자
		/*
		 * 이에 앞서 사용법부터 간단히 정리하면 아래와 같다
		 * 1. BooleanBuilder 객체를 생성한다
		 * 2. 조건에 맞는 구문은 QueryDSL에서 Predicate 타입의 함수를 생성해서 사용한다
		 * 3. 1번 객체를 2번에 추가하고 실행한다
		 */
		
		Pageable pageable = PageRequest.of(0, 10, Sort.by("bno"));
		String keyword = "1";
		
		// 엔티티를 그대로 미러링한 QEntity 객체를 생성한다.. 여기를 대상으로 조회한다
		QGuestbook qGuestbook = QGuestbook.guestbook;
		
		// 불린빌더 생성
		BooleanBuilder booleanBuilder = new BooleanBuilder();
		BooleanExpression booleanExpression = qGuestbook.title.contains(keyword);
		
		booleanBuilder.and(booleanExpression);
		
		Page<Guestbook> page = guestbookRepository.findAll(booleanBuilder, pageable);
		
		page.stream().forEach(guestbook -> {
			System.out.println(guestbook);
		});
	}
	
	@Test
	public void testQuery2() {		
		Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
		String keyword = "1";
		
		QGuestbook qguestbook = QGuestbook.guestbook;
		
		BooleanBuilder builder = new BooleanBuilder();
		
		BooleanExpression exTitle = qguestbook.title.contains(keyword);
		BooleanExpression exContent = qguestbook.content.contains(keyword);
		
		BooleanExpression exAll = exTitle.or(exContent);
		builder.and(exAll);
		
		builder.and(qguestbook.bno.gt(0L));
		
		Page<Guestbook> page = guestbookRepository.findAll(builder, pageable);
		
		page.stream().forEach(guestbook -> {
			System.out.println(guestbook);
		});
	}
	
}