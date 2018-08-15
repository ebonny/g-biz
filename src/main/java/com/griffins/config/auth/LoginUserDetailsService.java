package com.griffins.config.auth;

import com.griffins.common.util.CommonUtil;
import com.griffins.common.util.StringUtil;
import com.griffins.config.auth.repository.LoginUserDetailsDAO;
import com.griffins.config.common.ConfigUtil;
import com.griffins.gbiz.admin.member.repository.MemberRepository;
import com.griffins.gbiz.admin.menu.domain.FuncVO;
import com.griffins.gbiz.admin.menu.domain.MenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 파일명 : com.griffins.config.login.LoginUserDetailsService
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
public class LoginUserDetailsService implements UserDetailsService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private LoginUserDetailsDAO loginUserDetailsDAO;

//    private String usernamePrefix = ConfigUtil.CODE.get("username.prefix");

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        boolean isSimplecheck = false;
//        if(loginUserDetailsDAO == null) {
//            loginUserDetailsDAO = (LoginUserDetailsDAO) SpringUtil.getBeanByType("com.griffins.config.auth.repository.LoginUserDetailsDAO");
//        }

        if (username.startsWith(ConfigUtil.CODE.get("username.prefix"))) {
            isSimplecheck = true;
            username = username.substring(ConfigUtil.CODE.get("username.prefix").length());
        }

        LoginUserDetails user = memberRepository.findById(username)
                .filter(m -> m != null)
                .map(u -> new LoginUserDetails(u)).get();

        if (user == null) {
            throw new UsernameNotFoundException(ConfigUtil.MSG.getMessage("security.nouser.msg"));
        }
        if (!user.isEnabled()) {
            throw new UsernameNotFoundException(ConfigUtil.MSG.getMessage("security.notallow.msg"));
        }

        List<Integer> auths = loginUserDetailsDAO.selectMemberAuthorities(username);
//        List<String> roles = user.getRoles();
//        user.setAuthorities(getAuthoritiesFrom(roles));
//        user.setAdmin(roles.stream().filter(role -> role.equalsIgnoreCase("SUPER")).count() > 0);

        if (/*StringUtil.isEmpty(roles) || */!user.isAdmin() && auths.size() == 0) {
            throw new UsernameNotFoundException(ConfigUtil.MSG.getMessage("1005"));
        }

        if (isSimplecheck)
            return user;

        List<MenuVO> menuList = loadMenuByAuthorities(user, auths);
        List<FuncVO> funcList = loadFuncByAuthorities(user, auths);
        if (funcList != null && menuList != null) {
            CommonUtil.mapFuncToMenu(funcList, menuList);
            List<MenuVO> menuTree = getHierarchicalMenu(menuList);
            user.setMenuList(menuList);
            user.setMenuTree(menuTree);
            user.setFuncList(funcList);
        }

        return user;

    }

    private List<GrantedAuthority> getAuthoritiesFrom(List<String> authlist) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String authority : authlist) {
            authorities.add(new SimpleGrantedAuthority(authority));
        }
        return authorities;
    }

    private List<MenuVO> loadMenuByAuthorities(LoginUserDetails user, List<Integer> authorities) {
        if (user.isAdmin()) {
            return loginUserDetailsDAO.selectMenuByAdmin();
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("authorities", authorities);
//            map.put("roles", user.getRoles());
            return loginUserDetailsDAO.selectMenuByAuthorities(map);
        }
    }

    private List<FuncVO> loadFuncByAuthorities(LoginUserDetails user, List<Integer> authorities) {
        if (user.isAdmin()) {
            return loginUserDetailsDAO.selectFuncByAdmin();
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("authorities", authorities);
            return loginUserDetailsDAO.selectFuncByAuthorities(map);
        }
    }

    private List<MenuVO> getHierarchicalMenu(List<MenuVO> menuList) {
        List<MenuVO> newList = new ArrayList<>();
        for (MenuVO menu : menuList) {
            if (menu.getParentSeq() == 0) {
                newList.add(getRecursiveMenu(menu, menuList));
            }
        }
        return newList;
    }

    private MenuVO getRecursiveMenu(MenuVO parentMenu, List<MenuVO> menuList) {
        if (StringUtil.isEmpty(parentMenu.getUrl())) {
            for (MenuVO menu : menuList) {
                if (parentMenu.getSeq().intValue() == menu.getParentSeq().intValue()) {
                    parentMenu.getMenuList().add(getRecursiveMenu(menu, menuList));
                }
            }
        }
        return parentMenu;
    }


}
