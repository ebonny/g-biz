package com.griffins.config;

import com.griffins.config.auth.GInterceptor;
import com.griffins.config.common.NewExcelView;
import com.griffins.config.common.PdfView;
import com.griffins.config.filter.EntryControlFilter;
import com.griffins.config.xss.EsmpXssEscapeServletFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.nio.charset.Charset;
import java.util.Locale;

/**
 * 서블릿 설정
 * (xml 설정 시절의 dispatcher-servlet.xml + web.xml 설정에 해당함)
 * ===============================================
 *
 * @author 이재철
 * @since 2017-09-26 0026
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
@Configuration
@EnableWebMvc
@ComponentScan({"com.griffins"})
public class WebConfig implements WebMvcConfigurer {
    @Value("${spring.mvc.locale}")
    Locale locale = null;
    @Value("${spring.messages.encoding}")
    String messageEncoding = null;

    /* ######################################################## Override ################################################################### */

    /* DispatcherServlet 사용자 설정 적용을 위해 enable 메소드 호출 (DispatcherServletInitializer.java) */
//   @Override
//   public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
//      configurer.enable();
//   }

    /* jsp 가 서블릿으로 합쳐질때 코드길이가 64KB 를 넘을경우 JVM 스펙위반으로 오류발생하는거 방지 */
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> customizer() {
        return new JspConfig();
    }


    /* 인터셉터 등록 */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor())
                .addPathPatterns("/**/*.gs")
                .excludePathPatterns("/login/**", "/home*")
                .excludePathPatterns("**/*Ajax.gs", "/**/*Download*")
                .excludePathPatterns("/**/create*.gs", "/**/update*.gs", "/**/remove*.gs", "/**/confirm*.gs", "/**/reject*.gs");
    }

    //=== 인터셉터 안에서 @Autowired 를 사용하는데, 인터셉터 자체가 Bean 이 아니면 내부에서 @Autowired 가 되지 않는다
    @Bean
    public AsyncHandlerInterceptor interceptor() {
        return new GInterceptor();
    }

    /* ######################################################## 다국어 설정 ################################################################### */
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
        ms.setBasenames("classpath:messages/message-common", "classpath:messages/message-error");
        ms.setDefaultEncoding(messageEncoding);
        return ms;
    }

    @Bean
    public MessageSourceAccessor getMessageSourceAccessor() {
        ReloadableResourceBundleMessageSource m = messageSource();
        return new MessageSourceAccessor(m);
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor intceptor = new LocaleChangeInterceptor();
        // request 로 넘오오는 language parameter 를 받아서 locale 로 설정한다.
        intceptor.setParamName("lang");
        return intceptor;
    }

    @Bean
    public LocaleResolver localeResolver() {
        //=== 세션 기준으로 로케일 지정
        SessionLocaleResolver resolver = new SessionLocaleResolver();
        //=== 쿠키 기준으로 로케일 지정 (세션이 끊겨도 브라우저에 설정된 쿠키 기준)
        // CookieLocaleResolver clr = new CookieLocaleResolver();

        //=== 기본 로케일 지정 : [참고] https://justinrodenbostel.com/2014/05/13/part-4-internationalization-in-spring-boot/
        resolver.setDefaultLocale(locale);
        return resolver;
    }
    /* ######################################################## 리소스 설정 ############################################################# */

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations(new String[]{"classpath:/resources/", "classpath:/static/"});
    }

    /* ######################################################## ViewResolver ############################################################# */
    @Bean
    public NewExcelView excelView() {
        return new NewExcelView();
    }

    @Bean
    public PdfView pdfView() {
        return new PdfView();
    }

    /* ######################################################## 기타 설정 ################################################################### */

    //=== 다중 탭 세션관리 필터
    @Bean
    public FilterRegistrationBean multipleTabSessionControlFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean(new EntryControlFilter());
        bean.addUrlPatterns("*.gs");
        bean.setOrder(0);
        return bean;
    }

    //=== Lucy XSS 필터
    @Bean
    public FilterRegistrationBean xssFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean(new EsmpXssEscapeServletFilter());
//        bean.addUrlPatterns("/*");
        bean.addUrlPatterns("*.gs");
        bean.setOrder(3);
        return bean;
    }


    //=== Concurrent Session 컨트롤을 위해 등록
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }


    //=== ResponseBody 메세지의 한글 처리
    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    }

}
