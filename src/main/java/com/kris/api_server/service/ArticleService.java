package com.kris.api_server.service;

import java.lang.StackWalker.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kris.api_server.models.User;
import com.kris.api_server.repositories.ArticleRepository;
import com.kris.api_server.repositories.UserRepository;
import com.kris.api_server.requestMappers.CreateArticleRequest;
import com.okta.commons.lang.Collections;
import com.kris.api_server.models.Article;
import com.kris.api_server.requestMappers.CreateArticleRequest;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public List<Article> getall() {
        return articleRepository.findAllTitles();
    }

    public Optional<Article> getArticleByID(int id) {
        return articleRepository.findById(id);
    }

    @Transactional
    public Article create(CreateArticleRequest createArticleRequest) {
        Article article = new Article(createArticleRequest.getTitle(), createArticleRequest.getContent(),
                createArticleRequest.getIs_active());
        // article.setUser(Collections.toList(userRepository.getReferenceById(createArticleRequest.getUser_id())));
        List<User> list = new ArrayList<>();
        for (Integer userId : createArticleRequest.getUser_id()) {
            list.add(userRepository.getReferenceById(userId));
        }
        article.setUser(list);
        return articleRepository.saveAndFlush(article);
    }

    @Transactional
    public Article update(int id, String title, String content, Boolean is_active) throws IllegalArgumentException {
        Optional<Article> optArticle = articleRepository.findById(id);
        if (optArticle.isPresent()) {
            Article article = optArticle.get();
            article.setTitle(title);
            article.setContent(content);
            article.setIs_active(is_active);
            return articleRepository.saveAndFlush(article);
        }
        throw new IllegalArgumentException("Article does not exist");
    }

    @Transactional
    public Article delete(int id) throws IllegalArgumentException {
        Optional<Article> optArticle = articleRepository.findById(id);
        if (optArticle.isPresent()) {
            Article tmpArticle = optArticle.get();
            articleRepository.deleteById(id);
            articleRepository.flush();
            return tmpArticle;
        }
        throw new IllegalArgumentException();
    }
}
