package com.griffins.config.filter;


import com.griffins.common.util.SessionUtil;
import com.griffins.config.common.ConfigUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 파일명 : griffins.common.filter.InvalidSessionHandlerSupportFilter
 * <p>
 * 세션 만료이후 2개 이상의 비동기 요청이 들어왔을 때
 * 두 번째 이후의 요청쓰레드는 InvalidSessionHandler 에서 처리하지 못한다.
 * 그로 인해 ROLE 의 권한거부 관리자인 EsmpAuthenticationEntryPoint 로 넘어가게 되어 권한오류로 간주되어 버린다.
 * 이를 방지하지 위해 두 번째 이후의 요청쓰레드를 처리하는 세션관리 보조역할을 수행.
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
public class InvalidSessionHandlerSupportFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println(ConfigUtil.CODE.get("url.expired"));
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        if (SessionUtil.isAjax(request)) {
            try {
                chain.doFilter(req, res);
            } catch (AccessDeniedException | AuthenticationException e) {
                System.out.println(e);
            }
        } else {
            chain.doFilter(req, res);
        }

    }

    @Override
    public void destroy() {

    }
}
