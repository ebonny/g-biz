package com.griffins.config.common;

import org.springframework.context.support.MessageSourceAccessor;

import java.util.Map;

/**
 * 환경설정 값들을 관리하는 유틸객체
 * ===============================================
 *
 * @author 이재철
 * @since 2017-07-16
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public class ConfigUtil {
    public static Map<String, String> FILE;
    public static MessageSourceAccessor MSG;
    public static Map<String, String> EXT;
    public static Map<String, String> CODE;
    public static Map<String, String> OPTION;

    public static void initMembers(
            MessageSourceAccessor messageSource
            , Map<String, String> fileConfig
            , Map<String, String> extConfig
            , Map<String, String> codeConfig
            , Map<String, String> optionConfig
    ) {
        MSG = messageSource;
        FILE = fileConfig;
        EXT = extConfig;
        CODE = codeConfig;
        OPTION = optionConfig;
    }

}
