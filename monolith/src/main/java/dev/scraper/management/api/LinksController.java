package dev.scraper.management.api;

import dev.scraper.common.Link;
import dev.scraper.management.domain.LinksService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/links")
@ControllerAdvice
public class LinksController {

    private final LinksService linksService;

    public LinksController(LinksService linksService) {
        this.linksService = linksService;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Link createLink(@RequestBody Link link, @AuthenticationPrincipal Jwt jwt) {
        String userId = getUserId(jwt);
        return linksService.createLink(userId, link);
    }

    private String getUserId(Jwt jwt) {
        return (String) jwt.getClaims().get("sub");
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Link updateLink(@PathVariable String id, @RequestBody Link link, @AuthenticationPrincipal Jwt jwt) {
        String userId = getUserId(jwt);
        return linksService.updateLink(id, userId, link);
    }

    @GetMapping(path = "/all")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Link> getAllLinks(@RequestParam(required = false, defaultValue = "0") Integer page,
                                  @RequestParam(required = false, defaultValue = "5") Integer size,
                                  @AuthenticationPrincipal Jwt jwt) {
        String userId = getUserId(jwt);
        Pageable paging = PageRequest.of(page, size);
        return linksService.getAllLinks(userId, paging).getContent();
    }

    @GetMapping(path = "/search")
    @ResponseStatus(code = HttpStatus.OK)
    public List<Link> getAllLinks(@RequestParam String tag,
                                  @AuthenticationPrincipal Jwt jwt) {
        String userId = getUserId(jwt);
        return linksService.searchLinks(userId, tag);
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
