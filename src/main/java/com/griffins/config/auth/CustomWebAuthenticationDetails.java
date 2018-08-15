package com.griffins.config.auth;

import com.griffins.common.util.StringUtil;
import lombok.Getter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * 프로젝트명 : gsales
 * 파일명 : griffins.config.security.CustomWebAuthenticationDetails
 * *
 * {클래스의 기능과 용도 설명}
 * ===============================================
 *
 * @author 이재철
 * @since #{DATE}
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public class CustomWebAuthenticationDetails extends WebAuthenticationDetails {
    @Getter
    private Integer otpCode;

    public CustomWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        String otpKey = request.getParameter("otpCode");
        if (StringUtil.isNotEmpty(otpKey)) {
            try {
                this.otpCode = Integer.valueOf(otpKey);
            } catch (NumberFormatException e) {
                this.otpCode = null;
            }
        }
    }
}
