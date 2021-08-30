package com.revature.ncu.services;

import com.revature.ncu.util.exceptions.*;
import com.revature.ncu.datasources.documents.Course;
import com.revature.ncu.datasources.repositories.CourseRepository;
import com.revature.ncu.web.dtos.UserCourseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

/**
 * Service for handling course business logic and passing information into the Relevant repository
 */
public class CourseService {

    private final Logger logger = LoggerFactory.getLogger(CourseService.class);
    private final CourseRepository courseRepo;
    private final CourseValidatorService courseValidatorService;

    public CourseService(CourseRepository courseRepo, CourseValidatorService courseValidatorService) {
        this.courseRepo = courseRepo;
        this.courseValidatorService = courseValidatorService;
    }

    // For faculty creating a new course
    public Course add(Course newCourse) {

        // Verify that the course data is valid.
        courseValidatorService.newCourseEntryValidator(newCourse);

        // Duplicate prevention
        if (courseRepo.findCourseByName(newCourse.getCourseName()) != null)
        {
            logger.error("Provided course already exists!");
            throw new ResourcePersistenceException("User provided a course name that already exists.");
        }
        if (courseRepo.findCourseByAbbreviation(newCourse.getCourseAbbreviation()) != null)
        {
            logger.error("User provided an abbreviation that already exists.");
            throw new ResourcePersistenceException("A course with the existing abbreviation already exists!");
        }

        // Save course to database if no issues are found
        return courseRepo.save(newCourse);

    }

    public void removeCourse(Course course){

        if(courseRepo.findCourseByAbbreviation(course.getCourseAbbreviation())==null)
        {
            throw new InvalidEntryException("Course does not exist!");
        }

        courseRepo.removeCourseByAbbreviation(course);
    }

    public Course updateCourse(Course original, Course update){

        // Verify information is valid.
        courseValidatorService.courseUpdateValidator(original, update);

        String originalAbv = original.getCourseAbbreviation();
        String newAbv = update.getCourseAbbreviation();

        // Check for duplicate abbreviation
        if (!originalAbv.equals(newAbv) && courseRepo.findCourseByAbbreviation(newAbv) != null) {
            throw new ResourcePersistenceException("Course abbreviation already exists!");
        }

        String originalName = original.getCourseName();
        String newName = update.getCourseName();

        // Check for duplicate course name
        if (!originalName.equals(newName) && courseRepo.findCourseByName(newName) != null) {
            throw new ResourcePersistenceException("Course name already exists!");
        }

        courseRepo.updateCourse(original,update);
        return update;
    }


    // Checks to see if the user has already joined a course, passes the course requested and the username to the Repo if not
    public void joinCourse(String joiningCourseAbv, String username){

        Course course = courseRepo.findCourseByAbbreviation(joiningCourseAbv);

        if(course==null){
            throw new NoSuchCourseException("No such course found!");
        }

        // Making sure course is open
        if(!courseValidatorService.isOpen(course)) {
            logger.error("User tried to register for course that was closed.");
            throw new CourseNotOpenException("Course is closed!");
        }

        // Making sure user is not already registered
        course.getStudentUsernames().stream().filter(id -> id.equals(username)).forEach(id -> {
            logger.info("User tried to join a course they were already registered for.");
            throw new AlreadyRegisteredForCourseException("You are already registered for this course!");
        });

        courseRepo.addStudentUsername(joiningCourseAbv, username);
    }

    // Used to check if the user entered a valid abbreviation
    public Course findCourseByAbbreviation(String abv){

        Course verifiedCourse = courseRepo.findCourseByAbbreviation(abv);

        if (verifiedCourse == null)
        {
            logger.error("No course found with provided abbreviation.");
            throw new ResourcePersistenceException("No course found with provided abbreviation!");
        }

        return verifiedCourse;

    }

    public List<Course> getCourses(){

        List<Course> openCourses = courseRepo.retrieveOpenCourses();

        if(openCourses.isEmpty())
        {
            logger.error("No open courses found.");
            throw new NoOpenCoursesException("There are no open courses! Please contact your student manager.");
        }

        return openCourses;
    }

    public List<Course> getAllCourses(){

        List<Course> courses = courseRepo.findAll();

        if(courses.isEmpty())
        {
            logger.error("No courses found.");
            throw new NoOpenCoursesException("There are no courses! What kind of university is this?");
        }

        return courses;
    }

    public List<UserCourseDTO> getCoursesByUsername(String username){
        List<UserCourseDTO> courses = courseRepo.findCoursesByUsername(username);

        if(courses.isEmpty())
        {
            logger.error("User not enrolled for any courses");
            throw new NoOpenCoursesException("You're not enrolled for any courses right now!");
        }

        return courses;
    }

    public void removeStudent(String username, String courseAbv) {

        Course withdraw = courseRepo.findCourseByAbbreviation(courseAbv);

        if(withdraw == null){
            logger.error("User entered an invalid abbreviation when attempting to withdraw.");
            throw new NoSuchCourseException("No course found with that Abbreviation!");
        }

        if(!withdraw.getStudentUsernames().contains(username)){
            logger.error("User attempted to withdraw from a course they are not registered for.");
            throw new NotRegisteredForCourseException("You are not registered for that course!");
        }

        if(withdraw.getCourseCloseDate().isBefore(LocalDate.now())){
            logger.error("User attempted to withdraw from a course that is closed.");
            throw new CourseNotOpenException("Withdraw window for this course is closed.");
        }
        courseRepo.removeStudent(username, courseAbv);
    }
}
