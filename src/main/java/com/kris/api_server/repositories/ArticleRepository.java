package com.kris.api_server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import com.kris.api_server.models.Article;


@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer>  {
   

    //@Query(value = "Select * From article Where title Like '_s%'", nativeQuery = true)
    @Query("Select a From article a  Where a.title Like '_s%'")
    List<Article> findAllTitles();
// List<Article> findByOrderByTitle();

}
