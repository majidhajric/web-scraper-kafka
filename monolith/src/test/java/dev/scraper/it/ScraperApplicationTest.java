package dev.scraper.it;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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

    @WithMockUser
    @Test
    public void duplicate_link_creat_test() throws Exception {
        String content = mockMvc.perform(get("/api/suggestions")
                .param("pageURL", "https://www.bbc.co.uk/news/av/world-europe-50849839")
                .with(jwt().jwt((jwt) -> jwt.claim("sub", "935d75a4-1f27-4de7-9c4b-c63d3f0b7f33"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$.tags").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        mockMvc.perform(post("/api/links")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(jwt().jwt((jwt) -> jwt.claim("sub", "935d75a4-1f27-4de7-9c4b-c63d3f0b7f33"))))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(post("/api/links")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(jwt().jwt((jwt) -> jwt.claim("sub", "935d75a4-1f27-4de7-9c4b-c63d3f0b7f33"))))
                .andExpect(status().is4xxClientError())
                .andReturn();

        mockMvc.perform(delete("/api/links/all")
                .with(jwt().jwt((jwt) -> jwt.claim("sub", "935d75a4-1f27-4de7-9c4b-c63d3f0b7f33"))))
                .andExpect(status().isOk())
                .andReturn();
    }
}
