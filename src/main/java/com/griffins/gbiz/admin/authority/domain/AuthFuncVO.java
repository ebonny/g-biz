package com.griffins.gbiz.admin.authority.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 파일명 : com.griffins.gsales.admin.authority.domain.AuthFuncVO
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
@Data
@EqualsAndHashCode(of = {"authSeq", "funcSeq"})
public class AuthFuncVO {
    private Integer authSeq;
    private Integer funcSeq;
    private List<Integer> funcSeqs;
}
