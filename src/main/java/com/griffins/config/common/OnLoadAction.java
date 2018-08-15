package com.griffins.config.common;

import com.griffins.common.util.CodeUtil;
import com.griffins.common.util.CommonUtil;
import com.griffins.config.auth.repository.LoginUserDetailsDAO;
import com.griffins.gbiz.admin.code.repository.CodeDAO;
import com.griffins.gbiz.admin.menu.domain.FuncVO;
import com.griffins.gbiz.admin.menu.domain.MenuVO;
import lombok.Getter;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.util.List;
import java.util.Map;

/**
 * 프로젝트명 : gsales
 * 파일명 : com.griffins.config.common.OnLoadAction
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
@Component
public class OnLoadAction implements ServletContextAware {
    @Resource(name = "exceptionPageList")
    private List<String> exceptionPageList;

    @Autowired
    private LoginUserDetailsDAO securityDAO;

    @Resource(name = "optionConfig")
    private Map<String, String> optionConfig;
    @Resource(name = "codeConfig")
    private Map<String, String> codeConfig;
    @Resource(name = "extConfig")
    private Map<String, String> extConfig;

    @Getter
    private List<MenuVO> exceptMenus;

    @Autowired
    private CodeDAO codeDAO;

    private ServletContext servletContext;


    @PostConstruct
    public void load() {
        initExceptMenu();
        initBeanForJsp();
        CodeUtil.init(codeDAO);
    }

    private void initExceptMenu() {
        String exceptions = "";
        for (String url : exceptionPageList) {
            exceptions += "'" + url + "',";
        }
        if (exceptions.length() > 0) {
            exceptions = exceptions.substring(0, exceptions.length() - 1);
        }
        List<MenuVO> exceptMenus = securityDAO.selectExceptionMenuList(exceptions);
        List<FuncVO> exceptFuncs = securityDAO.selectExceptionFuncList(exceptions);

        if (exceptFuncs != null && exceptMenus != null) {
            CommonUtil.mapFuncToMenu(exceptFuncs, exceptMenus);
            this.exceptMenus = exceptMenus;
        }
    }

    /**
     * jsp 에서 사용할 Bean 을 등록한다.
     */
    private void initBeanForJsp() {
        this.servletContext.setAttribute("serverOption", JSONObject.fromObject(optionConfig));
        this.servletContext.setAttribute("serverCode", JSONObject.fromObject(codeConfig));
        this.servletContext.setAttribute("serverExt", JSONObject.fromObject(extConfig));
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
