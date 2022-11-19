package com.kris.api_server.requestMappers;

import javax.validation.constraints.NotBlank;

public class CreateCommentRequest {
    
    private int user_id;
    
    private int article_id;
    
    @NotBlank(message = "content cannot be empty")
    private String content;

   
    public int getUser_id() {
        return user_id;
    }
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getArticle_id() {
        return article_id;
    }
    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }
    
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

}
