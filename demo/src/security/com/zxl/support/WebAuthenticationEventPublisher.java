package com.zxl.support;

import com.zxl.model.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * 记录认证事件
 *
 * @author zxl
 */
public class WebAuthenticationEventPublisher implements AuthenticationEventPublisher {
    protected final Log logger = LogFactory.getLog(getClass());

    @Override
    public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
        logger.warn("用户登录失败：" + authentication.getName() + "失败原因:" + exception.getMessage());
    }

    @Override
    public void publishAuthenticationSuccess(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        logger.warn("用户登录成功：" + user.getId() + ":" + user.getNickname());
    }
}