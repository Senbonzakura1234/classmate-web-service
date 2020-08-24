package com.app.manager.service.implementClass;

import com.app.manager.entity.Session;
import com.app.manager.model.payload.SessionModel;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import com.app.manager.context.repository.SessionRepository;
import com.app.manager.service.interfaceClass.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionServiceImp implements SessionService {
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    SessionRepository sessionRepository;

    @Override
    public Page<SessionModel> getAll(String queryName, Session.StatusEnum status, Pageable pageable) {
        try {
            Page<Session> sessions;
            if(queryName != null && !queryName.isEmpty()){
                if(status != null){
                    sessions = sessionRepository.findByNameContains(queryName, pageable);
                }else {
                    sessions = sessionRepository.findByNameContains(queryName, pageable);
                }
            }else {
                if(status != null){
                    sessions = sessionRepository.findByStatus(status, pageable);
                }else {
                    sessions = sessionRepository.findBy(pageable);
                }
            }
            return sessions.map(session -> new SessionModel(session.getId(),
                    session.getCourseId(), session.getUserId(),
                    session.getName(), session.getStarttime(), session.getAttendanceduration(),
                    session.isAttendancechecked(), session.getStatus(), session.getCreatedat()));
        } catch (Exception e) {
            e.printStackTrace();
            return Page.empty();
        }
    }

    @Override
    public DatabaseQueryResult save(SessionModel sessionModel) {
        try {
            sessionRepository.save(SessionModel.castToEntity(sessionModel));
            return new DatabaseQueryResult(true,
                    "save course success");
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseQueryResult(false,
                    "save course failed");
        }
    }

    @Override
    public Optional<SessionModel> getOne(String id) {
        try {
            var session = sessionRepository.findById(id);
            if(session.isEmpty()){
                return Optional.empty();
            }
            return Optional.of(SessionModel.castToObjectModel(session.get()));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public DatabaseQueryResult update(SessionModel sessionModel, String id) {
        try {
            var s = sessionRepository.findById(id);
            if(s.isEmpty()){
                return new DatabaseQueryResult(false,
                        "save course failed");
            }

            var session  = s.get();
            session.setCreatedat(sessionModel.getCreatedat());
            session.setAttendancechecked(sessionModel.isAttendancechecked());
            session.setAttendanceduration(sessionModel.getAttendanceduration());
            session.setName(sessionModel.getName());
            session.setStarttime(sessionModel.getStarttime());
            session.setStatus(sessionModel.getStatus());
            session.setUserId(sessionModel.getUserId());
            session.setCourseId(sessionModel.getCourseId());

            return new DatabaseQueryResult(true,
                    "save course success");
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseQueryResult(false,
                    "save course failed");
        }
    }

    @Override
    public DatabaseQueryResult delete(String id) {
        try {
            var session = sessionRepository.findById(id);
            if(session.isEmpty()){
                return new DatabaseQueryResult(false,
                        "delete course failed");
            }
            sessionRepository.delete(session.get());
            return new DatabaseQueryResult(true,
                    "delete course success");
        }catch (Exception e){
            e.printStackTrace();
            return new DatabaseQueryResult(false,
                    "save course failed");
        }
    }
}
