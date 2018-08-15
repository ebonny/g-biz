package com.griffins.common.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * 파일명 : griffins.common.oop.tree.TreeStorage
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
public abstract class TreeStorage<T> extends ArrayList<T> {
    protected List<T> list;

    public List<T> getList() {
        return list;
    }

    public TreeStorage(Object source) {
        this.list = new ArrayList<>();
        initList(source);
    }

    public int getSize() {
        return list.size();
    }

    public abstract void initList(Object source);

    public abstract Object getKey(T vo);

    public abstract String getLabel(T vo);

    public abstract Object getParentKey(T vo);

    public abstract void addChildren(T parent, T child);

    public abstract void setChildren(T parent, List<T> children);

    public abstract boolean isLeaf(T vo);

    public T getDTO(int index) {
        return this.list.get(index);
    }
}
