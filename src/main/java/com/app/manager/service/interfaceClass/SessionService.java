package com.app.manager.service.interfaceClass;

import com.app.manager.context.specification.SessionSpecification;
import com.app.manager.model.payload.SessionModel;
import com.app.manager.model.returnResult.DatabaseQueryResult;

import java.util.List;
import java.util.Optional;

public interface SessionService {
    DatabaseQueryResult save(SessionModel sessionModel);
    Optional<SessionModel> getOne(String id);
    DatabaseQueryResult update(SessionModel sessionModel, String id, String currentUsername);
    DatabaseQueryResult delete(String id, String currentUsername);
    List<SessionModel> findAll(SessionSpecification sessionSpecification);
}
