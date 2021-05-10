package pl.dkiszka;

import io.vavr.control.Try;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.dkiszka.kafka.KafkaTwitterProducerFactory;
import pl.dkiszka.twitter.TwitterProducerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Dominik Kiszka {dominikk19}
 * @project kafka-twitter-elasticsearch
 * @date 10.05.2021
 */
public class Main {
    private Logger logger = LoggerFactory.getLogger("Main");
    private KafkaProducer<String, String> kafkaTwitterProducer;

    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue(1000);
        var client = TwitterProducerFactory.createHbcClient(msgQueue);
        client.connect();

        kafkaTwitterProducer = KafkaTwitterProducerFactory.createKafkaTwitterProducer();

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            logger.info("stopping application");
            client.stop();
            logger.info("Twitter client stopped");
            kafkaTwitterProducer.close();
            logger.info("Kafka producer stopped");
        }));

        while (!client.isDone()) {
            Try.of(() -> msgQueue.poll(5, TimeUnit.SECONDS))
                    .onFailure(ex -> {
                        ex.printStackTrace();
                        client.stop();
                    })
                    .toOption()
                    .forEach(msg -> kafkaTwitterProducer.send(new ProducerRecord<>("twitter-tweets", null, msg), this::loggingException));
        }

    }

    private void loggingException(RecordMetadata recordMetadata, Exception exception) {
        if (exception != null) {
            logger.error("Something bad happened", exception);
        }
    }
}
