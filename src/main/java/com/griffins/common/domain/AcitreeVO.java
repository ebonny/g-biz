package com.griffins.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 파일명 : com.griffins.common.domain.AcitreeVO
 * *
 * {클래스의 기능과 용도 설명}
 * ===============================================
 *
 * @author 이재철
 * @since 2018-07-08
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
@Data
@EqualsAndHashCode(of = "id")
public class AcitreeVO {
    private Integer id;            // 노드 id
    private Integer parentId;    // 부모노드 id
    private String label;        // 노드이름
    private String icon;        // 노드 아이콘
    private Boolean inode;        // 자식이 있으면 true
    private Boolean radio;        // 라디오버튼 생성여부
    private Boolean open;        // 확장 여부

    private List<AcitreeVO> branch;

    /**
     * 트리에서 현재 아이템에 하위 아이템을 추가
     *
     * @param vo
     * @author 이재철
     * @date 2016. 8. 23.
     */
    public void addBranch(AcitreeVO vo) {
        if (branch == null)
            branch = new ArrayList<AcitreeVO>();
        branch.add(vo);
    }
}
