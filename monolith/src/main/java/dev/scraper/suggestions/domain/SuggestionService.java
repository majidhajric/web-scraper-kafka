package dev.scraper.suggestions.domain;

import dev.scraper.common.Link;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@Service
public class SuggestionService {

    private final ScrapingService scrapingService;

    private final SuggestionsCache suggestionsCache;

    public SuggestionService(ScrapingService scrapingService, SuggestionsCache suggestionsCache) {
        this.scrapingService = scrapingService;
        this.suggestionsCache = suggestionsCache;
    }

    public Link createSuggestion(String userId, String pageURL) throws InterruptedException, IOException, URISyntaxException {
        PageDetails pageDetails = scrapingService.extractKeywords(pageURL);
        Link link = Link.create(userId, pageURL, pageDetails.getTitle(), pageDetails.getKeywords());
        suggestionsCache.save(userId, link);
        return link;
    }
}
