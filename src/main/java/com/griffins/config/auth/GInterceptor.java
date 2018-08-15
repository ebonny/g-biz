package com.griffins.config.auth;

import com.griffins.common.GConstants;
import com.griffins.common.domain.SessionData;
import com.griffins.common.util.SessionUtil;
import com.griffins.config.common.GSessionManager;
import com.griffins.gbiz.admin.menu.domain.FuncVO;
import com.griffins.gbiz.admin.menu.domain.MenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 파일명 : com.griffins.config.common.GInterceptor
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
public class GInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private GSessionManager sessionManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler == null)
            return false;

        if(mustPass(request, handler))
            return true;

        if(SessionUtil.getSessionData(request) == null) {
            sessionManager.logout(request, response, GConstants.ERROR_DISCONNECTED);
            return false;
        }

        if (!isPermittedUrl(request)) {
            SessionData sd = SessionUtil.getSessionData(request);
            if (SessionUtil.isAjax(request)) {
                SessionUtil.sendError(response, GConstants.ERROR_NO_AUTH);
            } else {
                SessionUtil.redirect(request, response, GConstants.ERROR_NO_AUTH, SessionUtil.REDIRECT_ALERT_BACK);
            }
            return false;
        }

        return true;

    }

    private boolean mustPass(HttpServletRequest request, Object handler) {
        return SessionUtil.isAjax(request) || ((HandlerMethod)handler).getMethodAnnotation(PermitAll.class) != null;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        SessionData sd = SessionUtil.getSessionData(request);
    }

    private boolean isPermittedUrl(HttpServletRequest request) {
        boolean isPermittedURL = false;

        String url = request.getServletPath();
        SessionData sessionData = SessionUtil.getSessionData(request);

        if (sessionData.getIsAdmin()) {
            String menuSeq = getPermittedMenuSeq(sessionData.getMenuTree(), url);
            if (menuSeq != null) {
                if (menuSeq.equals("#"))
                    return false;
                sessionData.setCurMenuStr(menuSeq);
                return true;
            }
            return false;
        }

        String menuSeq = getPermittedMenuSeq(sessionData.getMenuTree(), url);
        if (menuSeq != null && !menuSeq.equals("#")) {
            sessionData.setCurMenuStr(menuSeq);
            isPermittedURL = true;
        }

        return isPermittedURL;
    }

    /**
     * 현재 사용자가 요청한 주소가 접근가능한 주소목록에 있는지 검사한다
     *
     * @param url
     * @return 메뉴의 link 중에 url과 일치하는게 있는지 여부
     * @date 2016. 8. 15.
     * @author 이재철
     */
    private String getPermittedMenuSeq(List<MenuVO> menuTree, String url) {
        String menuSeq = null;
        for (MenuVO menu : menuTree) {
            if ((menuSeq = checkRecursiveUrl(menu, url)) != null) {
                break;
            }
        }
        return menuSeq;
    }

    /**
     * 재귀를 돌면서 각 최상위 메뉴의 하위메뉴들의 기능을 가져와서 각 기능의 URL과 요청주소를 비교한다
     *
     * @param menu 메뉴 트리에서 최상위 -> 최하위 순으로 재귀반복
     * @param url  사용자가 접근을 요청한 URL
     * @return String 요청주소가 접근가능한 기능주소 목록에 있을 경우 해당 메뉴의 id 반환
     * @date 2016. 8. 15.
     * @author 이재철
     */
    private String checkRecursiveUrl(MenuVO menu, String url) {
        List<MenuVO> subMenuList = menu.getMenuList();
        if (subMenuList.size() == 0) {    // 최하위 메뉴일 경우
            List<FuncVO> funcs = menu.getFuncList();
            if (funcs != null)
                for (FuncVO func : funcs) {
                    if (isSameUrl(url, func.getUrl())) {
                        return String.valueOf(func.getMenuSeq() + "." + func.getLv());
                    }
                }
        } else {                        // 상위 메뉴일 경우
            for (MenuVO m : subMenuList) {
                String menuSeq = null;
                if ((menuSeq = checkRecursiveUrl(m, url)) != null)
                    return menuSeq;
            }
        }
        return null;
    }

    private boolean isSameUrl(String url, String link) {
        return url.equalsIgnoreCase(link);
    }
}
