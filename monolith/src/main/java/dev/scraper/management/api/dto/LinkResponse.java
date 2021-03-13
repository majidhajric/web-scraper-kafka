package dev.scraper.management.api.dto;

import dev.scraper.common.Link;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

@Data
public class LinkResponse implements Serializable {

    private String id;

    private LocalDateTime timeCreated;

    private String url;

    private String title;

    private final Collection<String> tags = new HashSet<>();

    public static LinkResponse toResponse(Link link) {
        LinkResponse response = new LinkResponse();
        response.id = link.getId();
        response.timeCreated = link.getTimeCreated();
        response.url = link.getUrl();
        response.title = link.getTitle();
        response.tags.addAll(link.getTags());
        return response;
    }
}
