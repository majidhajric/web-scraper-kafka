package dev.scraper.suggestions;

import dev.scraper.suggestions.domain.PageURL;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
public class PageURLTest {

    @Test
    public void whenTwoSameURIs_thenEqualsTagsSourcesExpected() {
        String uriOne = "http://www.example.com?two=2&one=1";
        String uriTwo = "www.example.com?one=1&two=2";

        PageURL sourceOne = new PageURL(uriOne);
        PageURL sourceTwo = new PageURL(uriTwo);

        log.info("Source one: {}", sourceOne);
        log.info("Source two: {}", sourceTwo);
        assertEquals(sourceOne, sourceTwo);
    }

}
