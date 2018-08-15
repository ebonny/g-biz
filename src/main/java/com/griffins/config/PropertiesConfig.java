package com.griffins.config;

import com.griffins.common.util.LogUtil;
import com.griffins.common.util.StringUtil;
import com.griffins.config.common.PropertiesMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.*;

/**
 * 프로젝트명 : gsales
 * 파일명 : com.griffins.config.PropertiesConfig
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
public class PropertiesConfig {
    @Resource(name = "environment")
    private Environment environment;
    @Resource(name = "properties")
    private PropertiesMap propMap;
    @Value("${server.upload-dir}")
    private String uploadDir;


    @Bean(name = "uploadDir")
    public String uploadDir() {
        return uploadDir;
    }

    /* ######################################################## properties.yml ################################################################### */
    @Bean(name = "fileConfig")
    public Map<String, String> fileConfig() {
        Map map = (Map) propMap.get("fileupload");
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            map.put(key, StringUtil.toString(map.get(key)));
        }
        return map;
    }

    @Bean(name = "extConfig")
    public Map<String, String> extConfig() {
        return propMap.get("ext");
    }

    @Bean(name = "codeConfig")
    public Map<String, String> codeConfig() {
        return propMap.get("code");
    }

    @Bean(name = "optionConfig")
    public Map<String, String> optionConfig() {
        /*Map map = propMap.get("option");
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            map.put(key, StringUtil.toString(map.get(key)));
        }
        return map;*/
        Map map = new HashMap();
        addToMapByKey("/application.yml", "option", map);
        String[] profiles = environment.getActiveProfiles();
        for (String profile : profiles) {
            addToMapByKey("/application-" + profile + ".yml", "option", map);
        }
        return map;
    }

    @Bean(name = "aesConfig")
    public Map<String, String> aesConfig() {
        return propMap.get("aes");
    }

    @Bean(name = "exceptionPageList")
    public List<String> exceptionPageList() {
        String str = (String) propMap.get("exception").get("pages");
        return Arrays.asList(str.split(" "));
    }
    /* ######################################################## properties.yml ################################################################### */


    private void addToMapByKey(String filePath, String key, Map map) {
        try (InputStream is = getClass().getResource(filePath).openStream()) {
            Map<String, Map> pmap = new Yaml().loadAs(is, Map.class);
            Map tmap = (Map) pmap.get(key);
            Iterator<String> it = tmap.keySet().iterator();
            while (it.hasNext()) {
                String k = it.next();
                map.put(k, StringUtil.toString(tmap.get(k)));
            }
        } catch (Exception e) {
            LogUtil.warn(LogUtil.getStackTrace(e) + "\nFile Path = " + filePath, LogUtil.CONSOLE_FILE);
        }
    }
}
