package it.zano.microservices.unit.aspect;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import it.zano.microservices.aspect.LogAspect;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

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

        TestFilteredAppender ap = new TestFilteredAppender(Level.INFO);
        ap.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        ap.start();
        Logger logger = (Logger) LoggerFactory.getLogger("it.zano.microservices.aspect.LogAspect");
        logger.addAppender(ap);
        try {
            proxy.exampleMethod("Cat");
        } finally {
            logger.detachAndStopAllAppenders();
        }
        List<ILoggingEvent> loggingEvents = ap.getLoggingEvents();
        Assert.assertEquals(2, loggingEvents.size());

    }


    @RestController
    private static class StubRestController {

        public String exampleMethod(String input) {
            return input + " now it's output! ¯\\_(ツ)_/¯ ";
        }

    }


    private class TestFilteredAppender extends AppenderBase<ILoggingEvent> {

        private ConcurrentLinkedDeque<ILoggingEvent> loggingEvents;
        private Level level;

        TestFilteredAppender(Level level) {
            this.level = level;
            this.loggingEvents = new ConcurrentLinkedDeque<>();
        }

        @Override
        protected void append(ILoggingEvent eventObject) {
            if (eventObject.getLevel().equals(level)) {
                loggingEvents.add(eventObject);
            }
        }

        List<ILoggingEvent> getLoggingEvents() {
            return new ArrayList<>(loggingEvents);
        }
    }
}
