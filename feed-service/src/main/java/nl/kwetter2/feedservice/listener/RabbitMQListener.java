package nl.kwetter2.feedservice.listener;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class RabbitMQListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQListener.class);


    @RabbitListener(queues = "tweets_queue")
    public void receiveNewPostMessage(String data) {
        try {
            LOGGER.info("New Post Data Received: {}", data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
