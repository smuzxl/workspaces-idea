package com.zxl.web.support;

import com.zxl.Config;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.ViewToolContext;
import org.springframework.web.servlet.view.velocity.VelocityToolboxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


public class VelocityToolbox2View extends VelocityToolboxView {
    @Override
    protected Context createVelocityContext(Map<String, Object> model, HttpServletRequest request,
                                            HttpServletResponse response) throws Exception {
        ViewToolContext ctx = new ViewToolContext(getVelocityEngine(), request, response, getServletContext());
        ctx.putAll(model);
        ctx.putAll(Config.getPageTools());
        return ctx;
    }
}