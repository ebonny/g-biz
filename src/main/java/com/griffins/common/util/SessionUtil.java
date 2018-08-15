package com.griffins.common.util;

import com.griffins.common.GConstants;
import com.griffins.common.domain.SessionData;
import com.griffins.config.common.ConfigUtil;
import net.sf.json.JSONObject;
import org.springframework.http.MediaType;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;

/**
 * 파일명 : com.griffins.common.util.SessionUtil
 * *
 * {클래스의 기능과 용도 설명}
 * ===============================================
 *
 * @author 이재철
 * @since 2018-05-23
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public class SessionUtil {
    private static final String SESSION_DATA_KEY = "__sd__";

    public static final int REDIRECT_TO_LOGIN = 1;
    public static final int REDIRECT_ALERT_BACK = 2;
    public static final int REDIRECT_TO_ERROR = 3;
    public static final int FORWARD_TO_ERROR = 4;
    public static final int REDIRECT_RELOAD = 5;
    public static final int REDIRECT = 6;
    public static final int FORWARD = 7;
    public static final int REDIRECT_ALERT = 8;
    public static final int REDIRECT_TO_LOGIN_WITHOUT_LOGOUT = 10;

    public static void setSessionData(HttpServletRequest request, SessionData sessionData) {
        setData(request, SESSION_DATA_KEY, sessionData);
    }

    private static void setData(HttpServletRequest request, String key, Object data) {
        if (StringUtil.isNotEmpty(key) && StringUtil.isNotEmpty(data)) {
            request.getSession().setAttribute(key, data);
        }
    }

    public static boolean isAjax(HttpServletRequest request) {
        return request.getHeader(ConfigUtil.CODE.get("ajax.header")) != null;
    }

    public static SessionData getSessionData(HttpServletRequest request) {
        return (SessionData) getData(request, SESSION_DATA_KEY);
    }

    private static Object getData(HttpServletRequest request, String key) {
        return request.getSession().getAttribute(key);
    }

    public static void sendError(HttpServletResponse response, int code, String message) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setIntHeader(ConfigUtil.CODE.get("response.header.name"), code);
        response.sendError(GConstants.ERROR_I_AM_NOT_TEAPOT, StringUtil.toClientString(message));
    }

    public static void sendError(HttpServletResponse response, int code) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setIntHeader(ConfigUtil.CODE.get("response.header.name"), code);
        response.sendError(GConstants.ERROR_I_AM_NOT_TEAPOT, "");
    }

    public static void redirect(HttpServletRequest req, HttpServletResponse res, int code, int redirectMethod) {
        if (code == GConstants.ERROR_SESSION_TIMEOUT || code == GConstants.ERROR_ALREADY_LOGOUT) {
            String script = "#script# if(sessionStorage.getItem('" + ConfigUtil.CODE.get("storage.id") + "')) " +
                    "{ alert(\"" + StringUtil.toClientString(ConfigUtil.MSG.getMessage(StringUtil.toString(code))) + "\"); }";
            redirect(req, res, script, redirectMethod);
        } else {
            redirect(req, res, ConfigUtil.MSG.getMessage(StringUtil.toString(code)), redirectMethod);
        }
    }


    /**
     * Redirect Forward to XXX
     *
     * @param req
     * @param res
     * @param msg_or_url_or_json redirect url 혹은 alert 메세지 혹은 alert 와 console 메세지를 포함한 JSONObject 객체
     * @param redirectMethod     EsmpConstants.REDIRECT_XXX 옵션 적용
     * @author 이재철
     * @date 2016. 11. 2.
     */
    public static void redirect(HttpServletRequest req, HttpServletResponse res, Object msg_or_url_or_json, int redirectMethod) {
        boolean isURL = false;
        String messageOrUrl = "";
        String clientMsg = "";

        if (StringUtil.isEmpty(msg_or_url_or_json)) {
            messageOrUrl = "";
        } else if (msg_or_url_or_json instanceof String) {
            messageOrUrl = msg_or_url_or_json.toString();
        } else if (msg_or_url_or_json instanceof JSONObject) {
            try {
                messageOrUrl = ((JSONObject) msg_or_url_or_json).getString("serverMsg");
                clientMsg = ((JSONObject) msg_or_url_or_json).getString("clientMsg");
            } catch (Exception e) {
                LogUtil.error(e, LogUtil.CONSOLE_FILE);
                throw new RuntimeException(e.getMessage());
            }
        } else {
            messageOrUrl = ConfigUtil.MSG.getMessage("common.error.msg");
            clientMsg = "SessionUtil.redirect - msg_or_url_or_json 은 String 과 JSONObject 타입만 허용됩니다";
            return;
        }

        if (messageOrUrl.equals(ConfigUtil.CODE.get("url.logout")) || (messageOrUrl.startsWith("/") && messageOrUrl.contains(".gs"))) {
            isURL = true;
        } else {
            if (StringUtil.isNotEmpty(messageOrUrl) && !StringUtil.toString(messageOrUrl).startsWith("#script#")) {
                messageOrUrl = "alert(\"" + StringUtil.toClientString(messageOrUrl) + "\")";
            }
            if (StringUtil.isNotEmpty(clientMsg)) {
                clientMsg = "console.log(\"" + StringUtil.toClientString(clientMsg) + "\")";
            }
            messageOrUrl = clientMsg + ";" + messageOrUrl.replaceAll(Matcher.quoteReplacement("#script#"), "");
        }

        res.setContentType("text/html; charset=UTF-8");
        try (PrintWriter pw = res.getWriter()) {
            switch (redirectMethod) {
                case REDIRECT:
                    if (isURL) {
                        res.sendRedirect(messageOrUrl);
                    } else {
                        pw.write("<script>console.log('Wrong Usage of redirect Method - REDIRECT need url');history.back();</script>");
                        pw.flush();
                    }
                    break;
                case FORWARD:
                    if (isURL) {
                        RequestDispatcher dis = req.getRequestDispatcher(messageOrUrl);
                        dis.forward(req, res);
                    } else {
                        pw.write("<script>console.log('Wrong Usage of redirect Method - FORWARD need url');history.back();</script>");
                        pw.flush();
                    }
                    break;
                case REDIRECT_ALERT_BACK:
                    pw.write("<script>" + messageOrUrl + ";history.back();</script>");
                    pw.flush();
                    break;
                case REDIRECT_ALERT:
                    pw.write("<script>" + messageOrUrl + ";</script>");
                    pw.flush();
                    break;
                case REDIRECT_TO_LOGIN:
                    pw.write("<script>" + messageOrUrl + ";location.replace('" + req.getContextPath() + ConfigUtil.CODE.get("url.logout") + "');</script>");
                    pw.flush();
                    break;
                case REDIRECT_TO_LOGIN_WITHOUT_LOGOUT:
                    pw.write("<script>" + messageOrUrl + ";location.replace('" + req.getContextPath() + ConfigUtil.CODE.get("url.home") + "');</script>");
                    pw.flush();
                case REDIRECT_TO_ERROR:
                    res.sendRedirect(GConstants.URL_ERROR);
                    break;
                case FORWARD_TO_ERROR:
                    RequestDispatcher dis = req.getRequestDispatcher(GConstants.URL_ERROR);
                    if (StringUtil.isNotEmpty(messageOrUrl)) {
                        req.setAttribute("errorAlert", messageOrUrl);
                    }
                    if (StringUtil.isNotEmpty(clientMsg)) {
                        req.setAttribute("errorMsg", clientMsg);
                    }
                    dis.forward(req, res);
                    break;
                case REDIRECT_RELOAD:
                    pw.write("<script>" + messageOrUrl + "location.reload();</script>");
                    pw.flush();
                    break;
            }
        } catch (Exception e) {
            LogUtil.error(e, LogUtil.CONSOLE_FILE);
        }
    }

    public static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-FORWARDED-FOR");
        if (StringUtil.isEmpty(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getClientInfo(HttpServletRequest request) {
        SessionData sd = SessionUtil.getSessionData(request);
        String userinfo = null;

        if (sd != null) {
            userinfo = sd.getId();
        } else {
            userinfo = request.getRemoteUser();
        }

        return StringUtil.isEmpty(userinfo) ? null : (userinfo + "#" + getClientIP(request));
    }

    public static void setCookie(ServletResponse response, String name, String value, float day) {
        Cookie ck = new Cookie(name, value);
        ck.setPath("/");
        ck.setValue(value);
        ck.setMaxAge(Math.round(day * 24 * 60 * 60));  // 초단위. 0 = 당장 쿠키를 지워라. 음수 = 브라우저종료시 쿠키를 지워라.(이론상만 그렇고 실제론 안먹힘)
        ((HttpServletResponse) response).addCookie(ck);
    }

    public static void clearCookie(ServletResponse response, String name) {
        Cookie ck = new Cookie(name, null);
        ck.setMaxAge(0);
        ck.setPath("/");
        ((HttpServletResponse) response).addCookie(ck);
    }

    public static Cookie getCookie(ServletRequest request, String name) {
        Cookie[] cks = ((HttpServletRequest) request).getCookies();
        if (cks != null)
            for (Cookie ck : cks) {
                if (ck.getName().equals(name)) {
                    return ck;
                }
            }
        return null;
    }

    public static JSONObject toJsonMessage(Integer code, Throwable error) {
        JSONObject json = new JSONObject();
        try {
            json.put("serverMsg", ConfigUtil.MSG.getMessage(StringUtil.toString(code)));
            json.put("clientMsg", StringUtil.toClientString(LogUtil.getStackTrace(error, 30)));
        } catch (Exception e) {
            LogUtil.error(e, LogUtil.CONSOLE_FILE);
            throw new RuntimeException(e.getMessage());
        }
        return json;
    }

}
