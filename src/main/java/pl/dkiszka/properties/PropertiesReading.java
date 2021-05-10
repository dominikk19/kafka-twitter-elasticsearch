package pl.dkiszka.properties;

import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author Dominik Kiszka {dominikk19}
 * @project kafka-twitter-elasticsearch
 * @date 09.05.2021
 */

public class PropertiesReading {

    Logger log = LoggerFactory.getLogger("PropertiesReading");

    public Properties read() {
        var prop = new Properties();
        var inputStream = getClass().getClassLoader().getResourceAsStream("application.properties");

        Try.run(() -> prop.load(inputStream))
                .onSuccess(ignore -> log.info("Read properties"))
                .onFailure(exc -> log.error("Cannot read properties", exc))
                .getOrElseThrow(() -> new NullPointerException("Cannot find properties file"));

        return prop;
    }
}
