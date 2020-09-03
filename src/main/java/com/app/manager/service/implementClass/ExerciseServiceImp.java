package com.app.manager.service.implementClass;

import com.app.manager.context.repository.*;
import com.app.manager.context.specification.ExerciseSpecification;
import com.app.manager.entity.*;
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
    @Autowired RoleRepository roleRepository;
    @Autowired CastObject castObject;


    @Override
    public List<ExerciseResponse> findAll(ExerciseSpecification exerciseSpecification) {
        try {
            var exercises = exerciseRepository.findAll(exerciseSpecification);
            return exercises.stream().map(exercise ->
                    castObject.exerciseModel(exercise))
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
    public Optional<ExerciseResponse> getOne(String id, String currentUsername) {
        try {
            var currentUser = userRepository
                    .findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("user not found"));
            var role = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("role not found"));
            if(role.getStatus() == Role.StatusEnum.HIDE) return Optional.empty();


            var exercise = exerciseRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("exercise not found"));

            var session = sessionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("session not found"));

            if (currentUser.getRoles().contains(role))
                return Optional.of(castObject.exerciseModel(exercise));

            var course = courseRepository.findById(session.getCourse_id())
                    .orElseThrow(() -> new RuntimeException("course not found"));
            if(course.getUser_id().equals(currentUser.getId()))
                return Optional.of(castObject.exerciseModel(exercise));

            var listStudents = studentCourseRepository
                    .findAllByCourse_idAndStatus(course.getId(), StudentCourse.StatusEnum.SHOW);
            if (listStudents.stream().anyMatch(studentCourse ->
                    studentCourse.getUser_id().equals(currentUser.getId())))
                return Optional.of(castObject.exerciseModel(exercise));
            return Optional.of(castObject.exerciseModelPublic(exercise));
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
            if(status == Exercise.StatusEnum.ALL)
                return new DatabaseQueryResult(false,
                        "Validation error", HttpStatus.BAD_REQUEST, "");

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
            if (status == Exercise.StatusEnum.ONGOING)
                e.setExercise_start_time(System.currentTimeMillis());
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
            String currentUsername, String id) {
        try {
            var student = userRepository.findByUsername(currentUsername);
            if(student.isEmpty())
                return new DatabaseQueryResult(false, "student not found",
                        HttpStatus.NOT_FOUND, studentExerciseRequest);

            var exercise = exerciseRepository.findById(id);
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

            var oldStudentExercise = studentExerciseRepository
                    .findFirstByUser_idAndExercise_id(student.get().getId(),
                            exercise.get().getId());

            if(oldStudentExercise.isPresent()){
                try {
                    var s = oldStudentExercise.get();
                    s.setStatus(StudentExercise.StatusEnum.HIDE);
                    studentExerciseRepository.save(s);
                    var oldFiles = fileRepository
                            .findAllByStudentexercise_idAndStatus(
                                    s.getId(), File.StatusEnum.SHOW);
                    if(!oldFiles.isEmpty()){
                        oldFiles.forEach(file -> {
                            try {
                                file.setStatus(File.StatusEnum.HIDE);
                                fileRepository.save(file);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }

            var studentExercise = castObject.studentExerciseEntity(
                    student.get().getId(), id, studentExerciseRequest);
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
