package com.zxl.test;

import com.zxl.util.CacheConstant;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class ClientOneTest extends BaseTest {

    @Test
    public void test() throws InterruptedException {
        String key = "index1top1";
        String topStr = (String) cacheService.get(CacheConstant.REGION_TENMIN, key);

        System.out.println("---------ClientOneTest topStr----------" + topStr);
        if (StringUtils.isBlank(topStr)) {
            topStr = "test cache";
            cacheService.set(CacheConstant.REGION_TENMIN, key, topStr);
        }
        System.out.println("---------ClientOneTest begin----------");
        getUser(new Long(1));
        System.out.println("---------ClientOneTest wait 60s----------");
        Thread.currentThread().sleep(60 * 1000);
        hibernateTemplate.clear();
        getUser(new Long(1));
        System.out.println("---------ClientOneTest end----------");

    }

}
