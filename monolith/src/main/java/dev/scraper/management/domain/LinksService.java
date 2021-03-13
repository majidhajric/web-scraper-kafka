package dev.scraper.management.domain;

import dev.scraper.common.Hash;
import dev.scraper.common.Link;
import dev.scraper.common.Suggestion;
import dev.scraper.management.infra.MongoLinkRepository;
import dev.scraper.suggestions.domain.SuggestionsCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class LinksService {

    @Value("${links.topic.name:links-topic}")
    private String linksTopicName;

    private final KafkaTemplate<String, Link> kafkaTemplate;

    private final SuggestionsCache suggestionsCache;

    private final MongoLinkRepository linkRepository;

    public LinksService(KafkaTemplate<String, Link> kafkaTemplate,
                        SuggestionsCache suggestionsCache, MongoLinkRepository linkRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.suggestionsCache = suggestionsCache;
        this.linkRepository = linkRepository;
    }

    public Link createLink(String userId, String url, Collection<String> tags) {
        Suggestion cachedSuggestion = suggestionsCache.findById(userId);
        Hash hash = new Hash(url);
        if (!cachedSuggestion.getHash().equals(hash) || !cachedSuggestion.containsTags(tags)) {
            throw new RuntimeException("Invalid Link");
        }

        Optional<Link> optionalLink = linkRepository.findByUserIdAndHash(userId, hash.getValue());
        if (optionalLink.isPresent()) {
            throw new RuntimeException("Link already exists");
        }

        Link link = new Link(userId, url, cachedSuggestion.getTitle(), tags);
        link = linkRepository.save(link);
        kafkaTemplate.send(linksTopicName, link);

        return link;
    }

    public Link updateLink(String id, String userId, Link link) {
        if (!id.equals(link.getId())) {
            throw new RuntimeException("Invalid Link");
        }

        Suggestion cachedSuggestion = suggestionsCache.findById(userId);

        if (!cachedSuggestion.getHash().equals(link.getHash()) || !cachedSuggestion.containsTags(link.getTags())) {
            throw new RuntimeException("Invalid Link");
        }
        Link existingLink = linkRepository.findById(id).orElseThrow(RuntimeException::new);
        link = linkRepository.save(link);
        return link;
    }

    public void deleteLink(String id, String userId) {
        linkRepository.deleteByIdAndUserId(id, userId);
    }

    public void deleteAllLinks(String userId) {
        linkRepository.deleteAllByUserId(userId);
    }

    public Page<Link> getAllLinks(String userId, Pageable pageable) {
        return linkRepository.findAllByUserIdOrderByTimeCreatedDesc(userId, pageable);
    }

    public Page<Link> searchLinks(String userId, String tag, Pageable pageable) {
        return linkRepository.findAllByUserIdAndTagsContainingOrderByTimeCreatedDesc(userId, tag, pageable);
    }

}
