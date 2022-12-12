package com.kris.api_server.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.web.server.ResponseStatusException;

import com.jayway.jsonpath.Option;
import com.kris.api_server.models.Article;
import com.kris.api_server.repositories.ArticleRepository;
import com.kris.api_server.service.ArticleService;

@ExtendWith(MockitoExtension.class)
public class ArticleControllerUnitTest {

    @InjectMocks
    private ArticleController articleController;

    @Mock
    private ArticleService articleService;

    @Test
    public void getAllArticles() {
        List<Article> unitTestArticleList = new ArrayList<Article>();
        unitTestArticleList.add(new Article("Big news", "Svetlio is bisexual", true));
        unitTestArticleList.add(new Article("Mr. Olympia news", "Kris has muscles", true));
        when(articleService.getall()).thenReturn(unitTestArticleList);

        List<Article> articleList = articleController.getAll();
        assertEquals(2, articleList.size());

        assertEquals("Mr. Olympia news", articleList.get(1).getTitle());

        System.out.println("Successful test");
    }

    @Test
    public void getArticleByID_WithExistingID() {
        Article unitTestArticle = new Article("Big news", "Kris has more muscles than Dido", true);
        unitTestArticle.setId(2);
        when(articleService.getArticleByID(2)).thenReturn(Optional.of(unitTestArticle));

        Article article = articleController.getArticleById(2);
        // TODO: asserts here

        assertEquals("Big news", article.getTitle());
        assertEquals(2, article.getId());

        // assertEquals(articleController.getArticleById(2), article);

        // assertNotEquals(articleController.getArticleById(6), article);

        System.out.println("passed");
    }

    @Test
    public void getArticleByID_WithNonExistingId() {
        // fake article with id 2

        //Article unitTestArticle = new Article("Big news", "Kris has split ass", true);
        //when(articleService.getArticleByID(6)).thenReturn(Optional.of(unitTestArticle));
        /*when(articleService.getArticleByID(2)).thenReturn(Optional.empty());
         try{
            Article article = articleController.getArticleById(2);
            fail("Exception not thrown");
         }catch(ResponseStatusException e){
            assertTrue(true);
         }
         assert that ResponseStatusException is thrown*/


         when(articleService.getArticleByID(anyInt())).thenAnswer(new Answer<Optional<Article>>() {

            @Override
            public Optional<Article> answer(InvocationOnMock invocation) throws Throwable {
                // TODO Auto-generated method stub
                //Integer argument = invocation.getArgument(0);
                //if(argument == 2){
                  //  return Optional.empty();
                //}
                return Optional.empty();
            }
         });
         try{
            Article article = articleController.getArticleById(2);
            fail("Exception not thrown");
         }catch(ResponseStatusException e){
            assertTrue(true);
         }
    }

    @Test
    public void getArticleByID_WithInvalidId() {
        // fake article with id 2


        when(articleService.getArticleByID(anyInt())).thenAnswer(new Answer<Optional<Article>>() {

            @Override
            public Optional<Article> answer(InvocationOnMock invocation) throws Throwable {
                // TODO Auto-generated method stub
                return Optional.empty();
            }
            
        });
        try{
            Article article = articleController.getArticleById(-59);
            fail("None Exception has been thrown");
        }catch(ResponseStatusException e){
            //assertTrue(true);
        }
    // articleController.getArticleById(-59)

    // assert that ResponseStatusException is thrown
}
}