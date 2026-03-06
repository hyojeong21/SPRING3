package com.example.demo.dtos;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import lombok.Data;

// 이 클래스는 페이지 처리 결과를 Page<Entity> 타입으로 변환하게 되는 Repository의 데이터를 서비스 계층에서 처리하기 위해서 정의함
@Data
public class PageResultDTO<DTO, EN> {	// DTO와 Entity를 타입 제네릭으로 선언함
	
	private List<DTO> dtoList;
	
	private int totalPage;	// 총 페이지 개수
	private int page;		// 현재 페이지
	private int size;		// 목록 사이즈
	
	// 시작 페이지 번호, 끝 페이지 번호
	private int start, end;
	
	// 이전, 다음
	private boolean prev, next;
	
	// 페이지 목록 번호
	private List<Integer> pageList;

	public PageResultDTO(Page<EN> result, Function<EN, DTO> fn) {
		dtoList = result.stream().map(fn).collect(Collectors.toList());
		
		totalPage = result.getTotalPages();
		makePageList(result.getPageable());
	}
	
	// 페이지 목록 구하는 건 메서드 정의
	private void makePageList(Pageable pageable) {
		this.page = pageable.getPageNumber() + 1;
		this.size = pageable.getPageSize();
		
		int tempEnd = (int)(Math.ceil(page/10.0)) * 10;
		
		start = tempEnd - 9;
		prev = start > 1;
		end = totalPage > tempEnd ? tempEnd : totalPage;
		next = totalPage > tempEnd;
		
		pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
	}

}