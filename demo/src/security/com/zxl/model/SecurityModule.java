package com.zxl.model;

public interface SecurityModule {
    String getModuleurl();

    Long getId();

    String getMenucode();

    String getMenutitle();

    String getTarget();

    String getDisplay();

    boolean isTop();

    String getParentcode();

    String getTag();

    String getTopMenucode();

    String getRolenames();
}
