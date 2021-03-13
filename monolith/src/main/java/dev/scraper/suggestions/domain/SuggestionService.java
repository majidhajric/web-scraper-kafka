package dev.scraper.suggestions.domain;

import dev.scraper.common.Hash;
import dev.scraper.common.Suggestion;
import dev.scraper.suggestions.infra.StateStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class SuggestionService {

    private final ScrapingService scrapingService;

    private final StateStoreService stateStoreService;

    private final SuggestionsCache suggestionsCache;

    public SuggestionService(ScrapingService scrapingService, StateStoreService stateStoreService,
                             SuggestionsCache suggestionsCache) {
        this.scrapingService = scrapingService;
        this.stateStoreService = stateStoreService;
        this.suggestionsCache = suggestionsCache;
    }

    public Suggestion createSuggestion(String userId, String pageURL) throws InterruptedException, IOException, URISyntaxException {
        PageDetails pageDetails = scrapingService.scrapPage(pageURL);
        Hash hash = new Hash(pageURL);
        List<String> keywords = new LinkedList<>(pageDetails.getKeywords());
        List<String> popularTags = new LinkedList<>();
        PopularLink popularLink = stateStoreService.getPopularLink(hash.getValue());
        if (popularLink != null) {
            popularTags.addAll(popularLink.getTags());
        }

        Suggestion suggestion = new Suggestion(pageURL, pageDetails.getTitle(), keywords, popularTags);

        suggestionsCache.save(userId, suggestion);
        return suggestion;
    }
}
