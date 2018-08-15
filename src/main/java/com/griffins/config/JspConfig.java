package com.griffins.config;

import org.apache.catalina.Context;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.apache.tomcat.util.descriptor.web.*;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

import javax.servlet.descriptor.JspConfigDescriptor;
import javax.servlet.descriptor.JspPropertyGroupDescriptor;
import javax.servlet.descriptor.TaglibDescriptor;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 파일명 : griffins.config.security.JspConfig
 * *
 * WebConfig 에서 설정 못하는 나머지 web.xml 설정
 * [참고1] https://gist.github.com/ghillert/39536f902d7ac0017964
 * [참고2] http://kamsi76.tistory.com/entry/Spring4-JavaConfig-%EC%84%A4%EC%A0%95-WebInitializerjava
 * <p>
 * 1. jsp 전역 설정
 * - 페이지에 UTF-8 적용
 * - 모든 jsp 에 taglib.jsp 를 include 시킴 (컴파일 단계 이전)
 * <p>
 * 2. custom taglib (esm.tld) 선언
 * <p>
 * 3. 톰캣의 "대용량 업로드 오류" 방지
 * <p>
 * ===============================================
 *
 * @author 이재철
 * @since 2017-10-04
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
public class JspConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addContextCustomizers(new TomcatContextCustomizer() {
            @Override
            public void customize(Context context) {
                final Collection<JspPropertyGroupDescriptor> groupList = new ArrayList<>();
                final Collection<TaglibDescriptor> taglibList = new ArrayList<>();

                //=== index 페이지
                context.addWelcomeFile("index.jsp");

                //=== <error-page> : /resources/error 폴더로 대체
                final ErrorPage errorPage400 = new ErrorPage();
                errorPage400.setErrorCode(400);
                errorPage400.setLocation("/WEB-INF/view/common/400error.jsp");

                final ErrorPage errorPage403 = new ErrorPage();
                errorPage403.setErrorCode(403);
                errorPage403.setLocation("/WEB-INF/view/common/403error.jsp");

                final ErrorPage errorPage404 = new ErrorPage();
                errorPage404.setErrorCode(404);
                errorPage403.setLocation("/WEB-INF/view/common/404error.jsp");

                final ErrorPage errorPage500 = new ErrorPage();
                errorPage500.setErrorCode(500);
                errorPage403.setLocation("/WEB-INF/view/common/500error.jsp");

                context.addErrorPage(errorPage500);
                context.addErrorPage(errorPage404);
                context.addErrorPage(errorPage403);

                //=== <jsp-property-group>
                final JspPropertyGroup group = new JspPropertyGroup();
                group.addUrlPattern("*.jsp");
                group.setPageEncoding("UTF-8");
                group.setElIgnored(Boolean.FALSE.toString());
                group.addIncludePrelude("/WEB-INF/tld/taglib.jsp");
                groupList.add(new JspPropertyGroupDescriptorImpl(group));

                //=== <taglib>
                TaglibDescriptor utilTag = new TaglibDescriptorImpl("/WEB-INF/tld/jutil.tld", "griffins.common.jutil");
                TaglibDescriptor profileTag = new TaglibDescriptorImpl("/WEB-INF/tld/qcm.tld", "griffins.common.qcm");
                TaglibDescriptor gsTag = new TaglibDescriptorImpl("/WEB-INF/tld/gs.tld", "griffins.common.gs");
                taglibList.add(utilTag);
                taglibList.add(profileTag);
                taglibList.add(gsTag);

                final JspConfigDescriptor jspConfig = new JspConfigDescriptorImpl(groupList, taglibList);
                context.setJspConfigDescriptor(jspConfig);
            }
        });


        //=== 파일 업로드시 Connection Reset 방지 : WenConfig 에 tomcatEmbedded 로 대체
        factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
            if ((connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>)) {
                ((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(-1); //-1 means unlimited
            }
        });
    }

}
