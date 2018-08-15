package com.griffins.common.log.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 파일명 : griffins.common.util.log.console.LogConsole
 * *
 * 로그를 콘솔에 출력
 * ===============================================
 *
 * @author 이재철
 * @since 2017-07-16
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public interface LogConsole {
    Logger LOG = LoggerFactory.getLogger(LogConsole.class);
}
