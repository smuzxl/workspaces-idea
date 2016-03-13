package com.zxl.test;

import com.zxl.model.User;
import com.zxl.service.DaoService;
import com.zxl.untrans.CacheService;
import org.hibernate.stat.Statistics;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/appContext-hibernate.xml", "classpath:spring/appContext-service-base.xml",
        "classpath:spring/appContext-config.xml", "classpath:spring/appContent-module.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public class BaseTest {
    @Autowired
    protected DaoService daoService;
    @Autowired
    protected HibernateTemplate hibernateTemplate;
    @Autowired
    @Qualifier("cacheService")
    protected CacheService cacheService;

    @After
    public void printStatistics() {
        Statistics statistics = hibernateTemplate.getSessionFactory().getStatistics();
        System.out.println(statistics);
        System.out.println("放入" + statistics.getSecondLevelCachePutCount());
        System.out.println("命中" + statistics.getSecondLevelCacheHitCount());
        System.out.println("错过" + statistics.getSecondLevelCacheMissCount());
    }

    protected void getUser(Long id) {
        User user = hibernateTemplate.get(User.class, id);
        printUser(user);
    }

    protected void printUser(User user) {
        System.out.println(user.getId() + "---->" + user.getUsername());
    }
}
