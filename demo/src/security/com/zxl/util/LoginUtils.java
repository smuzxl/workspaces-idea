package com.zxl.util;

import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.Map;

public class LoginUtils {
    public static final String SESS_COOKIE_NAME = "custom_uskey_";

    public static final String ERROR_PASSORNAME = "passOrName";    //用户名或密码错误！
    public static final String ERROR_USERNAME = "username";        //用户名不存在
    public static final String ERROR_PASSWORD = "password";        //密码错误
    public static final String ERROR_CAPTCHA = "captcha";            //验证码错误
    public static final String ERROR_REJECTED = "rejected";        //用户名被禁用

    public static Map<String, Authentication> loginMap = new HashMap<String, Authentication>();

}
