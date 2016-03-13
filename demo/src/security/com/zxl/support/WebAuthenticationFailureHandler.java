package com.zxl.support;

import com.zxl.util.BaseWebUtils;
import com.zxl.util.LoginUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证失败处理
 *
 * @author zxl
 */
public class WebAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private String defaultFailureUrl;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String failure = defaultFailureUrl;
        if (failure == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed: " + exception.getMessage());
        } else {
            String errortype = LoginUtils.ERROR_PASSORNAME;
            if (exception instanceof InvalidCookieException) {
                errortype = LoginUtils.ERROR_CAPTCHA;
            } else if (exception instanceof DisabledException) {
                errortype = LoginUtils.ERROR_REJECTED;
            } else if (exception instanceof BadCredentialsException) {
                errortype = LoginUtils.ERROR_PASSWORD;
            } else if (exception instanceof AuthenticationServiceException) {
                errortype = LoginUtils.ERROR_USERNAME;
            }
            if (StringUtils.isNotBlank(errortype)) {
                failure += "?errortype=" + URLEncoder.encode(errortype, "utf-8");
            }
            // 记录日志
            Map<String, String> result = new HashMap<String, String>();
            String ip = BaseWebUtils.getRemoteIp(request);
            String userAgent = request.getHeader("user-agent");
            String username = request.getParameter("j_username");
            result.put("userHost", ip);
            result.put("userAgent", userAgent);
            result.put("membername", username);
            String port = BaseWebUtils.getRemotePort(request);
            if (StringUtils.isNotBlank(port)) {
                result.put("port", port);
            }
            result.put("errortype", errortype);
            logger.info("login fail..." + result);
            getRedirectStrategy().sendRedirect(request, response, failure);
        }
    }

    public void setDefaultFailureUrl(String defaultFailureUrl) {
        Assert.isTrue(UrlUtils.isValidRedirectUrl(defaultFailureUrl), "'" + defaultFailureUrl
                + "' is not a valid redirect URL");
        this.defaultFailureUrl = defaultFailureUrl;
    }
}
