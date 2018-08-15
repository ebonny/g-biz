package com.griffins.config.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 파일명 : griffins.common.filter.ParameterControllRequest
 * *
 * 파라미터 조작용 Request
 * ===============================================
 *
 * @author 이재철
 * @since 2016-12-30
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public class ParameterControlRequest extends HttpServletRequestWrapper {

    private Map<String, String[]> params;

    public ParameterControlRequest(HttpServletRequest request, Map<String, String[]> params) {
        super(request);
        this.params = params;
    }

    public ParameterControlRequest(HttpServletRequest request) {
        super(request);
        this.params = new HashMap<>(request.getParameterMap());
    }

    public String getParameter(String name) {
        String returnValue = null;
        String[] paramArray = getParameterValues(name);
        if (paramArray != null && paramArray.length > 0) {
            returnValue = paramArray[0];
        }
        return returnValue;
    }

    public Map<String, String[]> getParameterMap() {
        return Collections.unmodifiableMap(params);
    }

    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(params.keySet());
    }

    public String[] getParameterValues(String name) {
        String[] result = null;
        String[] temp = params.get(name);
        if (temp != null) {
            result = new String[temp.length];
            System.arraycopy(temp, 0, result, 0, temp.length);
        }
        return result;
    }

    public void setParameter(String name, String value) {
        String[] aValue = {value};
        setParameter(name, aValue);
    }

    public void setParameter(String name, String[] values) {
        params.put(name, values);
    }
}
