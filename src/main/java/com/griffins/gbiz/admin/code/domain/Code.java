package com.griffins.gbiz.admin.code.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

/**
 * 파일명 : com.griffins.gsales.admin.code.domain.Code
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
@Entity
@Data
public class Code {
    @EmbeddedId
    private CodeId codeId = new CodeId();

    @Column
    private String codeDesc;
    @Column
    private Integer sort;
}
