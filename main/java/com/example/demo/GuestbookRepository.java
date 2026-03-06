package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.example.demo.entity.Guestbook;

public interface GuestbookRepository extends JpaRepository<Guestbook, Long>,QuerydslPredicateExecutor<Guestbook>{

}