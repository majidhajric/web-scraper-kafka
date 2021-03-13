package dev.scraper.suggestions.api.dto;

import dev.scraper.common.Suggestion;
import lombok.Getter;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;

@Getter
public class SuggestionResponse implements Serializable {

    private String url;

    private String title;

    private Collection<String> keywords = new HashSet<>();

    private Collection<String> tags = new LinkedHashSet<>();

    public static SuggestionResponse toResponse(Suggestion suggestion) {
        SuggestionResponse response = new SuggestionResponse();
        response.url = suggestion.getUrl();
        response.title = suggestion.getTitle();
        response.keywords.addAll(suggestion.getKeywords());
        response.tags.addAll(suggestion.getPopularTags());
        return response;
    }
}
