package dev.scraper.suggestions.domain;

import java.io.IOException;
import java.net.URISyntaxException;

public interface ScrapingService {

    PageDetails extractKeywords(String pageURL)
            throws URISyntaxException, IOException, InterruptedException;
}
