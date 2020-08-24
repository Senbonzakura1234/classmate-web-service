package com.app.manager.service.interfaceClass;

import com.app.manager.context.specification.SessionSpecification;
import com.app.manager.model.payload.SessionModel;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SessionService {
    DatabaseQueryResult save(SessionModel sessionModel);
    Optional<SessionModel> getOne(String id);
    DatabaseQueryResult update(SessionModel sessionModel, String id);
    DatabaseQueryResult delete(String id);
    Page<SessionModel> findAll(SessionSpecification sessionSpecification, Pageable pageable);
}
