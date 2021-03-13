package dev.scraper.common;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "hash")
@Document(collection = "links")
public class Link implements Serializable {

    @MongoId
    private String id;

    private LocalDateTime timeCreated;

    private String userId;

    private String url;

    private String hash;

    private String title;

    private Collection<String> tags = new HashSet<>();

    public Link(String userId, String url, String title, Collection<String> tags) {
        this.id = UUID.randomUUID().toString();
        this.timeCreated = LocalDateTime.now();
        this.userId = userId;
        this.url = url;
        this.hash = new Hash(url).getValue();
        this.title = title;
        this.tags.addAll(tags);
    }
}
