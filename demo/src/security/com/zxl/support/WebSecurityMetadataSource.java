package com.zxl.support;

import com.zxl.model.SecurityModule;
import com.zxl.service.AclService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * [核心处理逻辑]
 * <p/>
 * 资源源数据定义，即定义某一资源可以被哪些角色,访问 建立资源与权限的对应关系
 *
 * @author zxl
 */
public class WebSecurityMetadataSource implements FilterInvocationSecurityMetadataSource, InitializingBean {

    private Map<RequestMatcher, Collection<ConfigAttribute>> resourceMap = new HashMap<RequestMatcher, Collection<ConfigAttribute>>();
    private List<String> rolenameList = new ArrayList<String>();
    private AclService<SecurityModule> aclService;

    public AclService<SecurityModule> getAclService() {
        return aclService;
    }

    public void setAclService(AclService<SecurityModule> aclService) {
        this.aclService = aclService;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(aclService, "必须提供aclService实现！");
        initResource();
    }

    /**
     * 初始化资源配置
     */
    public void initResource() {
        List<SecurityModule> moduleList = aclService.getSecurityModuleList();
        Map<RequestMatcher, Collection<ConfigAttribute>> tmp = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
        for (SecurityModule module : moduleList) {
            String url = module.getModuleurl();
            if (StringUtils.isNotBlank(url)) {
                int idx = module.getModuleurl().indexOf('?');
                if (idx > 0) {
                    url = url.substring(0, idx);
                }
                if (StringUtils.isBlank(module.getRolenames())) {
                    tmp.put(new AntPathRequestMatcher(url), new ArrayList<ConfigAttribute>(0));
                } else {
                    tmp.put(new AntPathRequestMatcher(url),
                            SecurityConfig.createList(StringUtils.split(module.getRolenames(), ",")));
                }
            }
        }
        this.resourceMap = tmp;
        this.rolenameList = aclService.getRolenameList();
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) {
        HttpServletRequest request = ((FilterInvocation) object).getRequest();
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : resourceMap.entrySet()) {
            if (entry.getKey().matches(request)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return SecurityConfig.createList(rolenameList.toArray(new String[rolenameList.size()]));
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

}
