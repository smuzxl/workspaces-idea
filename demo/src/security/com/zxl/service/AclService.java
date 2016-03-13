package com.zxl.service;

import com.zxl.model.SecurityModule;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AclService<T extends SecurityModule> extends UserDetailsService {
    List<T> getSecurityModuleList();

    List<String> getRolenameList();
}
