package com.zxl.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginAction {

    @RequestMapping(value = "/login.xhtml")
    public String login(String errortype, ModelMap model) {
        model.put("errortype", errortype);
        return "login.vm";
    }

    @RequestMapping("/admin/index.xhtml")
    public String indexPage(ModelMap model) {
        return "index.vm";
    }

    @RequestMapping("/admin/main.xhtml")
    public String main(ModelMap model) {
        return "main.vm";
    }

}
