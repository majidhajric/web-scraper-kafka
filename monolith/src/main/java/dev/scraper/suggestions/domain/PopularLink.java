package dev.scraper.suggestions.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
@Data
public class PopularLink implements Serializable {

    public String hash;

    public List<String> tags = new LinkedList<>();

    public PopularLink update(String hash, Collection<String> tags) {
        this.hash = hash;
        tags.forEach(newTag-> {
            if (this.tags.contains(newTag)) {
                int position = this.tags.indexOf(newTag);
                this.tags.remove(position);
                this.tags.add(position > 0 ? position - 1 : 0, newTag);
            } else {
                this.tags.add(newTag);
            }
        });
        return this;
    }
}
