package com.griffins.config.auth;

import com.griffins.common.util.StringUtil;
import com.griffins.config.common.ConfigUtil;
import com.warrenstrange.googleauth.IGoogleAuthenticator;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * import com.warrenstrange.googleauth.IGoogleAuthenticator;
 * import griffins.common.util.ConfigUtil;
 * import griffins.common.util.StringUtil;
 * import griffins.config.security.user.LoginUserDetails;
 * import lombok.Setter;
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.security.authentication.BadCredentialsException;
 * import org.springframework.security.authentication.InternalAuthenticationServiceException;
 * import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
 * import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
 * import org.springframework.security.core.AuthenticationException;
 * import org.springframework.security.core.userdetails.UserDetails;
 * import org.springframework.security.core.userdetails.UserDetailsService;
 * import org.springframework.security.core.userdetails.UsernameNotFoundException;
 * import org.springframework.security.crypto.password.PasswordEncoder;
 * <p>
 * import javax.annotation.Resource;
 * import java.util.Locale;
 * <p>
 * /**
 * 프로젝트명 : esmp
 * 파일명 : griffins.config.security.CustomUserDetailsAuthenticationProvider
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
public class CustomUserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    @Autowired
    private UserDetailsService userDetailsService;
    @Setter
    private IGoogleAuthenticator authenticator;
    private PasswordEncoder passwordEncoder;

    @Resource(name = "passwordEncoder")
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException("No Credentials");
        }

        String password = authentication.getCredentials().toString();
        checkPassword(userDetails, password);
    }

    private void checkPassword(UserDetails user, String pwd) throws BadCredentialsException {
        if (!passwordEncoder.matches(pwd, user.getPassword())) {
            throw new UsernameNotFoundException(ConfigUtil.MSG.getMessage("security.nouser.msg"));
        }
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        LoginUserDetails user;
        try {
            user = (LoginUserDetails) userDetailsService.loadUserByUsername(username);
            if (user == null)
                throw new InternalAuthenticationServiceException(ConfigUtil.MSG.getMessage("security.nouser.msg", Locale.KOREA));
            if (ConfigUtil.OPTION.get("otp.use").equals("1")) {
                if (StringUtil.isEmpty(user.getOtpKey()))
                    throw new BadCredentialsException(ConfigUtil.MSG.getMessage("security.nootp.msg"));
                Integer verifyCode = ((CustomWebAuthenticationDetails) authentication.getDetails()).getOtpCode();
                if (StringUtil.isNotEmpty(verifyCode)) {
                    if (!authenticator.authorize(user.getOtpKey(), verifyCode)) {
                        throw new BadCredentialsException(ConfigUtil.MSG.getMessage("invalid.otp.msg", Locale.KOREA));
                    }
                } else {
                    throw new BadCredentialsException("OTP code is mandatory");
                }
            }
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage(), e);
        }
        return user;
    }


}
