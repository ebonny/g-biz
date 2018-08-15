package com.griffins.common.domain;

import com.griffins.common.util.StringUtil;
import com.griffins.gbiz.admin.menu.domain.MenuMap;
import com.griffins.gbiz.admin.menu.domain.MenuVO;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 파일명 : SessionData.java
 * <p>
 * 세션에 저장할 사용자 정보 관리
 * ===============================================
 *
 * @author 이재철
 * <p>
 * 수정자         수정일         수정내용
 * -------------  -------------  -----------------
 * <p>
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
@Data
@Builder
@EqualsAndHashCode(of = "id")
public class SessionData {
    private String id;
    private String name;
    private String email;
    private String lastLoginIp;
    @Getter(AccessLevel.NONE)
    private MenuVO curMenu;
    private MenuMap menuMap;

    private List<MenuVO> menuTree;
    private List<MenuVO> exceptMenus;
    private List<MenuVO> naviList;

    private Boolean isAdmin;

    public MenuVO getCurMenu() {
        return curMenu == null ? new MenuVO() : curMenu;
    }

    public void setCurMenuStr(String menuId) {
        String[] arr = menuId.split("[.]");
        curMenu = menuMap.get(arr[0]);
        curMenu.setCurLevel(arr[1]);
        if (naviList == null)
            naviList = new ArrayList<>();
        else
            naviList.clear();
        for (MenuVO rootMenu : menuTree) {
            if (makeNaviList(rootMenu, curMenu.getSeq())) {
                naviList.add(rootMenu);
                break;
            }
        }
        Collections.reverse(naviList);
    }

    public List<MenuVO> getCurMenuNaviList() {
        return naviList;
    }

    public String getCurMenuNaviDesc() {
        String navi = "";
        if (naviList == null || naviList.size() == 0)
            return navi;
        for (MenuVO vo : naviList) {
            navi += vo.getTitle() + " > ";
        }
        if (StringUtil.isNotEmpty(navi))
            navi = navi.substring(0, navi.length() - 3);
        return navi;
    }

    private boolean makeNaviList(MenuVO parentMenu, Integer curMenuId) {
        if (parentMenu.getMenuList() == null || parentMenu.getMenuList().isEmpty())
            return parentMenu.getSeq().equals(curMenuId);
        for (MenuVO childMenu : parentMenu.getMenuList()) {
            if (makeNaviList(childMenu, curMenuId)) {
                naviList.add(childMenu);
                return true;
            }
        }
        return false;
    }
}
