package com.revature.projectzero.services;

import com.revature.projectzero.documents.AppUser;
import com.revature.projectzero.repositories.UserRepository;
import com.revature.projectzero.util.UserSession;
import com.revature.projectzero.util.exceptions.AuthenticationException;
import com.revature.projectzero.util.exceptions.InvalidEntryException;
import com.revature.projectzero.util.exceptions.ResourcePersistenceException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import static org.mockito.Mockito.*;

public class UserServiceTestSuite {

    // Instantiate system under test
    private UserService sut;

    // Mock dependencies required for the system under test
    private UserRepository mockUserRepo;
    private UserSession mockSession;
    private InputValidatorService mockValidator;

    // Initialize objects before testing
    @Before
    public void setup(){

        mockUserRepo = mock(UserRepository.class);
        mockSession = mock(UserSession.class);
        mockValidator = mock(InputValidatorService.class);
        sut = new UserService( mockUserRepo, mockSession, mockValidator);

    }

    // Clear out objects after testing by setting them to null
    @After
    public void cleanUp(){
        mockUserRepo = null;
        mockSession = null;
        sut = null;
    }


    @Test
    public void register_returnsSuccessfully_whenGivenValidUser(){
        //Arrange
        AppUser expectedResult = new AppUser("test", "test", "te.st@test.test", "test", "test.test",false);
        AppUser validUser = new AppUser("test", "test", "te.st@test.test", "test", "test.test",false);
        when(mockValidator.newUserEntryValidator(validUser)).thenReturn(true);
        when(mockUserRepo.findUserByUsername(anyString())).thenReturn(null);
        when(mockUserRepo.findUserByEmail(anyString())).thenReturn(null);
        when(mockUserRepo.save(validUser)).thenReturn(expectedResult);

        //Act
        AppUser actualResult = sut.register(validUser);

        //Assert
        Assert.assertEquals(expectedResult,actualResult);
        verify(mockValidator,times(1)).newUserEntryValidator(validUser);
        verify(mockUserRepo,times(1)).findUserByUsername(anyString());
        verify(mockUserRepo,times(1)).findUserByEmail(anyString());
        verify(mockUserRepo,times(1)).save(validUser);

    }

    @Test(expected = ResourcePersistenceException.class)
    public void register_throwsException_whenGivenUser_withDuplicateUsername() {

        // Arrange
        AppUser existingUser = new AppUser("original", "original", "original", "duplicate", "original",false);
        AppUser duplicate = new AppUser("first", "last", "email", "duplicate", "password",false);
        when(mockUserRepo.findUserByUsername(duplicate.getUsername())).thenReturn(existingUser);

        // Act
        try {
            sut.register(duplicate);
        } finally {
            // Assert
            verify(mockUserRepo, times(1)).findUserByUsername(duplicate.getUsername());
            verify(mockUserRepo, times(0)).save(duplicate);
        }

    }

    @Test(expected = ResourcePersistenceException.class)
    public void register_throwsException_whenGivenUser_withDuplicateEmail() {

        // Arrange
        AppUser existingUser = new AppUser("original", "original", "duplicate", "original", "original",false);
        AppUser duplicate = new AppUser("first", "last", "duplicate", "username", "password",false);
        when(mockUserRepo.findUserByUsername(duplicate.getUsername())).thenReturn(null);
        when(mockUserRepo.findUserByEmail(duplicate.getEmail())).thenReturn(existingUser);


        // Act
        try {
            sut.register(duplicate);
        } finally {
            // Assert
            verify(mockUserRepo, times(1)).findUserByUsername(duplicate.getUsername());
            verify(mockUserRepo, times(0)).save(duplicate);
        }

    }

    @Test(expected = InvalidEntryException.class)
    public void register_throwsException_whenGivenUser_withBlankValues(){
        //Arrange
        AppUser invalidUser = new AppUser("","","","","",false);
        when(mockValidator.newUserEntryValidator(invalidUser)).thenThrow(InvalidEntryException.class);

        //Act
        try{
            sut.register(invalidUser);
        } finally{
            // Assert
            verify(mockValidator, times(1)).newUserEntryValidator(invalidUser);
            verify(mockUserRepo, times(0)).save(invalidUser);
        }

    }

    @Test
    public void login_returnsSuccessfully_whenGivenValidCredentials(){
        // Arrange
        String validUsername = "valid";
        String validPassword = "valid-password";
        AppUser expectedUser = new AppUser("test","tester","test@test.test","valid","valid-password",false);
        when(mockUserRepo.findUserByCredentials(validUsername,validPassword)).thenReturn(expectedUser);

        // Act
        AppUser actualUser = sut.login(validUsername, validPassword);

        // Assert
        Assert.assertEquals(expectedUser,actualUser);
        verify(mockUserRepo, times(1)).findUserByCredentials(validUsername,validPassword);
        verify(mockSession, times(1)).setCurrentUser(actualUser);


    }

    @Test(expected = AuthenticationException.class)
    public void login_throwsException_whenProvidedWith_InvalidCredentials(){
        // Arrange
        String invalidUsername = "invalid";
        String invalidPassword = "invalid-password";
        when(mockUserRepo.findUserByCredentials(invalidUsername,invalidPassword)).thenReturn(null);
        // Act
        sut.login(invalidUsername, invalidPassword);
    }





}
