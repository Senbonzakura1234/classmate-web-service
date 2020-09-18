package com.app.manager.service.interfaceClass;

import com.app.manager.entity.Post;
import com.app.manager.model.payload.request.PostRequest;
import com.app.manager.model.payload.response.PostResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;

import java.util.List;
import java.util.Optional;

public interface PostService {
    List<PostResponse> getAllByCourse(String courseId, String currentUsername);
    Optional<PostResponse> getOne(String postId, String currentUsername);
    DatabaseQueryResult save(PostRequest postRequest,
                             String currentUsername, String courseId);
    DatabaseQueryResult edit(PostRequest postRequest,
                             String postId, String currentUsername);
    DatabaseQueryResult updatePin(String postId, String currentUsername);
    DatabaseQueryResult delete(String postId, String currentUsername);
    DatabaseQueryResult updateStatus(String postId, Post.StatusEnum status);
}
