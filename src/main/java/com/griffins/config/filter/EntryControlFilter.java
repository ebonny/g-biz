package com.griffins.config.filter;

import com.griffins.common.GConstants;
import com.griffins.common.domain.SessionData;
import com.griffins.common.util.LogUtil;
import com.griffins.common.util.SessionUtil;
import com.griffins.common.util.StringUtil;
import com.griffins.config.common.ConfigUtil;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 파일명 : griffins.common.filter.EntryControlFilter
 * *
 * 다른탭의 로그인이 감지된 경우 강제로 로그인페이지로 이동시킨다.
 * ===============================================
 *
 * @author 이재철
 * @since 2017-10-06
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public class EntryControlFilter extends GenericFilterBean {

    private String logoutUrl;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String sessionUserId = request.getParameter("sessionUserId");
        SessionData sd = SessionUtil.getSessionData(request);

        if (StringUtil.isEmpty(logoutUrl))
            logoutUrl = ConfigUtil.CODE.get("url.logout");


        if (StringUtil.isNotEmpty(sessionUserId)) {
            /*HttpSession session = request.getSession();*/
            if (/*session != null && !session.isNew() && sd == null || */sd != null && !sessionUserId.equals(sd.getId())) {
                if (SessionUtil.isAjax(request)) {
                    SessionUtil.sendError(response, GConstants.ERROR_ANOTHER_LOGIN);
                } else {
                    SessionUtil.redirect(request, response, GConstants.ERROR_ANOTHER_LOGIN, SessionUtil.REDIRECT_TO_LOGIN_WITHOUT_LOGOUT);
                }
                return;
            }
        }

        if (request.getServletPath().equals(logoutUrl)) {
            if (sd != null)
                LogUtil.chasing("info")
                        .title("사용자 로그아웃")
                        .append("작업", "로그아웃", "w")
                        .append("IP", SessionUtil.getClientIP(request), "i")
                        .append("이름", sd.getName(), "n")
                        .end();
            else
                LogUtil.chasing("error")
                        .title("사용자 로그아웃")
                        .append("작업", "로그아웃", "w")
                        .append("IP", SessionUtil.getClientIP(request), "i")
                        .append("사용자 정보", "알 수 없음", "u")
                        .end();
        }

        chain.doFilter(req, res);
    }
}
