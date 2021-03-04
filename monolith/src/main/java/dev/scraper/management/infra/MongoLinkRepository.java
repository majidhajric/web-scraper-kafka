package dev.scraper.management.infra;

import dev.scraper.common.Link;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MongoLinkRepository extends MongoRepository<Link, String> {

    Link save(Link link);

    void deleteByIdAndUserId(String id, String userId);

    Page<Link> findAllByUserId(String userId, Pageable pageable);

    List<Link> findAllByUserId(String userId);

    Optional<Link> findByUserIdAndPageHash(String userId, String pageHash);

    List<Link> findAllByTagsContaining(String tag);
}
