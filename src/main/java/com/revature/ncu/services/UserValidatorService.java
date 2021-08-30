package com.revature.ncu.services;

import com.revature.ncu.datasources.documents.AppUser;
import com.revature.ncu.util.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  Class for validating user input to check for:
 *  Empty values,
 *  Password security, (Minimum of 8 characters)
 *  Username length, (Minimum of 4 characters)
 *  invalid username characters, (no symbols in username)
 *
 */

public class UserValidatorService {

    private final Logger logger = LoggerFactory.getLogger(UserValidatorService.class);

    // User restrictions are defined here.
    private static final int MIN_USERNAME = 4;
    private static final int MIN_PASSWORD = 8;

    // Pattern for username verification, can be any character from a-z, A-Z, and 0-9. No symbols permitted.
    static Pattern usernamePattern = Pattern.compile("[^a-zA-Z0-9]");

    // Pattern for simple email verification.
    static Pattern emailPattern = Pattern.compile("^(.+)@(.+)$");

    // Validator for student registration
    public boolean newUserEntryValidator(AppUser user){

        // Matcher for checking that the username matches the given pattern
        Matcher userMatch = usernamePattern.matcher(user.getUsername());
        // Matcher for checking that the email matches the given pattern
        Matcher emailMatch = emailPattern.matcher(user.getEmail());

        if(user.getUsername().trim().equals("")||user.getPassword().trim().equals("")||user.getFirstName().trim().equals("")
                ||user.getLastName().trim().equals("")||user.getEmail().trim().equals("")) {
            logger.error("Blank fields detected.");
            throw new InvalidEntryException("Fields cannot be blank!");
        }
        else if(user.getUsername().length() < MIN_USERNAME) {
            logger.error("Entered username below the minimum character limit.");
            throw new InvalidUsernameException("Username must be at least 4 characters.");
        }
        else if(userMatch.find()) {
            logger.error("Invalid characters entered in username.");
            throw  new InvalidUsernameException("Username contains invalid characters.");
        }
        else if(user.getPassword().length() < MIN_PASSWORD) {
            logger.error("Entered password below the minimum character limit.");
            throw new InvalidPasswordException("Password must be at least 8 characters.");
        }
        else if(!emailMatch.find()) {
            logger.error("Invalid email address entered.");
            throw new InvalidEmailException("Please enter a valid email address.");
        }

        return true;
    }

}
