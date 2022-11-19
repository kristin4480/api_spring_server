package com.kris.api_server.requestMappers;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.kris.api_server.models.User;

public class CreateArticleRequest {
    
    
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


    private List<Integer> user_id;
    
    //this is a getter
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
    public List<Integer> getUser_id() {
        return user_id;
    }
    public void setUser_id(List<Integer> user_id) {
        this.user_id = user_id;
    }
}

