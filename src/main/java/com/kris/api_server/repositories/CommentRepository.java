package com.kris.api_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kris.api_server.models.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    
}
