package dev.scraper.management.infra;

import dev.scraper.common.Link;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoLinkRepository extends MongoRepository<Link, String> {

    Link save(Link link);

    void deleteByIdAndUserId(String id, String userId);

    void deleteAllByUserId(String userId);

    Page<Link> findAllByUserIdOrderByTimeCreatedDesc(String userId, Pageable pageable);

    Page<Link> findAllByUserIdAndTagsContainingOrderByTimeCreatedDesc(String userId, String tag, Pageable pageable);

    Optional<Link> findByUserIdAndHash(String userId, String pageHash);
}
