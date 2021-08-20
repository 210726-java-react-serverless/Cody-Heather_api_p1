package com.revature.ncu.web.util;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.revature.ncu.datasources.repositories.CourseRepository;
import com.revature.ncu.datasources.repositories.UserCoursesRepository;
import com.revature.ncu.datasources.repositories.UserRepository;
import com.revature.ncu.datasources.utils.MongoClientFactory;
import com.revature.ncu.services.*;
import com.revature.ncu.util.PasswordUtils;
import com.revature.ncu.web.servlets.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;

public class ContextLoaderListener implements ServletContextListener {

    private final Logger logger = LoggerFactory.getLogger(ContextLoaderListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce){
        MongoClient mongoClient = MongoClientFactory.getInstance().getConnection();
        PasswordUtils passwordUtils = new PasswordUtils();
        UserValidatorService userValidatorService = new UserValidatorService();
        CourseValidatorService courseValidatorService = new CourseValidatorService();

        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

        UserRepository userRepo = new UserRepository(mongoClient);
        UserService userService = new UserService(userRepo, userValidatorService, passwordUtils);

        CourseRepository courseRepository = new CourseRepository(mongoClient);
        CourseService courseService = new CourseService(courseRepository, courseValidatorService);

        UserCoursesRepository userCoursesRepository = new UserCoursesRepository(mongoClient);
        UserCoursesService userCoursesService = new UserCoursesService(userCoursesRepository, courseValidatorService, courseRepository);

        UserServlet userServlet = new UserServlet(userService, mapper);
        AuthServlet authServlet = new AuthServlet(userService, mapper);
        HelloWorld helloWorld = new HelloWorld();
        GoodbyeWorld goodbyeWorld= new GoodbyeWorld();
        HealthCheckServlet healthCheckServlet = new HealthCheckServlet();
        FacultyServlet facultyServlet = new FacultyServlet();
        CourseServlet courseServlet = new CourseServlet(userService, courseService, mapper);
        StudentServlet studentServlet = new StudentServlet();
        StudentCourseServlet studentCourseServlet = new StudentCourseServlet(userService,courseService,userCoursesService,mapper);

        ServletContext servletContext = sce.getServletContext();
        servletContext.addServlet("HelloWorld", helloWorld).addMapping("/hello");
        servletContext.addServlet("UserServlet", userServlet).addMapping("/users/*");
        servletContext.addServlet("AuthServlet", authServlet).addMapping("/auth");
        servletContext.addServlet("GoodbyeWorld", goodbyeWorld).addMapping("/goodbye");
        servletContext.addServlet("HealthCheckServlet", healthCheckServlet).addMapping("/health");
        servletContext.addServlet("FacultyServlet", facultyServlet).addMapping("/faculty");
        servletContext.addServlet("CourseServlet", courseServlet).addMapping("/faculty/courses");
        servletContext.addServlet("StudentServlet", studentServlet).addMapping("/student");
        servletContext.addServlet("StudentCourseServlet", studentCourseServlet).addMapping("/student/courses");

        configureLogback(servletContext);

        logger.info("ContextLoaderListener initialized.\nLogger initialized.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        MongoClientFactory.getInstance().cleanUp();
    }

    private void configureLogback(ServletContext servletContext) {

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        JoranConfigurator logbackConfig = new JoranConfigurator();
        logbackConfig.setContext(loggerContext);
        loggerContext.reset();

        String logbackConfigFilePath = servletContext.getRealPath("") + File.separator + servletContext.getInitParameter("logback-config");

        try {
            logbackConfig.doConfigure(logbackConfigFilePath);
        } catch (JoranException e) {
            e.printStackTrace();
            System.out.println("An unexpected exception occurred. Unable to configure Logback.");
        }

    }

}
