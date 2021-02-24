package dev.scraper.suggestions.domain;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

public interface ScrapingService {

    List<String> extractKeywords(String pageURL)
            throws URISyntaxException, IOException, InterruptedException;
}
