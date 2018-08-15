package com.griffins.common.domain;

import com.griffins.common.GConstants;
import com.griffins.common.util.LogUtil;
import com.griffins.common.util.StringUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 파일명 : com.griffins.common.domain.ZtreeVO
 * *
 * {클래스의 기능과 용도 설명}
 * ===============================================
 *
 * @author 이재철
 * @since 2018-07-26 0026
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
@Data
@EqualsAndHashCode(of = "id")
public class ZtreeVO {
    private String id;
    private String pId;
    private String name;
    private String email;
    private String category;
    private String etc;

    private Boolean isParent;
    private Boolean isLeaf;
    private Boolean nocheck;
    private Boolean open;
    private Boolean checked;
    private Boolean chkDisabled;
    private Boolean drag;
    private Boolean isHidden;
    private Boolean editable;
    private Boolean isParentLeaf;
    private Boolean noselect;

    @Getter(AccessLevel.NONE)
    private String icon;
    private final String iconFolder = GConstants.ZTREE_FOLDER_ICON;
    private final String iconRoot = GConstants.ZTREE_ROOT_ICON;

    private List<ZtreeVO> children;


    public String getIconFolder() {
        String ctx = "";
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            ctx = request.getContextPath();
        } catch (Exception e) {
            LogUtil.debug("ZtreeVO-getIconFolder : request 획득 실패", LogUtil.CONSOLE_ONLY);
        }
        return StringUtil.isNotEmpty(iconFolder) ? ctx + iconFolder : iconFolder;
    }

    public String getIconRoot() {
        String ctx = "";
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            ctx = request.getContextPath();
        } catch (Exception e) {
            LogUtil.debug("ZtreeVO-getIconRoot : request 획득 실패", LogUtil.CONSOLE_ONLY);
        }
        return StringUtil.isNotEmpty(iconRoot) ? ctx + iconRoot : iconRoot;
    }

    public String getIcon() {
        String ctx = "";
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            ctx = request.getContextPath();
        } catch (Exception e) {
            LogUtil.debug("ZtreeVO-getIcon : request 획득 실패", LogUtil.CONSOLE_ONLY);
        }
        return StringUtil.isNotEmpty(icon) ? ctx + icon : icon;
    }

    public void addChildren(ZtreeVO vo) {
        if (children == null)
            children = new ArrayList<>();
        children.add(vo);
    }
}
