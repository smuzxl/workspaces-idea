package com.zxl.untrans;

public interface CacheTools {
    Object get(String regionName, String key);

    void set(String regionName, String key, Object value);

    void remove(String regionName, String key);

    boolean isLocal();
}
