package com.griffins.config;

import com.griffins.config.auth.*;
import com.griffins.config.filter.DuplicationLoginCheckFilter;
import com.griffins.config.filter.InvalidSessionHandlerSupportFilter;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.IGoogleAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 파일명 : com.griffins.config.SecurityConfig
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
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource(name = "codeConfig")
    private Map<String, String> codeConfig;

    @Autowired
    private Environment environment;

    @Autowired
    private CustomAuthenticationEntryPoint authenticationEntryPoint;

    //@Autowired
    //private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/index.jsp", "/js/**", "/font/**", "/css/**", "/img/**", "/resources/**", "/WEB-INF/**", "/**/*Ajax.gs");
//      .anyRequest();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsServiceBean())
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
//            .csrfTokenRepository(csrfTokenRepository())
//            .requireCsrfProtectionMatcher(new CsrfSecurityRequestMatcher())
//            .and()
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandlerImpl())
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/public/**", "/home", "/login**", "/signin**").permitAll()
                .anyRequest().authenticated()
                .and()
//         .httpBasic()
//            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .maximumSessions(Integer.parseInt(environment.getProperty("option.session.max.count")))
                .maxSessionsPreventsLogin(false)
                .expiredUrl(codeConfig.get("url.expired"))
                .and()
                .invalidSessionStrategy(invalidSessionHandler())
                .and()
                .formLogin()
                .authenticationDetailsSource(new CustomWebAuthenticationDetailsSource())
                .usernameParameter(codeConfig.get("security.param.id"))
                .passwordParameter(codeConfig.get("security.param.pwd"))
                .loginPage(codeConfig.get("url.loginpage"))
                .loginProcessingUrl(codeConfig.get("url.login"))
                .successHandler(loginSuccessHandler())
                .failureHandler(loginFailureHandler())
                .permitAll()
                .and()
                .logout()
                .deleteCookies("JSESSIONID")
                .deleteCookies(codeConfig.get("session.cookie.name"))
                .logoutSuccessHandler((req, res, auth) -> {
//                req.getSession().invalidate();
                    res.sendRedirect(codeConfig.get("url.home"));
                })
                .invalidateHttpSession(true)
//            .logoutRequestMatcher(new AntPathRequestMatcher(codeConfig.get("url.logout")))
                .permitAll()
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(new DuplicationLoginCheckFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new InvalidSessionHandlerSupportFilter(), ExceptionTranslationFilter.class);
    }

    @Bean
    public InvalidSessionStrategy invalidSessionHandler() {
        return new InvalidSessionHandler();
    }

    //=== Custom 비밀번호 인증
    @Bean
    public CustomUserDetailsAuthenticationProvider authenticationProvider() {
        CustomUserDetailsAuthenticationProvider provider = new CustomUserDetailsAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setAuthenticator(googleAuthenticator());
        return provider;
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repo = new HttpSessionCsrfTokenRepository();
        repo.setHeaderName(codeConfig.get("security.csrf.headerName"));
        repo.setParameterName(codeConfig.get("security.csrf.paramName"));
        repo.setSessionAttributeName(codeConfig.get("security.csrf.attribute"));
        return repo;
    }

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return new LoginUserDetailsService();
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        SimpleUrlAuthenticationSuccessHandler handler = new LoginSuccessHandler();
        handler.setUseReferer(true);   // true => 로그인후 원래 페이지로 돌아간다.
        return handler;
    }

    @Bean
    public AuthenticationFailureHandler loginFailureHandler() {
        LoginFailureHandler handler = new LoginFailureHandler();
        return handler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
//      return PasswordEncoderFactories.createDelegatingPasswordEncoder();  // http://java.ihoney.pe.kr/498
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public IGoogleAuthenticator googleAuthenticator() {
        return new GoogleAuthenticator();
    }
}
