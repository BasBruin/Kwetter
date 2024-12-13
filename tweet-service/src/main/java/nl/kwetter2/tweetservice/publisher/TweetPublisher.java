    package nl.kwetter2.tweetservice.publisher;


    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.amqp.rabbit.core.RabbitTemplate;
    import org.springframework.stereotype.Service;

    /*
    Published tweet naar RabbitMQ
     */
    @Service
    public class TweetPublisher {

        @org.springframework.beans.factory.annotation.Value("${rabbitmq.log.exchange.name}")
        private String exchange;

        @org.springframework.beans.factory.annotation.Value("${rabbitmq.log.routing.key}")
        private String routingKey;

        private static final Logger LOGGER = LoggerFactory.getLogger(TweetPublisher.class);

        private final RabbitTemplate rabbitTemplate;

        public TweetPublisher(RabbitTemplate rabbitTemplate) {
            this.rabbitTemplate = rabbitTemplate;
        }

        public void publishTweet(String TweetMessage) {
            LOGGER.info("TweetMessage published to RabbitMQ " + TweetMessage);
            rabbitTemplate.convertAndSend(exchange, routingKey, TweetMessage);
        }

    }

