package com.griffins.config.auth;

import com.griffins.common.GConstants;
import com.griffins.common.util.SessionUtil;
import com.griffins.config.common.ConfigUtil;
import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 파일명 : griffins.config.security.InvalidSessionHandler
 * *
 * {클래스의 기능과 용도 설명}
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
public class InvalidSessionHandler implements InvalidSessionStrategy {

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = request.getServletPath();

        request.getSession(true);

        //=== 세션 만기후 로그아웃 요청
        if (path.equals(ConfigUtil.CODE.get("url.logout"))) {
            SessionUtil.redirect(request, response, null, SessionUtil.REDIRECT_TO_LOGIN);
            return;
        }

        //=== 중복로그인 처리 : invalid-session-url(or strategy/handler) 가 있으면 expired-url 을 함께 처리해야 한다.
        if (request.getServletPath().equals(ConfigUtil.CODE.get("url.expired"))) {
            if (SessionUtil.isAjax(request))
                SessionUtil.sendError(response, GConstants.ERROR_SESSION_EXPIRED);
            else
                SessionUtil.redirect(request, response, GConstants.ERROR_SESSION_EXPIRED, SessionUtil.REDIRECT_TO_LOGIN);
            return;
        }

        //=== 로그인 이후 세션만료 처리
        if (SessionUtil.isAjax(request)) {
            SessionUtil.sendError(response, GConstants.ERROR_SESSION_TIMEOUT);
        } else {
            SessionUtil.redirect(request, response, GConstants.ERROR_SESSION_TIMEOUT, SessionUtil.REDIRECT_TO_LOGIN);
        }
    }

}