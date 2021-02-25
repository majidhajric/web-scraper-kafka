package dev.scraper.suggestions.infra;

import dev.scraper.suggestions.domain.ScrapingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.jsoup.Jsoup;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LuceneScrapingService implements ScrapingService {

    private final HttpClient client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();

    private HttpResponse<String> loadHtmlPage(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .timeout(Duration.ofMinutes(1))
                .header("Content-Type", "text/html; charset=UTF-8")
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    private String extractContent(String html) {
        return Jsoup.parse(html).body().data();
    }

    private List<String> analyzeContent(String content) {
        Map<String, Integer> keywords = new HashMap<>();

        // hack to keep dashed words (e.g. "non-specific" rather than "non" and "specific")
        content = content.replaceAll("-+", "-0");
        // replace any punctuation char but dashes and apostrophes and by a space
        content = content.replaceAll("[\\p{Punct}&&[^'-]]+", " ");
        // replace most common english contractions
        content = content.replaceAll("(?:'(?:[tdsm]|[vr]e|ll))+\\b", "");

        try (StandardAnalyzer analyzer = new StandardAnalyzer();
             TokenStream stream = analyzer.tokenStream("contents", new StringReader(content))) {
            // to lower case
            TokenStream tokenStream = new LowerCaseFilter(stream);
            // remove dots from acronyms (and "'s" but already done manually above)
            tokenStream = new ClassicFilter(tokenStream);
            // convert any char to ASCII
            tokenStream = new ASCIIFoldingFilter(tokenStream);
            // remove english stop words
            tokenStream = new StopFilter(tokenStream, EnglishAnalyzer.getDefaultStopSet());

            tokenStream = new PorterStemFilter(tokenStream);

            CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);

            stream.reset();
            // for each token
            while (tokenStream.incrementToken()) {
                String term = token.toString();

                keywords.compute(term, (k, v) -> (v == null) ? 1 : v+1);
            }
        } catch (Exception e) {
            log.error("Extraction Exception:", e);
        }

        return keywords.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<String> extractKeywords(String pageURL)
            throws URISyntaxException, IOException, InterruptedException {
        String body = loadHtmlPage(new URI(pageURL)).body();
        String content = extractContent(body);
        List<String> keywords = analyzeContent(content);

        return keywords;
    }
}
