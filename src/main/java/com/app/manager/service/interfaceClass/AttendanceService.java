package com.app.manager.service.interfaceClass;

import com.app.manager.model.payload.request.AttendanceCheckRequest;
import com.app.manager.model.payload.request.FaceCheckClientRequest;
import com.app.manager.model.payload.response.AttendanceCheckResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;

import java.util.List;

public interface AttendanceService {
    DatabaseQueryResult studentAttendaneCheck(FaceCheckClientRequest faceCheckClientRequest,
                                      String currentUsername);
    DatabaseQueryResult teacherAttendaneCheck(List<AttendanceCheckRequest> attendanceCheckRequests,
                                              String currentUsername, String sessionId);

    List<AttendanceCheckResponse> getAttendanceResult(String sessionId);
}
