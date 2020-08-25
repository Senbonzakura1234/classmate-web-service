package com.app.manager.service.implementClass;

import com.app.manager.context.repository.CourseRepository;
import com.app.manager.context.repository.UserRepository;
import com.app.manager.context.specification.SessionSpecification;
import com.app.manager.entity.Course;
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

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    UserRepository userRepository;

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    CourseRepository courseRepository;

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
    public DatabaseQueryResult update(SessionModel sessionModel,
                                      String id, String currentUsername) {
        try {
            var teacher = userRepository.findByUsername(currentUsername);
            if(teacher.isEmpty())
                return new DatabaseQueryResult(false, "Teacher not found",
                        HttpStatus.NOT_FOUND, "");

            var s = sessionRepository.findById(id);
            if(s.isEmpty()){
                return new DatabaseQueryResult(false,
                        "save course failed", HttpStatus.NOT_FOUND, "");
            }

            var course = courseRepository
                    .findById(s.get().getCourseid())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            if(!course.getUserid().equals(teacher.get().getId()))
                return new DatabaseQueryResult(false, "Not your course",
                        HttpStatus.BAD_REQUEST, "");

            if(course.getStatus() != Course.StatusEnum.ONGOING)
                return new DatabaseQueryResult(false, "Course is not ongoing",
                        HttpStatus.BAD_REQUEST, "");

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
    public DatabaseQueryResult delete(String id, String currentUsername) {
        try {
            var teacher = userRepository.findByUsername(currentUsername);
            if(teacher.isEmpty())
                return new DatabaseQueryResult(false, "Teacher not found",
                        HttpStatus.NOT_FOUND, "");

            var session = sessionRepository.findById(id);
            if(session.isEmpty()){
                return new DatabaseQueryResult(false,
                        "delete course failed", HttpStatus.NOT_FOUND, "");
            }

            var course = courseRepository
                    .findById(session.get().getCourseid())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            if(!course.getUserid().equals(teacher.get().getId()))
                return new DatabaseQueryResult(false, "Not your course",
                        HttpStatus.BAD_REQUEST, "");

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
    public Page<SessionModel> findAll(SessionSpecification sessionSpecification,
                                      Pageable pageable) {
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
