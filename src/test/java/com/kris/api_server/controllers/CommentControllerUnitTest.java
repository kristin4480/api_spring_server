package com.kris.api_server.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.web.server.ResponseStatusException;

import com.kris.api_server.models.Comment;
import com.kris.api_server.requestMappers.UpdateCommentRequest;
import com.kris.api_server.service.CommentService;

@ExtendWith(MockitoExtension.class)
public class CommentControllerUnitTest {

    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentService commentService;

    @Test
    public void updateComment_SuccessfullUpdateComment_UpdatedComment() {

       // Comment unitTestComment = new Comment(1, 1, "Shadow hackers");
        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest();

        updateCommentRequest.setId(2);
        updateCommentRequest.setArticle_id(2);
        updateCommentRequest.setUser_id(2);
        updateCommentRequest.setContent("Terminator Genesys");

        when(commentService.update(anyInt(), anyInt(), anyInt(), anyString())).thenAnswer(new Answer<Comment>() {

            @Override
            public Comment answer(InvocationOnMock invocation) throws Throwable {
                // TODO Auto-generated method stub

                Comment comment = new Comment(invocation.getArgument(1),invocation.getArgument(2),invocation.getArgument(3));
                comment.setId(invocation.getArgument(0));
                return comment;
            }

        });
    
            Comment comment = commentController.updateComment(2, updateCommentRequest);
            assertEquals(2, comment.getId()); 
            assertEquals("Terminator Genesys", comment.getContent());
    }
}
