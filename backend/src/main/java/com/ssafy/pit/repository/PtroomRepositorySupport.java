package com.ssafy.pit.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.pit.entity.Ptroom;
import com.ssafy.pit.entity.QPtroom;

@Repository
public class PtroomRepositorySupport {
	
	@Autowired
	private JPAQueryFactory query;
	
	QPtroom qPtroom= QPtroom.ptroom;
	
	public Ptroom getPtroomByClassNo(int classNo) {
		Ptroom ptroom = query.selectFrom(qPtroom).where(qPtroom.classes.classNo.eq(classNo)).fetchOne();
		return ptroom;
	}
	
}
