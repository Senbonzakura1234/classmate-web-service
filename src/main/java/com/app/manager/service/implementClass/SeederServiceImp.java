package com.app.manager.service.implementClass;

import com.app.manager.context.repository.*;
import com.app.manager.entity.*;
import com.app.manager.model.seeder.SeederData;
import com.app.manager.service.interfaceClass.SeederService;
import com.app.manager.service.interfaceClass.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Service
public class SeederServiceImp implements SeederService {
    @Autowired SeederData seederData;
    @Autowired RoleRepository roleRepository;
    @Autowired SubscriptionRepository subscriptionRepository;
    @Autowired CourseCategoryRepository coursecategoryRepository;
    @Autowired UserRepository userRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired StudentCourseRepository studentCourseRepository;
    @Autowired SessionRepository sessionRepository;
    @Autowired UserService userService;

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
                System.out.println("Can create role: " + eRole.getName());
                System.out.println("Reason: " + e.getMessage());
                System.out.println("Cause by: " + e.getCause().toString());
            }
        }
    }

    @Override
    public void generateSubscription() {
        for(var eSubscription : ESubscription.values()){
            var subscription = subscriptionRepository.findFirstByName(eSubscription.getName());
            try {
                if (subscription.isEmpty()) {
                    var newSubscription = new Subscription(
                            eSubscription.getLevel(), eSubscription.getName(), eSubscription.getPrice(),
                            eSubscription.getDiscount(), eSubscription.getMax_student(),
                            eSubscription.getMax_course_duration(), eSubscription.getMax_session_duration(),
                            eSubscription.getMax_exercise_per_session(), eSubscription.isAllow_face_check());
                    subscriptionRepository.save(newSubscription);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Can create subscription: " + eSubscription.getName());
                System.out.println("Reason: " + e.getMessage());
                System.out.println("Cause by: " + e.getCause().toString());
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
                System.out.println("Can create role: " + name);
                System.out.println("Reason: " + e.getMessage());
                System.out.println("Cause by: " + e.getCause().toString());
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
            var rand = new Random();
            var chance = rand.nextInt(1000);
            var subscribtionName = signupRequest.getRole()
                    .contains(ERole.ROLE_ADMIN.getName()) ? ESubscription.PREMIUM :
                    chance % 2 == 0 ? ESubscription.PREMIUM :
                            ESubscription.FREE;
            var subscribtion = getSubscribtionInstant(subscribtionName);
            if(subscribtion.isEmpty()) return;
            System.out.println(userService.saveUser(user, signupRequest.getRole(),
                    subscribtion.get().getId()).getDescription());
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
                System.out.println("Add course success");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
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
                        System.out.println(e.getMessage());
                    }
                });
                System.out.println("Add student to course " + course.getName());
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
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
                        System.out.println("Add sesion success");
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(e.getMessage());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        });
    }


    private Optional<Subscription> getSubscribtionInstant(
            ESubscription eSubscription){
        try {
            var subscription = subscriptionRepository
                    .findFirstByName(eSubscription.getName());
            if(subscription.isPresent()) return subscription;
            var newSubscription = new Subscription(
                    eSubscription.getLevel(), eSubscription.getName(), eSubscription.getPrice(),
                    eSubscription.getDiscount(), eSubscription.getMax_student(),
                    eSubscription.getMax_course_duration(), eSubscription.getMax_session_duration(),
                    eSubscription.getMax_exercise_per_session(), eSubscription.isAllow_face_check());
            subscriptionRepository.save(newSubscription);
            return Optional.of(newSubscription);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }
}
