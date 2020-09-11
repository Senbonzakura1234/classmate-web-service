package com.app.manager.service.interfaceClass;

import com.app.manager.context.specification.SessionSpecification;
import com.app.manager.entity.Session;
import com.app.manager.model.payload.request.SessionRequest;
import com.app.manager.model.payload.response.SessionResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;

import java.util.List;
import java.util.Optional;

public interface SessionService {
    List<SessionResponse> findAll(SessionSpecification sessionSpecification);
    DatabaseQueryResult save(SessionRequest sessionRequest, String currentUsername);
    Optional<SessionResponse> getOne(String id, String currentUsername);
    DatabaseQueryResult update(SessionRequest sessionRequest, String id, String currentUsername);
    DatabaseQueryResult updateStatus(String id, Session.StatusEnum status, String currentUsername);
    DatabaseQueryResult startAttendanceCheck(String id, String currentUsername, boolean adminAuthority);
    DatabaseQueryResult closeAttendanceCheck(String id, String currentUsername, boolean adminAuthority);
}
