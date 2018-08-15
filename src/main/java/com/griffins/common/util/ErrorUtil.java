package com.griffins.common.util;

import org.mybatis.spring.MyBatisSystemException;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.transaction.CannotCreateTransactionException;

/**
 * 파일명 : com.griffins.common.util.ErrorUtil
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
public class ErrorUtil {
    public static boolean isConnectionFail(Exception e) {
        return (e instanceof CannotCreateTransactionException
                || e instanceof RecoverableDataAccessException
                || e instanceof MyBatisSystemException
                || e instanceof CannotGetJdbcConnectionException);
    }
}
