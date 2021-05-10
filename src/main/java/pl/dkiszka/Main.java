package pl.dkiszka;

import io.vavr.control.Try;
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
    public static void main(String[] args) {
        new Main().run();
    }

    public void run() {
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue(1000);
        var client = TwitterProducerFactory.createHbcClient(msgQueue);
        client.connect();

        while (!client.isDone()) {
            Try.of(() -> msgQueue.poll(5, TimeUnit.SECONDS))
                    .onFailure(ex -> {
                        ex.printStackTrace();
                        client.stop();
                    })
                    .toOption()
                    .stdout();
        }
    }
}
