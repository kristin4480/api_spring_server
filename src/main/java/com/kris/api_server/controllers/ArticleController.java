package com.kris.api_server.controllers;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.kris.api_server.models.Article;
import com.kris.api_server.requestMappers.CreateArticleRequest;
import com.kris.api_server.requestMappers.UpdateArticleRequest;
import com.kris.api_server.service.ArticleService;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class ArticleController {
    
    private ArticleService articleService;
    @Autowired
    public ArticleController(ArticleService articleService){
        this.articleService = articleService;
    }

    @GetMapping(produces = "application/json", value = "/articles")
    public List<Article>  getAll(){
        return articleService.getall();
    }

    @GetMapping(produces = "application/json", value = "/articles/{id}")
    @ResponseBody
    public Article getArticleById(@PathVariable("id") int id){
        Optional<Article> optArticle = articleService.getArticleByID(id);
        if(optArticle.isPresent()){
            return optArticle.get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found");
    }

    @PostMapping(produces = "application/json", value = "/articles")
    public Article createArticle(@RequestBody @Valid CreateArticleRequest createArticleRequest, HttpServletResponse response){
        response.setStatus(HttpServletResponse.SC_CREATED);
        return articleService.create(createArticleRequest);
    }

    @PutMapping(produces = "application/json", value = "/articles/{id}")
    @ResponseBody
    public Article updateArticle(@PathVariable("id") int id, @RequestBody @Valid UpdateArticleRequest updateArticleRequest){
        try { 
            return articleService.update(id, updateArticleRequest.getTitle(), updateArticleRequest.getContent(), updateArticleRequest.getIs_active());
        } catch(IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,  "user not found");
          }
    }

    @DeleteMapping(produces = "application/json", value = "/articles/{id}")
    @ResponseBody
    public Article deleteArticle(@PathVariable("id") int id){
        try {
        return articleService.delete(id);
        } catch(IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,  "user not found");
           } 
    }
}
