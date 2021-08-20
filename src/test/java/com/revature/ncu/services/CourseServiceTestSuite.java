package com.revature.ncu.services;

import com.revature.ncu.datasources.documents.Course;
import com.revature.ncu.datasources.repositories.CourseRepository;
import com.revature.ncu.util.exceptions.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class CourseServiceTestSuite {

    // Instantiate system under test
    private CourseService sut;

    // Mock dependencies required for the system under test
    private CourseRepository mockCourseRepo;
    private CourseValidatorService mockValidator;

    @Before
    public void setup(){
        mockCourseRepo = mock(CourseRepository.class);
        mockValidator = mock(CourseValidatorService.class);
        sut = new CourseService(mockCourseRepo, mockValidator);
    }

    @After
    public void cleanUp(){
        mockCourseRepo = null;
        mockValidator = null;
        sut = null;
    }


    // add tests

    @Test
    public void add_returnsSuccessfully_whenGivenValidCourse(){
        //Arrange
        Course expectedResult = new Course("ValidCourse","VLDCS",
                "This is a valid course description");
        Course validCourse = new Course("ValidCourse","VLDCS",
                "This is a valid course description");
        when(mockValidator.newCourseEntryValidator(validCourse)).thenReturn(true);
        when(mockCourseRepo.findCourseByName(anyString())).thenReturn(null);
        when(mockCourseRepo.findCourseByAbbreviation(anyString())).thenReturn(null);
        when(mockCourseRepo.save(validCourse)).thenReturn(expectedResult);

        //Act
        Course actualResult = sut.add(validCourse);

        //Assert
        Assert.assertEquals(expectedResult,actualResult);
        verify(mockValidator,times(1)).newCourseEntryValidator(validCourse);
        verify(mockCourseRepo,times(1)).findCourseByName(anyString());
        verify(mockCourseRepo,times(1)).findCourseByAbbreviation(anyString());
        verify(mockCourseRepo,times(1)).save(validCourse);

    }

    @Test(expected = ResourcePersistenceException.class)
    public void add_throwsException_whenGivenCourse_withDuplicateCourseName(){
        //Arrange
        Course existingCourse = new Course("Duplicate","ORGNL",
                "Original course.");
        Course duplicate = new Course("Duplicate","TEST",
                "This course has a duplicate name");
        when(mockCourseRepo.findCourseByName(duplicate.getCourseName())).thenReturn(existingCourse);

        //Act
        try {
            sut.add(duplicate);
        }finally {

            //Assert
            verify(mockCourseRepo, times(1)).findCourseByName(duplicate.getCourseName());
            verify(mockCourseRepo, times(0)).save(duplicate);
        }

    }

    @Test(expected = ResourcePersistenceException.class)
    public void add_throwsException_whenGivenCourse_withDuplicateCourseAbbreviation(){
        //Arrange
        Course existingCourse = new Course("Original","DUPE",
                "Original course.");
        Course duplicate = new Course("Tester","DUPE",
                "This course has a duplicate abbreviation");
        when(mockCourseRepo.findCourseByAbbreviation(duplicate.getCourseAbbreviation())).thenReturn(existingCourse);

        //Act
        try {
            sut.add(duplicate);
        }finally {

            //Assert
            verify(mockCourseRepo, times(1))
                    .findCourseByAbbreviation(duplicate.getCourseAbbreviation());
            verify(mockCourseRepo, times(0)).save(duplicate);
        }

    }

    @Test(expected = InvalidEntryException.class)
    public void add_throwsException_whenGivenCourse_withEmptyValues(){
        //Arrange
        Course invalidCourse = new Course("","","");
        when(mockValidator.newCourseEntryValidator(invalidCourse)).thenThrow(InvalidEntryException.class);
        //Act
        try {
            sut.add(invalidCourse);
        }finally { //Assert
            verify(mockValidator,times(1)).newCourseEntryValidator(invalidCourse);
        }


    }

    //update tests

//    @Test
//    public void updateCourseName_ExecutesSuccessfully_whenProvidedValidCourseName(){
//        //Arrange
//        String validName = "ValidName";
//        Course originalCourse = new Course("ValidCourse","VLDCS",
//                "This is a valid course description");
//        when(mockValidator.newCourseNameValidator(originalCourse,validName)).thenReturn(true);
//        when(mockCourseRepo.findCourseByName(validName)).thenReturn(null);
//
//        //Act
//        sut.updateCourseName(originalCourse,validName);
//
//        //Assert
//        verify(mockValidator, times(1)).newCourseNameValidator(originalCourse,validName);
//        verify(mockCourseRepo,times(1)).findCourseByName(validName);
//        verify(mockCourseRepo, times(1)).updatingCourseName(originalCourse,validName);
//    }
//
//    @Test(expected = ResourcePersistenceException.class)
//    public void updateCourseName_throwsException_whenProvidedDuplicateCourseName(){
//        // Arrange
//        String duplicate = "Duplicate";
//        Course originalCourse = new Course("Duplicate","VLDCS",
//                "This is a valid course description");
//        when(mockValidator.newCourseNameValidator(originalCourse,duplicate)).thenReturn(true);
//        when(mockCourseRepo.findCourseByName(duplicate)).thenReturn(originalCourse);
//        // Act
//        try{
//            sut.updateCourseName(originalCourse,duplicate);
//        }finally {        // Assert
//            verify(mockValidator, times(1)).newCourseNameValidator(originalCourse,duplicate);
//            verify(mockCourseRepo,times(1)).findCourseByName(duplicate);
//            verify(mockCourseRepo, times(0)).updatingCourseName(originalCourse,duplicate);
//        }
//
//    }
//
//    @Test
//    public void updateCourseAbbreviation_ExecutesSuccessfully_whenProvidedValidCourseAbbreviation(){
//        //Arrange
//        String validAbv = "VLDABV";
//        Course originalCourse = new Course("ValidCourse","VLDCS",
//                "This is a valid course description");
//        when(mockValidator.newCourseAbvValidator(originalCourse,validAbv)).thenReturn(true);
//        when(mockCourseRepo.findCourseByAbbreviation(validAbv)).thenReturn(null);
//
//        //Act
//        sut.updateCourseAbv(originalCourse,validAbv);
//
//        //Assert
//        verify(mockValidator, times(1)).newCourseAbvValidator(originalCourse,validAbv);
//        verify(mockCourseRepo,times(1)).findCourseByAbbreviation(validAbv);
//        verify(mockCourseRepo, times(1)).updatingCourseAbv(originalCourse,validAbv);
//    }
//
//    @Test(expected = ResourcePersistenceException.class)
//    public void updateCourseAbv_throwsException_whenProvidedDuplicateCourseAbv(){
//        // Arrange
//        String duplicate = "DUPE";
//        Course originalCourse = new Course("Original","DUPE",
//                "This is a valid course description");
//        when(mockValidator.newCourseAbvValidator(originalCourse,duplicate)).thenReturn(true);
//        when(mockCourseRepo.findCourseByAbbreviation(duplicate)).thenReturn(originalCourse);
//        // Act
//        try{
//            sut.updateCourseAbv(originalCourse,duplicate);
//        }finally {        // Assert
//            verify(mockValidator, times(1)).newCourseAbvValidator(originalCourse,duplicate);
//            verify(mockCourseRepo,times(1)).findCourseByAbbreviation(duplicate);
//            verify(mockCourseRepo, times(0)).updatingCourseAbv(originalCourse,duplicate);
//        }
//
//    }
//
//    @Test
//    public void updateCourseDesc_ExecutesSuccessfully_whenProvidedValidCourseDesc(){
//        //Arrange
//        String validDescription = "This is also a valid course description.";
//        Course originalCourse = new Course("ValidCourse","VLDCS",
//                "This is a valid course description");
//        when(mockValidator.newCourseDetailsValidator(validDescription)).thenReturn(true);
//
//        //Act
//        sut.updateCourseDesc(originalCourse,validDescription);
//
//        //Assert
//        verify(mockValidator, times(1)).newCourseDetailsValidator(validDescription);
//        verify(mockCourseRepo, times(1)).updatingCourseDesc(originalCourse,validDescription);
//    }

    // verifyCourse tests

    @Test
    public void verifyCourse_returnsCourse_whenProvidedWithValidAbbreviation(){
        // Arrange
        String validAbv = "VALID";
        Course expectedResult = new Course("ValidCourse","VALID",
                "This is a valid course description");
        Course validCourse = new Course("ValidCourse","VALID",
                "This is a valid course description");
        when(mockCourseRepo.findCourseByAbbreviation(validAbv)).thenReturn(validCourse);
        // Act
        Course actualResult = sut.findCourseByAbbreviation(validAbv);
        // Assert
        verify(mockCourseRepo, times(1)).findCourseByAbbreviation(validAbv);
        Assert.assertEquals(expectedResult,actualResult);

    }

    @Test(expected = ResourcePersistenceException.class)
    public void verifyCourse_throwsException_whenProvidedWithAbbreviation_thatDoesNotExist(){
        // Arrange
        String invalidAbv = "HELP";
        when(mockCourseRepo.findCourseByAbbreviation(invalidAbv)).thenReturn(null);

        // Act
        try {
            sut.findCourseByAbbreviation(invalidAbv);
        } finally {// Assert
            verify(mockCourseRepo,times(1)).findCourseByAbbreviation(invalidAbv);
        }
    }

    @Test(expected = InvalidEntryException.class)
    public void verifyCourse_throwsException_whenProvidedWithBlankAbbreviation(){
        // Arrange
        String invalidAbv = "";
        // Act
        sut.findCourseByAbbreviation(invalidAbv);
    }

    //verifyCourseOpenByAbbreviation Tests

    @Test
    public void verifyCourseOpenByAbbreviation_returnsCourse_whenProvidedValidAbbreviation(){
        // Arrange
        String validAbv = "VALID";
        Course expectedResult = new Course("ValidCourse","VALID",
                "This is a valid course description");
        Course validCourse = new Course("ValidCourse","VALID",
                "This is a valid course description");
        when(mockCourseRepo.findCourseByAbbreviation(validAbv)).thenReturn(validCourse);
        // Act
        Course actualResult = sut.verifyCourseOpenByAbbreviation(validAbv);
        // Assert
        Assert.assertEquals(expectedResult,actualResult);
        verify(mockCourseRepo,times(1)).findCourseByAbbreviation(validAbv);
    }

    @Test(expected = CourseNotOpenException.class)
    public void verifyCourseOpenByAbbreviation_throwsException_whenCourseClosed(){
        // Arrange
        String closedAbv = "TEST";
        Course closedCourse = new Course("ValidCourse","TEST",
                "This test will fail because open is false.");
        when(mockCourseRepo.findCourseByAbbreviation(closedAbv)).thenReturn(closedCourse);
        // Act
        try{sut.verifyCourseOpenByAbbreviation(closedAbv);}
        finally { // Assert
            verify(mockCourseRepo, times(1)).findCourseByAbbreviation(closedAbv);
        }
    }

    @Test(expected = NoSuchCourseException.class)
    public void verifyCourseOpenByAbbreviation_throwsException_whenCourseNotFound(){
        // Arrange
        String invalidAbv = "TEST";
        when(mockCourseRepo.findCourseByAbbreviation(invalidAbv)).thenReturn(null);
        // Act
        try{sut.verifyCourseOpenByAbbreviation(invalidAbv);}
        finally { // Assert
            verify(mockCourseRepo, times(1)).findCourseByAbbreviation(invalidAbv);
        }
    }

    @Test(expected = InvalidEntryException.class)
    public void verifyCourseOpenByAbbreviation_throwsException_whenProvidedEmptyInput(){
        // Arrange
        String invalidAbv = "";
        // Act
        sut.verifyCourseOpenByAbbreviation(invalidAbv);

    }

    //verifyCourseOpenByName Tests
    @Test
    public void verifyCourseOpenByName_returnsCourse_whenProvidedValidName(){
        // Arrange
        String validName = "ValidCourse";
        Course expectedResult = new Course("ValidCourse","VALID",
                "This is a valid course description");
        Course validCourse = new Course("ValidCourse","VALID",
                "This is a valid course description");
        when(mockCourseRepo.findCourseByName(validName)).thenReturn(validCourse);
        // Act
        Course actualResult = sut.verifyCourseOpenByName(validName);
        // Assert
        Assert.assertEquals(expectedResult,actualResult);
        verify(mockCourseRepo,times(1)).findCourseByName(validName);
    }

    @Test(expected = CourseNotOpenException.class)
    public void verifyCourseOpenByName_throwsException_whenCourseClosed(){
        // Arrange
        String closedName = "Closed-course";
        Course closedCourse = new Course("Closed-course","TEST",
                "This test will fail because open is false.");
        when(mockCourseRepo.findCourseByName(closedName)).thenReturn(closedCourse);
        // Act
        try{sut.verifyCourseOpenByName(closedName);}
        finally { // Assert
            verify(mockCourseRepo, times(1)).findCourseByName(closedName);
        }
    }

    @Test(expected = NoSuchCourseException.class)
    public void verifyCourseOpenByName_throwsException_whenCourseNotFound(){
        // Arrange
        String invalidName = "Spelunking 101";
        when(mockCourseRepo.findCourseByName(invalidName)).thenReturn(null);
        // Act
        try{sut.verifyCourseOpenByName(invalidName);}
        finally { // Assert
            verify(mockCourseRepo, times(1)).findCourseByName(invalidName);
        }
    }

    @Test(expected = InvalidEntryException.class)
    public void verifyCourseOpenByName_throwsException_whenProvidedEmptyInput(){
        // Arrange
        String invalidName = "";
        // Act
        sut.verifyCourseOpenByName(invalidName);

    }

    //getCourses test

    @Test
    public void getCourses_returnsListOfOpenCourses_whenListIsPopulated(){
        // Arrange
        Course testCourse1 = new Course("","","");
        Course testCourse2 = new Course("","","");
        List<Course> validCourseList = new ArrayList<Course>();
            validCourseList.add(testCourse1);
            validCourseList.add(testCourse2);
        when(mockCourseRepo.retrieveOpenCourses()).thenReturn(validCourseList);
        // Act
        sut.getCourses();
        // Assert
        verify(mockCourseRepo,times(1)).retrieveOpenCourses();
    }

    @Test(expected = NoOpenCoursesException.class)
    public void getCourses_throwsException_whenNoOpenCoursesFound(){
        // Arrange
        List<Course> emptyCourseList = new ArrayList<Course>();
        when(mockCourseRepo.retrieveOpenCourses()).thenReturn(emptyCourseList);
        // Act
        sut.getCourses();

    }



}
