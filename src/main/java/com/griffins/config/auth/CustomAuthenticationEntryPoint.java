package com.griffins.config.auth;


import com.griffins.common.GConstants;
import com.griffins.common.util.LogUtil;
import com.griffins.common.util.SessionUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 파일명 : griffins.config.security.EsmpBasicAuthenticationEntryPoint
 * *
 * ROLE 위반한 접근 처리
 * 다른 탭에서 로그아웃한 경우도 여기서 처리
 * ===============================================
 *
 * @author 이재철
 * @since 2017-10-04
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
@Component
public class CustomAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        //=== 다른 탭에서 로그아웃한 경우 : 접근 비허용 처리로 대체
      /*if(SessionUtil.getSessionData(request) == null) {
         if(SessionUtil.isAjax(request))
            SessionUtil.sendError(response, EsmpConstants.ERROR_ALREADY_LOGOUT);
         else
            SessionUtil.redirect(request, response, EsmpConstants.ERROR_ALREADY_LOGOUT, SessionUtil.REDIRECT_TO_LOGIN);
         return;
      }*/

        //=== ROLE 이 허용되지 않은 접근 처리
        Object principal = null;
        Authentication authentication = null;
        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
            principal = authentication.getPrincipal();
        } catch (Exception ex) {
        }

        if (principal == null && SessionUtil.getSessionData(request) == null) {  // 다른탭에서 로그아웃한 경우
            LogUtil.error(request.getServletPath() + " 접근오류가 발생했습니다. 로그인전일 경우 SecurityConfig 에서 antMatchers 에 접근하려는 URL 이 permitAll 로 매핑되어있나 확인해주세요.", LogUtil.CONSOLE_FILE);
            if (SessionUtil.isAjax(request)) {
                SessionUtil.sendError(response, GConstants.ERROR_ALREADY_LOGOUT, LogUtil.getStackTrace(e));
            } else {
                SessionUtil.redirect(request, response, GConstants.ERROR_ALREADY_LOGOUT, SessionUtil.REDIRECT_TO_LOGIN_WITHOUT_LOGOUT);
            }
        } else {                                                                // ROLE 접근거부 처리
            if (SessionUtil.isAjax(request)) {
                SessionUtil.sendError(response, GConstants.ERROR_UNAUTHORIZED, LogUtil.getStackTrace(e));
            } else {
                SessionUtil.redirect(request, response, SessionUtil.toJsonMessage(GConstants.ERROR_UNAUTHORIZED, e), SessionUtil.REDIRECT_ALERT_BACK);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("ESMP-Authentication-EntryPoint");
        super.afterPropertiesSet();
    }
}
