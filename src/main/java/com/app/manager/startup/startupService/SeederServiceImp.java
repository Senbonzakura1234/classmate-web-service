package com.app.manager.startup.startupService;

import com.app.manager.context.repository.*;
import com.app.manager.entity.*;
import com.app.manager.model.returnResult.MigrationQueryResult;
import com.app.manager.model.seeder.SeederData;
import com.app.manager.service.interfaceClass.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
    @Autowired
    HistoryRepository historyRepository;

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
                var randCate = new Random();
                var randTeacher = new Random();
                var categoryName = categories.get(randCate.nextInt(categories.size()));
                var teacherName = teachers.get(randTeacher.nextInt(teachers.size())).getUsername();

                var category = coursecategoryRepository.findFirstByName(categoryName);
                if (category.isEmpty()) return;

                var teacher = userRepository.findByUsername(teacherName);
                if (teacher.isEmpty()) return;

                var course = new Course();
                course.setName("Course Name " + i);
                var checkCourse = courseRepository.findFirstByName("Course Name " + i);
                if (checkCourse.isPresent()) return;

                course.setDescription("Teacher: " + teacherName + ", Category: " + categoryName);
                course.setCoursecategory_id(category.get().getId());
                course.setUser_id(teacher.get().getId());


//                course.setStatus(Course.StatusEnum.ONGOING);

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
            var courses = courseRepository.findAll();
            courses.forEach(course -> {
                var students= seederData.getUserSeeds()
                        .stream().filter(signupRequest -> signupRequest.getRole()
                                .contains(ERole.ROLE_STUDENT.getName()))
                                .collect(Collectors.toList());

                students.forEach(signupRequest -> {
                    try {
                        var student = userRepository
                                .findByUsername(signupRequest.getUsername());
                        if (student.isEmpty()) return;
                        var studentCourse = new StudentCourse();
                        studentCourse.setUser_id(student.get().getId());
                        studentCourse.setCourse_id(course.getId());
                        studentCourseRepository.save(studentCourse);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.info(e.getMessage());
                    }
                });
                logger.info("Add student to course " + course.getName());
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
