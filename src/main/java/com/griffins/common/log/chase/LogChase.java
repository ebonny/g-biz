package com.griffins.common.log.chase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 프로젝트명 : esmp
 * 파일명 : griffins.common.util.log.chase.LogChase
 * *
 * 사용자 행위 추적용 로그 기록
 * ===============================================
 *
 * @author 이재철
 * @since #{DATE}
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public interface LogChase {
    Logger LOG = LoggerFactory.getLogger(LogChase.class);
}
