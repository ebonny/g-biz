package com.griffins.common.util;

import com.griffins.gbiz.admin.code.domain.CodeVO;
import com.griffins.gbiz.admin.code.repository.CodeDAO;

import java.util.*;

/**
 * The Class Code Util.
 */
public class CodeUtil {

    private static Map<String, List<CodeVO>> codeMap = new HashMap<>();

    public static void init(CodeDAO codeDAO) {
        List<CodeVO> list = codeDAO.selectCodeList();
        for (CodeVO vo : list) {
            if (vo.getCodeGroup().equals("0")) {
                codeMap.put(vo.getCodeValue(), getSubList(vo.getCodeValue(), list));
            }
        }
    }

    private static List<CodeVO> getSubList(String group, List<CodeVO> list) {
        Set<CodeVO> newList = new TreeSet(new Comparator() {
            @Override
            public int compare(Object o, Object t1) {
                CodeVO a = (CodeVO) o;
                CodeVO b = (CodeVO) t1;
                return a.getCodeDesc().compareTo(b.getCodeDesc());
            }
        });
        for (CodeVO vo : list) {
            if (vo.getCodeGroup().equals(group)) {
                newList.add(vo);
            }
        }
        List<CodeVO> finalList = new ArrayList<>();
        finalList.addAll(newList);
        return finalList;
    }


    public static List<CodeVO> getCodeList(String codeGroup) {
        return codeMap.get(codeGroup);
    }
}
