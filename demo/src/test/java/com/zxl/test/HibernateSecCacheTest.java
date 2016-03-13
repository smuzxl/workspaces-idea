package com.zxl.test;

import org.junit.Test;

public class HibernateSecCacheTest extends BaseTest {

    @Test
    public void test1() {
        System.out.println("------test1-------");
        getUser(new Long(1));
        getUser(new Long(2));
        getUser(new Long(3));
        getUser(new Long(4));
        System.out.println("------test1-----一级缓存命中--");
        getUser(new Long(1));
        getUser(new Long(2));
        getUser(new Long(3));
        getUser(new Long(4));
    }

    @Test
    public void test2() {
        System.out.println("------test2---二级缓存命中----");
        getUser(new Long(1));
        getUser(new Long(2));
    }

}
