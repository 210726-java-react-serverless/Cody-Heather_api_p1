package com.revature.projectzero.services;

import com.revature.projectzero.documents.AppUser;
import com.revature.projectzero.documents.Course;
import com.revature.projectzero.util.exceptions.*;

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

public class InputValidatorService {

    // User restrictions are defined here.
    private static final int MIN_USERNAME = 4;
    private static final int MIN_PASSWORD = 8;

    // Pattern for username verification, can be any character from a-z, A-Z, and 0-9. No symbols permitted.
    static Pattern usernamePattern = Pattern.compile("[^a-zA-Z0-9]");

    // Pattern for simple email verification.
    static Pattern emailPattern = Pattern.compile("^(.+)@(.+)$");

    //Course restrictions are defined here.
    private static final int MAX_COURSE_NAME = 24;
    private static final int MAX_COURSE_ABV = 6;
    private static final int MAX_COURSE_DESC = 255;
    private static final int MIN_COURSE_DESC = 10;

    // Validator for student registration
    public boolean newUserEntryValidator(AppUser user){

        // Matcher for checking that the username matches the given pattern
        Matcher userMatch = usernamePattern.matcher(user.getUsername());
        // Matcher for checking that the email matches the given pattern
        Matcher emailMatch = emailPattern.matcher(user.getEmail());

        if(user.getUsername().trim().equals("")||user.getPassword().trim().equals("")||user.getFirstName().trim().equals("")
                ||user.getLastName().trim().equals("")||user.getEmail().trim().equals(""))
        {
            System.out.println("Fields cannot be blank!");
            throw new InvalidEntryException("Blank fields detected.");
        }
        else if(user.getUsername().length() < MIN_USERNAME)
        {
            System.out.println("Username must be at least 4 characters.");
            throw new InvalidUsernameException("Entered username below the minimum character limit.");
        }
        else if(userMatch.find())
        {
            System.out.println("Username contains invalid characters.");
            throw  new InvalidUsernameException("Invalid characters entered in username.");
        }
        else if(user.getPassword().length() < MIN_PASSWORD)
        {
            System.out.println("Password must be at least 8 characters.");
            throw new InvalidPasswordException("Entered password below the minimum character limit.");
        }
        else if(!emailMatch.find())
        {
            System.out.println("Please enter a valid email address.");
            throw new InvalidEmailException("Invalid email address entered.");
        }

        return true;
    }

    // Validator for adding a new Course
    public boolean newCourseEntryValidator(Course course){

        if(course.getCourseName().trim().equals("")||course.getCourseAbbreviation().trim().equals("")||course.getCourseDetail().trim().equals(""))
        {
            System.out.println("Fields cannot be blank!");
            throw new InvalidEntryException("Blank fields detected.");
        }
        else if(course.getCourseName().length() > MAX_COURSE_NAME)
        {
            System.out.printf("Please keep course name concise and under %d characters.%n", MAX_COURSE_NAME);
            throw new InvalidCourseNameException("Course name too long.");
        }
        else if(course.getCourseName().length() < course.getCourseAbbreviation().length())
        {
            System.out.println("Please make your course name longer than the abbreviation.");
            throw new InvalidCourseNameException("Course name too short.");
        }
        else if(course.getCourseAbbreviation().length() > MAX_COURSE_ABV)
        {
            System.out.printf("Please keep the course Abbreviation to %d characters or less.%n", MAX_COURSE_ABV);
            throw new InvalidCourseAbbreviationException("Course Abbreviation too long.");
        }
        else if(course.getCourseDetail().length() < MIN_COURSE_DESC)
        {
            System.out.printf("Course description must be at least %d characters long.%nPlease provide more details about the course.%n"
                    , MIN_COURSE_DESC);
            throw new InvalidCourseDescriptionException("Course description too short.");
        }
        else if(course.getCourseDetail().length() > MAX_COURSE_DESC)
        {
            System.out.printf("Course description is too long.%nPlease enter a more concise description under %d characters."
                    , MAX_COURSE_DESC);
            throw new InvalidCourseDescriptionException("Course description too long.");
        }

        return true;

    }

    // Validator for Editing Course Values

    public boolean newCourseNameValidator(Course original, String newName){

        if(newName.trim().equals(""))
        {
            System.out.println("Fields cannot be blank!");
            throw new InvalidEntryException("Blank fields detected.");
        }
        else if(newName.length() > MAX_COURSE_NAME)
        {
            System.out.printf("Please keep course name concise and under %d characters.%n", MAX_COURSE_NAME);
            throw new InvalidCourseNameException("Course name too long.");
        }
        else if(newName.length() < original.getCourseAbbreviation().length())
        {
            System.out.println("Please make your course name longer than the abbreviation.");
            throw new InvalidCourseNameException("Course name too short.");
        }
        return true;
    }

    public boolean newCourseAbvValidator(Course original, String newAbv){
        if(newAbv.trim().equals(""))
        {
            System.out.println("Fields cannot be blank!");
            throw new InvalidEntryException("Blank fields detected.");
        }
        else if(newAbv.length() > MAX_COURSE_ABV)
        {
            System.out.printf("Please keep the course Abbreviation to %d characters or less.%n", MAX_COURSE_ABV);
            throw new InvalidCourseAbbreviationException("Course Abbreviation too long.");
        }else if(newAbv.length() > original.getCourseName().length())
        {
            System.out.println("Please make your new Abbreviation shorter than the course name.");
            throw new InvalidCourseAbbreviationException("Course abbreviation too long.");
        }
        return true;
    }

    public boolean newCourseDetailsValidator(String newDetails){

        if(newDetails.trim().equals(""))
        {
            System.out.println("Fields cannot be blank!");
            throw new InvalidEntryException("Blank fields detected.");
        }
        else if(newDetails.length() < MIN_COURSE_DESC)
        {
            System.out.printf("Course description must be at least %d characters long.%nPlease provide more details about the course.%n"
                    , MIN_COURSE_DESC);
            throw new InvalidCourseDescriptionException("Course description too short.");
        }
        else if(newDetails.length() > MAX_COURSE_DESC)
        {
            System.out.printf("Course description is too long.%nPlease enter a more concise description under %d characters."
                    , MAX_COURSE_DESC);
            throw new InvalidCourseDescriptionException("Course description too long.");
        }
        return true;
    }
}
