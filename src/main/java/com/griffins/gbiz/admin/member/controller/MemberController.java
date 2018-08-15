package com.griffins.gbiz.admin.member.controller;

import com.griffins.common.domain.AjaxResult;
import com.griffins.common.util.StringUtil;
import com.griffins.gbiz.admin.authority.repository.AuthorityRepository;
import com.griffins.gbiz.admin.member.domain.Member;
import com.griffins.gbiz.admin.member.domain.MemberVO;
import com.griffins.gbiz.admin.member.repository.MemberDAO;
import com.griffins.gbiz.admin.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Collection;

/**
 * 파일명 : com.griffins.gsales.admin.member.controller.MemberController
 * *
 * {클래스의 기능과 용도 설명}
 * ===============================================
 *
 * @author 이재철
 * @since 2018-07-30 0030
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
@Controller
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberDAO memberDAO;
    @Autowired
    private AuthorityRepository authRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping("/list.gs")
    public String list() {
        return "admin/member/getMemberList.tiles";
    }

    @RequestMapping("/listAjax.gs")
    @ResponseBody
    public Collection<Member> listAjax() {
        return memberRepository.findAll();
    }

    @RequestMapping("/addAuthModalAjax.gs")
    public String addauthModal(Model model) {
        model.addAttribute("authList", authRepository.findAll());
        return "admin/member/addMemberAuth.jp";
    }

    @RequestMapping("/viewAjax.gs")
    @ResponseBody
    public AjaxResult<Member> view(Member member) throws Exception {
        AjaxResult<Member> result = new AjaxResult<>();
        try {
            Member vo = memberRepository.findById(member.getId()).get();
            vo.setAuthList(memberDAO.selectAuthsByMemberId(vo.getId()));
            result.setData(vo);
        }catch (Exception e) {
            result.setIssuccess(e);
        }
        return result;
    }

    @RequestMapping("/saveAjax.gs")
    @ResponseBody
    public AjaxResult<Member> save(Member member, String authSeqs) throws Exception {
        AjaxResult<Member> result = new AjaxResult<>();
        try {
            member.setPassword(passwordEncoder.encode(member.getPassword()));
            result.setData(memberRepository.save(member));
            String[] seqs = authSeqs.split(",");
            if(seqs != null && seqs.length > 0) {
                memberDAO.deleteMemberAuth(member.getId());
                MemberVO mvo = new MemberVO();
                mvo.setId(member.getId());
                Arrays.asList(seqs).stream().forEach(item -> {
                    mvo.setAuthSeq(Integer.parseInt(item));
                    memberDAO.insertMemberAuth(mvo);
                });
            }
        }catch (Exception e) {
            result.setIssuccess(e);
        }
        return result;
    }

    @RequestMapping("/removeAjax.gs")
    @ResponseBody
    public AjaxResult<ResponseEntity> remove(Member member) throws Exception {
        AjaxResult<ResponseEntity> result = new AjaxResult<>();
        try {
            memberRepository.delete(member);
            result.setData(ResponseEntity.ok().build());
        }catch (Exception e) {
            result.setIssuccess(e);
        }
        return result;
    }

    @RequestMapping("/updateAjax.gs")
    @ResponseBody
    public AjaxResult<Integer> update(MemberVO param, String authSeqs) throws Exception {
        AjaxResult<Integer> result = new AjaxResult<>();
        try {
            if(StringUtil.isNotEmpty(param.getPassword())) {
                param.setPassword(passwordEncoder.encode(param.getPassword()));
            }
            result.setData(memberDAO.updateMember(param));

            memberDAO.deleteMemberAuth(param.getId());
            String[] seqs = authSeqs.split(",");
            if(seqs != null && seqs.length > 0) {
                MemberVO mvo = new MemberVO();
                mvo.setId(param.getId());
                Arrays.asList(seqs).stream().forEach(item -> {
                    mvo.setAuthSeq(Integer.parseInt(item));
                    memberDAO.insertMemberAuth(mvo);
                });
            }
        }catch (Exception e) {
            result.setIssuccess(e);
        }
        return result;
    }

}
