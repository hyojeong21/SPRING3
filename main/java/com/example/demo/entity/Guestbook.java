package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Guestbook extends BaseEntity {
   // 저장될 정보는 글번호, 제목, 내용, 작성자와 작성일, 수정일이다
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long bno;
   @Column(length=100, nullable=false)
   private String title;
   @Column(length=1500, nullable=false)
   private String content;
   @Column(length=50, nullable=false)
   private String writer;
}