package com.griffins.common.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.griffins.common.tree.TreeStorage;
import com.griffins.config.common.ConfigUtil;
import com.griffins.gbiz.admin.menu.domain.FuncVO;
import com.griffins.gbiz.admin.menu.domain.MenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;

/**
 * 파일명 : com.griffins.common.util.CommonUtil
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
@Component
public class CommonUtil {
    private final static ObjectMapper jsonMapper = new ObjectMapper();
    private static String UPLOAD_ROOT;

    @Autowired
    @Qualifier("uploadDir")
    public void setUploadDir(String upload) {
        UPLOAD_ROOT = upload;
    }

    public static void initMapper() {
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String json(Object obj) {
        try {
            if (obj instanceof String)
                throw new Exception("Please USE method -> json(String, Class)");
            return jsonMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static <T> T json(String str, Class<?> clazz) {
        try {
            return (T) jsonMapper.readValue(StringUtil.decodeScript(str), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void mapFuncToMenu(List<FuncVO> funcList, List<MenuVO> menuList) {
        for (FuncVO func : funcList) {
            Integer menuid = func.getMenuSeq();
            for (MenuVO menu : menuList) {
                if (menu.getSeq().intValue() == menuid.intValue()) {
                    menu.getFuncList().add(func);
                }
            }
        }
    }

    public static String getRealPath(String basePath) {
        basePath = basePath.startsWith("/") ? basePath : "/" + basePath;
        String rootPath = "";
        if (basePath.equalsIgnoreCase("/upload") || basePath.contains("/upload/")) {
//            CommonUtil.OS os = CommonUtil.getOS();
            rootPath = UPLOAD_ROOT;
            String restPath = basePath.replaceAll("/upload", "").replaceAll("//", "/");
            if (basePath.contains("/upload") && restPath.length() > 0) {
                rootPath = rootPath + "/" + restPath;
            }
        } else {
            rootPath = rootPath + "/" + basePath;
            rootPath = rootPath.replaceAll("\\\\", "/").replaceAll("//", "/");
        }

        File dir = new File(rootPath);
        if (!dir.exists())
            dir.mkdirs();
        return rootPath.endsWith("/") ? rootPath : rootPath + "/";
    }

    /**
     * 실제 경로를 반환
     *
     * @param request
     * @param keyName  : fileConfig 에 정의된 basPath 하위의 경로에 대한 키값
     * @param basePath : WEB-ROOT 하위의 최상의 폴더명 (upload, resource 등)
     * @return
     */
    public static String getPathProperty(String keyName, String basePath) {
        if (StringUtil.isEmpty(basePath))
            throw new RuntimeException("basePath cannot be null");
        basePath = basePath.startsWith("/") ? basePath : "/" + basePath;
        String value = ConfigUtil.FILE.get(keyName).trim();

        String uploadPath = UPLOAD_ROOT;

        String restPath = basePath.replaceAll("/upload", "").replaceAll("//", "/");
        if (basePath.contains("/upload") && restPath.length() > 0) {
            uploadPath = uploadPath + "/" + restPath;
        }

        value = uploadPath + "/" + value;
        value = value.replaceAll("\\\\", "/").replaceAll("//", "/");

        File uploadDir = new File(value);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        return value;
    }

    public static String getPathProperty(String keyName) {
        return getPathProperty(keyName, "upload");
    }

    public static String shortenUUID(UUID uuid) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());

        return Base64.getEncoder().withoutPadding().encodeToString(byteBuffer.array())
                .replace("/", "").replace("\\+", "-");

    }

    public static <T> List<T> buildTree(TreeStorage<T> storage) {
        List<T> newlist = new ArrayList<>();
        List<T> list = storage.getList();
        for (T vo : list) {
            if (StringUtil.isSame(storage.getParentKey(vo), "0")) {
                newlist.add(getRecursiveTree(vo, list, storage));
            }
        }
        return newlist;
    }

    public static <T> List<T> buildTree(List<T> list, TreeStorage storage) {
        List<T> newlist = new ArrayList<>();
        for (T vo : list) {
            if (StringUtil.isSame(storage.getParentKey(vo), "0")) {
                newlist.add(getRecursiveTree(vo, list, storage));
            }
        }
        return newlist;
    }

    public static <T> List<T> buildTree(TreeStorage<T> storage, String prefix) {
        List<T> newlist = new ArrayList<>();
        List<T> list = storage.getList();
        for (T vo : list) {
            if (StringUtil.isSame(StringUtil.nvl(storage.getParentKey(vo)).replaceAll(Matcher.quoteReplacement(prefix), ""), "0")) {
                newlist.add(getRecursiveTree(vo, list, storage));
            }
        }
        return newlist;
    }

    public static <T> List<T> buildTree(List<T> list, TreeStorage storage, String prefix) {
        List<T> newlist = new ArrayList<>();
        for (T vo : list) {
            if (StringUtil.isSame(StringUtil.nvl(storage.getParentKey(vo)).replaceAll(Matcher.quoteReplacement(prefix), ""), "0")) {
                newlist.add(getRecursiveTree(vo, list, storage));
            }
        }
        return newlist;
    }

    public static <T> T getRecursiveTree(T parentMenu, List<T> list, TreeStorage storage) {
        if (!storage.isLeaf(parentMenu)) {
            for (T menu : list) {
                if (StringUtil.isSame(storage.getKey(parentMenu), storage.getParentKey(menu))) {
                    storage.addChildren(parentMenu, getRecursiveTree(menu, list, storage));
                }
            }
        }
        return parentMenu;
    }

}
