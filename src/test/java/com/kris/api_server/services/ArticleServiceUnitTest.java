package com.kris.api_server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import com.kris.api_server.models.Article;
import com.kris.api_server.models.User;
import com.kris.api_server.repositories.ArticleRepository;
import com.kris.api_server.repositories.UserRepository;
import com.kris.api_server.requestMappers.CreateArticleRequest;
import com.kris.api_server.requestMappers.UpdateArticleRequest;
import com.kris.api_server.service.ArticleService;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceUnitTest {

    @InjectMocks
    private ArticleService articleService;

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testCreateArticle_SuccessfullyCreateArticle() {
        List<Integer> users = new ArrayList<>();
        users.add(5);
        CreateArticleRequest createArticleRequest = new CreateArticleRequest();
        createArticleRequest.setContent("neshto");
        createArticleRequest.setTitle("Big news");
        createArticleRequest.setIs_active(true);
        createArticleRequest.setUser_id(users);
        Article unitTestArticle = new Article(createArticleRequest.getTitle(), createArticleRequest.getContent(),
                createArticleRequest.getIs_active());
        when(articleRepository.saveAndFlush(any())).thenAnswer(new Answer<Article>() {

            @Override
            public Article answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgument(0);
            }
        });
        when(userRepository.getReferenceById(anyInt())).thenAnswer(new Answer<User>() {

            @Override
            public User answer(InvocationOnMock invocation) throws Throwable {
                User user = new User();
                user.setId(invocation.getArgument(0));
                return user;
            }

        });
        Article article = articleService.create(createArticleRequest);

    }

    @Test
    public void updateArticle_WhenIdIsNotValid_FailedUpdate() {
        Article uniTestArticle = new Article(4, "Big news", "Kris is making unit tests on his own", true);
        UpdateArticleRequest updateArticleRequest = new UpdateArticleRequest();

        updateArticleRequest.setTitle("Big news");
        updateArticleRequest.setContent("Kris is making unit tests on his own");
        updateArticleRequest.setIs_active(true);
        // when(articleRepository.saveAndFlush(any())).thenAnswer(new Answer<Article>()
        // {

        // @Override
        // public Article answer(InvocationOnMock invocation) throws Throwable {
        // TODO Auto-generated method stub
        // updateArticleRequest.setTitle("Big news");
        // updateArticleRequest.setContent("Kris is making unit tests on his own");
        // updateArticleRequest.setIs_active(true);
        // return uniTestArticle;
        // }

        // });
        try {
            Article article = articleService.update(5, updateArticleRequest.getTitle(),
                    updateArticleRequest.getContent(), updateArticleRequest.getIs_active());
            fail("Exception was not thrown");
        } catch (IllegalArgumentException e) {

        }
    }

    @Test
    public void updateArticle_WhenSucessful_UpdatedArticle() {
        Article unitTestArticle = new Article(4, "Big ne1ws", "Kris is making unit tests on his own", true);
        UpdateArticleRequest updateArticleRequest = new UpdateArticleRequest();

        updateArticleRequest.setTitle("Big news");
        updateArticleRequest.setContent("Kris is making unit tests on his own");
        updateArticleRequest.setIs_active(true);

        when(articleRepository.findById(anyInt())).thenAnswer(new Answer<Optional<Article>>() {

            @Override
            public Optional<Article> answer(InvocationOnMock invocation) throws Throwable {
                // TODO Auto-generated method stub

                return Optional.of(unitTestArticle);
            }
        });

        when(articleRepository.saveAndFlush(any())).thenAnswer(new Answer<Article>() {

            @Override
            public Article answer(InvocationOnMock invocation) throws Throwable {
                // TODO Auto-generated method stub

                return invocation.getArgument(0);
            }
        });
        assertEquals("Big ne1ws", unitTestArticle.getTitle());
        Article article = articleService.update(4, updateArticleRequest.getTitle(), updateArticleRequest.getContent(),
                updateArticleRequest.getIs_active());

        assertEquals("Big news", article.getTitle());

    }

    @Test
    public void deleteArticle_WhenIdIsNotValid_FailedToDeleteArticle() {

        // Article unitTestArticle = new Article(4, "Big news", "Kris is making unit
        // tests on his own", true);

        try {
            articleService.delete(5);
            fail("Exception was not thrown");
        } catch (IllegalArgumentException e) {
            // ignored
        }
        // asserted exception thrown
    }


    @Test
    public void deleteArticle_SucessfullDelete_ArticleDeleted(){

        Article unitTestArticle = new Article(4, "Big ne1ws", "Kris is making unit tests on his own", true);

        when(articleRepository.findById(anyInt())).thenAnswer(new Answer<Optional<Article>>() {

            @Override
            public Optional<Article> answer(InvocationOnMock invocation) throws Throwable {
                // TODO Auto-generated method stub
                return Optional.of(unitTestArticle);
            }
        });

        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                // TODO Auto-generated method stub

                assertEquals(4, (int)invocation.getArgument(0));
                
                return null;
            }
            
        }).when(articleRepository).deleteById(anyInt());

        Article article = articleService.delete(4);
        assertEquals(4, article.getId());
    }

    @Test
    public void deleteArticle_WhenIdIsNegativeInt_FailedToDeleteArticle(){

        try {
            articleService.delete(-1);
            fail("Exception was not thrown");
        } catch (IllegalArgumentException e) {
            // ignored
        }
    }

}
