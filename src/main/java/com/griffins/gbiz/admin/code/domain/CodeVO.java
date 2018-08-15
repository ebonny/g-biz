package com.griffins.gbiz.admin.code.domain;

import lombok.Data;

import java.util.List;

/**
 * 파일명 : com.griffins.gsales.admin.code.domain.CodeVO
 * *
 * {클래스의 기능과 용도 설명}
 * ===============================================
 *
 * @author 이재철
 * @since 2018-05-27
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
@Data
public class CodeVO {
    private String codeGroup;
    private String codeDesc;
    private String codeValue;
    private Integer menuSeq;
    private String isuse;
    private String chkDisabled;
    private Integer sort;

    private String oldCodeGroup;
    private String oldCodeValue;
    private String menuList;
    private List<String> valueList;
    private List<String> groupList;

}
