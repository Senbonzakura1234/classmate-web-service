package com.app.manager.service.implementClass;

import com.app.manager.context.repository.CourseRepository;
import com.app.manager.context.repository.ExerciseRepository;
import com.app.manager.context.repository.SessionRepository;
import com.app.manager.context.repository.UserRepository;
import com.app.manager.context.specification.ExerciseSpecification;
import com.app.manager.model.payload.request.ExerciseRequest;
import com.app.manager.model.payload.response.ExerciseResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import com.app.manager.service.interfaceClass.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class ExerciseServiceImp implements ExerciseService {
    @Autowired ExerciseRepository exerciseRepository;
    @Autowired UserRepository userRepository;
    @Autowired SessionRepository sessionRepository;
    @Autowired CourseRepository courseRepository;


    @Override
    public List<ExerciseResponse> findAll(ExerciseSpecification exerciseSpecification) {
        try {
            var exercises = exerciseRepository.findAll(exerciseSpecification);
            var list = new ArrayList<ExerciseResponse>();
            exercises.forEach(exercise -> list.add(new ExerciseResponse(exercise.getId(),
                    exercise.getSession_id(), exercise.getTitle(), exercise.getContent(),
                    exercise.isShow_answer()? exercise.getAnswer() : "", exercise.getDuration(),
                    exercise.isShow_answer(), exercise.getStatus(), exercise.getCreated_at())));
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public DatabaseQueryResult save(ExerciseRequest exerciseRequest, String currentUsername) {
        try {
            var teacher = userRepository.findByUsername(currentUsername);
            if(teacher.isEmpty())
                return new DatabaseQueryResult(false,
                        "Teacher not found", HttpStatus.NOT_FOUND, exerciseRequest);
            var session = sessionRepository.findById(exerciseRequest.getSession_id());
            if(session.isEmpty())
                return new DatabaseQueryResult(false, "Session not found",
                        HttpStatus.NOT_FOUND, exerciseRequest);
            var course = courseRepository.findById(session.get().getCourse_id());
            if(course.isEmpty())
            return new DatabaseQueryResult(false, "Course not found",
                    HttpStatus.NOT_FOUND, exerciseRequest);
            if(course.get().getUser_id().equals(teacher.get().getId()))
                return new DatabaseQueryResult(false, "Not your course",
                        HttpStatus.BAD_REQUEST, exerciseRequest);

            exerciseRepository.save(ExerciseRequest.castToEntity(exerciseRequest));
            return new DatabaseQueryResult(true, "save exercise success",
                    HttpStatus.OK, exerciseRequest);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new DatabaseQueryResult(false,
                    "save exercise failed",
                    HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }

    @Override
    public Optional<ExerciseResponse> getOne(String id) {
        try {
            var exercise = exerciseRepository.findById(id);
            if(exercise.isEmpty()) return Optional.empty();
            return Optional.of(ExerciseResponse.castToObjectModel(exercise.get()));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public DatabaseQueryResult update(ExerciseRequest exerciseRequest,
                                      String id, String currentUsername) {
        try {
            var teacher = userRepository.findByUsername(currentUsername);
            if(teacher.isEmpty())
                return new DatabaseQueryResult(false,
                        "Teacher not found", HttpStatus.NOT_FOUND, exerciseRequest);

            var exercise = exerciseRepository.findById(id);

            if(exercise.isEmpty())
                return new DatabaseQueryResult(false, "exercise not found",
                        HttpStatus.NOT_FOUND, exerciseRequest);

            var session = sessionRepository.findById(exercise.get().getSession_id());
            if(session.isEmpty())
                return new DatabaseQueryResult(false, "session not found",
                        HttpStatus.NOT_FOUND, exerciseRequest);

            var course = courseRepository.findById(session.get().getCourse_id());
            if(course.isEmpty())
                return new DatabaseQueryResult(false, "course not found",
                        HttpStatus.NOT_FOUND, exerciseRequest);
            if(course.get().getUser_id().equals(teacher.get().getId()))
                return new DatabaseQueryResult(false, "Not your course",
                        HttpStatus.BAD_REQUEST, exerciseRequest);

            exerciseRepository.save(ExerciseRequest.castToEntity(exerciseRequest));
            return new DatabaseQueryResult(true, "update exercise success",
                    HttpStatus.OK, exerciseRequest);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new DatabaseQueryResult(false,
                    "update exercise failed",
                    HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }
}
