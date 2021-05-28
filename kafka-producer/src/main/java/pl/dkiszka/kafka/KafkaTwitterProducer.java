package pl.dkiszka.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import pl.dkiszka.properties.PropertiesReading;

import java.util.Properties;

/**
 * @author Dominik Kiszka {dominikk19}
 * @project kafka-twitter-elasticsearch
 * @date 10.05.2021
 */
class KafkaTwitterProducer {

    private String bootstrap;

    KafkaTwitterProducer() {
        var properties = new PropertiesReading().read();
        this.bootstrap = properties.getProperty("kafka.bootstrap");
    }

    KafkaProducer<String, String> createKafkaProducer() {
        return new KafkaProducer<>(createProperties());
    }

    private Properties createProperties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        //safe producer
        properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        properties.setProperty(ProducerConfig.RETRIES_CONFIG, Integer.toString(Integer.MAX_VALUE));
        properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");

        //high throughput producer
        //compression
        //  gzip, lz4, snappy
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "20");
        properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, Integer.toString(32*1024)); //default 16KB
        properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");




        return properties;
    }


}
