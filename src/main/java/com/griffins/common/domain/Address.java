package com.griffins.common.domain;

import lombok.Data;

import javax.persistence.Embeddable;

/**
 * 프로젝트명 : gsales
 * 파일명 : com.griffins.common.domain.Address
 * *
 * {클래스의 기능과 용도 설명}
 * ===============================================
 *
 * @author 이재철
 * @since #{DATE}
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
@Embeddable
@Data
public class Address {
    private String base;
    private String zip;
    private String detail;
}
