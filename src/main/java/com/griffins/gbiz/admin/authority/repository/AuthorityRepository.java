package com.griffins.gbiz.admin.authority.repository;

import com.griffins.gbiz.admin.authority.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 파일명 : com.griffins.gsales.admin.authority.repository.AuthorityRepository
 * *
 * {클래스의 기능과 용도 설명}
 * ===============================================
 *
 * @author 이재철
 * @since 2018-07-27 0027
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public interface AuthorityRepository extends JpaRepository<Authority, Integer> {
}
