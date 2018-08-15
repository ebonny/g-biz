package com.griffins.config.common;

import com.griffins.common.GConstants;
import com.griffins.common.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 파일명 : com.griffins.config.common.GSessionManager
 * *
 * {클래스의 기능과 용도 설명}
 * ===============================================
 *
 * @author 이재철
 * @since 2018-07-30 0030
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
@Component
public class GSessionManager {
    @Autowired
    private SessionRegistry sessionRegistry;

    public void logout(HttpServletRequest request, HttpServletResponse response, Integer errCode) throws IOException {
        if(response != null) {  // 정상 로그아웃 요청인 경우
            if(SessionUtil.isAjax(request))
                SessionUtil.sendError(response, errCode != null ? errCode : GConstants.ERROR_SESSION_TIMEOUT_NO_ALERT);
            else
                SessionUtil.redirect(request, response, errCode, SessionUtil.REDIRECT_TO_LOGIN);
        } else {
            HttpSession session = request.getSession(false);
            if(session != null) {
                sessionRegistry.removeSessionInformation(session.getId());
                session.invalidate();
            }
        }
    }
}
