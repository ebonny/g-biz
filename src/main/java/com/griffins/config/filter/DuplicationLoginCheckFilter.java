package com.griffins.config.filter;

import com.griffins.common.domain.AjaxResult;
import com.griffins.common.util.AESUtil;
import com.griffins.common.util.CommonUtil;
import com.griffins.common.util.SpringUtil;
import com.griffins.common.util.StringUtil;
import com.griffins.config.common.ConfigUtil;
import com.griffins.config.common.ParameterControlRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 파일명 : DuplicationLoginCheckFilter.java
 * <p>
 * 중복 로그인 검사
 * ===============================================
 *
 * @author 이재철
 * @since 2016. 11. 7.
 * <p>
 * 수정자         수정일         수정내용
 * -------------  -------------  -----------------
 * <p>
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public class DuplicationLoginCheckFilter implements Filter {
    private UserDetailsService userService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationProvider authenticationProvider;
    private String usernamePrefix;
    private String ignoreParam;
    private String idParam;
    private String pwdParam;
    private Boolean checkAuthBeforeDuplCheck;    // 중복로그인 체크전 인증체크 여부
    private Boolean useLoginDuplicationCheck;    // 로그인 할때 중복로그인 체크 여부
    private SessionRegistry sessionRegistry;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (sessionRegistry == null) {
            sessionRegistry = SpringUtil.getBean(request, SessionRegistry.class);
            userService = SpringUtil.getBean(request, UserDetailsService.class);
            passwordEncoder = SpringUtil.getBean(request, PasswordEncoder.class);
            authenticationProvider = SpringUtil.getBean(request, AuthenticationProvider.class);
            Map<String, String> codeConfig = ConfigUtil.CODE;
            usernamePrefix = codeConfig.get("username.prefix");
            ignoreParam = codeConfig.get("security.param.ignore");
            idParam = codeConfig.get("security.param.id");
            pwdParam = codeConfig.get("security.param.pwd");
            checkAuthBeforeDuplCheck = true;
            useLoginDuplicationCheck = true;
        }

        String username = request.getParameter(idParam);
        String pwd = request.getParameter(pwdParam);

        if (StringUtil.isNotEmpty(username))
            username = AESUtil.decode(username);
        if (StringUtil.isNotEmpty(pwd))
            pwd = AESUtil.decode(pwd);

        if (StringUtil.isEmpty(username) || StringUtil.isEmpty(pwd)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            if (useLoginDuplicationCheck && StringUtil.isEmpty(request.getParameter(ignoreParam)) && authCheckPass(username, pwd) && userExists(username)) {
                AjaxResult result = new AjaxResult();
                result.setCode("0002");
                result.setMsg(ConfigUtil.MSG.getMessage("security.duplicate.found.msg"));
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                PrintWriter out = response.getWriter();
                out.print(CommonUtil.json(result));
                out.flush();
                out.close();
            } else {
                ParameterControlRequest newReq = new ParameterControlRequest((HttpServletRequest) request);
                newReq.setParameter(idParam, username);
                newReq.setParameter(pwdParam, pwd);
                chain.doFilter(newReq, response);
            }
        } catch (UsernameNotFoundException | AccessDeniedException e) {
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

    @Override
    public void destroy() {

    }

    private boolean authCheckPass(String username, String pwd) throws UsernameNotFoundException, AccessDeniedException {
        if (!checkAuthBeforeDuplCheck)
            return true;
        UserDetails user = userService.loadUserByUsername(usernamePrefix + username);
        boolean match = passwordEncoder.matches(pwd, user.getPassword());
        return match;
    }

    private boolean userExists(String username) {
        List<String> sessions = getSessionInformations(username);
        return sessions.size() >= Integer.parseInt(ConfigUtil.OPTION.get("session.max.count"));
        /*for(String info : sessions) {
            if (username.equals(info)) {
				return true;
			}
	    }
		return false;*/
    }

    private List<String> getSessionInformations(String username) {
        List<SessionInformation> sessionInformations = new ArrayList<>();
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            sessionInformations.addAll(sessionRegistry.getAllSessions(principal, false));
        }

        List<String> informations = new ArrayList<>();
        for (SessionInformation sessionInformation : sessionInformations) {
            Object principal = sessionInformation.getPrincipal();
            if (principal instanceof UserDetails && ((UserDetails) principal).getUsername().equals(username)) {
                informations.add(((UserDetails) principal).getUsername());
            }
        }
        return informations;
    }
}
