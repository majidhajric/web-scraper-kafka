package dev.scraper.management.domain;

import dev.scraper.common.Link;
import dev.scraper.management.infra.MongoLinkRepository;
import dev.scraper.suggestions.domain.SuggestionsCache;
import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LinksService {
    private static final String TOPIC = "links-topic";

    private final KafkaTemplate<String, Link> kafkaTemplate;

    private final SuggestionsCache suggestionsCache;

    private final MongoLinkRepository linkRepository;

    StreamsBuilder builder = new StreamsBuilder();

    public LinksService(KafkaTemplate<String, Link> kafkaTemplate,
                        SuggestionsCache suggestionsCache, MongoLinkRepository linkRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.suggestionsCache = suggestionsCache;
        this.linkRepository = linkRepository;
    }

    public Link createLink(String userId, Link link) {
        Link cachedLink = suggestionsCache.findById(userId);
        if (!cachedLink.getPageHash().equals(link.getPageHash()) || !cachedLink.getTags().containsAll(link.getTags())) {
            throw new RuntimeException("Invalid Link");
        }
        Optional<Link> optionalLink = linkRepository.findByUserIdAndPageHash(userId, link.getPageHash());
        if (optionalLink.isPresent()) {
            throw new RuntimeException("Link already exists");
        }
        link = linkRepository.save(link);

        kafkaTemplate.send(TOPIC, link);

        return link;
    }

    public Link updateLink(String id, String userId, Link link) {
        if (!id.equals(link.getId())) {
            throw new RuntimeException("Invalid Link");
        }

        Link cachedLink = suggestionsCache.findById(userId);
        if (!cachedLink.getPageHash().equals(link.getPageHash()) || !cachedLink.getTags().containsAll(link.getTags())) {
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

    public Page<Link> getAllLinks(String userId, Pageable paging) {
        return linkRepository.findAllByUserId(userId, paging);
    }

    public List<Link> searchLinks(String userId, String tag) {
        return linkRepository.findAllByUserIdAndTagsContaining(userId, tag);
    }
}
