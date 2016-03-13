package com.zxl.untrans;

import java.util.Map;

public interface CacheConfigure {
    Map<String, String> getRegionVersion();

    Map<String, String> getServiceCachePre();
}
