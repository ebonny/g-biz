package com.griffins.config.xss;

import javax.servlet.*;
import java.io.IOException;

/**
 * 파일명 : griffins.common.filter.EsmpXssEscapeServletFilter
 * *
 * {클래스의 기능과 용도 설명}
 * ===============================================
 *
 * @author 이재철
 * @since 2017-10-08
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public class EsmpXssEscapeServletFilter implements Filter {

    private EsmpXssEscapeFilter xssEscapeFilter = EsmpXssEscapeFilter.getInstance();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new EsmpXssEscapeServletFilterWrapper(request, xssEscapeFilter), response);
    }

    @Override
    public void destroy() {
    }
}
