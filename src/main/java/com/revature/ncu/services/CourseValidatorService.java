package com.revature.ncu.services;

import com.revature.ncu.datasources.documents.AppUser;
import com.revature.ncu.datasources.documents.Course;
import com.revature.ncu.util.exceptions.*;
import com.revature.ncu.web.util.ContextLoaderListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 *  Class for validating user input to check for:
 *  Empty values,
 *  Password security, (Minimum of 8 characters)
 *  Username length, (Minimum of 4 characters)
 *  invalid username characters, (no symbols in username)
 *
 */

public class CourseValidatorService {

    private final Logger logger = LoggerFactory.getLogger(CourseValidatorService.class);

    //Course restrictions are defined here.
    private static final int MAX_COURSE_NAME = 24;
    private static final int MAX_COURSE_ABV = 6;
    private static final int MAX_COURSE_DESC = 255;
    private static final int MIN_COURSE_DESC = 10;
    private static final int MIN_COURSE_CAP = 12;
    private static final int MAX_COURSE_CAP = 100;

     // Validator for adding a new Course
    public boolean newCourseEntryValidator(Course course){

        if(course.getCourseName().trim().equals("")||course.getCourseAbbreviation().trim().equals("")||course.getCourseDetail().trim().equals(""))
        {
            logger.error("Fields cannot be blank!");
            throw new InvalidEntryException("Blank fields detected.");
        }
        else if(course.getCourseName().length() > MAX_COURSE_NAME)
        {
            logger.error("Course name over {} characters provided.", MAX_COURSE_NAME);
            throw new InvalidCourseNameException("Course name too long.");
        }
        else if(course.getCourseName().length() < course.getCourseAbbreviation().length())
        {
            logger.error("Course name shorter than abbreviation provided.");
            throw new InvalidCourseNameException("Course name too short.");
        }
        else if(course.getCourseAbbreviation().length() > MAX_COURSE_ABV)
        {
            logger.error("Course abbreviation over {} characters provided.", MAX_COURSE_ABV);
            throw new InvalidCourseAbbreviationException("Course Abbreviation too long.");
        }
        else if(course.getCourseDetail().length() < MIN_COURSE_DESC)
        {
            logger.error("Course description under {} characters provided."
                    , MIN_COURSE_DESC);
            throw new InvalidCourseDescriptionException("Course description too short.");
        }
        else if(course.getCourseDetail().length() > MAX_COURSE_DESC)
        {

            logger.error("Course description over {} characters provided.", MAX_COURSE_DESC);
            throw new InvalidCourseDescriptionException("Course description too long.");
        }
        else if(course.getCourseCapacity()<MIN_COURSE_CAP)
        {
            logger.error("Course capacity under {} provided.", MIN_COURSE_CAP);
            throw new InvalidCourseCapacityException("Course capacity too low.");
        }
        else if(course.getCourseCapacity()>MAX_COURSE_CAP)
        {
            logger.error("Course capacity over {} provided.", MAX_COURSE_CAP);
            throw new InvalidCourseCapacityException("Course capacity too high.");
        }
        else if(course.getCourseOpenDate().isAfter(course.getCourseCloseDate()))
        {
            logger.error("Provided course open date is after course close date.");
            throw new InvalidCourseDateException("Course open date after course close date.");
        }
        else if(course.getCourseOpenDate().isBefore(LocalDate.now()))
        {
            logger.error("Provided course open date is before today.");
            throw new InvalidCourseDateException("Course open date before current date.");
        }


        return true;

    }

