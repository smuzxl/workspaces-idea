package com.zxl.support;

import com.zxl.model.User;
import com.zxl.util.BaseWebUtils;
import com.zxl.util.LoginUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登出处理
 *
 * @author zxl
 */
public class WebUrlLogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler implements
        LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        doLogout(request, response, authentication);
    }

    private void doLogout(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String ip = BaseWebUtils.getRemoteIp(request);
        User user = (User) authentication.getPrincipal();
        addMemberLogoutLog(user.getId(), user.getNickname(), ip);
        //TO DO 移除已保存的认证信息
        LoginUtils.loginMap.remove(ip);
        super.handle(request, response, authentication);
    }

    public void addMemberLogoutLog(Long memberid, String nickname, String ip) {
        logger.info("nickname: " + nickname + " logout,ip: " + ip);
    }

}
