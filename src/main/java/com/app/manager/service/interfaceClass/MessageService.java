package com.app.manager.service.interfaceClass;

import com.app.manager.entity.Message;
import com.app.manager.model.payload.request.CourseMessageRequest;
import com.app.manager.model.payload.response.CourseMessageResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;

import java.util.List;

public interface MessageService {
    List<CourseMessageResponse> getAllByCourse(String courseId, String currentUsername);
    DatabaseQueryResult saveMessage(CourseMessageRequest courseMessageRequest,
                                    String currentUsername, String courseId);
    DatabaseQueryResult editMessage(CourseMessageRequest courseMessageRequest,
                                    String messageId, String currentUsername);
    DatabaseQueryResult updatePinnedMessage(String messageId, String currentUsername);
    DatabaseQueryResult updateStatusMessage(String messageId, String currentUsername,
                                            Message.StatusEnum status);
}
