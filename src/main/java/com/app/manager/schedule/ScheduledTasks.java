package com.app.manager.schedule;

import com.app.manager.context.repository.CourseRepository;
import com.app.manager.context.repository.ExerciseRepository;
import com.app.manager.context.repository.SessionRepository;
import com.app.manager.entity.Course;
import com.app.manager.entity.Exercise;
import com.app.manager.entity.Session;
import com.app.manager.service.interfaceClass.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired CourseRepository courseRepository;
    @Autowired SessionRepository sessionRepository;
    @Autowired SessionService sessionService;
    @Autowired ExerciseRepository exerciseRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void courseUpdateStatus(){
        logger.info("Getting course list");
        try {
           courseRepository.findAll().forEach(course -> {
                logger.info("Checking course " + course.getName());

                if(course.getStatus() == Course.StatusEnum.CANCEL
                    || course.getStatus() == Course.StatusEnum.END
                    || course.getStatus() == Course.StatusEnum.ALL){
                    logger.info("Skip " + course.getName());
                    logger.info("Reason: course status is "
                            + course.getStatus().getName());
                    return;
                }

                if(course.getStart_date() <= System.currentTimeMillis() &&
                    course.getEnd_date() >= System.currentTimeMillis()) {
                    logger.info("Starting " + course.getName());
                    try {
                        course.setStatus(Course.StatusEnum.ONGOING);
                        courseRepository.save(course);
                        logger.info("Started " + course.getName());

                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.info("Start " + course.getName() + " failed");
                        logger.info("Error: " + e.getMessage());
                    }
                    return;
                }
                if(course.getEnd_date() < System.currentTimeMillis()) {
                    try {
                        logger.info("Closing " + course.getName());
                        course.setStatus(Course.StatusEnum.END);
                        courseRepository.save(course);
                        logger.info("Closed " + course.getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.info("Close " + course.getName() + " failed");
                        logger.info("Error: " + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Get Courses failed");
            logger.info("Error: " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    public void sessionUpdateStatus(){
        logger.info("Getting session list");
        try {
            sessionRepository.findAll().forEach(session -> {
                logger.info("Checking session " + session.getName());
                if(session.getStatus() == Session.StatusEnum.CANCEL
                        || session.getStatus() == Session.StatusEnum.END
                        || session.getStatus() == Session.StatusEnum.ALL){
                    logger.info("Skip " + session.getName());
                    logger.info("Reason: session status is "
                            + session.getStatus().getName());
                    return;
                }

//                if(session.getStart_time() <= System.currentTimeMillis() &&
//                    (session.getStart_time() + session.getSession_duration()*3600000)
//                            >= System.currentTimeMillis()){
//                    logger.info("Starting " + session.getName());
//                    try {
//                        session.setStatus(Session.StatusEnum.ONGOING);
//                        sessionRepository.save(session);
//
//                        logger.info("Start attendancecheck for session " + session.getName());
//                        sessionService.startAttendanceCheck(session.getId(),
//                                "", true);
//
//                        exerciseRepository.findAllBySession_idAndStatus
//                                (session.getId(), Exercise.StatusEnum.PENDING)
//
//                        .forEach(exercise -> {
//                            if (exercise.isAuto_start()) {
//                                try {
//                                    exercise.setStatus(Exercise.StatusEnum.ONGOING);
//                                    exerciseRepository.save(exercise);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                    logger.info("Start Exercice failed");
//                                    logger.info("Error: " + e.getMessage());
//                                }
//                            }
//                        });
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        logger.info("Start Session failed");
//                        logger.info("Error: " + e.getMessage());
//                    }
//                    return;
//                }

                if((session.getStart_time() + session.getSession_duration()*3600000)
                        < System.currentTimeMillis()){
                    logger.info("Closing " + session.getName());
                    try {
                        session.setStatus(Session.StatusEnum.END);
                        sessionRepository.save(session);

                        logger.info("Stop attendancecheck for session " + session.getName());
                        sessionService.closeAttendanceCheck(session.getId(),
                                "", true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.info("Close Session failed");
                        logger.info("Error: " + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Get Sessions failed");
            logger.info("Error: " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 * * * *")
    public void exerciseUpdateStatus(){
        logger.info("Getting exercises list");
        try {
           exerciseRepository.findAll().forEach(exercise -> {
               if(exercise.getStatus() != Exercise.StatusEnum.ONGOING){
                   logger.info("Skip exercise");
                   logger.info("Reason: exercise status is "
                           + exercise.getStatus().getName());
                   return;
               }
               if (exercise.isAuto_close()) {
                   try {
                       if(exercise.getExercise_end_time()
                               <= System.currentTimeMillis()){
                           logger.info("Closing exercise");
                           exercise.setStatus(Exercise.StatusEnum.MARKING);
                           exerciseRepository.save(exercise);
                       }
                   } catch (Exception e) {
                       e.printStackTrace();
                       logger.info("Close exercise failed");
                       logger.info("Error: " + e.getMessage());
                   }
               }
           });
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Get exercises failed");
            logger.info("Error: " + e.getMessage());
        }
    }
}
