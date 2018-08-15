package com.griffins.gbiz.admin.menu.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 프로젝트명 : gsales
 * 파일명 : com.griffins.gsales.admin.menu.domain.FuncVO
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
@Data
@EqualsAndHashCode(of = "seq")
public class FuncVO {
    private Integer seq;
    private Integer menuSeq;
    private String title;
    private String type;
    private String dsc;
    private String url;
    private Integer lv;

}
