package com.griffins.common.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * 프로젝트명 : gsales
 * 파일명 : com.griffins.common.domain.BaseEntity
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
public class CommonEntity {
    @Column
    private String regId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date regDate;

    @Column
    private String editId;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date editDate;
}
