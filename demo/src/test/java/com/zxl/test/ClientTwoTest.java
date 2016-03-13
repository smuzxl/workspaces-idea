package com.zxl.test;

import net.spy.memcached.MemcachedClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ClientTwoTest extends BaseTest {
    @Autowired
    private MemcachedClient memcachedClient;
    @Test
    public void test() throws InterruptedException {
        /*System.out.println("---------ClientTwoTest begin----------");
        getUser(new Long(1));
        System.out.println("---------ClientTwoTest wait 10s----------");
        Thread.currentThread().sleep(1 * 1000);
        getUser(new Long(1));
        System.out.println("---------ClientTwoTest end----------");
        String key = "index1top1";
        String topStr = (String) cacheService.get(CacheConstant.REGION_TENMIN, key);

        System.out.println("---------ClientTwoTest topStr----------" + topStr);*/

        memcachedClient.add("test",1000,"test value");
        Thread.currentThread().sleep(2 * 1000);

        System.out.println(memcachedClient.get("test"));
    }

}
