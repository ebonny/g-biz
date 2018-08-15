package com.griffins.config;

import com.griffins.common.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * 프로젝트명 : gsales
 * 파일명 : com.griffins.config.InitStaticConfig
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
@Configuration
public class InitStaticConfig {
    @Resource(name = "fileConfig")
    private Map<String, String> fileConfig;
    @Resource(name = "extConfig")
    private Map<String, String> extConfig;
    @Resource(name = "codeConfig")
    private Map<String, String> codeConfig;
    @Resource(name = "optionConfig")
    private Map<String, String> optionConfig;
    @Resource(name = "aesConfig")
    private Map<String, String> aesConfig;


    @Autowired
    MessageSourceAccessor messageSource;

    @Bean
    public MethodInvokingFactoryBean initAESUtil() {
        MethodInvokingFactoryBean bean = new MethodInvokingFactoryBean();
        bean.setTargetClass(AESUtil.class);
        bean.setTargetMethod("initKeyMap");
        bean.setArguments(new Object[]{aesConfig});
        return bean;
    }

    @Bean
    public MethodInvokingFactoryBean initCommonUtil() {
        MethodInvokingFactoryBean bean = new MethodInvokingFactoryBean();
        bean.setStaticMethod("com.griffins.common.util.CommonUtil.initMapper");
        return bean;
    }

    @Bean
    public MethodInvokingFactoryBean initConfigUtil() throws IOException {
        MethodInvokingFactoryBean bean = new MethodInvokingFactoryBean();
        bean.setStaticMethod("com.griffins.config.common.ConfigUtil.initMembers");
        bean.setArguments(new Object[]{messageSource, fileConfig, extConfig, codeConfig, optionConfig});
        return bean;
    }


}
