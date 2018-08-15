package com.griffins.gbiz.admin.member.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 파일명 : com.griffins.gsales.admin.member.domain.MemberVO
 * *
 * {클래스의 기능과 용도 설명}
 * ===============================================
 *
 * @author 이재철
 * @since 2018-07-30 0030
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
@Data
@EqualsAndHashCode(of = "id")
public class MemberVO {
    private String id;
    private String name;
    private String email;
    private String password;
    private Integer authSeq;
    private String islogin;

}
