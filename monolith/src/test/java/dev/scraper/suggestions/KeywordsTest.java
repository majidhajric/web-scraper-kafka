package dev.scraper.suggestions;

import dev.scraper.suggestions.infra.LuceneScrapingService;
import io.micrometer.core.instrument.util.IOUtils;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class KeywordsTest {

    private LuceneScrapingService scrapingService = new LuceneScrapingService();

    @Test
    public void keywords_extraction_test() {
        String html = IOUtils.toString(this.getClass().getResourceAsStream("sport.html"), StandardCharsets.UTF_8);
        List<String> keywords = scrapingService.extractKeywordsFromHtml(html);
        assertThat(keywords.containsAll(Arrays.asList("Real Madrid", "Barcelona", "people")));
    }
}
