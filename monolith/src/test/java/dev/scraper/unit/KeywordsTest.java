package dev.scraper.unit;

import dev.scraper.suggestions.domain.PageDetails;
import dev.scraper.suggestions.infra.LuceneScrapingService;
import io.micrometer.core.instrument.util.IOUtils;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class KeywordsTest {

    private LuceneScrapingService scrapingService = new LuceneScrapingService();

    @Test
    public void keywords_extraction_test() {
        String html = IOUtils.toString(this.getClass().getResourceAsStream("sport.html"), StandardCharsets.UTF_8);
        PageDetails pageDetails = scrapingService.extractKeywordsFromHtml(html);
        Set<String> keywords = pageDetails.getKeywords();
        assertThat(keywords.containsAll(Arrays.asList("Real Madrid", "Barcelona", "people")));
    }
}
