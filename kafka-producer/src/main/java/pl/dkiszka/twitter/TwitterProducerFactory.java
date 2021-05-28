package pl.dkiszka.twitter;

import com.twitter.hbc.core.Client;

import java.util.concurrent.BlockingQueue;

/**
 * @author Dominik Kiszka {dominikk19}
 * @project kafka-twitter-elasticsearch
 * @date 10.05.2021
 */
public class TwitterProducerFactory {

    public static Client createHbcClient(BlockingQueue<String> msgQueue) {
        return new TwitterProducer().createTwitterClient(msgQueue);
    }

    private TwitterProducerFactory() {
    }
}
