package dev.scraper.common;

import lombok.Getter;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Getter
public class Suggestion implements Serializable {

    private String url;

    private Hash hash;

    private String title;

    private List<String> keywords = new LinkedList<>();

    private List<String> popularTags = new LinkedList<>();

    public Suggestion(String url, String title, List<String> keywords, List<String> popularTags) {
        this.url = url;
        this.hash = new Hash(url);
        this.title = title;
        this.keywords.addAll(keywords);
        this.popularTags.addAll(popularTags);
    }

    public boolean containsTags(Collection<String> tags) {
        Set<String> allTags = new HashSet<>(keywords);
        allTags.addAll(popularTags);
        return allTags.containsAll(tags);
    }

}
