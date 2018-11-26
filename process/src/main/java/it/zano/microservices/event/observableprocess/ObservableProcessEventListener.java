package it.zano.microservices.event.observableprocess;

import it.zano.microservices.event.EventConfiguration;
import it.zano.microservices.observableprocess.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * @author a.zanotti
 * @since 20/11/2018
 */
@Service
public class ObservableProcessEventListener{

    private final static Logger logger = LoggerFactory.getLogger(ObservableProcessEventListener.class);

    private final OprocManagerImpl observableProcessManager;
    private final OprocPropertiesImpl properties;

    @Autowired
    public ObservableProcessEventListener(OprocManagerImpl observableProcessManager, OprocPropertiesImpl properties) {
        this.observableProcessManager = observableProcessManager;
        this.properties = properties;
    }

    @RabbitListener(queues = EventConfiguration.OPROC_QUEUE)
    public void handleRabbitMessage(@Payload OprocTransitionMessage message) {
        logger.info("Received message! {}", message);
        try {
            //Easy case, there is only 1 exit case from each state.
            //If not, here the logic to handle eventual routing of the graph
            OprocStateEnum landingState = properties.getTransitions().get(message.getTransition()).getTo();
            //When the transition is finished, I schedule the following one
            if(properties.getStates().get(landingState).getTransitions().isEmpty()) {
                observableProcessManager.clearResources(message.getProcessId());
                logger.info("No transition out from state {}, exiting and removing resources", landingState);
            } else {
                OprocTransitionEnum oprocTransitionEnum = properties.getStates().get(landingState).getTransitions().get(0);
                observableProcessManager.executeEvent(oprocTransitionEnum, message.getProcessId());
            }
        } catch (Exception e) {
            logger.error("Error in manager, but concluding gracefully so that message is removed");
        }
    }
}
