package com.griffins.common.file;

import lombok.Data;

import java.io.File;

/**
 * 파일명 : griffins.common.file.FileNameVO
 * *
 * tranfer 된 파일, 실제파일명, 저장파일명 을 세트로 관리
 * ===============================================
 *
 * @author 이재철
 * @since 2017-04-30
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
@Data
public class FileNameVO {
    private String originalName;
    private String saveName;
    private File file;
}
