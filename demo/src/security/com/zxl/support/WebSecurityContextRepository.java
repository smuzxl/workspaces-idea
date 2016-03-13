package com.zxl.support;

import com.zxl.service.LoginService;
import com.zxl.util.BaseWebUtils;
import com.zxl.util.LoginUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 获取SecurityContext
 *
 * @author zxl
 */
public class WebSecurityContextRepository implements SecurityContextRepository {
    protected final Log logger = LogFactory.getLog(getClass());
    @Autowired
    @Qualifier("loginService")
    private LoginService loginService;

    @Override
    public boolean containsContext(HttpServletRequest request) {
        String sessid = BaseWebUtils.getCookieValue(request, LoginUtils.SESS_COOKIE_NAME);
        if (StringUtils.isNotBlank(sessid)) {
            return loginService.isUserLogon(request, sessid);
        }
        return false;
    }

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        String ukey = BaseWebUtils.getCookieValue(request, LoginUtils.SESS_COOKIE_NAME);
        SecurityContext context = new SecurityContextImpl();
        String ip = BaseWebUtils.getRemoteIp(request);
        Authentication auth = loginService.loadAuthentication(ip, ukey);
        context.setAuthentication(auth);
        return context;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
    }
}