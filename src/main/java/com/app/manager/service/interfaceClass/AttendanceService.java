package com.app.manager.service.interfaceClass;

import com.app.manager.model.payload.request.FaceCheckClientRequest;
import com.app.manager.model.returnResult.DatabaseQueryResult;

public interface AttendanceService {
    DatabaseQueryResult studentAttendaneCheck(FaceCheckClientRequest faceCheckClientRequest,
                                      String currentUsername);
}
