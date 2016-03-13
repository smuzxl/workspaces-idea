package com.zxl.untrans.impl;

import com.zxl.support.CachePair;
import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemcachedCacheServiceImpl extends AbstractCacheService {
    @Autowired
    @Qualifier("memcachedClient")
    private MemcachedClient memcachedClient;

    @Override
    public void set(String regionName, String key, Object value) {
        Integer expSeconds = getRegionTime(regionName);
        set(regionName, key, value, expSeconds);
    }

    @Override
    public void set(String regionName, String key, Object value, int expSeconds) {
        if (StringUtils.isBlank(key) || value == null)
            return;
        key = getRealKey(regionName, key);
        try {
            memcachedClient.set(key, expSeconds, value);
        } catch (Exception e) {
            logger.warn(regionName + ":" + key, e);
        }
    }

    @Override
    public Object get(String regionName, String key) {
        getRegionTime(regionName);
        if (StringUtils.isBlank(key))
            return null;
        key = getRealKey(regionName, key);
        try {
            return memcachedClient.get(key);
        } catch (Exception e) {
            logger.warn(regionName + ":" + key, e);
        }
        return null;
    }

    @Override
    public Map<String, Object> getBulk(String regionName, Collection<String> keys) {
        getRegionTime(regionName);
        if (keys == null || keys.isEmpty())
            return null;
        Map<String, String> keyMap = new HashMap<String, String>();
        for (String key : keys) {
            String newkey = getRealKey(regionName, key);
            keyMap.put(newkey, key);
        }
        try {
            Map<String, Object> result = memcachedClient.getBulk(keyMap.keySet());
            Map<String, Object> returnMap = new HashMap<String, Object>();
            for (String newkey : result.keySet()) {// ����ʹ���ϵ�key
                returnMap.put(keyMap.get(newkey), result.get(newkey));
            }
            return returnMap;
        } catch (Exception e) {
            logger.warn(regionName + ":" + keys, e);
        }
        return null;
    }

    @Override
    public void remove(String regionName, String key) {
        if (StringUtils.isNotBlank(key)) {
            key = getRealKey(regionName, key);
            memcachedClient.delete(key).getStatus().isSuccess();
        }
    }

    @Override
    public int incrementAndGet(String regionName, String key, int by, int def) {
        Integer expSeconds = getRegionTime(regionName);
        return incrementAndGet(regionName, key, by, def, expSeconds);
        /*
		 * if(StringUtils.isNotBlank(key)){ try{ key = getRealKey(regionName,
		 * key); return (int) this.memcachedClient.incr(key, by, def);
		 * }catch(Exception e){ logger.warn("", e); } } return -1;
		 */
    }

    @Override
    public int incrementAndGet(String regionName, String key, int by, int def, int exp) {
        if (StringUtils.isNotBlank(key)) {
            try {
                key = getRealKey(regionName, key);
                return (int) this.memcachedClient.incr(key, by, def, exp);
            } catch (Exception e) {
                logger.warn("", e);
            }
        }
        return -1;
    }

    @Override
    public int decrAndGet(String regionName, String key, int by, int def) {
        if (StringUtils.isNotBlank(key)) {
            key = getRealKey(regionName, key);
            return (int) this.memcachedClient.decr(key, by, def);
        }
        return -1;
    }

    @Override
    public CachePair getCachePair(String regionName, String key) {
        if (StringUtils.isBlank(key))
            return null;
        try {
            key = getRealKey(regionName, key);
            CASValue<Object> casV = memcachedClient.gets(key);
            if (casV != null) {
                CachePair pair = new CachePair();
                pair.setValue(casV.getValue());
                pair.setVersion(casV.getCas());
                return pair;
            }
        } catch (Exception e) {
            logger.warn("", e);
        }
        return null;
    }

    @Override
    public boolean setCachePair(String regionName, String key, long version, Object value, int expSeconds) {
        if (StringUtils.isBlank(key) || value == null)
            return false;
        key = getRealKey(regionName, key);
        try {
            CASResponse ss = memcachedClient.cas(key, version, expSeconds, value);
            return ss == CASResponse.OK;
        } catch (Exception e) {
            logger.warn("", e);
            return false;
        }
    }

    @Override
    public void add(String regionName, String key, Object value, int expSeconds) {
        if (StringUtils.isBlank(key) || value == null)
            return;
        key = getRealKey(regionName, key);
        try {
            memcachedClient.add(key, expSeconds, value);
        } catch (Exception e) {
            logger.warn("", e);
        }
    }

    @Override
    public boolean isLocal() {
        return false;
    }
}
