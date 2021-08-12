package com.revature.projectzero.util;
import com.revature.projectzero.repositories.CourseRepository;
import com.revature.projectzero.repositories.UserCoursesRepository;
import com.revature.projectzero.repositories.UserRepository;
import com.revature.projectzero.services.CourseService;
import com.revature.projectzero.services.InputValidatorService;
import com.revature.projectzero.services.UserCoursesService;
import com.revature.projectzero.services.UserService;
import com.revature.projectzero.screens.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
/*
 * The "heart" of the program.
 * Instantiates all of the screens and injects the dependencies needed to perform operations.
 * Contains static variable and object references used by the Screens.
 *
 */

public class AppState {

    private final Logger logger = LogManager.getLogger(AppState.class);
    private static boolean appRunning;
    private final ScreenRouter router;


    public AppState() {
        appRunning = true;
        router = new ScreenRouter();

        // Dependencies for injection
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        InputValidatorService inputValidatorService = new InputValidatorService();


        // Create app components and dependencies
        UserSession userSession = new UserSession();
        UserRepository userRepo = new UserRepository();
        UserService userService = new UserService(userRepo, userSession, inputValidatorService);

        CourseRepository courseRepo = new CourseRepository();
        CourseService courseService = new CourseService(courseRepo, inputValidatorService);

        UserCoursesRepository courseListRepo = new UserCoursesRepository();
        UserCoursesService userCoursesService = new UserCoursesService(courseListRepo, userSession);

        //Instantiate Screens and inject required dependencies
        router.addScreen(new WelcomeScreen(consoleReader, router))
                .addScreen(new AddCourseScreen(consoleReader, router, courseService))
                .addScreen(new CourseRegistrationScreen(consoleReader, router, courseService, userCoursesService))
                .addScreen(new EditCourseScreen(consoleReader, router, courseService, userCoursesService))
                .addScreen(new FacultyDashboardScreen(consoleReader, router, userService))
                .addScreen(new RegisteredCoursesScreen(consoleReader, router, userCoursesService))
                .addScreen(new RemoveCourseScreen(consoleReader, router, courseService, userCoursesService))
                .addScreen(new StudentDashboardScreen(consoleReader, router, userService))
                .addScreen(new LoginScreen(consoleReader, router, userService))
                .addScreen(new ViewCoursesScreen(consoleReader, router, courseService))
                .addScreen(new SystemAdminScreen(consoleReader, router))
                .addScreen(new NewStudentScreen(consoleReader, router, userService, userCoursesService))
                .addScreen(new CourseWithdrawalScreen(consoleReader, router, courseService, userCoursesService));
    }

    public void startup() {

        // Display welcome screen on startup
        router.navigate("/welcome");

        // While the app is running, continue to render the current screen.
        // Prevents the program from closing unexpectedly in the event that a user provides invalid input.
        while (appRunning) {
            try {
                router.getCurrentScreen().render();
            } catch (Exception e) {
                logger.error("An unexpected error occurred while trying to display a screen.", e);
                e.printStackTrace();
                return;
            }
        }

    }

    // Static method for properly shutting down the app
    public static void closeApp() {
        System.out.println("Shutting down...");
        AppState.appRunning = false;
    }
}
