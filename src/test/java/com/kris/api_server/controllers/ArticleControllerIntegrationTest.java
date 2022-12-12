package com.kris.api_server.controllers;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.logging.log4j.util.Strings;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kris.api_server.models.Article;
import com.kris.api_server.models.User;
import com.kris.api_server.repositories.ArticleRepository;
import com.kris.api_server.repositories.UserRepository;
import com.kris.api_server.requestMappers.CreateArticleRequest;
import com.kris.api_server.requestMappers.UpdateArticleRequest;

@SpringBootTest
@ActiveProfiles("test")
public class ArticleControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc; // something/similar to postman
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach // this will be run, before any other method
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    // @BeforeEach
    // public void ensure(){
    // articleRepository.deleteAll();
    // }

    @Test
    @Transactional
    public void getAllArticles_ArticlesPresent_SucessfullListingOfArticles() throws Exception {

        articleRepository.deleteAll();

        Article article = new Article("Integration test example", "Get all articles examples with integrationn test",
                true);
        articleRepository.saveAndFlush(article);

        Article article1 = new Article("Integration test example 2",
                "Get all articles with integrartionn test example 2", false);

        articleRepository.saveAndFlush(article1);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/articles"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title", Matchers.is("Integration test example")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content",
                        Matchers.is("Get all articles examples with integrationn test")));

        // perform - inside it, make a REST call (get, post, etc.)
        // andDo - actioned after perform, no logic inside, used for login, monitoring.
        // andExpect - rules that assert the response from the request
    }

    @Test
    @Transactional
    public void getAllArticles_NoArticlesPresent_NoArticlesProvided() throws Exception {

        articleRepository.deleteAll();

        this.mockMvc.perform(MockMvcRequestBuilders.get("/articles"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());

    }

    @Test
    @Transactional
    public void getArticleById_WithInvalidId_FailedToGetArticle() throws Exception {

        articleRepository.deleteAll();

        this.mockMvc.perform(MockMvcRequestBuilders.get("/articles/7"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    @Transactional
    public void getArticleByID_WithValidId_SuccessfullArticleRetrieved() throws Exception {

        articleRepository.deleteAll();

        Article article = new Article("Valid Article", "Retrieved", true);
        Article article1 = articleRepository.saveAndFlush(article);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/articles/" + article1.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("Valid Article")));

    }

    @Test
    @Transactional
    public void createArticle_ParametersMet_SuccessfullCreate() throws Exception {
        articleRepository.deleteAll();
        // List<Integer> usersIds = new ArrayList<>();
        CreateArticleRequest createArticleRequest = new CreateArticleRequest();

        createArticleRequest.setTitle("Shadow League");
        createArticleRequest.setContent("Pain is an illusion");
        createArticleRequest.setIs_active(true);
        User user = new User("Kris", "something@gmail.com");
        userRepository.saveAndFlush(user);
        // usersIds.add(user.getId());
        // createArticleRequest.setUser_id(usersIds);

        createArticleRequest.setUser_id(List.of(user.getId()));

        ResultActions resultAction = this.mockMvc.perform(
                MockMvcRequestBuilders.post("/articles")
                        .content(asJsonString(createArticleRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("Shadow League")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is("Pain is an illusion")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.is_active", Matchers.is(true)));

        MvcResult result = resultAction.andReturn();
        String response = result.getResponse().getContentAsString();
        JsonNode jsonResponse = stringToJson(response);
        int articleResponseId = jsonResponse.get("id").asInt();

        Optional<Article> optArticle = articleRepository.findById(articleResponseId);
        if (optArticle.isPresent()) {
            Article article = optArticle.get();
            assertEquals("Shadow League", article.getTitle());
        } else {
            fail("Article not fount in the DB");
        }
    }

    @Test
    @Transactional
    public void createArticle_InvalidParameters_FailedToCreate() throws Exception {

        articleRepository.deleteAll();
        CreateArticleRequest createArticleRequest = new CreateArticleRequest();

        createArticleRequest.setTitle("");
        createArticleRequest.setContent("   ");
        createArticleRequest.setIs_active(false);

        User user = new User("Kris", "something@gmail.com");
        userRepository.saveAndFlush(user);
        createArticleRequest.setUser_id(List.of());

        this.mockMvc.perform(MockMvcRequestBuilders.post("/articles")
                .content(asJsonString(createArticleRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(""));

    }

    @Test
    @Transactional
    public void updateArticle_conditionsMet_SuccessfulUpdateOfArticle() throws Exception {

        articleRepository.deleteAll();
        Article article = new Article("This is a title", "This is some content", true);
        articleRepository.saveAndFlush(article);
        UpdateArticleRequest updateArticleRequest = new UpdateArticleRequest();

        updateArticleRequest.setTitle("This is an updated title");
        updateArticleRequest.setContent("This is an updated content");
        updateArticleRequest.setIs_active(false);

        ResultActions resultAction = this.mockMvc.perform(MockMvcRequestBuilders.put("/articles/" + article.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(updateArticleRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", Matchers.is("This is an updated title")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.is("This is an updated content")));

        MvcResult result = resultAction.andReturn();
        String response = result.getResponse().getContentAsString();
        JsonNode jsonResponse = stringToJson(response);
        Integer articleResponseId = jsonResponse.get("id").asInt();
        Optional<Article> optArticle = articleRepository.findById(articleResponseId);
        if (optArticle.isPresent()) {
            Article integrationTestArticle = optArticle.get();
            assertEquals("This is an updated title", integrationTestArticle.getTitle());
        } else {
            fail("Failed to update");
        }

    }

    @Test
    @Transactional
    public void updateArticle_InvalidId_UnableToUpdateArticle() throws Exception {

        articleRepository.deleteAll();
        UpdateArticleRequest updateArticleRequest = new UpdateArticleRequest();

        updateArticleRequest.setTitle("This is an updated title");
        updateArticleRequest.setContent("This is an updated content");
        updateArticleRequest.setIs_active(false);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/articles/69")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(updateArticleRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    @Transactional
    public void deleteArticle_MetConditionds_SuccessfulDeletionOfArticle() throws Exception {

        articleRepository.deleteAll();

        Article article = new Article("This is a title", "This is some content", true);
        int deletedArticleId = article.getId();
        articleRepository.saveAndFlush(article);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/articles/" + article.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

                Optional<Article> optArticle = articleRepository.findById(deletedArticleId);
                if(optArticle.isPresent()){
                    fail("Article is stil present");
                } else {
                    System.out.println("Article deleted");
                }
    }

    @Test
    @Transactional
    public void deleteArticle_InvalidId_UnableToDeleteArticle() throws Exception {

        articleRepository.deleteAll();

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/articles/69")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static JsonNode stringToJson(final String obj) {
        try {
            return new ObjectMapper().readTree(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
