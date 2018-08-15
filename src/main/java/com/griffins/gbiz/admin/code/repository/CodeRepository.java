package com.griffins.gbiz.admin.code.repository;

import com.griffins.common.annotation.Mapper1;
import com.griffins.gbiz.admin.code.domain.Code;
import com.griffins.gbiz.admin.code.domain.CodeId;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 파일명 : com.griffins.gsales.admin.code.repository.CodeRepository
 * *
 * {클래스의 기능과 용도 설명}
 * ===============================================
 *
 * @author 이재철
 * @since 2018-07-26 0026
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
@Mapper1
@Mapper
public interface CodeRepository extends JpaRepository<Code, CodeId> {

    List<Code> findByCodeIdCodeGroupOrderBySort(String codeGroup);
}