    // Validator for Editing Course Values

//    public boolean newCourseNameValidator(Course original, String newName){
//
//        if(newName.trim().equals(""))
//        {
//            System.out.println("Fields cannot be blank!");
//            throw new InvalidEntryException("Blank fields detected.");
//        }
//        else if(newName.length() > MAX_COURSE_NAME)
//        {
//            System.out.printf("Please keep course name concise and under %d characters.%n", MAX_COURSE_NAME);
//            throw new InvalidCourseNameException("Course name too long.");
//        }
//        else if(newName.length() < original.getCourseAbbreviation().length())
//        {
//            System.out.println("Please make your course name longer than the abbreviation.");
//            throw new InvalidCourseNameException("Course name too short.");
//        }
//        return true;
//    }
//
//    public boolean newCourseAbvValidator(Course original, String newAbv){
//        if(newAbv.trim().equals(""))
//        {
//            System.out.println("Fields cannot be blank!");
//            throw new InvalidEntryException("Blank fields detected.");
//        }
//        else if(newAbv.length() > MAX_COURSE_ABV)
//        {
//            System.out.printf("Please keep the course Abbreviation to %d characters or less.%n", MAX_COURSE_ABV);
//            throw new InvalidCourseAbbreviationException("Course Abbreviation too long.");
//        }else if(newAbv.length() > original.getCourseName().length())
//        {
//            System.out.println("Please make your new Abbreviation shorter than the course name.");
//            throw new InvalidCourseAbbreviationException("Course abbreviation too long.");
//        }
//        return true;
//    }
//
//    public boolean newCourseDetailsValidator(String newDetails){
//
//        if(newDetails.trim().equals(""))
//        {
//            System.out.println("Fields cannot be blank!");
//            throw new InvalidEntryException("Blank fields detected.");
//        }
//        else if(newDetails.length() < MIN_COURSE_DESC)
//        {
//            System.out.printf("Course description must be at least %d characters long.%nPlease provide more details about the course.%n"
//                    , MIN_COURSE_DESC);
//            throw new InvalidCourseDescriptionException("Course description too short.");
//        }
//        else if(newDetails.length() > MAX_COURSE_DESC)
//        {
//            System.out.printf("Course description is too long.%nPlease enter a more concise description under %d characters."
//                    , MAX_COURSE_DESC);
//            throw new InvalidCourseDescriptionException("Course description too long.");
//        }
//        return true;
//    }

    // Returns true if within registration window and course is not full.
    public boolean isOpen(Course course){
        return course.getCourseOpenDate().isAfter(LocalDate.now())
                && course.getCourseCloseDate().isBefore(LocalDate.now())
                && course.getSlotsTaken() < course.getCourseCapacity();
    }

    public boolean courseUpdateValidator(Course original, Course updatingCourse) {
        if(updatingCourse.getCourseName().trim().equals("")||updatingCourse.getCourseAbbreviation().trim().equals("")||updatingCourse.getCourseDetail().trim().equals(""))
        {
            logger.error("Fields cannot be blank!");
            throw new InvalidEntryException("Blank fields detected.");
        }
        else if(updatingCourse.getCourseName().length() > MAX_COURSE_NAME)
        {
            logger.error("Course name over {} characters provided.", MAX_COURSE_NAME);
            throw new InvalidCourseNameException("Course name too long.");
        }
        else if(updatingCourse.getCourseName().length() < updatingCourse.getCourseAbbreviation().length())
        {
            logger.error("Course name shorter than abbreviation provided.");
            throw new InvalidCourseNameException("Course name too short.");
        }
        else if(updatingCourse.getCourseAbbreviation().length() > MAX_COURSE_ABV)
        {
            logger.error("Course abbreviation over {} characters provided.", MAX_COURSE_ABV);
            throw new InvalidCourseAbbreviationException("Course Abbreviation too long.");
        }
        else if(updatingCourse.getCourseDetail().length() < MIN_COURSE_DESC)
        {
            logger.error("Course description under {} characters provided."
                    , MIN_COURSE_DESC);
            throw new InvalidCourseDescriptionException("Course description too short.");
        }
        else if(updatingCourse.getCourseDetail().length() > MAX_COURSE_DESC)
        {

            logger.error("Course description over {} characters provided.", MAX_COURSE_DESC);
            throw new InvalidCourseDescriptionException("Course description too long.");
        }
        else if(updatingCourse.getCourseCapacity()<=original.getSlotsTaken()){
            logger.error("Invalid capacity provided ({}) cannot be less than enrolled students({})!",
                    updatingCourse.getCourseCapacity(),original.getSlotsTaken());
            throw new InvalidCourseCapacityException("Cannot reduce capacity below current enrollment.");
        }
        else if(updatingCourse.getCourseCapacity()<MIN_COURSE_CAP)
        {
            logger.error("Course capacity under {} provided.", MIN_COURSE_CAP);
            throw new InvalidCourseCapacityException("Course capacity too low.");
        }
        else if(updatingCourse.getCourseCapacity()>MAX_COURSE_CAP)
        {
            logger.error("Course capacity over {} provided.", MAX_COURSE_CAP);
            throw new InvalidCourseCapacityException("Course capacity too high.");
        }
        else if(updatingCourse.getCourseOpenDate().isAfter(updatingCourse.getCourseCloseDate()))
        {
            logger.error("Provided course open date is after course close date.");
            throw new InvalidCourseDateException("Course open date after course close date.");
        }
        else if(updatingCourse.getCourseOpenDate().isBefore(LocalDate.now()))
        {
            logger.error("Provided course open date is before today.");
            throw new InvalidCourseDateException("Course open date before current date.");
        }
        return false;
    }
}
