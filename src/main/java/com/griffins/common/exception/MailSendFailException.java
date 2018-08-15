package com.griffins.common.exception;

/**
 * 파일명 : griffins.common.exception.MailSendFailException
 * *
 * 메일전송 실패용 오류
 * ===============================================
 *
 * @author 이재철
 * @since 2017-05-02
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public class MailSendFailException extends RuntimeException {
    public MailSendFailException(String msg) {
        super(msg);
    }
}
