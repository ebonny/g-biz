package com.griffins.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 파일명 : com.griffins.common.domain.CalendarVO
 * *
 * {클래스의 기능과 용도 설명}
 * ===============================================
 *
 * @author 이재철
 * @since 2018-07-31 0031
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
@Data
@EqualsAndHashCode(of = "id")
public class CalendarVO {
    private Boolean allDay;
    private String title;
    private String id;
    private String start;
    private String end;
    private String backgroundColor;
    private String borderColor;
    private String textColor;
    private String year;
    private String month;
    private String day;
    private String sort;
}
