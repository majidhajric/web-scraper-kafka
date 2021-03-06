package dev.scraper.suggestions.domain;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

public interface ScrapingService {

    Set<String> extractKeywords(String pageURL)
            throws URISyntaxException, IOException, InterruptedException;
}
