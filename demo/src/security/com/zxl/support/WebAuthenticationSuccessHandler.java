package com.zxl.support;

import com.zxl.model.User;
import com.zxl.util.BaseWebUtils;
import com.zxl.util.LoginUtils;
import com.zxl.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证成功处理
 *
 * @author zxl
 */
public class WebAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    @Qualifier("authenticationManager")
    protected ProviderManager authenticationManager;
    protected int defaultDuration = 60 * 60 * 12;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        processSuccess(request, response, authentication);
        String targetUrl = determineTargetUrl(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    /**
     * 认证成功后处理cookie、cache
     *
     * @param request
     * @param response
     * @param authentication
     */
    public void processSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication.isAuthenticated()) {
            //TO DO 保存认证信息
            String ip = BaseWebUtils.getRemoteIp(request);
            LoginUtils.loginMap.put(ip, authentication);
//			int duration = defaultDuration;
//
//			String sessid = saveAuthentication(ip, duration, false, authentication);
//
//			setLogonSessid(sessid, response, duration);
//			User user = (User) authentication.getPrincipal();
//			// LoginUtils.setLogonTrace(user.getId(), request, response);
//			successCallback(request, user, ip, sessid);
        }
    }

    public String saveAuthentication(String ip, int duration, boolean rememberMe, Authentication authentication) {

        // User user = (User) authentication.getPrincipal();
        // if (authentication.isAuthenticated()) {
        // String sessid = sessidGenerator.generateSessid(authentication);
        //
        // CachedAuthentication ca = new CachedAuthentication();
        // ca.setAuthentication(authentication);
        // ca.setIp(ip);
        // ca.setTimeout(System.currentTimeMillis() + duration * 1000);
        // String ukey = LoginUtils.getCacheUkey(sessid);
        // cacheService.set(CacheService.REGION_LOGINAUTH, ukey, ca, duration);
        //
        // String repeatKey = LoginUtils.getRepeatKey(user.getUsertype(),
        // user.getUsername());
        // if (isRelogin) {
        // Object sessidOld = cacheService.get(CacheService.REGION_LOGINAUTH,
        // repeatKey);
        // if (sessidOld != null && !sessid.equals(sessidOld)) {
        // cacheService.remove(CacheService.REGION_LOGINAUTH,
        // LoginUtils.getCacheUkey(sessidOld.toString()));
        // logger.error("重复用户登录，剔除用户" + repeatKey + " " + user.getUsername());
        // }
        // cacheService.set(CacheService.REGION_LOGINAUTH, repeatKey, sessid);
        // }
        // return sessid;
        // } else {
        // throw new IllegalStateException(user.getUsername() +
        // " not authenticated!");
        // }
        return "";
    }

    protected void successCallback(String userAgent, String port, User user, String ip, String sessid) {
        String nickname = user.getNickname();
        String brower = BaseWebUtils.getBrowerInfo(userAgent);
        Map<String, String> info = new HashMap<String, String>();
        info.put("nickname", nickname);
        info.put("sid", StringUtil.md5(sessid));
        info.put("brower", brower);
        if (StringUtils.isNotBlank(port)) {
            info.put("port", port);
        }
        logger.info("用户登录" + user.getId() + ",ip" + ip + " info:" + info);
    }

    protected void successCallback(HttpServletRequest request, User user, String ip, String sessid) {
        String userAgent = request.getHeader("user-agent");
        String port = BaseWebUtils.getRemotePort(request);
        successCallback(userAgent, port, user, ip, sessid);
    }

    protected void setLogonSessid(String sessid, HttpServletResponse response, int duration) {
        // 设置前台登录Cookie
        Cookie cookie = new Cookie(LoginUtils.SESS_COOKIE_NAME, sessid);
        cookie.setMaxAge(duration);
        cookie.setPath("/");
        cookie.setSecure(false);
        response.addCookie(cookie);
    }

}