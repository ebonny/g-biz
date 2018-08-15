package com.griffins.gbiz.admin.menu.domain;

import java.util.HashMap;

/**
 * 파일명 : griffins.common.util.MenuMap
 * *
 * HashMap 은 get 의 파라미터가 Object 이다.
 * 그래서, put 할때는 키를 String "11" 넣었다가 get 할때는 Integer 11 로 가져오려고 시도할 경우
 * 컴파일 오류는 안나는데 값을 가져오지 못하는 문제가 발생한다.
 * 이런 개발자의 실수를 방지하기 위해서 만든 클래스.
 * ===============================================
 *
 * @author 이재철
 * @since 2017-06-16
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public class MenuMap extends HashMap<String, MenuVO> {
    public void put(Integer menuId, MenuVO vo) {
        put(String.valueOf(menuId), vo);
    }

    public MenuVO get(Integer key) {
        return get(String.valueOf(key));
    }
}
