package com.griffins.config.auth;

import com.griffins.common.domain.AjaxResult;
import com.griffins.common.domain.SessionData;
import com.griffins.common.util.CommonUtil;
import com.griffins.common.util.LogUtil;
import com.griffins.common.util.SessionUtil;
import com.griffins.common.util.StringUtil;
import com.griffins.config.auth.repository.LoginUserDetailsDAO;
import com.griffins.config.common.ConfigUtil;
import com.griffins.config.common.OnLoadAction;
import com.griffins.gbiz.admin.menu.domain.MenuMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 파일명 : com.griffins.config.login.LoginSuccessHandler
 * *
 * {클래스의 기능과 용도 설명}
 * ===============================================
 *
 * @author 이재철
 * @since 2018-05-22
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private OnLoadAction onLoadAction;

    @Autowired
    private LoginUserDetailsDAO securityDAO;

    @Autowired
    private SessionRegistry sessionRegistry;

    private final String cookieName = ConfigUtil.CODE.get("session.cookie.name");

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        final LoginUserDetails user = (LoginUserDetails) authentication.getPrincipal();

        //=== 1. 세션에 사용자정보 저장
        final SessionData.SessionDataBuilder builder = SessionData.builder();
        builder.id(user.getUsername())
                .name(user.getName())
                .menuTree(user.getMenuTree())
                .isAdmin(user.isAdmin())
                .exceptMenus(onLoadAction.getExceptMenus());
        final SessionData sessionData = builder.build();

        // 로그인 시간 단축을 위해 쓰레드를 사용.
        new Thread(new Runnable() {
            @Override
            public void run() {
                //=== SessionData 에 menuMap 생성
                MenuMap menuMap = new MenuMap();
                user.getMenuList().stream().forEach(item -> {
                    menuMap.put(item.getSeq(), item);
                });
                sessionData.setMenuMap(menuMap);
            }
        }).run();


        //=== 2. 로그인정보 저장
        saveLoginStatus(request, user);


        //=== 3. 세션 및 쿠키 처리 (다른탭에 다른 사용자로 로그인했을 경우 해당사용자의 접속 종료)
        Cookie disconnCookie = SessionUtil.getCookie(request, cookieName);
        if (disconnCookie != null && StringUtil.isNotEmpty(disconnCookie.getValue())) {
            sessionRegistry.removeSessionInformation(disconnCookie.getValue());
            SessionUtil.clearCookie(response, cookieName);
        }

        HttpSession session = request.getSession();
        sessionRegistry.registerNewSession(session.getId(), user);
        SessionUtil.setCookie(response, cookieName, session.getId(), 1f); // 현재 세션 ID를 쿠키에 저장. 다른 탭에서 로그인할 경우 이 ID 를 sessionRegistry 에서 제거하기 위한 용도.
        SessionUtil.setSessionData(request, sessionData);


        // 4. 사용자 로그인 정보 로깅
        LogUtil.chasing("info")
                .title("사용자 로그인")
                .append("작업", "로그인", "w")
                .append("IP", SessionUtil.getClientIP(request), "i")
                .append("이름", sessionData.getName(), "n")
                .append("ID", sessionData.getId(), "e")
                .end();

        // 5. 로그인 Ajax 결과 코드를 Json 형식으로 생성하여 login.jsp로 반환
        String pwChangeURL = "/login/change.gs";
        AjaxResult<String> result = new AjaxResult<>();
        result.setData(request.getContextPath() + (user.isPasswordExpired() ? pwChangeURL : ConfigUtil.CODE.get("url.home")));
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        try (PrintWriter out = response.getWriter()) {
            out.write(CommonUtil.json(result));
            out.flush();
        } catch (Exception e) {
            LogUtil.error(e, LogUtil.CONSOLE_FILE);
        }

    }

    private void saveLoginStatus(HttpServletRequest request, LoginUserDetails user) {
        Map<String, Object> map = new HashMap<>();
        map.put("ip", SessionUtil.getClientIP(request));
        map.put("id", user.getUsername());
        securityDAO.updateLoginStatus(map);
    }
}
