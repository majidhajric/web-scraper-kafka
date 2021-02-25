package dev.scraper.suggestions.api;

import dev.scraper.suggestions.domain.ScrapingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/suggestions")
@ControllerAdvice
public class SuggestionsController {

    private final ScrapingService scrapingService;

    public SuggestionsController(ScrapingService scrapingService) {
        this.scrapingService = scrapingService;
    }

    @GetMapping
    public List<String> getSuggestions(@RequestParam String pageURL)
            throws InterruptedException, IOException, URISyntaxException {
        return scrapingService.extractKeywords(pageURL);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String employeeNotFoundHandler(Exception ex) {
        return ex.getMessage();
    }
}
