package dev.scraper.management.infra;

import dev.scraper.suggestions.domain.Link;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoLinkRepository extends MongoRepository<Link, String> {

    Link save(Link link);

    void deleteByIdAndUserId(String id, String userId);

    Page<Link> findAllByUserId(String userId, Pageable pageable);

    List<Link> findAllByUserId(String userId);

    List<Link> findAllByTagsContaining(String tag);
}
