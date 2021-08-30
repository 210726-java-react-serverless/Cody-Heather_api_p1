package com.revature.ncu.services;

import com.revature.ncu.datasources.documents.AppUser;
import com.revature.ncu.datasources.repositories.UserRepository;
import com.revature.ncu.util.PasswordUtils;
import com.revature.ncu.util.exceptions.*;
import com.revature.ncu.web.dtos.AppUserDTO;
import com.revature.ncu.web.dtos.Principal;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class UserServiceTestSuite {

    // Instantiate system under test
    private UserService sut;

    // Mock dependencies required for the system under test
    private UserRepository mockUserRepo;
    private UserValidatorService mockValidator;
    private PasswordUtils mockPasswordUtils;

    // Initialize objects before testing
    @Before
    public void setup(){
        mockUserRepo = mock(UserRepository.class);
        mockValidator = mock(UserValidatorService.class);
        mockPasswordUtils = mock(PasswordUtils.class);
        sut = new UserService(mockUserRepo, mockValidator, mockPasswordUtils);
    }

    // Clear out objects after testing by setting them to null
    @After
    public void cleanUp(){
        mockUserRepo = null;
        mockValidator = null;
        mockPasswordUtils = null;
        sut = null;
    }


    // register tests
    @Test
    public void register_executesSuccessfully_whenGivenValidUser() {
        //Arrange
        AppUser expected = new AppUser("valid","valid","valid@valid.com","valid","valid",false);
        AppUser validUser = new AppUser("valid","valid","valid@valid.com","valid","encryptedPassword",false);
        when(mockValidator.newUserEntryValidator(validUser)).thenReturn(true);
        when(mockUserRepo.findUserByUsername(validUser.getUsername())).thenReturn(null);
        when(mockUserRepo.findUserByEmail(validUser.getEmail())).thenReturn(null);
        when(mockPasswordUtils.generateSecurePassword(validUser.getPassword())).thenReturn("encryptedPassword");
        when(mockUserRepo.save(validUser)).thenReturn(expected);

        //Act
        AppUser result = sut.register(validUser);

        //Assert
        Assert.assertEquals(expected,result);
        verify(mockValidator, times(1)).newUserEntryValidator(validUser);
        verify(mockUserRepo, times(1)).findUserByUsername(anyString());
        verify(mockUserRepo, times(1)).findUserByEmail(anyString());
        verify(mockPasswordUtils,times(1)).generateSecurePassword(anyString());
        verify(mockUserRepo,times(1)).save(validUser);

    }

    @Test(expected = ResourcePersistenceException.class)
    public void register_throwsException_whenUsernameAlreadyExists() {
        // Assert
        AppUser validUser = new AppUser("valid","valid","valid@valid.com","valid","encryptedPassword",false);
        AppUser existingUser = new AppUser("valid","valid","valid@valid.com","valid","valid",false);
        when(mockValidator.newUserEntryValidator(validUser)).thenReturn(true);
        when(mockUserRepo.findUserByUsername(anyString())).thenReturn(existingUser);

        //Act
        try {
         sut.register(validUser);
        }finally {//Assert
            verify(mockValidator, times(1)).newUserEntryValidator(validUser);
            verify(mockUserRepo, times(1)).findUserByUsername(anyString());
        }

    }

    @Test(expected = ResourcePersistenceException.class)
    public void register_throwsException_whenEmailIsAlreadyExists() {
        //A
        AppUser validUser = new AppUser("valid","valid","valid@valid.com","valid","encryptedPassword",false);
        AppUser existingUser = new AppUser("valid","valid","valid@valid.com","valid","valid",false);
        when(mockValidator.newUserEntryValidator(validUser)).thenReturn(true);
        when(mockUserRepo.findUserByUsername(anyString())).thenReturn(null);
        when(mockUserRepo.findUserByEmail(validUser.getEmail())).thenReturn(existingUser);

        //Act
        try {
            AppUser result = sut.register(validUser);

        } finally {//Assert
            verify(mockValidator, times(1)).newUserEntryValidator(validUser);
            verify(mockUserRepo, times(1)).findUserByUsername(anyString());
            verify(mockUserRepo, times(1)).findUserByEmail(anyString());
        }

    }

    // findAll tests
    @Test
    public void findAll_successfullyReturns_AppUserDTOList(){
        // Arrange
        List<AppUser> returnedList = new ArrayList<>();
        when(mockUserRepo.findAll()).thenReturn(returnedList);

        List<AppUserDTO> expectedList = new ArrayList<>();
        // Act
        List<AppUserDTO> actualList = sut.findAll();
        // Assert
        Assert.assertEquals(actualList,expectedList);
    }

    // findUserById tests
    @Test
    public void findUserById_executesSuccessfully_whenGiveValidString() {
        //Arrange
        String id = "validId";
        AppUser existingUser = new AppUser("valid","valid","valid@valid","valid","valid",false);
        existingUser.setId(id);
        AppUserDTO expected = new AppUserDTO(existingUser);
        when(mockUserRepo.findById(id)).thenReturn(existingUser);
        //Act
        AppUserDTO result = sut.findUserById(id);
        //Assert
        Assert.assertEquals(result, expected);
        verify(mockUserRepo,times(1)).findById(id);
    }

    @Test(expected = InvalidRequestException.class)
    public void findUserById_throwsException_whenFieldIsLeftBlank() {
        //Arrange
        String id = "";
        AppUser existingUser = new AppUser("valid","valid","valid@valid","valid","valid",false);
        existingUser.setId(id);
        when(mockUserRepo.findById(id)).thenReturn(existingUser);
        //Act
        sut.findUserById(id);
        //Assert

    }

    @Test(expected = ResourceNotFoundException.class)
    public void findUserById_throwsException_whenUserNotFound() {
        //Arrange
        String id = "validId";
        AppUser existingUser = new AppUser("valid","valid","valid@valid","valid","valid",false);
        existingUser.setId(id);
        when(mockUserRepo.findById(id)).thenReturn(null);
        //Act
        try {
            sut.findUserById(id);
        }finally { //Assert
            verify(mockUserRepo, times(1)).findById(id);
        }
    }



    // login tests
    @Test
    public void login_returnsPrincipal_whenUser_providesValidCredentials(){
        // Arrange
        String username = "username";
        String password = "password";
        AppUser authUser = new AppUser("valid","user","valid@user.com","username","encryptedPassword", false);
        Principal expectedResult = new Principal(authUser);
        when(mockPasswordUtils.generateSecurePassword(password)).thenReturn("encryptedPassword");
        when(mockUserRepo.findUserByCredentials(username, "encryptedPassword")).thenReturn(authUser);
        // Act
        Principal actualResult = sut.login(username, password);
        // Assert
        Assert.assertEquals(expectedResult,actualResult);
    }

    @Test(expected = AuthenticationException.class)
    public void login_throwsException_whenUser_providesInvalidCredentials(){
        // Arrange
        String username = "username";
        String password = "password";
        when(mockPasswordUtils.generateSecurePassword(password)).thenReturn("encryptedPassword");
        when(mockUserRepo.findUserByCredentials(username, "encryptedPassword")).thenReturn(null);
        // Act
        try {
            sut.login(username, password);
        }finally{
            verify(mockPasswordUtils, times(1)).generateSecurePassword(password);
        }

    }


    // getProfNameById tests
    @Test
    public void getProfNameById_executesSuccessfully_whenGivenAValidId(){
        //Arrange
        String profId = "validID";
        String expected = "validID";
        when(mockUserRepo.getProfName(profId)).thenReturn(profId);
        //Act
        String result = sut.getProfNameById(profId);
        //Assert
        Assert.assertEquals(result, expected);
        verify(mockUserRepo,times(1)).getProfName(profId);
    }






}
