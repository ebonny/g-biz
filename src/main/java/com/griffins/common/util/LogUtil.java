package com.griffins.common.util;

import com.griffins.common.log.chase.LogChase;
import com.griffins.common.log.chase.MessageDecorator;
import com.griffins.common.log.console.LogConsole;
import com.griffins.common.log.file.LogFile;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

/**
 * 파일명 : LogUtil.java
 * <p>
 * 로그를 콘솔, 파일로 출력
 * ===============================================
 *
 * @author 이재철
 * @since 2016. 11. 2.
 * <p>
 * 수정자         수정일         수정내용
 * -------------  -------------  -----------------
 * <p>
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public class LogUtil {
    public static final int CONSOLE_ONLY = 1;
    public static final int CONSOLE_FILE = 2;
    public static final int CHASING_FILE = 3;    /* 사용자 행위 추적용 로그 */

    public static String getStackTrace(final Throwable error) {
        return getStackTrace(error, 20);
    }

    public static String getStackTrace(final Throwable error, int count) {
        StackTraceElement[] stack = error.getStackTrace();
        String msg = "";
        int size = Math.min(count, stack.length);

        for (int i = 0; i < size; i++) {
            msg += (i == 0 ? "" : "\n") + "\t\t" + stack[i];
        }

        String message = "\n\n############## Error Start ###############\n";
        message += "\n오류 내용 : " + (error.getCause() != null ? error.getCause().getMessage() : error.getMessage());
        message += "\n오류 스택 : " + msg;
        message += "\n############## Error End ###############\n\n";
        return message;
    }

    //=== 일반 오류 및 로그 처리
    public static void error(String desc, Throwable error, int option) {
        error(getMessage(desc, error), option);
    }

    public static void error(Throwable error, int option) {
        error(null, error, option);
    }

    public static void error(String message, int option) {
        switch (option) {
            case CONSOLE_FILE:
                LogFile.LOG.error(message);
            case CONSOLE_ONLY:
                LogConsole.LOG.error(message);
                break;
            case CHASING_FILE:
                LogChase.LOG.error(message);
                break;
        }
    }

    public static void debug(String desc, Throwable error, int option) {
        debug(getMessage(desc, error), option);
    }

    public static void debug(Throwable error, int option) {
        debug(null, error, option);
    }

    public static void debug(String message, int option) {
        switch (option) {
            case CONSOLE_FILE:
                LogFile.LOG.debug(message);
            case CONSOLE_ONLY:
                LogConsole.LOG.debug(message);
                break;
            case CHASING_FILE:
                LogChase.LOG.debug(message);
                break;
        }
    }

    public static void info(String desc, Throwable error, int option) {
        info(getMessage(desc, error), option);
    }

    public static void info(Throwable error, int option) {
        info(null, error, option);
    }

    public static void info(String message, int option) {
        switch (option) {
            case CONSOLE_FILE:
                LogFile.LOG.info(message);
            case CONSOLE_ONLY:
                LogConsole.LOG.info(message);
                break;
            case CHASING_FILE:
                LogChase.LOG.info(message);
                break;
        }
    }

    public static void warn(String desc, Throwable error, int option) {
        warn(getMessage(desc, error), option);
    }

    public static void warn(Throwable error, int option) {
        warn(null, error, option);
    }

    public static void warn(String message, int option) {
        switch (option) {
            case CONSOLE_FILE:
                LogFile.LOG.warn(message);
            case CONSOLE_ONLY:
                LogConsole.LOG.warn(message);
                break;
            case CHASING_FILE:
                LogChase.LOG.warn(message);
                break;
        }
    }

    public static void trace(String desc, Throwable error, int option) {
        trace(getMessage(desc, error), option);
    }

    public static void trace(Throwable error, int option) {
        trace(null, error, option);
    }

    public static void trace(String message, int option) {
        switch (option) {
            case CONSOLE_FILE:
                LogFile.LOG.trace(message);
            case CONSOLE_ONLY:
                LogConsole.LOG.trace(message);
                break;
            case CHASING_FILE:
                LogChase.LOG.trace(message);
                break;
        }
    }

    private static String getMessage(String desc, Throwable error) {
        Object location = null;
        try {
            location = error.getStackTrace()[0] + "\n" + error.getStackTrace()[1] + "\n" + error.getStackTrace()[2];
        } catch (IndexOutOfBoundsException e) {
            location = error.getStackTrace()[0];
        }
        String message = "\n\n############## Error Start ###############\n";
        message += (desc != null ? "\n오류 설명 : " + desc : "");
        message += "\n오류 내용 : " + (error.getCause() == null ? error.getMessage() : error.getCause().getMessage());
        message += "\n오류 위치 : " + location;
        message += "\n############## Error End ###############\n\n";
        return message;
    }

    //=== 상세 오류 처리
    public static void errorDetail(HttpServletRequest request, Throwable error, int outputOption) {
        switch (outputOption) {
            case CONSOLE_FILE:
                LogFile.LOG.error(getDetailMessage(request, error));
            case CONSOLE_ONLY:
                LogConsole.LOG.error(getDetailMessage(request, error));
                break;
        }
    }

    public static void debugDetail(HttpServletRequest request, Throwable error, int outputOption) {
        switch (outputOption) {
            case CONSOLE_FILE:
                LogFile.LOG.debug(getDetailMessage(request, error));
            case CONSOLE_ONLY:
                LogConsole.LOG.debug(getDetailMessage(request, error));
                break;
            case CHASING_FILE:
                LogChase.LOG.debug(getDetailMessage(request, error));
                break;
        }
    }

    public static void infoDetail(HttpServletRequest request, Throwable error, int outputOption) {
        switch (outputOption) {
            case CONSOLE_FILE:
                LogFile.LOG.info(getDetailMessage(request, error));
            case CONSOLE_ONLY:
                LogConsole.LOG.info(getDetailMessage(request, error));
                break;
            case CHASING_FILE:
                LogChase.LOG.info(getDetailMessage(request, error));
                break;

        }
    }

    public static void warnDetail(HttpServletRequest request, Throwable error, int outputOption) {
        switch (outputOption) {
            case CONSOLE_FILE:
                LogFile.LOG.warn(getDetailMessage(request, error));
            case CONSOLE_ONLY:
                LogConsole.LOG.warn(getDetailMessage(request, error));
                break;
            case CHASING_FILE:
                LogChase.LOG.warn(getDetailMessage(request, error));
                break;
        }
    }

    public static void traceDetail(HttpServletRequest request, Throwable error, int outputOption) {
        switch (outputOption) {
            case CONSOLE_FILE:
                LogFile.LOG.trace(getDetailMessage(request, error));
            case CONSOLE_ONLY:
                LogConsole.LOG.trace(getDetailMessage(request, error));
                break;
            case CHASING_FILE:
                LogChase.LOG.trace(getDetailMessage(request, error));
                break;
        }
    }

    private static String getDetailMessage(HttpServletRequest request, Throwable error) {
        Object location = null;
        try {
            location = error.getStackTrace()[0] + "\n" + error.getStackTrace()[1] + "\n" + error.getStackTrace()[2];
        } catch (IndexOutOfBoundsException e) {
            location = error.getStackTrace()[0];
        }
        String msg = "\n\n############## Error Start ###############\n"
                + "\n오류 발생시간 : " + new SimpleDateFormat("yy년 MM월 dd일 HH:mm:ss.SSS").format(new Date())
                + "\n오류 발생위치 : " + location
                + "\n요청 URL : " + request.getRequestURL()
                + "\n요청자 정보 : " + request.getRemoteAddr() + "(" + request.getRemoteHost() + "[" + request.getRemoteUser() + "])"
                + "\n오류 내용 : " + (error.getCause() == null ? error.getMessage() : error.getCause().getMessage())
                + "\n쿼리 스트링 : " + request.getQueryString()
                + "\n파라미터 : ";

        @SuppressWarnings("unchecked")
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            msg += "\n" + name + " : " + request.getParameter(name);
        }

        msg += "\n############## Error End ###############\n\n";
        return msg;
    }

    //=== 상세 오류 처리 in AOP
    public static void errorDetail(ProceedingJoinPoint pjp, HttpServletRequest request, Throwable error, int outputOption) {
        switch (outputOption) {
            case CONSOLE_FILE:
                LogFile.LOG.error(getAOPMessage(pjp, request, error));
            case CONSOLE_ONLY:
                LogConsole.LOG.error(getAOPMessage(pjp, request, error));
                break;
            case CHASING_FILE:
                LogChase.LOG.error(getAOPMessage(pjp, request, error));
                break;
        }
    }

    public static void debugDetail(ProceedingJoinPoint pjp, HttpServletRequest request, Throwable error, int outputOption) {
        switch (outputOption) {
            case CONSOLE_FILE:
                LogFile.LOG.debug(getAOPMessage(pjp, request, error));
            case CONSOLE_ONLY:
                LogConsole.LOG.debug(getAOPMessage(pjp, request, error));
                break;
            case CHASING_FILE:
                LogChase.LOG.debug(getAOPMessage(pjp, request, error));
                break;
        }
    }

    public static void infoDetail(ProceedingJoinPoint pjp, HttpServletRequest request, Throwable error, int outputOption) {
        switch (outputOption) {
            case CONSOLE_FILE:
                LogFile.LOG.info(getAOPMessage(pjp, request, error));
            case CONSOLE_ONLY:
                LogConsole.LOG.info(getAOPMessage(pjp, request, error));
                break;
            case CHASING_FILE:
                LogChase.LOG.info(getAOPMessage(pjp, request, error));
                break;
        }
    }

    public static void warnDetail(ProceedingJoinPoint pjp, HttpServletRequest request, Throwable error, int outputOption) {
        switch (outputOption) {
            case CONSOLE_FILE:
                LogFile.LOG.warn(getAOPMessage(pjp, request, error));
            case CONSOLE_ONLY:
                LogConsole.LOG.warn(getAOPMessage(pjp, request, error));
                break;
            case CHASING_FILE:
                LogChase.LOG.warn(getAOPMessage(pjp, request, error));
                break;
        }
    }

    public static void traceDetail(ProceedingJoinPoint pjp, HttpServletRequest request, Throwable error, int outputOption) {
        switch (outputOption) {
            case CONSOLE_FILE:
                LogFile.LOG.trace(getAOPMessage(pjp, request, error));
            case CONSOLE_ONLY:
                LogConsole.LOG.trace(getAOPMessage(pjp, request, error));
                break;
            case CHASING_FILE:
                LogChase.LOG.trace(getAOPMessage(pjp, request, error));
                break;
        }
    }

    private static String getAOPMessage(ProceedingJoinPoint pjp, HttpServletRequest request, Throwable error) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Object[] params = pjp.getArgs();
        String[] paramNames = signature.getParameterNames();
        StackTraceElement[] traces = error.getStackTrace();
        String desc = "";
        int lineNum = 0;
        for (StackTraceElement trace : traces) {
            desc = trace.toString();
            desc = desc.substring(0, desc.indexOf("("));
            if (signature.toString().contains(desc)) {
                lineNum = trace.getLineNumber();
                break;
            }
        }
        String msg = "\n\n############## Error Start ###############"
                + "\n오류 발생시간 : " + new SimpleDateFormat("yy년 MM월 dd일 HH:mm:ss.SSS").format(new Date())
                + "\n오류 발생위치 : " + signature + "[" + lineNum + "]"
                + "\n요청 URL : " + request.getRequestURL()
                + "\n요청자 정보 : " + request.getRemoteAddr() + "(" + request.getRemoteHost() + "[" + request.getRemoteUser() + "])"
                + "\n오류 내용 : " + (error.getCause() == null ? error.getMessage() : error.getCause().getMessage())
                + "\n쿼리 스트링 : " + request.getQueryString()
                + "\n파라미터 : ";
        for (int i = 0; i < params.length; i++) {
            Object obj = params[i];
            if (!(obj instanceof HttpServletRequest)
                    && !(obj instanceof HttpServletResponse)
                    && !(obj instanceof Model)) {
                if (obj != null) {
                    msg += "\n\t■Param Type : " + obj.getClass() + "\n\tParam Name : " + paramNames[i];
                    String[] vars = obj.toString().split(";");
                    for (String var : vars) {
                        msg += "\n\t" + (var.startsWith("\n") ? var.substring(1) : var);
                    }
                }
            }
        }
        msg += "\n############## Error End ###############\n\n";
        return msg;
    }


    public static MessageDecorator chasing(String level) {
        MessageDecorator md = new MessageDecorator(level, CHASING_FILE);
        return md.start();
    }


}
