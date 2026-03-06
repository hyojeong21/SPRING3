package com.example.demo.dtos;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data

public class PageRequestDTO {

	private int page;
	private int size;
	
	// 생성자를 이용해서 필드값 초기화
	public PageRequestDTO() {
		this.page = 1;
		this.size = 10;
	}
	
	// 페이징 처리가 가능하도록 Pageable 객체를 리턴하는 메서드 정의
	public Pageable getPageable(Sort sort) {
		return PageRequest.of(this.page-1, size, sort);
	}
	
}