package com.griffins.gbiz.admin.code.repository;

import com.griffins.common.annotation.Mapper1;
import com.griffins.common.domain.ZtreeVO;
import com.griffins.gbiz.admin.code.domain.CodeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 파일명 : com.griffins.gsales.admin.code.repository.CodeDAO
 * *
 * {클래스의 기능과 용도 설명}
 * ===============================================
 *
 * @author 이재철
 * @since 2018-05-27
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
@Mapper1
@Mapper
public interface CodeDAO {
    List<CodeVO> selectCodeList();

    Integer selectLastSort(CodeVO param);

    List<ZtreeVO> selectMenuCodeTreeList(CodeVO param);

    List<ZtreeVO> selectMenuCodeMapTree(CodeVO param);

    int insertCode(CodeVO insertParam);

    int insertMenuCodeMap(CodeVO insertParam);

    CodeVO selectCode(CodeVO vo);

    int updateCode(CodeVO vo);

    int deleteMenuCodeMap(CodeVO vo);

    int updateLowerCode(CodeVO oldVo);

    Integer selectCodeCount(CodeVO param);

    List<Integer> selectMenuSeqListByCode(CodeVO param);

    int updateCodeSort(CodeVO param);

    Integer deleteCode(CodeVO deleteParam);
}
