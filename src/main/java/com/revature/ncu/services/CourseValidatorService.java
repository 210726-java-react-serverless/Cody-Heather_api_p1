package com.revature.ncu.services;

import com.revature.ncu.datasources.documents.Course;
import com.revature.ncu.util.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

/**
 *  Class for validating user input
 *
 */

public class CourseValidatorService {

    private final Logger logger = LoggerFactory.getLogger(CourseValidatorService.class);

    //Course restrictions are defined here.
    private static final int MAX_COURSE_NAME = 30;
    private static final int MAX_COURSE_ABV = 6;
    private static final int MAX_COURSE_DESC = 255;
    private static final int MIN_COURSE_DESC = 10;
    private static final int MIN_COURSE_CAP = 12;
    private static final int MAX_COURSE_CAP = 100;

    // Validator for adding a new Course
    public boolean newCourseEntryValidator(Course course){

        if(course.getCourseName().trim().equals("")||course.getCourseAbbreviation().trim().equals("")||course.getCourseDetail().trim().equals(""))
        {
            logger.error("Blank fields detected.");
            throw new InvalidEntryException("Fields cannot be blank!");
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
        else if(course.getCourseCloseDate().isBefore(LocalDate.now()))
        {
            logger.error("Provided course close date is before today.");
            throw new InvalidCourseDateException("Course close date before current date.");
        }

        return true;

    }

    // Check if within registration window and course is not full.
    public boolean isOpen(Course course){
        return (course.getCourseOpenDate().isBefore(LocalDate.now())
                || course.getCourseOpenDate().isEqual(LocalDate.now()))
                && course.getCourseCloseDate().isAfter(LocalDate.now())
                && course.getSlotsTaken() < course.getCourseCapacity();
    }

    // Validator for Editing Course Values
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
        else if(updatingCourse.getCourseCloseDate().isBefore(LocalDate.now()))
        {
            logger.error("Provided course close date is before today.");
            throw new InvalidCourseDateException("Course close date before current date.");
        }
        return true;
    }
}
