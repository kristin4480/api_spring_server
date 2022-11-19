package com.kris.api_server.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.kris.api_server.models.Comment;
import com.kris.api_server.requestMappers.CreateCommentRequest;
//import com.kris.api_server.requestMappers.UpdateArticleRequest;
import com.kris.api_server.requestMappers.UpdateCommentRequest;
import com.kris.api_server.service.CommentService;



@RestController
public class CommentController {
    private CommentService commentService;
    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }


    @GetMapping(produces = "application/json", path = "/comments")
    public List<Comment> getAll(){
        return commentService.getAll();
    }

    @PostMapping(produces = "application/json", value = "/comments")
    public Comment createComment(@RequestBody @Valid CreateCommentRequest createCommentRequest){
        return commentService.create(createCommentRequest);
    }

    @PutMapping(produces = "application/json", value = "/comments/{id}")
    @ResponseBody
    public Comment updateComment(@PathVariable("id") int id, @RequestBody @Valid UpdateCommentRequest updateCommentRequest){
       try {
        return commentService.update(id, updateCommentRequest.getUser_id(), updateCommentRequest.getArticle_id(), updateCommentRequest.getContent());
       } catch(IllegalArgumentException e ) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,  "user not found");
       }
    }

    @DeleteMapping(produces = "application/json", value = "/comments/{id}")
    @ResponseBody
    public Comment deleteComment(@PathVariable("id") int id)throws IllegalArgumentException{
        try {
            return commentService.delete(id);
        } catch(IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,  "user not found");
        }
    }
}
