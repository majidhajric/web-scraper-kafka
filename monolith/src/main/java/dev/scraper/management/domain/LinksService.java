package dev.scraper.management.domain;

import dev.scraper.management.infra.MongoLinkRepository;
import dev.scraper.common.Link;
import dev.scraper.suggestions.domain.SuggestionsCache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LinksService {

    private final SuggestionsCache suggestionsCache;

    private final MongoLinkRepository linkRepository;

    public LinksService(SuggestionsCache suggestionsCache, MongoLinkRepository linkRepository) {
        this.suggestionsCache = suggestionsCache;
        this.linkRepository = linkRepository;
    }

    public Link createLink(String userId, Link link) {
        Link cachedLink = suggestionsCache.findById(userId);
        if (!cachedLink.getPageHash().equals(link.getPageHash()) || !cachedLink.getTags().containsAll(link.getTags())) {
            throw new RuntimeException("Invalid Link");
        }
        Optional<Link> optionalLink = linkRepository.findByUserIdAndPageHash(userId, link.getPageHash());
        if (optionalLink.isPresent()){
            throw new RuntimeException("Link already exists");
        }
        link = linkRepository.save(link);
        return link;
    }

    public Link updateLink(String userId, Link link) {
        Link cachedLink = suggestionsCache.findById(userId);
        if (!cachedLink.getPageURL().equals(cachedLink.getPageURL()) || !cachedLink.getTags().containsAll(link.getTags())) {
            throw new RuntimeException("Invalid Link");
        }
        Link existingLink = linkRepository.findById(link.getId()).orElseThrow(RuntimeException::new);
        link = linkRepository.save(link);
        return link;
    }

    public void deleteLink(String id, String userId) {
        linkRepository.deleteByIdAndUserId(id, userId);
    }

    public Page<Link> getAllLinks(String userId, Pageable paging) {
        return linkRepository.findAllByUserId(userId, paging);
    }

}