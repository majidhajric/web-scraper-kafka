package dev.scraper.suggestions.domain;

import dev.scraper.common.Link;
import dev.scraper.management.infra.MongoLinkRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class SuggestionService {

    private final ScrapingService scrapingService;

    private final SuggestionsCache suggestionsCache;

    private final MongoLinkRepository linkRepository;

    public SuggestionService(ScrapingService scrapingService, SuggestionsCache suggestionsCache,
                             MongoLinkRepository linkRepository) {
        this.scrapingService = scrapingService;
        this.suggestionsCache = suggestionsCache;
        this.linkRepository = linkRepository;
    }

    public Link createSuggestion(String userId, String pageURL) throws InterruptedException, IOException, URISyntaxException {
        List<String> keywords = scrapingService.extractKeywords(pageURL);
        Link link = Link.create(userId, pageURL, keywords);
        suggestionsCache.save(userId, link);
        return link;
    }
}
