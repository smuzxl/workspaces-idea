package com.zxl.web.support;

import com.zxl.util.BaseWebUtils;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class PageExceptionResolver extends SimpleMappingExceptionResolver {

    protected Map<String, String> saveExceptionMessage(Exception ex, HttpServletRequest request) {
        String uri = request.getRequestURI();
        String remoteIp = BaseWebUtils.getRemoteIp(request);
        String title = uri + "@" + ", RemoteIp:" + remoteIp;
        String reqMap = BaseWebUtils.getParamStr(request, true);
        Map<String, String> result = new HashMap<String, String>();
        result.put("title", title);
        result.put("remoteIp", remoteIp);
        result.put("reqParams", "" + reqMap);
        result.put("reqHeader", BaseWebUtils.getHeaderStr(request));
        result.put("reqUri", uri);
        return result;
    }

    @Override
    protected void logException(Exception ex, HttpServletRequest request) {
        Map<String, String> result = saveExceptionMessage(ex, request);
        ex.printStackTrace();
        logger.error(result + "");
    }

}
