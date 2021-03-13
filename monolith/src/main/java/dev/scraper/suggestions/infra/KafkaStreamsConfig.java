package dev.scraper.suggestions.infra;

import dev.scraper.common.Link;
import dev.scraper.suggestions.domain.PopularLink;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Properties;

;

@Configuration
public class KafkaStreamsConfig {

    @Value("${app.server.config:http://localhost:8080}")
    private String appServerConfig;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${links.topic.name:links-topic}")
    private String linksTopicName;

    @Value("${state.store.name:popular-tags-store}")
    private String stateStoreName;

    @Bean
    @Primary
    public KafkaStreams kafkaStreams(KafkaProperties kafkaProperties) {
        final Properties props = new Properties();
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, appName);
        props.put(StreamsConfig.STATE_DIR_CONFIG, "data");
        props.put(StreamsConfig.APPLICATION_SERVER_CONFIG, appServerConfig);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        Topology topology = this.buildTopology(new StreamsBuilder());

        final KafkaStreams kafkaStreams = new KafkaStreams(topology, props);
        kafkaStreams.start();

        return kafkaStreams;
    }

    private Topology buildTopology(StreamsBuilder builder) {

        JsonSerializer<Link> linkJsonSerializer = new JsonSerializer<>();
        JsonDeserializer<Link> linkJsonDeserializer = new JsonDeserializer<>(Link.class);
        JsonSerializer<PopularLink> popularLinkJsonSerializer = new JsonSerializer<>();
        JsonDeserializer<PopularLink> popularLinkJsonDeserializer = new JsonDeserializer<>(PopularLink.class);
        StringSerializer stringSerializer = new StringSerializer();
        StringDeserializer stringDeserializer = new StringDeserializer();
        Serde<Link> linkSerde = Serdes.serdeFrom(linkJsonSerializer, linkJsonDeserializer);
        Serde<PopularLink> popularLinkSerde = Serdes.serdeFrom(popularLinkJsonSerializer, popularLinkJsonDeserializer);
        Serde<String> stringSerde = Serdes.serdeFrom(stringSerializer, stringDeserializer);

        KStream<String, Link> linkKStream = builder.stream(linksTopicName, Consumed.with(stringSerde, linkSerde));


        // Grouping Ratings
        KGroupedStream<String, Link> groupedStream = linkKStream
                .groupBy((key, value) -> value.getHash(), Grouped.with(stringSerde, linkSerde));

        groupedStream.aggregate(PopularLink::new,
                        (key, value, aggregate) ->
                    aggregate.update(value.getHash(), value.getTags()),
                        Materialized.<String, PopularLink, KeyValueStore<Bytes, byte[]>>as(stateStoreName)
                                .withKeySerde(stringSerde)
                                .withValueSerde(popularLinkSerde));

        // finish the topology
        return builder.build();
    }

}
