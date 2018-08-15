package com.griffins.gbiz.admin.member.repository;

import com.griffins.gbiz.admin.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 파일명 : com.griffins.gsales.admin.auth.repository.MemberRepository
 * *
 * {클래스의 기능과 용도 설명}
 * ===============================================
 *
 * @author 이재철
 * @since 2018-05-22
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public interface MemberRepository extends JpaRepository<Member, String> {
}
