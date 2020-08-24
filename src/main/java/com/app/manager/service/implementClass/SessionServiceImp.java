package com.app.manager.service.implementClass;

import com.app.manager.context.specification.SessionSpecification;
import com.app.manager.entity.Session;
import com.app.manager.model.payload.SessionModel;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import com.app.manager.context.repository.SessionRepository;
import com.app.manager.service.interfaceClass.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SessionServiceImp implements SessionService {
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    SessionRepository sessionRepository;


    @Override
    public DatabaseQueryResult save(SessionModel sessionModel) {
        try {
            sessionRepository.save(SessionModel.castToEntity(sessionModel));
            return new DatabaseQueryResult(true,
                    "save course success", HttpStatus.OK, sessionModel);
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseQueryResult(false,
                    "save course failed", HttpStatus.INTERNAL_SERVER_ERROR, "");
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
                        "save course failed", HttpStatus.NOT_FOUND, "");
            }

            var session  = s.get();
            session.setCreatedat(sessionModel.getCreatedat());
            session.setAttendancechecked(sessionModel.isAttendancechecked());
            session.setAttendanceduration(sessionModel.getAttendanceduration());
            session.setName(sessionModel.getName());
            session.setStarttime(sessionModel.getStarttime());
            session.setStatus(sessionModel.getStatus());
            session.setUserid(sessionModel.getUserId());
            session.setCourseid(sessionModel.getCourseId());

            return new DatabaseQueryResult(true,
                    "save course success", HttpStatus.OK, sessionModel);
        } catch (Exception e) {
            e.printStackTrace();
            return new DatabaseQueryResult(false,
                    "save course failed", HttpStatus.INTERNAL_SERVER_ERROR, sessionModel);
        }
    }

    @Override
    public DatabaseQueryResult delete(String id) {
        try {
            var session = sessionRepository.findById(id);
            if(session.isEmpty()){
                return new DatabaseQueryResult(false,
                        "delete course failed", HttpStatus.NOT_FOUND, "");
            }
            sessionRepository.delete(session.get());
            return new DatabaseQueryResult(true,
                    "delete course success", HttpStatus.OK, "");
        }catch (Exception e){
            e.printStackTrace();
            return new DatabaseQueryResult(false,
                    "save course failed", HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }

    @Override
    public Page<SessionModel> findAll(SessionSpecification sessionSpecification, Pageable pageable) {
        try {
            Page<Session> sessions = sessionRepository.findAll(sessionSpecification, pageable);
            return sessions.map(session -> new SessionModel(session.getId(),
                    session.getCourseid(), session.getUserid(),
                    session.getName(), session.getStarttime(), session.getAttendanceduration(),
                    session.isAttendancechecked(), session.getStatus(), session.getCreatedat()));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return Page.empty();
        }
    }
}
