package com.kris.api_server.requestMappers;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class UpdateArticleRequest {
    

    @NotBlank(message = "title cannot be empty")
    private String title;
  
    @NotBlank(message = "content cannot be empty")
    private String content;
   
    @NotNull(message = "this field cannot be empty")
    private boolean is_active;

    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }


    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    
    public boolean getIs_active() {
        return is_active;
    }
    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }
}
