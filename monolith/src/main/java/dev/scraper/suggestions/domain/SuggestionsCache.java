package dev.scraper.suggestions.domain;

import dev.scraper.common.Link;

public interface SuggestionsCache {
    void save(String userId, Link link);
    Link findById(String userId);
    void delete(String userId);
}
