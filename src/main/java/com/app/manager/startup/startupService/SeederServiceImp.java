package com.app.manager.startup.startupService;

import com.app.manager.context.repository.*;
import com.app.manager.entity.*;
import com.app.manager.model.payload.request.ExerciseRequest;
import com.app.manager.model.payload.request.FileRequest;
import com.app.manager.model.payload.request.StudentExerciseRequest;
import com.app.manager.model.returnResult.MigrationQueryResult;
import com.app.manager.model.seeder.SeederData;
import com.app.manager.service.interfaceClass.ExerciseService;
import com.app.manager.service.interfaceClass.StudentExerciseService;
import com.app.manager.service.interfaceClass.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class SeederServiceImp implements SeederService {
    @Autowired SeederData seederData;
    @Autowired RoleRepository roleRepository;
    @Autowired CourseCategoryRepository coursecategoryRepository;
    @Autowired UserRepository userRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired StudentCourseRepository studentCourseRepository;
    @Autowired SessionRepository sessionRepository;
    @Autowired UserService userService;
    @Autowired ExerciseService exerciseService;
    @Autowired ExerciseRepository exerciseRepository;
    @Autowired StudentExerciseService studentExerciseService;
    @Autowired PostRepository postRepository;
    @Autowired AttachmentRepository attachmentRepository;
    @Autowired HistoryRepository historyRepository;

    private static final Logger logger = LoggerFactory.getLogger(SeederServiceImp.class);

    @Override
    public MigrationQueryResult checkMigrationHistory() {
        try {
            var history = historyRepository.findAll();
            if(history.isEmpty())
                return new MigrationQueryResult(History.EMigration.SEEDABLE,
                    "history not found", HttpStatus.NOT_FOUND,
                    "");

            return new MigrationQueryResult(history.stream()
                    .findFirst().get().getStatus(), "History found",
                    HttpStatus.OK, "");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());

            return new MigrationQueryResult
                    (History.EMigration.UNSEEDABLE,
                    "Error",
                    HttpStatus.INTERNAL_SERVER_ERROR, "");
        }
    }

    @Override
    public void generateRoles() {
        for(var eRole : ERole.values()){
            try {
                if(eRole == ERole.ALL) continue;
                var role = roleRepository.findByName(eRole);
                if (role.isEmpty()) {
                    var newRole = new Role();
                    newRole.setName(eRole);
                    roleRepository.save(newRole);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("Can create role: " + eRole.getName());
                logger.info("Reason: " + e.getMessage());
                logger.info("Cause by: " + e.getCause().toString());
            }
        }
    }

    @Override
    public void generateCategory() {
        for (var name: seederData.getCourseCategoryNames()
        ) {
            try {
                var category = coursecategoryRepository.findFirstByName(name);
                if(category.isEmpty()){
                    var newCategory = new CourseCategory();
                    newCategory.setName(name);
                    newCategory.setDescription(name);
                    coursecategoryRepository.save(newCategory);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("Can create role: " + name);
                logger.info("Reason: " + e.getMessage());
                logger.info("Cause by: " + e.getCause().toString());
            }
        }
    }

    @Override
    public void generateUser() {
        seederData.getUserSeeds().forEach(signupRequest -> {
            var checkUsername = userService.checkExistUsername(signupRequest.getUsername());
            if (checkUsername.isEmpty() || checkUsername.get()) return;
            var checkEmail = userService.checkExistEmail(signupRequest.getEmail());
            if (checkEmail.isEmpty() || checkEmail.get()) return;
            var user = new User(signupRequest.getUsername(),
                    signupRequest.getEmail(),
                    signupRequest.getPassword());
            logger.info("Seed user " + signupRequest.getUsername());
            logger.info(userService.saveUser(user, signupRequest.getRole())
                    .getDescription());
        });
    }

    @Override
    public void generateCourse() {
        var categories = seederData.getCourseCategoryNames()
                .stream().filter(s -> !s.equals("Undefine")).collect(Collectors.toList());
        var teachers= seederData.getUserSeeds()
                .stream().filter(signupRequest -> signupRequest.getRole()
                        .contains(ERole.ROLE_TEACHER.getName())).collect(Collectors.toList());
        IntStream.range(0, 10).forEach(i -> {
            try {

                logger.info("Seeding Course Name " + i);
                var categoryName = categories.get(i);
                var teacherName = teachers.get(i).getUsername();

                var category = coursecategoryRepository.findFirstByName(categoryName);
                if (category.isEmpty()) return;

                var teacher = userRepository.findByUsername(teacherName);
                if (teacher.isEmpty()) return;

                var course = new Course();
                course.setName("Course Name " + i);
                var checkCourse = courseRepository.findFirstByName("Course Name " + i);
                if (checkCourse.isPresent()) return;

                course.setCover_file_id("10ni2kUeQARrDchDCLqcFAuw1yyRJYCOj");
                course.setDescription("Teacher: " + teacherName + ", Category: " + categoryName);
                course.setCoursecategory_id(category.get().getId());
                course.setUser_id(teacher.get().getId());

                if(i < 3){
                    course.setStatus(Course.StatusEnum.ONGOING);
                    course.setStart_date(System.currentTimeMillis());
                }


                courseRepository.save(course);
                logger.info("Add course success");
            } catch (Exception e) {
                e.printStackTrace();
                logger.info(e.getMessage());
            }
        });
    }

    @Override
    public void generateStudentCourse() {
        try {
            var courses = courseRepository.findAll(Sort.by("name"));
            courses.forEach(course -> {
                logger.info("Add student to course " + course.getName());

                var students= seederData.getUserSeeds()
                        .stream().filter(signupRequest -> signupRequest.getRole()
                                .contains(ERole.ROLE_STUDENT.getName()))
                                .collect(Collectors.toList());

                students.forEach(signupRequest -> {
                    try {
                        var student = userRepository
                                .findByUsername(signupRequest.getUsername());
                        if (student.isEmpty()) return;
                        logger.info("Seed: "  + student.get().getUsername()
                                + ", " + course.getName());
                        var studentCourse = new StudentCourse();
                        studentCourse.setUser_id(student.get().getId());
                        studentCourse.setCourse_id(course.getId());
                        studentCourse.setName(student.get().getUsername()
                                + ", " + course.getName());
                        studentCourseRepository.save(studentCourse);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.info(e.getMessage());
                    }
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
    }

    @Override
    public void generateSession() {
        IntStream.range(0, 10).forEach(i -> {
            try {
                var course = courseRepository.findFirstByName("Course Name " + i);
                if(course.isEmpty()) return;
                IntStream.range(0, 2).forEach(j -> {
                    try {
                        logger.info("Seeding Session Name " + j + ", Course " + i);
                        var session = new Session();
                        session.setCourse_id(course.get().getId());
                        session.setName("Session Name " + j + ", Course " + i);
                        session.setStart_time(course.get().getStart_date() + (j + 1L) * 86400000L);
                        var checkSession = sessionRepository
                                .findFirstByName("Session Name " + j + ", Course " + i);
                        if(checkSession.isPresent()) return;
                        session.setContent("Session " + j + " Content: " +
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                                "Integer cursus, nunc at vehicula tempor, dui dolor eleifend lacus, " +
                                "id dapibus dolor odio id risus. Cras quis erat nec nulla tempor vestibulum." +
                                " Aenean malesuada velit eu bibendum ullamcorper." +
                                " Ut aliquam enim sed gravida tempor. " +
                                "Interdum et malesuada fames ac ante ipsum primis in faucibus." +
                                " Cras aliquam est sit amet ipsum porta ultricies." +
                                " Aliquam rhoncus lectus quis laoreet aliquet.");
                        sessionRepository.save(session);
                        logger.info("Add sesion success");
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.info(e.getMessage());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                logger.info(e.getMessage());
            }
        });
    }

    @Override
    public void generateExercise() {
        sessionRepository.findAll().forEach(session -> {
            logger.info("Seeding Exercise for Session Name " + session.getName());
            IntStream.range(0, 2).forEach(i -> {
                logger.info("Seeding Exercise " + i);
                var exerciseRequest = new ExerciseRequest(session.getId(),
                        "Exercise " + i + " from " + session.getName(),
                        "Question: What is Lorem Ipsum?", "Answer: " +
                        "Lorem Ipsum is simply dummy text of the printing" +
                        " and typesetting industry. ", System.currentTimeMillis()
                        + 3L * 86400000L, false, true, true);
                exerciseService.save(exerciseRequest, "", true);
            });
        });
    }

    @Override
    public void generateStudentExercise() {
        exerciseRepository.findAll().forEach(exercise -> {
            try {
                logger.info("Seeding Student Exercise for Exercise id: " + exercise.getId());
                var session = sessionRepository.findById(exercise.getSession_id())
                        .orElseThrow(() -> new RuntimeException("session not found"));
                var course = courseRepository.findById(session.getCourse_id())
                        .orElseThrow(() -> new RuntimeException("course not found"));
                studentCourseRepository.findAllByCourse_idAndStatus
                    (course.getId(), StudentCourse.StatusEnum.SHOW).stream().map(studentCourse -> {
                        var student = userRepository.findById(studentCourse.getUser_id());
                        if(student.isEmpty()) return "";
                        return student.get().getUsername();
                    }).forEach(student -> {
                    var fileRequests = Collections.singletonList(
                        new FileRequest("lorem-ipsum-file.pdf", exercise.getContent(),
                                "1R27uYWN-ylclyIxsEcbUn9l2y_aQYNLx", 77123L));
                    var studentExerciseRequest =
                        new StudentExerciseRequest("Lorem Ipsum is simply dummy text of the printing " +
                            "and typesetting industry. Lorem Ipsum has been the industry's standard " +
                            "dummy text ever since the 1500s, when an unknown printer took a galley of" +
                            " type and scrambled it to make a type specimen book. It has survived not " +
                            "only five centuries, but also the leap into electronic typesetting," +
                            " remaining essentially unchanged. It was popularised in the 1960s with " +
                            "the release of Letraset sheets containing Lorem Ipsum passages, and more " +
                            "recently with desktop publishing software like Aldus PageMaker including versions" +
                            " of Lorem Ipsum.", exercise.getContent(), fileRequests);
                    studentExerciseService.saveStudentExercise(studentExerciseRequest,
                            student, exercise.getId());
                });
            } catch (RuntimeException e) {
                e.printStackTrace();
                logger.info(e.getMessage());
            }
        });
    }

    @Override
    public void generatePost() {
        var index = 0;
        logger.info("getting student");
        var studentName = seederData.getUserSeeds()
                .stream().filter(signupRequest -> signupRequest.getRole()
                        .contains(ERole.ROLE_STUDENT.getName()))
                .collect(Collectors.toList()).get(index).getUsername();
        var student = userRepository.findByUsername(studentName);
        if(student.isEmpty()) return;

        logger.info("getting teacher");
        var teacherName = seederData.getUserSeeds()
                .stream().filter(signupRequest -> signupRequest.getRole()
                        .contains(ERole.ROLE_TEACHER.getName()))
                .collect(Collectors.toList()).get(index).getUsername();
        var teacher = userRepository.findByUsername(teacherName);
        if(teacher.isEmpty()) return;

        logger.info("getting course");
        var course = courseRepository.findFirstByName("Course Name " + index);
        if(course.isEmpty()) return;

        logger.info("Seed post by " + studentName);
        IntStream.range(0, 5).forEach(i -> {
            logger.info("Seeding post " + i + " by"  + studentName);
            var post = new Post();
            post.setUser_id(student.get().getId());
            post.setCourse_id(course.get().getId());
            post.setContent("Post " + i + ", " +
                    " created by " + studentName +
                    " in " + course.get().getName());
            postRepository.save(post);

            IntStream.range(0, 3).forEach(j -> {
                var attachment = new Attachment();
                attachment.setPost_id(post.getId());
                attachment.setName("File " + j);
                attachment.setDescription("from post " + i + " of " + studentName);
                attachment.setFile_id("https://www.google.com.vn/");
                attachment.setFile_size((j+1)*100L);
                attachmentRepository.save(attachment);
            });
        });


        logger.info("Seed post by " + teacherName);
        IntStream.range(0, 5).forEach(i -> {
            logger.info("Seeding post " + i + " by " + teacherName);
            var post = new Post();
            post.setPin(i == 0);
            post.setUser_id(teacher.get().getId());
            post.setCourse_id(course.get().getId());
            post.setContent("Post " + i + ", " +
                    " created by " + teacherName +
                    " in " + course.get().getName());
            postRepository.save(post);

            IntStream.range(0, 3).forEach(j -> {
                var attachment = new Attachment();
                attachment.setPost_id(post.getId());
                attachment.setName("File " + j);
                attachment.setDescription("from post " + i + " of " + teacherName);
                attachment.setFile_id("https://www.google.com.vn/");
                attachment.setFile_size((j+1)*100L);
                attachmentRepository.save(attachment);
            });
        });
    }

    @Override
    public void generateComment() {
        var index = 0;
        logger.info("getting student");
        var studentName = seederData.getUserSeeds()
                .stream().filter(signupRequest -> signupRequest.getRole()
                        .contains(ERole.ROLE_STUDENT.getName()))
                .collect(Collectors.toList()).get(index).getUsername();
        var student = userRepository.findByUsername(studentName);
        if(student.isEmpty()) return;

        logger.info("getting teacher");
        var teacherName = seederData.getUserSeeds()
                .stream().filter(signupRequest -> signupRequest.getRole()
                        .contains(ERole.ROLE_TEACHER.getName()))
                .collect(Collectors.toList()).get(index).getUsername();
        var teacher = userRepository.findByUsername(teacherName);
        if(teacher.isEmpty()) return;

        logger.info("getting posts");
        postRepository.findAll(Sort.by("content")).forEach(post -> {
            var pinIndex = (new Random()).nextInt(6);
            IntStream.range(0, 7).forEach(i -> {
                logger.info("Seeding comment " + i + " by "
                        + (i%2 == 0? studentName : teacherName));
                var comment  = new Comment();
                comment.setUser_id(i%2 == 0? student.get().getId()
                        : teacher.get().getId());
                comment.setPost_id(post.getId());
                comment.setContent("Comment " + i + " by "
                        + (i%2 == 0? studentName : teacherName));
                comment.setPin(pinIndex == i);
            });
        });
    }

    @Override
    public void updateMigrationHistory(History.EMigration result) {
        try {
            historyRepository.deleteAll();
            var history = new History();
            history.setStatus(result);
            historyRepository.save(history);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
            logger.info(e.getCause().getMessage());
        }
    }
}
