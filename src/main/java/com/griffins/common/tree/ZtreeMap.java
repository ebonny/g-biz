package com.griffins.common.tree;

import com.griffins.common.domain.ZtreeVO;
import com.griffins.common.util.StringUtil;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 파일명 : griffins.common.oop.tree.ZtreeMap
 * *
 * {클래스의 기능과 용도 설명}
 * ===============================================
 *
 * @author 이재철
 * @since 2017-07-10
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public class ZtreeMap extends TreeStorage<ZtreeVO> {

    public ZtreeMap(Object source) {
        super(source);
    }

    @Override
    public void initList(Object source) {
        Map map = (Map) source;
        Iterator<Object> it = map.keySet().iterator();
        while (it.hasNext()) {
            Object key = it.next();
            ZtreeVO vo = new ZtreeVO();
            vo.setId(StringUtil.toString(key));
            vo.setName(StringUtil.toString(map.get(key)));
            list.add(vo);
        }
    }

    @Override
    public Object getKey(ZtreeVO vo) {
        return vo.getId();
    }

    @Override
    public String getLabel(ZtreeVO vo) {
        return vo.getName();
    }

    @Override
    public Object getParentKey(ZtreeVO vo) {
        return vo.getPId();
    }

    @Override
    public void addChildren(ZtreeVO parent, ZtreeVO child) {
        parent.addChildren(child);
    }

    @Override
    public void setChildren(ZtreeVO parent, List<ZtreeVO> children) {
        parent.setChildren(children);
    }

    @Override
    public boolean isLeaf(ZtreeVO vo) {
        return vo.getIsLeaf();
    }

}
