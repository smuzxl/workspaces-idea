package com.zxl.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {

    Authentication loadAuthentication(String ip, String sessid);

    boolean isUserLogon(HttpServletRequest request, String sessid);
}
