package dev.scraper.suggestions.api.dto;

import dev.scraper.common.Suggestion;
import lombok.Getter;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

@Getter
public class SuggestionResponse implements Serializable {

    private String url;

    private String title;

    private Set<String> keywords = new TreeSet<>();

    private Set<String> tags = new TreeSet<>();

    public static SuggestionResponse toResponse(Suggestion suggestion) {
        SuggestionResponse response = new SuggestionResponse();
        response.url = suggestion.getUrl();
        response.title = suggestion.getTitle();
        response.keywords.addAll(suggestion.getKeywords());
        response.tags.addAll(suggestion.getPopularTags());
        return response;
    }
}
