package com.griffins.config.mybatis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 파일명 : com.griffins.config.mybatis.MybatisConfig
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
@ConfigurationProperties(prefix = "mybatis")
@Component
@Data
public class MybatisConfig {
    private String configLocation;
    private String mapperLocation;
}
