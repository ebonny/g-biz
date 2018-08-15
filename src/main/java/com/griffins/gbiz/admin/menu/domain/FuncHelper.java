package com.griffins.gbiz.admin.menu.domain;

import java.util.HashMap;

/**
 * 파일명 : FuncHelper.java
 * <p>
 * 등록/수정/삭제 등 각 기능별로 기능유형, 이름, URL prefix 를 관리
 * ===============================================
 *
 * @author 이재철
 * @since 2016. 9. 1.
 * <p>
 * 수정자         수정일         수정내용
 * -------------  -------------  -----------------
 * <p>
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public class FuncHelper extends HashMap<String, String> {

    public FuncHelper(String trigger) {
        switch (trigger) {
            case "list":
                put("type", "list");
                put("label", "조회");
                break;
            case "add":
                put("type", "add");
                put("label", "등록");
                put("prefix", "add");
                break;
            case "edit":
                put("type", "edit");
                put("label", "수정");
                put("prefix", "modify");
                break;
            case "delete":
                put("type", "delete");
                put("label", "삭제");
                put("prefix", "remove");
                break;
            case "view":
                put("type", "view");
                put("label", "상세보기");
                put("prefix", "view");
                break;
            case "menu":
                put("type", "menu");
                put("label", "메뉴");
                break;
            case "super":
                put("type", "super");
                put("label", "관리");
                break;
            default:
                throw new RuntimeException(trigger + " is not a FuncHelper trigger");
        }
    }

    /**
     * 자바딴에서 사용하는 getter
     * 메소드명을 getType으로 변경하면 EL에서 사용하는 Map 의 get("type") 과 충돌난다.
     *
     * @return
     * @author 이재철
     * @date 2016. 9. 1.
     */
    public String type() {
        return (String) get("type");
    }

    public String label() {
        return (String) get("label");
    }

    public String prefix() {
        return (String) get("prefix");
    }
}
