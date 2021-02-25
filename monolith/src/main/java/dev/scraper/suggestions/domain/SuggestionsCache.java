package dev.scraper.suggestions.domain;

import java.util.List;
import java.util.Map;

public interface SuggestionsCache {
    void save(String userId, List<String> suggestions);
    List<String> findById(String userId);
    void delete(String userId);
}
