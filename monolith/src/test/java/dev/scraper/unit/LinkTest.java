package dev.scraper.unit;

import dev.scraper.common.Link;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Log4j2
public class LinkTest {

    @Test
    public void whenTwoSameURIs_thenEqualsTagsSourcesExpected() {
        String uriOne = "http://www.example.com?two=2&one=1";
        String uriTwo = "www.example.com?one=1&two=2";
        String title = "Example";
        String userId = "test";

        Link linkOne = new Link(userId,uriOne, title, Collections.emptySet());
        Link linkTwo = new Link(userId,uriTwo, title, Collections.emptySet());

        assertEquals(linkOne, linkTwo);
    }

}
