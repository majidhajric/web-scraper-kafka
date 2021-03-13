package dev.scraper.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.scraper.management.api.dto.LinkRequest;
import dev.scraper.suggestions.api.dto.SuggestionResponse;
import dev.scraper.suggestions.domain.PageDetails;
import dev.scraper.suggestions.domain.PopularLink;
import dev.scraper.suggestions.domain.ScrapingService;
import dev.scraper.suggestions.infra.StateStoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@WebAppConfiguration
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:29092", "port=29092" })
public class ScraperApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private ScrapingService scrapingService;

    @MockBean
    private StateStoreService stateStoreService;

    private final String firstUserId = UUID.randomUUID().toString();
    private final String secondUserId = UUID.randomUUID().toString();

    private final String testPage = "example.com/page";

    private PageDetails pageDetails() {
        Set<String> keywords = new LinkedHashSet<>();
        keywords.add("Barcelona");
        keywords.add("Real");
        keywords.add("Ronaldo");
        return new PageDetails("Sport", keywords);
    }

    private PageDetails updatedPageDetails() {
        Set<String> keywords = new LinkedHashSet<>();
        keywords.add("COVID");
        keywords.add("match");
        keywords.add("canceled");
        return new PageDetails("Sport", keywords);
    }
    @WithMockUser
    @Test
    public void create_duplicate_link_test() throws Exception {

        given(scrapingService.scrapPage(anyString())).willReturn(pageDetails());

        String content = mockMvc.perform(get("/api/suggestions")
                .param("pageURL", testPage)
                .with(jwt().jwt((jwt) -> jwt.claim("sub", firstUserId))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.keywords").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.keywords").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.keywords[0]").value("Barcelona"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.keywords[1]").value("Real"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tags").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();
        SuggestionResponse response = mapper.readValue(content, SuggestionResponse.class);
        LinkRequest request = new LinkRequest();
        request.setUrl(response.getUrl());
        request.setTags(response.getKeywords());

        String jsonBody = mapper.writeValueAsString(request);

        mockMvc.perform(post("/api/links")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(jwt().jwt((jwt) -> jwt.claim("sub", firstUserId))))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(post("/api/links")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(jwt().jwt((jwt) -> jwt.claim("sub", firstUserId))))
                .andExpect(status().is4xxClientError())
                .andReturn();

        mockMvc.perform(delete("/api/links/all")
                .with(jwt().jwt((jwt) -> jwt.claim("sub", firstUserId))))
                .andExpect(status().isOk())
                .andReturn();
    }

    @WithMockUser
    @Test
    public void create_link_with_popular_tags_test() throws Exception {

        given(scrapingService.scrapPage(anyString())).willReturn(pageDetails());

        String content = mockMvc.perform(get("/api/suggestions")
                .param("pageURL", testPage)
                .with(jwt().jwt((jwt) -> jwt.claim("sub", firstUserId))))
                .andReturn()
                .getResponse()
                .getContentAsString();
        SuggestionResponse firstUserResponse = mapper.readValue(content, SuggestionResponse.class);
        LinkRequest request = new LinkRequest();
        request.setUrl(firstUserResponse.getUrl());
        request.setTags(firstUserResponse.getKeywords());

        String jsonBody = mapper.writeValueAsString(request);

        mockMvc.perform(post("/api/links")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(jwt().jwt((jwt) -> jwt.claim("sub", firstUserId))))
                .andExpect(status().isCreated())
                .andReturn();

        given(scrapingService.scrapPage(anyString())).willReturn(updatedPageDetails());

        PopularLink popularLink = new PopularLink();
        popularLink.setTags(new ArrayList<>(firstUserResponse.getKeywords()));
        given(stateStoreService.getPopularLink(anyString())).willReturn(popularLink);

        content = mockMvc.perform(get("/api/suggestions")
                .param("pageURL", testPage)
                .with(jwt().jwt((jwt) -> jwt.claim("sub", secondUserId))))
                .andReturn()
                .getResponse()
                .getContentAsString();

        SuggestionResponse secondUserResponse = mapper.readValue(content, SuggestionResponse.class);

        assertThat(secondUserResponse.getKeywords()).containsAll(updatedPageDetails().getKeywords());
        assertThat(secondUserResponse.getTags()).containsAll(pageDetails().getKeywords());
    }


}
