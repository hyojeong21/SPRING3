package com.example.demo.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

// Super 클래스로만 존재한다는 의미.. 테이블 만들지 않음
@MappedSuperclass
// Entity 리스너에게 엔티티 객체가 in/update시에 이 엔티티를 참조해서 날짜값을 가져오도록 등록해야 함
@EntityListeners(value = {AuditingEntityListener.class})
@Getter

public abstract class BaseEntity {
	@CreatedDate
	// 테이블의 regdate 컬럼은 이 속성의 값을 가져다 사용하고
	// 엔티티가 update되어도, 이 날짜는 update 금지하라는 의미
	@Column(name = "regdate", updatable = false)
	private LocalDateTime regDate;
	
	// 수정된 날짜를 생성하도록 하는 어노테이션.. 엔티티가 변경되면 이 값도 자동으로 수정됨
	@LastModifiedDate
	@Column(name = "moddate")
	private LocalDateTime modDate;
}