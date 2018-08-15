package com.griffins.gbiz.admin.member.repository;

import com.griffins.common.annotation.Mapper1;
import com.griffins.common.domain.TagChipVO;
import com.griffins.gbiz.admin.member.domain.Member;
import com.griffins.gbiz.admin.member.domain.MemberVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 파일명 : com.griffins.gsales.admin.auth.repository.MemberDAO
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
@Mapper1
@Mapper
public interface MemberDAO {
    int increaseLoginFailCount(Member evo);

    int deleteMemberAuth(String id);

    int insertMemberAuth(MemberVO mvo);

    Integer updateMember(MemberVO param);

    List<TagChipVO> selectAuthsByMemberId(String id);
}
