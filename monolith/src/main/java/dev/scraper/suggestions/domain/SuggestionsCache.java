package dev.scraper.suggestions.domain;

import dev.scraper.common.Suggestion;

public interface SuggestionsCache {
    void save(String userId, Suggestion suggestion);

    Suggestion findById(String userId);

    void delete(String userId);
}
