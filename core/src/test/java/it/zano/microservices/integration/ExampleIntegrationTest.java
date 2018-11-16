package it.zano.microservices.integration;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author a.zanotti
 * @since 16/11/2018
 */
@RunWith(SpringRunner.class) //Needed -> without, the environment is not booted
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //with this config, a real server is started
public class ExampleIntegrationTest {

    @Autowired
    private ApplicationContext ctx;

    @Test
    public void startApplication() {
        //Remember that tests may not have filtered resources...
        Assert.assertEquals("core-test",ctx.getEnvironment().getProperty("spring.application.name"));
    }
}
