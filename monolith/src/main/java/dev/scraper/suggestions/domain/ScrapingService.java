package dev.scraper.suggestions.domain;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface ScrapingService {

    List<String> extractKeywords(String pageURL)
            throws URISyntaxException, IOException, InterruptedException;
}
