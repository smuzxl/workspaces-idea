package com.zxl.untrans.impl;

import com.zxl.untrans.CacheConfigure;
import com.zxl.util.JsonUtils;

import java.util.HashMap;
import java.util.Map;

public class CacheConfigureImpl implements CacheConfigure {

    @Override
    public Map<String, String> getRegionVersion() {
        String regionVersion = "{\"oneMin\":\"1v1\",\"tenMin\":\"10v1\",\"halfHour\":\"30v1\",\"oneHour\":\"60v1\",\"twoHour\":\"2hv1\",\"halfDay\":\"12hv1\",\"loginKey\":\"lkv1\",\"loginAuth\":\"lav1\"}";
        return JsonUtils.readJsonToMap(regionVersion);
    }

    @Override
    public Map<String, String> getServiceCachePre() {
        return new HashMap<String, String>();
    }

}
