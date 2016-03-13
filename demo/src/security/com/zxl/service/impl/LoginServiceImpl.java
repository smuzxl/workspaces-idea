package com.zxl.service.impl;

import com.zxl.service.LoginService;
import com.zxl.util.LoginUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service("loginService")
public class LoginServiceImpl implements LoginService {
    protected final Log logger = LogFactory.getLog(getClass());

    @Override
    public Authentication loadAuthentication(String ip, String sessid) {
        //TO DO 获取认证信息
        return LoginUtils.loginMap.get(ip);
    }

    @Override
    public boolean isUserLogon(HttpServletRequest request, String sessid) {
        return true;
    }

}
