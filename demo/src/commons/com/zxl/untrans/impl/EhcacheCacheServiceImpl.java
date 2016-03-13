package com.zxl.untrans.impl;

import com.zxl.support.CachePair;
import org.hibernate.cache.QueryResultsRegion;
import org.hibernate.cache.RegionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class EhcacheCacheServiceImpl extends AbstractCacheService implements InitializingBean {
    private RegionFactory regionFactory;

    public EhcacheCacheServiceImpl() {

    }

    public void setRegionFactory(RegionFactory regionFactory) {
        this.regionFactory = regionFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        Assert.notNull(regionFactory, "regionFactory can not be null!");
    }

    @Override
    public Map<String, Object> getBulk(String regionName, Collection<String> keys) {
        QueryResultsRegion region = regionFactory.buildQueryResultsRegion(regionName, new Properties());
        Map<String, Object> result = new HashMap<String, Object>();
        for (String key : keys) {
            String realkey = getRealKey(regionName, key);
            Object value = region.get(realkey);
            if (value != null)
                result.put(realkey, value);
        }
        return result;
    }

    @Override
    public Object get(String regionName, String key) {
        QueryResultsRegion region = regionFactory.buildQueryResultsRegion(regionName, new Properties());
        String realkey = getRealKey(regionName, key);
        return region.get(realkey);
    }

    @Override
    public void set(String regionName, String key, Object value) {
        QueryResultsRegion region = regionFactory.buildQueryResultsRegion(regionName, new Properties());
        String realkey = getRealKey(regionName, key);
        region.put(realkey, value);
    }

    @Override
    public void set(String regionName, String key, Object value, int timeout) {
        set(regionName, key, value);
    }

    @Override
    public void remove(String regionName, String key) {
        QueryResultsRegion region = regionFactory.buildQueryResultsRegion(regionName, new Properties());
        String realkey = getRealKey(regionName, key);
        region.evict(realkey);
    }

    @Override
    public int incrementAndGet(String regionName, String key, int by, int def) {
        throw new UnsupportedOperationException("not support");
    }

    @Override
    public CachePair getCachePair(String regionName, String key) {
        throw new IllegalArgumentException("not support");
    }

    @Override
    public boolean setCachePair(String regionName, String key, long version, Object value, int expSeconds) {
        throw new IllegalArgumentException("not support");
    }

    @Override
    public void add(String regionName, String key, Object value, int expSeconds) {
        throw new IllegalArgumentException("not support");
    }

    @Override
    public int decrAndGet(String regionName, String key, int by, int def) {
        throw new UnsupportedOperationException("not support");
    }

    @Override
    public int incrementAndGet(String regionName, String key, int by, int def, int exp) {
        throw new UnsupportedOperationException("not support");
    }

    @Override
    public boolean isLocal() {
        return false;
    }

}
