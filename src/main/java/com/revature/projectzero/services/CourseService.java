package com.revature.projectzero.services;

import com.revature.projectzero.util.exceptions.*;
import com.revature.projectzero.documents.Course;
import com.revature.projectzero.repositories.CourseRepository;

import java.util.List;

public class CourseService {

    private final CourseRepository courseRepo;
    private final InputValidatorService inputValidatorService;

    public CourseService(CourseRepository courseRepo, InputValidatorService inputValidatorService) {
        this.courseRepo = courseRepo;
        this.inputValidatorService = inputValidatorService;
    }

    public Course add(Course newCourse) {

        inputValidatorService.newCourseEntryValidator(newCourse);

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

        return courseRepo.save(newCourse);

    }

    public void removeCourse(Course course){

        courseRepo.removeCourse(course);

    }

    public void updateCourseName(Course editingCourse, String newName){


        inputValidatorService.newCourseNameValidator(editingCourse, newName);

        if (courseRepo.findCourseByName(newName) != null)
        {
            System.out.println("A course by that name already exists!");
            throw new ResourcePersistenceException("User provided a course name that already exists.");
        }

        courseRepo.updatingCourseName(editingCourse, newName);

    }
    public void updateCourseAbv(Course editingCourse, String newAbv){

        inputValidatorService.newCourseAbvValidator(editingCourse, newAbv);

        if (courseRepo.findCourseByAbbreviation(newAbv) != null)
        {
            System.out.println("A course with that abbreviation already exists!");
            throw new ResourcePersistenceException("User provided an abbreviation that already exists.");
        }

        courseRepo.updatingCourseAbv(editingCourse, newAbv);

    }
    public void updateCourseDesc(Course editingCourse, String newDesc){

        inputValidatorService.newCourseDetailsValidator(newDesc);

        courseRepo.updatingCourseDesc(editingCourse, newDesc);

    }

    public void toggleOpen(Course course){

        courseRepo.openClose(course);
    }

    public Course verifyCourse(String abv){

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
        }else if(!verifiedCourse.isOpen())
        {
            System.out.println("The registration and withdrawal windows for this course have closed!");
            throw new CourseNotOpenException("The registration and withdrawal windows for this course have closed!");
        }

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
        }else if(!verifiedCourse.isOpen())
        {
            System.out.println("The registration and withdrawal windows for this course have closed!");
            throw new CourseNotOpenException("User attempted to join or withdraw from a closed course.");
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

}
