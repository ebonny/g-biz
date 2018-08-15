package com.griffins.config.auth;

import com.griffins.common.domain.AjaxResult;
import com.griffins.common.util.CommonUtil;
import com.griffins.common.util.SpringUtil;
import com.griffins.config.common.ConfigUtil;
import com.griffins.gbiz.admin.member.domain.Member;
import com.griffins.gbiz.admin.member.repository.MemberDAO;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 파일명 : LoginFailureHandler.java
 * <p>
 * Spring Security에서 로그인 거부가 발생했을 때 이후 프로세스를 처리
 * ===============================================
 *
 * @author 이재철
 * @since 2016. 8. 15.
 * <p>
 * 수정자         수정일         수정내용
 * -------------  -------------  -----------------
 * <p>
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private MemberDAO dao = null;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        if (dao == null) {
            dao = (MemberDAO) SpringUtil.getBean(request, MemberDAO.class);
        }

        if (e instanceof BadCredentialsException) {
            Member evo = new Member();
            String idParam = ConfigUtil.CODE.get("param.id");
            String id = request.getParameter(idParam);
            evo.setId(id);

            dao.increaseLoginFailCount(evo);
        }

        if (!response.isCommitted()) {
            AjaxResult result = new AjaxResult();
            result.setCode("0001");
            result.setMsg(e.getMessage());
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            PrintWriter out = response.getWriter();
            out.print(CommonUtil.json(result));
            out.flush();
            out.close();
        }
    }
}
