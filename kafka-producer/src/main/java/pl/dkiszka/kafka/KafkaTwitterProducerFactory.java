package pl.dkiszka.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;

/**
 * @author Dominik Kiszka {dominikk19}
 * @project kafka-twitter-elasticsearch
 * @date 10.05.2021
 */
public class KafkaTwitterProducerFactory {

    public static KafkaProducer<String, String> createKafkaTwitterProducer() {
        return new KafkaTwitterProducer().createKafkaProducer();
    }

    private KafkaTwitterProducerFactory() {
    }
}
