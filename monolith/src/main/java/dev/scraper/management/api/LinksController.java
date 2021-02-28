package dev.scraper.management.api;

import dev.scraper.management.infra.MongoLinkRepository;
import dev.scraper.suggestions.domain.Link;
import dev.scraper.suggestions.domain.SuggestionsCache;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/links")
@ControllerAdvice
public class LinksController {

    private final SuggestionsCache suggestionsCache;

    private final MongoLinkRepository linkRepository;

    public LinksController(SuggestionsCache suggestionsCache, MongoLinkRepository linkRepository) {
        this.suggestionsCache = suggestionsCache;
        this.linkRepository = linkRepository;
    }

    @PostMapping
    public Link createLink(@RequestBody Link link, @AuthenticationPrincipal Jwt jwt) {
        String userId = (String) jwt.getClaims().get("sub");
        Link cachedLink = suggestionsCache.findById(userId);
        if (!cachedLink.getPageURL().equals(cachedLink.getPageURL()) || !cachedLink.getTags().containsAll(link.getTags())) {
            throw new RuntimeException("Invalid Link");
        }
        link = linkRepository.save(link);
        return link;
    }

    @GetMapping(path = "/all")
    public List<Link> getAllLinks(@AuthenticationPrincipal Jwt jwt) {
        String userId = (String) jwt.getClaims().get("sub");
        return linkRepository.findAllByUserId(userId);
    }
}
