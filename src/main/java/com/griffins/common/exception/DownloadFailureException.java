package com.griffins.common.exception;

/**
 * 파일명 : griffins.common.exception.DownloadFailureException
 * *
 * 다운로드 실패 오류 발생시 사용
 * ===============================================
 *
 * @author 이재철
 * @since 2017-03-12
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public class DownloadFailureException extends Exception {
    public DownloadFailureException(String msg) {
        super(msg);
    }
}
