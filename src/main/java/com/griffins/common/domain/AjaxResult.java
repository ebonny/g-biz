package com.griffins.common.domain;

import com.griffins.common.util.ErrorUtil;
import com.griffins.common.util.LogUtil;
import com.griffins.common.util.StringUtil;
import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 파일명 : com.griffins.common.domain.AjaxResult
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
@Data
public class AjaxResult<T> {
    private String code = "0000";
    private String msg = "성공적으로 처리되었습니다";
    private String issuccess = "1";
    private T data;

    public final static String FAIL = "0";
    public static final String SUCCESS = "1";

    public void setIssuccess(Exception e) throws Exception {
        setIssuccess(e, FAIL);
    }

    public void setIssuccess(Exception e, String issuccess) throws Exception {
        this.issuccess = issuccess;
        this.msg = "처리중 오류가 발생했습니다";
        LogUtil.error(e, LogUtil.CONSOLE_FILE);
        if (ErrorUtil.isConnectionFail(e)) {
            throw e;
        }

        String msg = e.getMessage();
        String colname = null;
        Matcher m = Pattern.compile("(.*?)((column\\s*'\\w+')|$)", Pattern.DOTALL).matcher(msg.toLowerCase());
        if (m.find())
            colname = m.group();
        colname = colname.replaceAll(".*column\\s*'(\\w+)'.*", "$1");
        if (StringUtil.isNotEmpty(colname)) {
            this.msg = "'" + colname + "' " + this.msg;
        }
    }
}
