package com.app.manager.service.interfaceClass;

import com.app.manager.entity.Post;
import com.app.manager.model.payload.request.PostRequest;
import com.app.manager.model.payload.response.PostResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;

import java.util.List;

public interface PostService {
    List<PostResponse> getAllByCourse(String courseId, String currentUsername);
    DatabaseQueryResult save(PostRequest postRequest,
                             String currentUsername, String courseId);
    DatabaseQueryResult edit(PostRequest postRequest,
                             String messageId, String currentUsername);
    DatabaseQueryResult updatePin(String messageId, String currentUsername);
    DatabaseQueryResult updateStatus(String messageId, String currentUsername,
                                     Post.StatusEnum status);
}
