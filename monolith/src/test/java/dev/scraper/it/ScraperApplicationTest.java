package dev.scraper.it;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@WebAppConfiguration
public class ScraperApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    private final String userId = UUID.randomUUID().toString();

    private final String testPage = "https://www.bbc.com/sport/football/56301568";

    @Test
    public void duplicate_link_create_test() throws Exception {
        String content = mockMvc.perform(get("/api/suggestions")
                .param("pageURL", testPage)
                .with(jwt().jwt((jwt) -> jwt.claim("sub", userId))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tags").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        mockMvc.perform(post("/api/links")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(jwt().jwt((jwt) -> jwt.claim("sub", userId))))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(post("/api/links")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(jwt().jwt((jwt) -> jwt.claim("sub", userId))))
                .andExpect(status().is4xxClientError())
                .andReturn();

        mockMvc.perform(delete("/api/links/all")
                .with(jwt().jwt((jwt) -> jwt.claim("sub", userId))))
                .andExpect(status().isOk())
                .andReturn();
    }


}
