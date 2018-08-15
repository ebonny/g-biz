package com.griffins.common.log.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 파일명 : griffins.common.util.log.file.LogFile
 * *
 * 로그를 파일로 저장
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
public interface LogFile {
    Logger LOG = LoggerFactory.getLogger(LogFile.class);
}
