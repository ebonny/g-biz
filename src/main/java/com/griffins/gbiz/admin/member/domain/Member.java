package com.griffins.gbiz.admin.member.domain;

import com.griffins.common.domain.TagChipVO;
import lombok.Data;
import org.hibernate.annotations.Formula;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * 파일명 : com.griffins.gsales.admin.member.domain.Member
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
@Entity
public class Member {
    @Id
    private String id;

    @Formula("1")
    private Integer seq;

    @Column
    private String name;
    @Column
    private String password;
    @Column
    private String email;
    @Column
    private String islogin;
    @Column
    private String lastip;
    @Column
    private String lastdate;
    @Column
    private Integer loginfailcount = 0;
    @Column
    private String otpKey;
    @Column
    private String isdelete = "0";

    @Formula("CASE WHEN EXISTS(SELECT 1 FROM member_super ms WHERE ms.id = id) THEN '1' ELSE '0' END")
    private String isAdmin;

    @Formula("(SELECT CASE WHEN changepassworddate >= DATE_FORMAT(CURDATE(), '%Y%m%d') THEN '1' ELSE '0' END)")
    private String passwordExpired;

    @Formula("REGEXP_REPLACE(CONCAT(" +
            "   CASE WHEN EXISTS(SELECT 1 FROM seller s WHERE s.id = id) THEN 'seller,' ELSE '' END" +
            ", " +
            "   CASE WHEN EXISTS(SELECT 1 FROM client c WHERE c.id = id) THEN 'customer,' ELSE '' END" +
            ", " +
            "   CASE WHEN EXISTS(SELECT 1 FROM vendor v WHERE v.id = id) THEN 'vendor,' ELSE '' END" +
            "), ',$', '')")
    private String roles;

    public boolean isVendor() {
        return roles.contains("vendor");
    }

    @Transient
    private List<TagChipVO> authList = new ArrayList<>();

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "member_role",
//            joinColumns = @JoinColumn(name = "member_id"),
//            inverseJoinColumns = @JoinColumn(name = "role_name")
//    )
//    private Set<Role> roles = new HashSet<>();
}
