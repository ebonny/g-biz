package com.griffins.gbiz.admin.menu.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 파일명 : com.griffins.gsales.admin.menu.domain.MenuVO
 * *
 * {클래스의 기능과 용도 설명}
 * ===============================================
 *
 * @author 이재철
 * @since 2018-05-22
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
@Data
@EqualsAndHashCode(of = "seq")
public class MenuVO {
    private Integer seq;
    private Integer parentSeq;
    private Integer firstSeq;
    private String title;
    private String icon;
    private String type;
    private String url;
    private String isuse;
    private String ispage;
    private Integer sort;

    private String curLevel;
    private String modes;
    private String parentName;

    private List<MenuVO> menuList = new ArrayList<>();
    private List<FuncVO> funcList = new ArrayList<>();
}
