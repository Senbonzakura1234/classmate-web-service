package com.app.manager.service.implementClass;

import com.app.manager.context.repository.*;
import com.app.manager.entity.*;
import com.app.manager.model.payload.CastObject;
import com.app.manager.model.payload.request.MarkExerciseRequest;
import com.app.manager.model.payload.request.StudentExerciseRequest;
import com.app.manager.model.payload.response.StudentExerciseResponse;
import com.app.manager.model.returnResult.DatabaseQueryResult;
import com.app.manager.service.interfaceClass.StudentExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class StudentExerciseServiceImp implements StudentExerciseService {
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

    @Override
    public List<StudentExerciseResponse> getAllStudentExercise(String exerciseId, String currentUsername) {
        try {
            var currentUser = userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
            var exercise = exerciseRepository.findById(exerciseId)
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));

            var studentExercises = studentExerciseRepository
                    .findAllByExercise_idAndStatus(exerciseId, StudentExercise.StatusEnum.SHOW);

            var role = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("role not found"));

            if (role.getStatus() != Role.StatusEnum.HIDE && currentUser.getRoles().contains(role)) {
                return studentExercises.stream().map(studentExercise -> {
                    var files = fileRepository.findAllByStudentexercise_idAndStatus(
                            studentExercise.getId(),
                            File.StatusEnum.SHOW).stream().map(file -> castObject.fileModel(file))
                            .collect(Collectors.toList());
                    return castObject.studentExerciseModel(studentExercise, files);
                }).collect(Collectors.toList());
            }

            var session = sessionRepository.findById(exercise.getSession_id())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
            var course = courseRepository.findById(session.getCourse_id())
                    .orElseThrow(() -> new RuntimeException("Teacher not found"));
            if(currentUser.getId().equals(course.getUser_id()))
                return studentExercises.stream().map(studentExercise -> {
                    var files = fileRepository.findAllByStudentexercise_idAndStatus(
                            studentExercise.getId(),
                            File.StatusEnum.SHOW).stream().map(file -> castObject.fileModel(file))
                            .collect(Collectors.toList());
                    return castObject.studentExerciseModel(studentExercise, files);
                }).collect(Collectors.toList());

            return studentExercises.stream().map(studentExercise ->
                    castObject.studentExerciseModelPublic(studentExercise)).collect(Collectors.toList());
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<StudentExerciseResponse> getStudentExercise(String id, String currentUsername) {
        try {
            var currentUser = userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            var studentExercise = studentExerciseRepository
                    .findById(id).orElseThrow(() -> new RuntimeException("Student Exercise not found"));
            if(studentExercise.getStatus() == StudentExercise.StatusEnum.HIDE)
                return Optional.empty();
            var files = fileRepository.findAllByStudentexercise_idAndStatus(
                    studentExercise.getId(),
                    File.StatusEnum.SHOW).stream().map(file -> castObject.fileModel(file))
                    .collect(Collectors.toList());

            var role = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("role not found"));

            if (role.getStatus() != Role.StatusEnum.HIDE && currentUser.getRoles().contains(role))
                return Optional.of(castObject.studentExerciseModel(studentExercise, files));

            if(studentExercise.getUser_id().equals(currentUser.getId()))
                return Optional.of(castObject.studentExerciseModel(studentExercise, files));

            var exercise = exerciseRepository.findById(studentExercise.getExercise_id())
                    .orElseThrow(() -> new RuntimeException("Exercise not found"));
            var session = sessionRepository.findById(exercise.getSession_id())
                    .orElseThrow(() -> new RuntimeException("Session not found"));
            var course = courseRepository.findById(session.getCourse_id())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            if(course.getUser_id().equals(currentUser.getId()))
                return Optional.of(castObject.studentExerciseModel(studentExercise, files));

            return Optional.of(castObject.studentExerciseModelPublic(studentExercise));
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public DatabaseQueryResult markExcercise(String studentExerciseId, String currentUsername,
                                             MarkExerciseRequest markExerciseRequest) {
        try {
            var studentExercise = studentExerciseRepository
                    .findById(studentExerciseId);

            if(studentExercise.isEmpty() || studentExercise
                    .get().getStatus() == StudentExercise.StatusEnum.HIDE)
                return new DatabaseQueryResult(false, "student exercise not found",
                        HttpStatus.NOT_FOUND, markExerciseRequest);

            var exercise = exerciseRepository
                    .findById(studentExercise.get().getExercise_id());
            if(exercise.isEmpty())
                return new DatabaseQueryResult(false,
                        "exercise not found", HttpStatus.NOT_FOUND, markExerciseRequest);

            var session = sessionRepository
                    .findById(exercise.get().getSession_id());
            if(session.isEmpty())
                return new DatabaseQueryResult(false,
                        "session not found", HttpStatus.NOT_FOUND, markExerciseRequest);

            var course = courseRepository
                    .findById(session.get().getCourse_id());
            if(course.isEmpty())
                return new DatabaseQueryResult(false,
                        "course not found", HttpStatus.NOT_FOUND, markExerciseRequest);

            var teacher = userRepository.findByUsername(currentUsername);
            if(teacher.isEmpty())
                return new DatabaseQueryResult(false,
                        "Teacher not found", HttpStatus.NOT_FOUND, markExerciseRequest);

            if(course.get().getUser_id().equals(teacher.get().getId()))
                return new DatabaseQueryResult(false,
                        "not your course", HttpStatus.BAD_REQUEST, markExerciseRequest);

            var se = studentExercise.get();
            se.setTeacher_message(markExerciseRequest.getTeacher_message());
            se.setMark(markExerciseRequest.getMark());
            se.setMarked(true);
            studentExerciseRepository.save(se);
            return new DatabaseQueryResult(true,
                    "mark exercise success", HttpStatus.OK, markExerciseRequest);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return new DatabaseQueryResult(false,
                    "Error: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }
}
