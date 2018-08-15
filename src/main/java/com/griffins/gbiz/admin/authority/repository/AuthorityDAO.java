package com.griffins.gbiz.admin.authority.repository;

import com.griffins.common.annotation.Mapper1;
import com.griffins.common.domain.ZtreeVO;
import com.griffins.gbiz.admin.authority.domain.AuthFuncVO;
import com.griffins.gbiz.admin.authority.domain.AuthorityVO;
import com.griffins.gbiz.admin.menu.domain.FuncVO;
import com.griffins.gbiz.admin.menu.domain.MenuVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 파일명 : com.griffins.gsales.admin.authority.repository.AuthorityDAO
 * *
 * {클래스의 기능과 용도 설명}
 * ===============================================
 *
 * @author 이재철
 * @since 2018-07-27 0027
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
@Mapper1
@Mapper
public interface AuthorityDAO {

    List<AuthorityVO> selectInheritAuthList();

    int insertAuthFunc(AuthFuncVO insertParam);

    AuthorityVO selectAuthorityBySeq(Integer seq);

    int insertAuth(AuthorityVO insertParam);

    List<ZtreeVO> selectMenuZtreeByAuth(Map map);

    List<ZtreeVO> selectMenuZtree(Map<String, String> map2);

    List<MenuVO> selectMenuList();

    List<MenuVO> selectMenuListByAuth(Integer authSeq);

    List<FuncVO> selectFuncList();

    List<FuncVO> selectFuncListByAuth(Integer authSeq);

    Integer updateAuth(AuthorityVO param);

    int deleteAuthFunc(Integer authSeq);

    FuncVO selectFuncByType(FuncVO searchParam);

    Integer deleteAuth(AuthorityVO param);
}
