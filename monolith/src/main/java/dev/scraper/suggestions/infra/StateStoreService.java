package dev.scraper.suggestions.infra;

import dev.scraper.suggestions.domain.PopularLink;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyQueryMetadata;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreType;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StateStoreService {

    private final KafkaStreams streams;

    @Value("${state.store.name}")
    private String stateStoreName;

    public StateStoreService(KafkaStreams streams) {
        this.streams = streams;
    }

    @Nullable
    public PopularLink getPopularLink(String pageHash) {
        try {

        final KeyQueryMetadata keyQueryMetadata = streams.queryMetadataForKey(stateStoreName, pageHash, Serdes.String().serializer());

        //use the above information to redirect the query to the host containing the partition for the key
        final int keyPartition = keyQueryMetadata.getPartition();

        //querying local key-value stores
        final QueryableStoreType<ReadOnlyKeyValueStore<String, PopularLink>> queryableStoreType = QueryableStoreTypes.keyValueStore();

        //fetch the store for specific partition where the key belongs and look into stale stores as well
        ReadOnlyKeyValueStore<String, PopularLink> store = streams
                .store(StoreQueryParameters.fromNameAndType(stateStoreName, queryableStoreType)
                        .enableStaleStores()
                        .withPartition(keyPartition));

        //get the value by key
        PopularLink result = store.get(pageHash);

        return result;

        } catch (Exception e) {
            log.debug("Popular tags exception", e);
            return null;
        }
    }
}
