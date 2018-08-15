package com.griffins.config.auth;


import com.griffins.common.util.SessionUtil;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

/**
 * 파일명 : CsrfSecurityRequestMatcher.java
 * <p>
 * CSRF 체크를 하지않을 Request Method 정의
 * ===============================================
 *
 * @author 이재철
 * @since 2016. 9. 12.
 * <p>
 * 수정자         수정일         수정내용
 * -------------  -------------  -----------------
 * <p>
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public class CsrfSecurityRequestMatcher implements RequestMatcher {
    private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
    private RegexRequestMatcher unprotectedMatcher = new RegexRequestMatcher("/unprotected", null);

    /**
     * true 면 CSRF 토큰 체크, false 면 제외
     *
     * @see RequestMatcher#matches(HttpServletRequest)
     */
    @Override
    public boolean matches(HttpServletRequest request) {
        if (SessionUtil.isAjax(request) || allowedMethods.matcher(request.getMethod()).matches()) {
            return false;
        }
        return !unprotectedMatcher.matches(request);
    }
}