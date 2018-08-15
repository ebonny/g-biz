package com.griffins.config.auth;

import com.griffins.gbiz.admin.member.domain.Member;
import com.griffins.gbiz.admin.menu.domain.FuncVO;
import com.griffins.gbiz.admin.menu.domain.MenuVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 파일명 : com.griffins.config.login.LoginUserDetails
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
@EqualsAndHashCode(of = {"username", "loginid"}, callSuper = false)
public class LoginUserDetails implements UserDetails, Serializable {
    /* 기본 */
    private String username;
    private String password;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled;

    private boolean admin;
    private String otpKey;
    private Collection<GrantedAuthority> authorities;
    private boolean passwordExpired;

    /* 기타 */
    private String name;
    private List<MenuVO> menuTree;
    private List<FuncVO> funcList;
    private List<MenuVO> menuList;
//    private List<String> roles;

    public LoginUserDetails() {
    }

    public LoginUserDetails(Member m) {
        this.username = m.getId();
        this.password = m.getPassword();
        this.name = m.getName();
//        this.roles = m.getRoles().stream().map(Role::getName).collect(Collectors.toList());
//        this.roles = Arrays.asList(m.getRole().split(",")).stream().map(role -> role.trim()).collect(Collectors.toList());
        this.enabled = m.getIslogin().equals("1");
        this.passwordExpired = m.getPasswordExpired().equals("1");
        this.otpKey = m.getOtpKey();
        this.admin = m.getIsAdmin().equals("1");
    }

}
