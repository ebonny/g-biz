package com.griffins.gbiz.admin.authority.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 파일명 : com.griffins.gsales.admin.authority.domain.AuthorityVO
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
@EqualsAndHashCode(of = "seq")
public class AuthorityVO {
    private Integer seq;
    private String name;
    private String dsc;
    private String regId;
    private String regDate;
    private String editId;
    private String editDate;
}
