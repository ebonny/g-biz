package com.griffins.common.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Map;

/**
 * 파일명 : AESUtil.java
 * <p>
 * AES 256 암호화/복호화
 * ===============================================
 *
 * @author 이재철
 * @since 2016. 12. 9.
 * <p>
 * 수정자         수정일         수정내용
 * -------------  -------------  -----------------
 * <p>
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public class AESUtil {

    public static byte[] ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    private static Map<String, String> keyMap;

    public static void initKeyMap(Map<String, String> aesConfig) {
        keyMap = aesConfig;
    }

    public static String encode(String str) {
        int today = DateUtil.getDayOfWeek();
        return encode(str, keyMap.get("key." + today));
    }

    public static String encode(String str, String key) {
        try {
            byte[] textBytes = str.getBytes("UTF-8");
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = null;
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
            return Base64.encodeBase64String(cipher.doFinal(textBytes));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String decode(String str) {
        int today = DateUtil.getDayOfWeek();
        return decode(str, keyMap.get("key." + today));
    }

    public static String decode(String str, String key) {
        try {
            byte[] textBytes = Base64.decodeBase64(str);
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            SecretKeySpec newKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            /**
             * illegal key size 오류가 날 경우
             *
             * 1. 프로젝트 우클릭 - Properties 선택 - Java Build Path 선택 - Libraries 탭에서 JRE System 선택 - Edit 버튼클릭 - Workspace default JRE 선택
             * 2. WEB-INF/lib 안에 있는
             *    local_policy.jar 와 US_export_policy.jar 파일을 %JAVA_HOME%/jre/lib/security 폴더에 덮어쓰면 됩니다.
             *    %JAVA_HOME% 은 보통 C:\Program Files\java 입니다.
             */
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
            return new String(cipher.doFinal(textBytes));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}