package com.kris.api_server.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kris.api_server.models.Comment;
import com.kris.api_server.repositories.CommentRepository;
//import com.kris.api_server.requestMappers.CreateArticleRequest;
//import com.kris.api_server.requestMappers.CreateCommentRequest;
import com.kris.api_server.requestMappers.CreateCommentRequest;

@Service
public class CommentService {
    
    private final CommentRepository commentRepository;
    @Autowired
    public CommentService(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    public List<Comment> getAll(){
        return commentRepository.findAll();
    }

    @Transactional
    public Comment create(CreateCommentRequest createCommentRequest){
        Comment comment = new Comment(createCommentRequest.getUser_id(), createCommentRequest.getArticle_id(),createCommentRequest.getContent());
        return commentRepository.saveAndFlush(comment);
    }

    @Transactional
    public Comment update(int id, int user_id, int article_id, String content)throws IllegalArgumentException{
        Optional<Comment> optComment = commentRepository.findById(id);
        if(optComment.isPresent()){
        Comment comment = optComment.get();
        comment.setId(id);
        comment.setUser_id(user_id);
        comment.setArticle_id(article_id);
        comment.setContent(content);
        return commentRepository.saveAndFlush(comment);
        }
        throw new IllegalArgumentException("Comment does not exist");
    }

    @Transactional
    public Comment delete(int id)throws IllegalArgumentException{
        Optional<Comment> optComment =  commentRepository.findById(id);
        if(optComment.isPresent()){
            Comment tmpComment = optComment.get();
            commentRepository.deleteById(id);
            commentRepository.flush();
            return tmpComment;
        }
        throw new IllegalArgumentException();
    }
}
