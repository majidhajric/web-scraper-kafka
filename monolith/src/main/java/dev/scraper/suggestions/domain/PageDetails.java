package dev.scraper.suggestions.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
@Getter
public class PageDetails {

    private final String title;

    private final Set<String> keywords;
}
