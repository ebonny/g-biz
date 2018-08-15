package com.griffins.common;

import com.griffins.gbiz.admin.menu.domain.FuncHelper;

/**
 * 파일명 : com.griffins.common.GConstants
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
public interface GConstants {

    int ERROR_SESSION_TIMEOUT = 1000;
    int ERROR_FORBIDDEN = 1001;
    int ERROR_UNKNOWN = 1002;
    int ERROR_SESSION_EXPIRED = 1003;
    int ERROR_BAD_EXT = 1004;
    int ERROR_NO_AUTH = 1005;
    int ERROR_CONNECT_FAIL = 1006;
    int ERROR_MAX_UPLOAD_EXCEED = 0;
    int ERROR_COMMON = 1010;
    int ERROR_INTERNAL = 1011;
    int ERROR_DISCONNECTED = 1012;
    int ERROR_MULTITAB_FOUND = 1013;
    int ERROR_UNAUTHORIZED = 1014;
    int ERROR_ALREADY_LOGOUT = 1015;
    int ERROR_ANOTHER_LOGIN = 1016;
    int ERROR_MAIL_SEND = 1017;
    int ERROR_LOGIN_REQUIRED = 1018;
    int ERROR_SESSION_TIMEOUT_NO_ALERT = 1020;
    int ERROR_UPLOAD_EXCEED = 1030;
    int ERROR_I_AM_NOT_TEAPOT = 418;

    String URL_ERROR = "/common/error";

    String ACITREE_ICON_FILE = "file";
    String ACITREE_ICON_FOLDER = "folder";
    String ACITREE_ICON_ROOT = "root";

    String UPLOAD_COMPANY = "path.company";
    String UPLOAD_CHANCE = "path.chance";
    String UPLOAD_CLIENT = "path.client";
    String UPLOAD_COMMON = "path.common";

    int CRLF_CONVERT_NONE = 0;
    int LF_TO_CRLF = 1;
    int CRLF_TO_LF = 2;

    FuncHelper FUNC_LIST = new FuncHelper("list");
    FuncHelper FUNC_REGIST = new FuncHelper("add");
    FuncHelper FUNC_EDIT = new FuncHelper("edit");
    FuncHelper FUNC_DEL = new FuncHelper("delete");
    FuncHelper FUNC_VIEW = new FuncHelper("view");
    FuncHelper FUNC_MENU = new FuncHelper("menu");

    String ZTREE_ROOT_ICON = "/css/plugins/ztree/root.png";
    String ZTREE_FOLDER_ICON = "/css/plugins/ztree/folder.png";
}
