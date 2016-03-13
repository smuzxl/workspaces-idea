package com.zxl.test;

import com.zxl.model.User;
import org.junit.Test;

import java.util.Random;

public class ClientThreeTest extends BaseTest {

    @Test
    public void test() throws InterruptedException {
        System.out.println("---------ClientThreeTest begin----------");
        Random random = new Random();
        User user = hibernateTemplate.get(User.class, new Long(1));
        printUser(user);
        user.setUsername("name" + random.nextInt(100));
        hibernateTemplate.update(user);
        hibernateTemplate.flush();
        printUser(user);
//		getUser(new Long(1));
        System.out.println("---------ClientThreeTest end----------");
    }
}
