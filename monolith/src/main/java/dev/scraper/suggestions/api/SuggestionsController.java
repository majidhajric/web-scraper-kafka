package dev.scraper.suggestions.api;

import dev.scraper.suggestions.domain.Link;
import dev.scraper.suggestions.domain.SuggestionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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

@RestController
@RequestMapping(path = "/api/suggestions")
@ControllerAdvice
public class SuggestionsController {

 private final SuggestionService suggestionService;

    public SuggestionsController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @GetMapping
    public Link getSuggestions(@RequestParam(name = "pageURL") String pageURL, @AuthenticationPrincipal Jwt jwt)
            throws InterruptedException, IOException, URISyntaxException {
        String userId = jwt.getClaim("sub");
        return suggestionService.createSuggestion(userId, pageURL);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String exceptionsHandler(Exception ex) {
        return ex.getMessage();
    }
}
