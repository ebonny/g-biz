package com.griffins.config.auth;


import com.griffins.common.GConstants;
import com.griffins.common.util.SessionUtil;
import com.griffins.config.common.ConfigUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 파일명 : griffins.config.security.handler.AccessDeniedHandlerImpl
 * *
 * CSRF 토큰이 인증되지 않았거나 없는 경우에 대한 처리를 핸들링 하는 클래스.
 * <p>
 * 주요역할 : 로그인정보 변경처리
 * Ajax 요청은 EntryControlFilter 가 처리하고, 그 외에 나머지를 여기서 처리한다.
 * ===============================================
 *
 * @author 이재철
 * @since 2017-10-02
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        //=== 로그아웃 요청인 경우 에러핸들링을 하지않음
        if (request.getServletPath().equals(ConfigUtil.CODE.get("url.logout"))) {
            SessionUtil.redirect(request, response, null, SessionUtil.REDIRECT_TO_LOGIN);
            return;
        }

        //=== CSRF 공격이거나 새탭의 로그인으로 인해 만료된 사용자의 요청인 경우
        if (e instanceof InvalidCsrfTokenException || e instanceof MissingCsrfTokenException) {
            if (SessionUtil.isAjax(request))
                SessionUtil.sendError(response, GConstants.ERROR_FORBIDDEN);
            else {
                SessionUtil.redirect(request, response, SessionUtil.toJsonMessage(GConstants.ERROR_ANOTHER_LOGIN, e), SessionUtil.REDIRECT_TO_LOGIN_WITHOUT_LOGOUT);
            }
        }
        //=== ROLE 권한이 없는 URL 로 접근한 경우
        else {
            if (SessionUtil.isAjax(request))
                SessionUtil.sendError(response, GConstants.ERROR_NO_AUTH);
            else {
                SessionUtil.redirect(request, response, SessionUtil.toJsonMessage(GConstants.ERROR_NO_AUTH, e), SessionUtil.REDIRECT_TO_LOGIN);
            }
        }
    }
}

