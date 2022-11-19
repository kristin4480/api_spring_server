package com.kris.api_server.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity(name = "article")
public class Article {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    

    @Column
    private String title;
  
    @Column
    private String content;
   
    @Column
    private boolean is_active;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonBackReference
    private List<User> user; 

    @OneToMany(mappedBy = "article_id", fetch = FetchType.LAZY)
    @JsonManagedReference
    @Where(clause = "parent_id is null")
    private List<Comment> comment;
    
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Article(String title, String content, Boolean is_active){
        this.title = title;
        this.content = content;
        this.is_active = is_active;
    }

    public Article(){
        
    }
    
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
    
    public boolean isIs_active() {
        return is_active;
    }
    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public List<User> getUser() {
        return user;
    }
    public void setUser(List<User> user) {
        this.user = user;
    }

    public List<Comment> getComment() {
        return comment;
    }
    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }
}
