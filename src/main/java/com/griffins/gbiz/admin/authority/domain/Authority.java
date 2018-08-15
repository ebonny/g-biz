package com.griffins.gbiz.admin.authority.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * 파일명 : com.griffins.gsales.admin.authority.domain.Authority
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
@Entity
@Data
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seq;

    @Column
    private String name;
    @Column
    private String dsc;
}
