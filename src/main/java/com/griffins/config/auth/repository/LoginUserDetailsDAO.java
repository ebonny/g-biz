package com.griffins.config.auth.repository;

import com.griffins.common.annotation.Mapper1;
import com.griffins.gbiz.admin.menu.domain.FuncVO;
import com.griffins.gbiz.admin.menu.domain.MenuVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 프로젝트명 : gsales
 * 파일명 : com.griffins.config.auth.repository.LoginUserDetailsDAO
 * *
 * {클래스의 기능과 용도 설명}
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
@Mapper1
@Mapper
public interface LoginUserDetailsDAO {

    List<Integer> selectMemberAuthorities(String username);

    List<MenuVO> selectMenuByAdmin();

    List<MenuVO> selectMenuByAuthorities(Map map);

    List<FuncVO> selectFuncByAdmin();

    List<FuncVO> selectFuncByAuthorities(Map map);

    List<MenuVO> selectExceptionMenuList(String exceptions);

    List<FuncVO> selectExceptionFuncList(String exceptions);

    int updateLoginStatus(Map<String, Object> map);
}
