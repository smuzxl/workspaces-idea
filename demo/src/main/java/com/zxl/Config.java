package com.zxl;

import org.apache.commons.lang.time.DateUtils;
import org.apache.velocity.tools.generic.MathTool;
import org.springframework.beans.factory.InitializingBean;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Config implements InitializingBean {

    public static final String SYSTEMID = "demo";
    private static Map<String, Object> pageTools;
    private Map<String, Object> pageMap = new HashMap<String, Object>();
    private boolean initedPage = false;

    public static Map<String, Object> getPageTools() {
        return pageTools;
    }

    public void setPageMap(Map<String, Object> pageMap) {
        if (!initedPage) {
            this.pageMap = pageMap;
            this.initedPage = true;
        } else {
            throw new IllegalStateException("不能再次调用");
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initPageTools();
    }

    public void initPageTools() {
        Map<String, Object> tmp = new HashMap<String, Object>();
        tmp.put("math", new MathTool());
        tmp.put("DateUtil", new DateUtils());
        tmp.putAll(pageMap);
        pageTools = Collections.unmodifiableMap(tmp);
    }

}
