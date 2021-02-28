package dev.scraper.management.infra;

import dev.scraper.suggestions.domain.Link;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoLinkRepository extends MongoRepository<Link, String> {

    Link save(Link link);

    void deleteById(String id);

    List<Link> findAllByUserId(String userId);

    List<Link> findAllByTagsContaining(String tag);
}
