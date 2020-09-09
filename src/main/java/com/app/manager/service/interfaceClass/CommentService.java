package com.app.manager.service.interfaceClass;

import com.app.manager.entity.Comment;
import com.app.manager.model.payload.request.CommentRequest;
import com.app.manager.model.payload.response.CommentResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;

import java.util.List;

public interface CommentService {
    List<CommentResponse> getAllByPost(String postId, String currentUsername);
    DatabaseQueryResult save(CommentRequest commentRequest,
                             String currentUsername, String postId);
    DatabaseQueryResult edit(CommentRequest commentRequest,
                             String commentId, String currentUsername);
    DatabaseQueryResult updatePin(String commentId, String currentUsername);
    DatabaseQueryResult delete(String commentId, String currentUsername);
    DatabaseQueryResult updateStatus(String commentId, Comment.StatusEnum status);
}
