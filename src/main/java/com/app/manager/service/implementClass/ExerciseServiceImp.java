package com.app.manager.service.implementClass;

import com.app.manager.context.repository.*;
import com.app.manager.context.specification.ExerciseSpecification;
import com.app.manager.entity.Exercise;
import com.app.manager.model.payload.CastObject;
import com.app.manager.model.payload.request.ExerciseRequest;
import com.app.manager.model.payload.request.StudentExerciseRequest;
import com.app.manager.model.payload.response.ExerciseResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import com.app.manager.service.interfaceClass.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class ExerciseServiceImp implements ExerciseService {
    @Autowired ExerciseRepository exerciseRepository;
    @Autowired UserRepository userRepository;
    @Autowired SessionRepository sessionRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired StudentCourseRepository studentCourseRepository;
    @Autowired StudentExerciseRepository studentExerciseRepository;
    @Autowired FileRepository fileRepository;
    @Autowired CastObject castObject;


    @Override
    public List<ExerciseResponse> findAll(ExerciseSpecification exerciseSpecification) {
        try {
            var exercises = exerciseRepository.findAll(exerciseSpecification);
            return exercises.stream().map(exercise -> new ExerciseResponse(exercise.getId(),
                    exercise.getSession_id(), exercise.getTitle(), exercise.getContent(),
                    exercise.isShow_answer() ? exercise.getAnswer() : "", exercise.getDuration(),
                    exercise.isShow_answer(), exercise.getStatus(), exercise.getCreated_at()))
                    .collect(Collectors.toList());
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

            exerciseRepository.save(castObject.exerciseEntity(exerciseRequest));
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
            return Optional.of(castObject.exerciseModel(exercise.get()));
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

            exerciseRepository.save(castObject.exerciseEntity(exerciseRequest));
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

    @Override
    public DatabaseQueryResult updateStatus(String id,
        Exercise.StatusEnum status, String currentUsername) {
        try {
            var teacher = userRepository.findByUsername(currentUsername);
            if(teacher.isEmpty())
                return new DatabaseQueryResult(false,
                        "Teacher not found", HttpStatus.NOT_FOUND, "");

            var exercise = exerciseRepository.findById(id);

            if(exercise.isEmpty())
                return new DatabaseQueryResult(false, "exercise not found",
                        HttpStatus.NOT_FOUND, "");

            var session = sessionRepository
                    .findById(exercise.get().getSession_id());
            if(session.isEmpty())
                return new DatabaseQueryResult(false, "session not found",
                        HttpStatus.NOT_FOUND, "");

            var course = courseRepository
                    .findById(session.get().getCourse_id());
            if(course.isEmpty())
                return new DatabaseQueryResult(false, "course not found",
                        HttpStatus.NOT_FOUND, "");
            if(course.get().getUser_id().equals(teacher.get().getId()))
                return new DatabaseQueryResult(false, "Not your course",
                        HttpStatus.BAD_REQUEST, "");

            var e = exercise.get();
            e.setStatus(status);
            exerciseRepository.save(e);
            return new DatabaseQueryResult(true,
                    "update exercise success", HttpStatus.OK,
                    castObject.exerciseModel(e));
        } catch (Exception exception) {
            exception.printStackTrace();
            System.out.println(exception.getMessage());
            return new DatabaseQueryResult(false,
                    "update exercise failed",
                    HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }

    @Override
    public DatabaseQueryResult saveStudentExercise(
            StudentExerciseRequest studentExerciseRequest,
            String currentUsername) {
        try {
            var student = userRepository.findByUsername(currentUsername);
            if(student.isEmpty())
                return new DatabaseQueryResult(false, "student not found",
                        HttpStatus.NOT_FOUND, studentExerciseRequest);

            var exercise = exerciseRepository
                    .findById(studentExerciseRequest.getExercise_id());
            if(exercise.isEmpty())
                return new DatabaseQueryResult(false, "exercise not found",
                        HttpStatus.NOT_FOUND, studentExerciseRequest);
            if(exercise.get().getStatus() != Exercise.StatusEnum.ONGOING)
                return new DatabaseQueryResult(false, "exercise ended",
                        HttpStatus.BAD_REQUEST, studentExerciseRequest);

            var session = sessionRepository
                    .findById(exercise.get().getSession_id());
            if(session.isEmpty())
                return new DatabaseQueryResult(false, "session not found",
                        HttpStatus.NOT_FOUND, studentExerciseRequest);

            var course = courseRepository
                    .findById(session.get().getCourse_id());
            if(course.isEmpty())
                return new DatabaseQueryResult(false, "course not found",
                        HttpStatus.NOT_FOUND, studentExerciseRequest);
            if(studentCourseRepository.findFirstByCourse_idAndUser_id(
                    course.get().getId(), student.get().getId()).isEmpty())
                return new DatabaseQueryResult(false,
                        "you are not in this course",
                        HttpStatus.BAD_REQUEST, studentExerciseRequest);
            var studentExercise = castObject.studentExerciseEntity(
                    student.get().getId(), studentExerciseRequest);
            studentExerciseRepository.save(studentExercise);
            if(!studentExerciseRequest.getFileRequests().isEmpty()){
                fileRepository.saveAll(studentExerciseRequest.getFileRequests()
                        .stream().map(fileRequest -> castObject
                                .fileEntity(studentExercise.getId(), fileRequest))
                        .collect(Collectors.toList()));
            }
            return new DatabaseQueryResult(true,
                    "Post Student Exercise success",
                    HttpStatus.OK, studentExerciseRequest);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new DatabaseQueryResult(false,
                    "Post Student Exercise failed",
                    HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }
}
