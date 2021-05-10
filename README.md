If you want to run application you must have a dev account on Twitter.

1. Run Kafka (you can use docker)
2. Create topic kafka-topics.sh --zookeeper {zookeeper address}:2181 --create --topic twitter-tweets --partitions 6 --replication-factor 1
   or create topic in akhq
3. Set properties(values from Twitter -> Keys and tokens)




