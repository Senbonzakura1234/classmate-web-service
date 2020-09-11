package com.app.manager.schedule;

import com.app.manager.context.repository.CourseRepository;
import com.app.manager.context.repository.SessionRepository;
import com.app.manager.entity.Course;
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

    @Scheduled(cron = "0 0 * * * *")
    public void courseUpdateStatus(){
        logger.info("Getting course list");
        var couses = courseRepository.findAll();

        couses.forEach(course -> {
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
                course.setStatus(Course.StatusEnum.ONGOING);
                courseRepository.save(course);
                return;
            }
            if(course.getEnd_date() < System.currentTimeMillis()) {
                logger.info("Closing " + course.getName());
                course.setStatus(Course.StatusEnum.END);
                courseRepository.save(course);
            }
        });
    }

    @Scheduled(cron = "0 0 * * * *")
    public void sessionUpdateStatus(){
        logger.info("Getting session list");
        var sessions = sessionRepository.findAll();

        sessions.forEach(session -> {
            logger.info("Checking session " + session.getName());
            if(session.getStatus() == Session.StatusEnum.CANCEL
                    || session.getStatus() == Session.StatusEnum.END
                    || session.getStatus() == Session.StatusEnum.ALL){
                logger.info("Skip " + session.getName());
                logger.info("Reason: session status is "
                        + session.getStatus().getName());
                return;
            }

            if(session.getStart_time() <= System.currentTimeMillis() &&
                (session.getStart_time() + session.getSession_duration()*3600000)
                        >= System.currentTimeMillis()){
                logger.info("Starting " + session.getName());
                session.setStatus(Session.StatusEnum.ONGOING);
                sessionRepository.save(session);

                logger.info("Start attendancecheck for session " + session.getName());
                sessionService.startAttendanceCheck(session.getId(),
                        "", true);
                return;
            }

            if((session.getStart_time() + session.getSession_duration()*3600000)
                    < System.currentTimeMillis()){
                logger.info("Closing " + session.getName());
                session.setStatus(Session.StatusEnum.END);
                sessionRepository.save(session);

                logger.info("Stop attendancecheck for session " + session.getName());
                sessionService.closeAttendanceCheck(session.getId(),
                        "", true);
            }
        });
    }
}
