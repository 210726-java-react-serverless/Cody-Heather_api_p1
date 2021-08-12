package com.revature.projectzero.services;

import com.revature.projectzero.util.UserSession;
import com.revature.projectzero.util.exceptions.AuthenticationException;
import com.revature.projectzero.documents.AppUser;
import com.revature.projectzero.repositories.UserRepository;
import com.revature.projectzero.util.exceptions.ResourcePersistenceException;

// Service for handling most User-related business logic and passing information into the User repository

public class UserService {

    private final UserRepository userRepo;
    private final UserSession session;
    private final InputValidatorService inputValidatorService;

    // Injecting Dependencies
    public UserService(UserRepository userRepo, UserSession session, InputValidatorService inputValidatorService) {
        this.userRepo = userRepo;
        this.session = session;
        this.inputValidatorService = inputValidatorService;
    }

    // Register a new user
    public AppUser register(AppUser newUser) {

        // Throws exception if entry is invalid
        inputValidatorService.newUserEntryValidator(newUser);

        if (userRepo.findUserByUsername(newUser.getUsername()) != null)
        {
            throw new ResourcePersistenceException("Provided username is already taken!");
        }
        else if (userRepo.findUserByEmail(newUser.getEmail()) != null)
        {
            throw new ResourcePersistenceException("Provided email is already taken!");
        }
        session.setCurrentUser(newUser);
        return userRepo.save(newUser);

    }

    // Validate user credentials and log them in
    public AppUser login(String username, String password) {

        AppUser authUser = userRepo.findUserByCredentials(username, password);

        if (authUser == null) {
            throw new AuthenticationException("Invalid credentials provided!");
        }

        session.setCurrentUser(authUser);

        return authUser;
    }

    // Getter
    public UserSession getSession() {
        return session;
    }
}
