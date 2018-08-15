package com.griffins.common.util;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * 프로젝트명 : gsales
 * 파일명 : com.griffins.common.util.SpringUtil
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
public class SpringUtil {
    public static <T> T getBean(ServletRequest request, Class<T> classtype) {
        return WebApplicationContextUtils.getWebApplicationContext(request.getServletContext()).getBean(classtype);
    }

    /* DispatcherServlet 이후에 사용 */
    public static <T> T getBean(Class<T> classtype) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return WebApplicationContextUtils.getWebApplicationContext(request.getServletContext()).getBean(classtype);
    }

    public static Object getBeanByType(String className) {
        try {
            Class<?> cls = Thread.currentThread().getContextClassLoader().loadClass(className);
            if (cls == null) {
                cls = Class.forName(className);
            }
            return getApplicationContext().getBean(cls);
        } catch (Exception e) {
            return null;
        }
    }

    private static ApplicationContext getApplicationContext() {
        return ContextLoader.getCurrentWebApplicationContext();
    }

}
