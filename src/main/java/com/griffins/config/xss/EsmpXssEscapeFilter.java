package com.griffins.config.xss;

import com.navercorp.lucy.security.xss.servletfilter.XssEscapeFilterRule;
import org.apache.commons.lang3.StringUtils;

/**
 * 파일명 : griffins.common.filter.xss.EsmpXssEscapeFilter
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
public final class EsmpXssEscapeFilter {
    private static EsmpXssEscapeFilter xssEscapeFilter;
    private static EsmpXssEscapeFilterConfig config;

    static {
        try {
            xssEscapeFilter = new EsmpXssEscapeFilter();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Default Constructor
     */
    private EsmpXssEscapeFilter() {
        config = new EsmpXssEscapeFilterConfig();
    }

    /**
     * @return XssEscapeFilter
     */
    public static EsmpXssEscapeFilter getInstance() {
        return xssEscapeFilter;
    }

    /**
     * @param url       String
     * @param paramName String
     * @param value     String
     * @return String
     */
    public String doFilter(String url, String paramName, String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }

        XssEscapeFilterRule urlRule = config.getUrlParamRule(url, paramName);
        if (urlRule == null) {
            // Default defender 적용
            return config.getDefaultDefender().doFilter(value);
        }

        if (!urlRule.isUseDefender()) {
            return value;
        }

        return urlRule.getDefender().doFilter(value);
    }

}
