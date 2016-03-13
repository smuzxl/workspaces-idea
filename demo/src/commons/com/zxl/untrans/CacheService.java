package com.zxl.untrans;

import com.zxl.support.CachePair;

import java.util.Collection;
import java.util.Map;

public interface CacheService extends CacheTools {
    String REGION_TENMIN = "tenMin";
    String REGION_ONEHOUR = "oneHour";
    String REGION_LOGINAUTH = "loginAuth"; // 2hour
    String REGION_SERVICE = "service"; // 12 hour
    String REGION_FIVEDAY = "fiveDay"; // 5day

    Map<String, Object> getBulk(String regionName, Collection<String> keys);

    void set(String regionName, String key, Object value, int timeoutSecond);

    void updateValue(String regionName, String key, String newvalue);

    void refreshVersion();

    Integer getCacheTime(String region);

    int incr(String regionName, String key, int by, int defvalue);

    int incrementAndGet(String regionName, String key, int by, int def);

    CachePair getCachePair(String regionName, String key);

    boolean setCachePair(String regionName, String key, long version, Object value, int expSeconds);

    void add(String regionName, String key, Object value, int expSeconds);

    int decrAndGet(String regionName, String key, int by, int def);

    int incrementAndGet(String regionName, String key, int by, int def, int exp);
}
