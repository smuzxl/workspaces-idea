package com.zxl.service.impl;

import com.zxl.model.User;
import com.zxl.model.WebModule;
import com.zxl.service.AclService;
import com.zxl.service.DaoService;
import com.zxl.util.MD5Util;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("aclService")
public class AclServiceImpl implements AclService<WebModule> {
    protected final Log logger = LogFactory.getLog(getClass());
    @Autowired
    @Qualifier("daoService")
    private DaoService daoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TO DO 获取用户,并设置用户角色
        DetachedCriteria query = DetachedCriteria.forClass(User.class);
        query.add(Restrictions.eq("username", username));
        List<User> list = daoService.findByCriteria(query, 0, 1);
        User user = null;
        if (CollectionUtils.isNotEmpty(list)) {
            user = list.get(0);
        }
        if (user == null) {
            user = new User();
            user.setId(1l);
            user.setUsername("admin");
            user.setPassword(MD5Util.md5("111111"));
            user.setNickname("1111111");
        }
        return user;

    }

    @Override
    public List<WebModule> getSecurityModuleList() {
        logger.info("getSecurityModuleList----->");
        List<WebModule> list = new ArrayList<WebModule>();
        WebModule webModule1 = new WebModule();
        webModule1.setId(1l);
        webModule1.setMatchorder(1);
        webModule1.setModuleurl("/admin/index.xhtml");
        webModule1.setRolenames("ROLE_ADMIN");
        list.add(webModule1);
        WebModule webModule2 = new WebModule();
        webModule2.setId(2l);
        webModule2.setMatchorder(2);
        webModule2.setModuleurl("/admin/main.xhtml");
        webModule2.setRolenames("ROLE_USER");
        list.add(webModule2);
        WebModule webModule3 = new WebModule();
        webModule3.setId(3l);
        webModule3.setMatchorder(3);
        webModule3.setModuleurl("/");
        webModule3.setRolenames("ROLE_USER");
        list.add(webModule3);
        return list;
    }

    @Override
    public List<String> getRolenameList() {
        List<String> list = new ArrayList<String>();
        list.add("ROLE_ADMIN");
        list.add("ROLE_USER");
        return list;
    }

}
