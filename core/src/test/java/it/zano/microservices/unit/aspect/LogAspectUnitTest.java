package it.zano.microservices.unit.aspect;

import it.zano.microservices.aspect.LogAspect;
import org.junit.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author a.zanotti
 * @since 21/12/2018
 */
public class LogAspectUnitTest {

    @Test
    public void testAspectLogging() {

        StubRestController target = new StubRestController();

        AspectJProxyFactory factory = new AspectJProxyFactory(target);
        LogAspect aspect = new LogAspect();
        factory.addAspect(aspect);
        StubRestController proxy = factory.getProxy();

        proxy.exampleMethod("Cat");

    }


    @RestController
    private static class StubRestController{

        public String exampleMethod(String input) {
            return input + " now it's output! ¯\\_(ツ)_/¯ ";
        }

    }
}
