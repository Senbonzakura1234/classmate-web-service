package com.app.manager.service.interfaceClass;

import com.app.manager.entity.Session;
import com.app.manager.model.payload.CourseModel;
import com.app.manager.model.payload.SessionModel;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SessionService {
    Page<SessionModel> getAll(String queryName, Session.StatusEnum status, Pageable pageable);
    DatabaseQueryResult save(SessionModel sessionModel);
    Optional<SessionModel> getOne(String id);
    DatabaseQueryResult update(SessionModel sessionModel, String id);
    DatabaseQueryResult delete(String id);
}
