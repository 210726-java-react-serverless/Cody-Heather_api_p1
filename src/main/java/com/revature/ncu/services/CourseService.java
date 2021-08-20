package com.revature.ncu.services;

import com.revature.ncu.util.exceptions.*;
import com.revature.ncu.datasources.documents.Course;
import com.revature.ncu.datasources.repositories.CourseRepository;

import java.util.List;

public class CourseService {

    private final CourseRepository courseRepo;
    private final CourseValidatorService courseValidatorService;

    public CourseService(CourseRepository courseRepo, CourseValidatorService courseValidatorService) {
        this.courseRepo = courseRepo;
        this.courseValidatorService = courseValidatorService;
    }

    // For faculty creating a new course
    // TODO: Grab professor's first name and last name and .setProfessorName(fn+ln) in the CourseServlet;
    public Course add(Course newCourse) {

        // Verify that the course data is valid.
        courseValidatorService.newCourseEntryValidator(newCourse);

        // Duplicate prevention
        if (courseRepo.findCourseByName(newCourse.getCourseName()) != null)
        {
            System.out.println("Provided course already exists!");
            throw new ResourcePersistenceException("User provided a course name that already exists.");
        }
        if (courseRepo.findCourseByAbbreviation(newCourse.getCourseAbbreviation()) != null)
        {
            System.out.println("A course with the existing abbreviation already exists!");
            throw new ResourcePersistenceException("User provided an abbreviation that already exists.");
        }

        // Save course to database if no issues are found
        return courseRepo.save(newCourse);

    }

    public void removeCourse(Course course){

        courseRepo.removeCourseByAbbreviation(course);

    }

    public Course updateCourse(Course original, Course update){

        courseValidatorService.courseUpdateValidator(original, update);

        courseRepo.updateCourse(original,update);
        return update;
    }

//    public void updateCourseName(Course editingCourse, String newName){
//
//
//        courseValidatorService.newCourseNameValidator(editingCourse, newName);
//
//        if (courseRepo.findCourseByName(newName) != null)
//        {
//            System.out.println("A course by that name already exists!");
//            throw new ResourcePersistenceException("User provided a course name that already exists.");
//        }
//
//        courseRepo.updatingCourseName(editingCourse, newName);
//
//    }
//    public void updateCourseAbv(Course editingCourse, String newAbv){
//
//        courseValidatorService.newCourseAbvValidator(editingCourse, newAbv);
//
//        if (courseRepo.findCourseByAbbreviation(newAbv) != null)
//        {
//            System.out.println("A course with that abbreviation already exists!");
//            throw new ResourcePersistenceException("User provided an abbreviation that already exists.");
//        }
//
//        courseRepo.updatingCourseAbv(editingCourse, newAbv);
//
//    }
//    public void updateCourseDesc(Course editingCourse, String newDesc){
//
//        courseValidatorService.newCourseDetailsValidator(newDesc);
//
//        courseRepo.updatingCourseDesc(editingCourse, newDesc);
//
//    }

    // Used to check if the user entered a valid abbreviation
    public Course findCourseByAbbreviation(String abv){

        if(abv==null||abv.trim().equals(""))
        {
            System.out.println("Abbreviation cannot be blank!");
            throw new InvalidEntryException("Invalid abbreviation provided.");
        }

        Course verifiedCourse = courseRepo.findCourseByAbbreviation(abv);

        if (verifiedCourse == null)
        {
            System.out.println("No course found with provided abbreviation!");
            throw new ResourcePersistenceException("No course found with provided abbreviation.");
        }

        return verifiedCourse;

    }

    public Course verifyCourseOpenByAbbreviation(String abv){

        if(abv==null||abv.trim().equals(""))
        {
            System.out.println("Invalid entry!");
            throw new InvalidEntryException("Blank entry.");

        }

        Course verifiedCourse = courseRepo.findCourseByAbbreviation(abv);

        if (verifiedCourse == null)
        {
            System.out.println("No course found with provided abbreviation!");
            throw new NoSuchCourseException("Invalid course abbreviation provided.");
        }

        //TODO time logic

        return verifiedCourse;

    }

    public Course verifyCourseOpenByName(String courseName){

        if(courseName==null||courseName.trim().equals(""))
        {
            throw new InvalidEntryException("Invalid course name provided");

        }

        Course verifiedCourse = courseRepo.findCourseByName(courseName);

        if (verifiedCourse == null)
        {
            System.out.println("No course found with provided name!");
            throw new NoSuchCourseException("No course found with provided name!");
        }

        return verifiedCourse;

    }

    public List<Course> getCourses(){

        List<Course> openCourses = courseRepo.retrieveOpenCourses();

        if(openCourses.isEmpty())
        {
            System.out.println("There are no open courses! Please contact your student manager.");
            throw new NoOpenCoursesException("No open courses found.");
        }

        return openCourses;
    }
    public List<Course> getAllCourses(){

        List<Course> courses = courseRepo.findAll();

        if(courses.isEmpty())
        {
            System.out.println("There are no courses! What kind of university is this?");
            throw new NoOpenCoursesException("No courses found.");
        }

        return courses;
    }
}
