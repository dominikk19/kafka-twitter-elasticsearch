package pl.dkiszka.twitter;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import pl.dkiszka.properties.PropertiesReading;

import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * @author Dominik Kiszka {dominikk19}
 * @project kafka-twitter-elasticsearch
 * @date 09.05.2021
 */
class TwitterProducer {

    private final String consumerKey;
    private final String consumerSecret;
    private final String token;
    private final String secret;

    public TwitterProducer() {
        var properties =  new PropertiesReading().read();
        this.consumerKey = properties.getProperty("twitter.consumerKey");
        this.consumerSecret = properties.getProperty("twitter.consumerSecret");
        this.token = properties.getProperty("twitter.token");
        this.secret = properties.getProperty("twitter.secret");
    }

    /**
     * Configuration from https://github.com/twitter/hbc
     */
    Client createTwitterClient(BlockingQueue<String> msgQueue) {

        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();

        List<String> terms = Lists.newArrayList("COVID", "sport");
        hosebirdEndpoint.trackTerms(terms);

        Authentication hosebirdAuth = new OAuth1(consumerKey, consumerSecret, token, secret);

        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01")
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue));

        return builder.build();
    }
}
