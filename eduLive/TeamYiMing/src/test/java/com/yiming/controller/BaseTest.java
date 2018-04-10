package com.yiming.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/resources/testConf/spring-dao.xml","file:src/main/resources/testConf/spring-service.xml"})
@WebAppConfiguration
public class BaseTest {
    @Test
    public void test() {
    }
}
