package dev.scraper.management.api;

import dev.scraper.common.Link;
import dev.scraper.management.api.dto.LinkRequest;
import dev.scraper.management.api.dto.LinkResponse;
import dev.scraper.management.domain.LinksService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/links")
@ControllerAdvice
public class LinkController {

    private final LinksService linksService;

    public LinkController(LinksService linksService) {
        this.linksService = linksService;
    }

    private String getUserId(Jwt jwt) {
        return (String) jwt.getClaims().get("sub");
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public LinkResponse createLink(@RequestBody LinkRequest linkRequest, @AuthenticationPrincipal Jwt jwt) {
        String userId = getUserId(jwt);
        Link link = linksService.createLink(userId, linkRequest.getUrl(), linkRequest.getTags());
        return LinkResponse.toResponse(link);
    }

    @GetMapping(path = "/all")
    @ResponseStatus(code = HttpStatus.OK)
    public Page<LinkResponse> getAllLinks(@RequestParam(required = false, defaultValue = "0") Integer page,
                                  @RequestParam(required = false, defaultValue = "5") Integer size,
                                  @AuthenticationPrincipal Jwt jwt) {
        String userId = getUserId(jwt);
        Pageable pageable = PageRequest.of(page, size);
        List<LinkResponse> responseList = linksService.getAllLinks(userId, pageable).stream()
                .map(LinkResponse::toResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(responseList);
    }

    @GetMapping(path = "/search")
    @ResponseStatus(code = HttpStatus.OK)
    public Page<Link> getAllLinks(@RequestParam String tag,
                                  @RequestParam(required = false, defaultValue = "0") Integer page,
                                  @RequestParam(required = false, defaultValue = "5") Integer size,
                                  @AuthenticationPrincipal Jwt jwt) {
        String userId = getUserId(jwt);
        Pageable pageable = PageRequest.of(page, size);
        return linksService.searchLinks(userId, tag, pageable);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteLink(@PathVariable String id, @AuthenticationPrincipal Jwt jwt) {
        String userId = getUserId(jwt);
        linksService.deleteLink(id, userId);
    }

    @DeleteMapping(path = "/all")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteLink(@AuthenticationPrincipal Jwt jwt) {
        String userId = getUserId(jwt);
        linksService.deleteAllLinks(userId);
    }
}
