package com.griffins.common.exception;

import com.griffins.common.GConstants;
import com.griffins.common.util.ErrorUtil;
import com.griffins.common.util.LogUtil;
import com.griffins.common.util.SessionUtil;
import com.griffins.common.util.StringUtil;
import com.griffins.config.common.ConfigUtil;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 파일명 : EsmpExceptionHandler.java
 * <p>
 * 공통 오류처리 핸들러
 * ===============================================
 *
 * @author 이재철
 * @since 2016. 11. 14.
 * <p>
 * 수정자         수정일         수정내용
 * -------------  -------------  -----------------
 * <p>
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
@ControllerAdvice
public class GExceptionHandler {
    private final int maxRetry = 3;

    private Map<String, Integer> retryCntMap = new HashMap<>();


    @ExceptionHandler(MailSendException.class)
    public ModelAndView mailException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (SessionUtil.isAjax(request)) {
            SessionUtil.sendError(response, GConstants.ERROR_MAIL_SEND, e.getMessage());
        } else {
            SessionUtil.redirect(request, response
                    , StringUtil.toClientString(ConfigUtil.MSG.getMessage(StringUtil.toString(GConstants.ERROR_MAIL_SEND), new Object[]{e.getMessage()}))
                    , SessionUtil.REDIRECT_ALERT_BACK);
        }
        return null;
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public void accessDeniedException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (SessionUtil.isAjax(request)) {
            SessionUtil.sendError(response, GConstants.ERROR_UNAUTHORIZED, LogUtil.getStackTrace(e));
        } else {
            SessionUtil.redirect(request, response, GConstants.ERROR_UNAUTHORIZED, SessionUtil.REDIRECT_ALERT_BACK);
        }
    }

    @ExceptionHandler(Throwable.class)
    public ModelAndView defaultException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mv = null;
        boolean beforeLogin = SessionUtil.getSessionData(request) == null;
        String errorUrl = beforeLogin ? "griffins/common/error" : "common/error.tiles";
        String myid = request.getSession().getId();

        LogUtil.error(LogUtil.getStackTrace(e), LogUtil.CONSOLE_FILE);

        //=== DB 접속 오류처리
        if (ErrorUtil.isConnectionFail(e)) {
            if (SessionUtil.isAjax(request)) {
                SessionUtil.sendError(response, GConstants.ERROR_CONNECT_FAIL, LogUtil.getStackTrace(e));
            } else {
                mv = checkAndRetry(e, myid, request, response, errorUrl);
            }
        }
        //=== 파일 다운로드 오류 처리
        else if (e instanceof DownloadFailureException) {
            SessionUtil.redirect(request, response, e.getMessage(), SessionUtil.REDIRECT_ALERT_BACK);
        }
        //=== 그 외의 오류 처리
        else {
            if (SessionUtil.isAjax(request)) {
                SessionUtil.sendError(response, GConstants.ERROR_COMMON, LogUtil.getStackTrace(e));
            } else {
                mv = new ModelAndView(errorUrl);
                mv.addObject("errorMsg", StringUtil.toClientString(LogUtil.getStackTrace(e)));
            }
        }

        return mv;
    }

    private synchronized ModelAndView checkAndRetry(Exception e, final String myid, HttpServletRequest request, HttpServletResponse response, String errorUrl) {
        ModelAndView mv = null;
        //=== [옵션1] 오류시 재시도 여부를 사용자가 결정
//             mv = new ModelAndView("loginIndex");
//             mv.addObject("errorCode", GConstants.ERROR_CONNECT_FAIL);

        //=== [옵션2] 오류시 maxRetry 횟수만큼 자동으로 재시도
        Integer cnt = retryCntMap.get(myid);
        if (cnt == null) {
            cnt = 0;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    retryCntMap.remove(myid);
                }
            }, 120000);
        }
        if (cnt < maxRetry) {
            retryCntMap.put(myid, cnt + 1);
//               if(beforeLogin)
//                  SessionUtil.redirect(request, response, loginPageUrl, SessionUtil.REDIRECT);
//               else
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                LogUtil.error(e1, LogUtil.CONSOLE_FILE);
            }
            SessionUtil.redirect(request, response, request.getServletPath(), SessionUtil.FORWARD);
        } else if (cnt == maxRetry) {
            retryCntMap.put(myid, cnt + 1);
            mv = new ModelAndView(errorUrl);
            mv.addObject("errorAlert", StringUtil.toClientString(ConfigUtil.MSG.getMessage("connect.fail.msg")));
            mv.addObject("errorMsg", StringUtil.toClientString(LogUtil.getStackTrace(e)));
        }
        return mv;
    }

}
