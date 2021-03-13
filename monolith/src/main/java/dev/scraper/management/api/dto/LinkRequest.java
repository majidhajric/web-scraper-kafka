package dev.scraper.management.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class LinkRequest implements Serializable {

    private String url;

    private Set<String> tags = new HashSet<>();

}
