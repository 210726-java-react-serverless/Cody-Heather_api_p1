package com.revature.ncu.services;

import com.revature.ncu.datasources.documents.Course;
import com.revature.ncu.datasources.repositories.CourseRepository;
import com.revature.ncu.util.exceptions.*;
import com.revature.ncu.web.dtos.UserCourseDTO;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


    // removeCourse Tests
    @Test
    public void removeCourse_executesSuccessfully_whenProvidedWithValidCourse(){
        //Arrange
        Course validCourse = new Course("VALID");
        Course expectedCourse = new Course("VALID");
        when(mockCourseRepo.findCourseByAbbreviation(validCourse.getCourseAbbreviation()))
                .thenReturn(expectedCourse);

        //Act
        sut.removeCourse(validCourse);

        //Assert
        verify(mockCourseRepo, times(1))
                .findCourseByAbbreviation(validCourse.getCourseAbbreviation());
        verify(mockCourseRepo, times(1)).removeCourseByAbbreviation(validCourse);

    }

    @Test(expected = InvalidEntryException.class)
    public void removeCourse_throwsException_whenGivenInvalidCourseAbbreviation() {
        //Arrange
        Course invalidCourse = new Course("NoVALID");
        when(mockCourseRepo.findCourseByAbbreviation(invalidCourse.getCourseAbbreviation()))
                .thenReturn(null);
        //Act
        sut.removeCourse(invalidCourse);
    }

    //update tests

    @Test
    public void updateCourse_returnsCourse_whenProvidedWithValidCourse(){
        // Arrange
        Course original = new Course("ValidCourse","VLD101","This is a valid course.",
                LocalDate.parse("2021-11-11"),LocalDate.parse("2021-11-13"),13);

        Course validUpdate = new Course("ValidUpdateCourse","VLD201","This is a valid changed course detail.",
                LocalDate.parse("2021-10-11"),LocalDate.parse("2022-10-13"),42);
        when(mockValidator.courseUpdateValidator(original,validUpdate)).thenReturn(true);
        when(mockCourseRepo.findCourseByAbbreviation("VLD201")).thenReturn(null);
        when(mockCourseRepo.findCourseByName("ValidUpdateCourse")).thenReturn(null);
        // Act
        Course actualResult = sut.updateCourse(original, validUpdate);
        // Assert
        Assert.assertEquals(actualResult,validUpdate);
    }

    @Test(expected = ResourcePersistenceException.class)
    public void updateCourse_throwsException_whenProvidedWithCourse_withDuplicateAbbreviation(){
        // Arrange
        Course original = new Course("ValidCourse","VLD101","This is a valid course.",
                LocalDate.parse("2021-11-11"),LocalDate.parse("2021-11-13"),13);

        Course duplicateAbv = new Course("Duplicate","DUPE","This is a valid changed course detail.",
                LocalDate.parse("2021-10-11"),LocalDate.parse("2022-10-13"),42);

        Course existingCourse = new Course("ValidUpdateCourse","DUPE","This is a valid changed course detail.",
                LocalDate.parse("2021-10-11"),LocalDate.parse("2022-10-13"),42);

        when(mockValidator.courseUpdateValidator(original,duplicateAbv)).thenReturn(true);
        when(mockCourseRepo.findCourseByAbbreviation("DUPE")).thenReturn(existingCourse);
        // Act
        try{
            sut.updateCourse(original, duplicateAbv);
        }finally {// Assert
            verify(mockValidator,times(1)).courseUpdateValidator(original,duplicateAbv);
            verify(mockCourseRepo,times(1)).findCourseByAbbreviation("DUPE");
        }
    }

    @Test(expected = ResourcePersistenceException.class)
    public void updateCourse_throwsException_whenProvidedWithCourse_withDuplicateName(){
        // Arrange
        Course original = new Course("ValidCourse","VLD101","This is a valid course.",
                LocalDate.parse("2021-11-11"),LocalDate.parse("2021-11-13"),13);

        Course duplicateAbv = new Course("Duplicate","VLD201","This is a valid changed course detail.",
                LocalDate.parse("2021-10-11"),LocalDate.parse("2022-10-13"),42);

        Course existingCourse = new Course("Duplicate","TEST","This is a valid course detail.",
                LocalDate.parse("2021-10-11"),LocalDate.parse("2022-10-13"),42);

        when(mockValidator.courseUpdateValidator(original,duplicateAbv)).thenReturn(true);
        when(mockCourseRepo.findCourseByAbbreviation("VLD201")).thenReturn(null);
        when(mockCourseRepo.findCourseByName("Duplicate")).thenReturn(existingCourse);
        // Act
        try{
            sut.updateCourse(original, duplicateAbv);
        }finally {// Assert
            verify(mockValidator,times(1)).courseUpdateValidator(original,duplicateAbv);
            verify(mockCourseRepo,times(1)).findCourseByAbbreviation("VLD201");
            verify(mockCourseRepo,times(1)).findCourseByName("Duplicate");
        }
    }


    @Test
    public void joinCourse_executesSuccessfully_whenProvidedWithValidAbbreviation(){
        //Arrange
        String joiningCourseAbv = "ABV101";
        Course expected = new Course();
        String username = "validusername";
        Set<String> studentUsernames = new HashSet<String>();
        when(mockCourseRepo.findCourseByAbbreviation(joiningCourseAbv)).thenReturn(expected);
        when(mockValidator.isOpen(expected)).thenReturn(true);
        //Act
        sut.joinCourse(joiningCourseAbv,username);
        //Assert
        verify(mockCourseRepo,times(1)).findCourseByAbbreviation(joiningCourseAbv);
        verify(mockValidator,times(1)).isOpen(expected);
        verify(mockCourseRepo,times(1)).addStudentUsername(joiningCourseAbv,username);
    }

    @Test(expected = NoSuchCourseException.class)
    public void joinCourse_throwsException_whenProvidedWithInValidAbbreviation() {
        // Arrange
        String joiningCourseAbv = "ABV101";
        Course expected = new Course();
        String username = "validusername";
        Set<String> studentUsernames = new HashSet<String>();
        when(mockCourseRepo.findCourseByAbbreviation(joiningCourseAbv)).thenReturn(null);
        when(mockValidator.isOpen(expected)).thenReturn(true);
        // Act
        sut.joinCourse(joiningCourseAbv, username);
        //Assert
        verify(mockCourseRepo, times(1)).findCourseByAbbreviation(joiningCourseAbv);
    }

    @Test(expected = CourseNotOpenException.class)
    public void joinCourse_throwsException_whenProvidedCourseIsClosed() {
        // Arrange
        String joiningCourseAbv = "ABV101";
        Course expected = new Course();
        String username = "validusername";
        Set<String> studentUsernames = new HashSet<String>();
        when(mockCourseRepo.findCourseByAbbreviation(joiningCourseAbv)).thenReturn(expected);
        when(mockValidator.isOpen(expected)).thenReturn(false);
        // Act
        sut.joinCourse(joiningCourseAbv, username);
        // Assert
        verify(mockCourseRepo, times(1)).findCourseByAbbreviation(joiningCourseAbv);
        verify(mockValidator,times(1)).isOpen(expected);
    }

    @Test(expected = AlreadyRegisteredForCourseException.class)
    public void joinCourse_throwsException_whenUserIsAlreadyInClass() {
        // Arrange
        String joiningCourseAbv = "ABV101";
        Course expected = new Course();
        String username = "validusername";

        Set<String> studentUsernames = new HashSet<>();
        studentUsernames.add("validusername");
        expected.setStudentUsernames(studentUsernames);

        when(mockCourseRepo.findCourseByAbbreviation(joiningCourseAbv)).thenReturn(expected);
        when(mockValidator.isOpen(expected)).thenReturn(true);
        // Act
        sut.joinCourse(joiningCourseAbv, username);
        // Assert
        verify(mockCourseRepo, times(1)).findCourseByAbbreviation(joiningCourseAbv);
        verify(mockValidator,times(1)).isOpen(expected);
    }


    // verifyCourse tests

    @Test
    public void findCourse_byAbbreviation_returnsCourse_whenProvidedWithValidAbbreviation(){
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

    // getAllCourses tests

    @Test
    public void getAllCourses_returnsCourseList_whenSuccessful(){
        // Arrange
        Course course1 = new Course("ONE");
        Course course2 = new Course("TWO");
        Course course3 = new Course("THREE");
        List<Course> validCourseList = new ArrayList<Course>(){{add(course1);add(course2);add(course3);}};
        when(mockCourseRepo.findAll()).thenReturn(validCourseList);

        // Act
        List<Course> actualResult = sut.getAllCourses();

        // Assert
        verify(mockCourseRepo,times(1)).findAll();
        Assert.assertEquals(actualResult,validCourseList);

    }

    @Test(expected = NoOpenCoursesException.class)
    public void getAllCourses_throwsException_whenNoCoursesFound(){
        // Arrange
        List<Course> emptyCourseList = new ArrayList<>();
        when(mockCourseRepo.findAll()).thenReturn(emptyCourseList);

        // Act
        try{
            sut.getAllCourses();
        } finally { // Assert
            verify(mockCourseRepo,times(1)).findAll();
        }

    }

    // getCoursesByUsername tests
    @Test
    public void getCoursesByUsername_returnsSuccessfully_whenCoursesWithUser_areFound(){
        // Arrange
        String username = "testUsername";
        Course course = new Course("ValidCourse","VLD101","This is a valid course.",
                LocalDate.parse("2020-11-11"),LocalDate.parse("2022-11-13"),13);
        UserCourseDTO courseDTO = new UserCourseDTO(course);
        List<UserCourseDTO> validCourseList = new ArrayList<UserCourseDTO>(){{add(courseDTO);}};
        when(mockCourseRepo.findCoursesByUsername(username)).thenReturn(validCourseList);
        // Act
        List<UserCourseDTO> actualResult = sut.getCoursesByUsername(username);
        // Assert
        verify(mockCourseRepo,times(1)).findCoursesByUsername(username);
        Assert.assertEquals(validCourseList,actualResult);
    }

    @Test(expected = NoOpenCoursesException.class)
    public void getCoursesByUsername_throwsException_whenNoCoursesFound(){
        // Arrange
        String username = "testUsername";
        List<UserCourseDTO> emptyCourseList = new ArrayList<UserCourseDTO>();
        when(mockCourseRepo.findCoursesByUsername(username)).thenReturn(emptyCourseList);
        // Act
        List<UserCourseDTO> actualResult = sut.getCoursesByUsername(username);
        // Assert
        verify(mockCourseRepo,times(1)).findCoursesByUsername(username);
    }

    // removeStudent tests

    @Test
    public void removeStudent_executesSuccessfully_whenProvided_openCourse_withUserEnrolled(){
        // Arrange
        String validStudent = "username";
        String validCourseAbv = "TEST";
        Course openCourse = new Course("ValidCourse","TEST","This is a valid course.",
                LocalDate.parse("2020-11-11"),LocalDate.parse("2023-11-13"),13);
        Set<String> validStudentList = new HashSet<String>(){{add(validStudent);}};
        openCourse.setStudentUsernames(validStudentList);
        when(mockCourseRepo.findCourseByAbbreviation(validCourseAbv)).thenReturn(openCourse);
        // Act
        sut.removeStudent(validStudent,validCourseAbv);
        // Assert
        verify(mockCourseRepo, times(1)).findCourseByAbbreviation(validCourseAbv);
        verify(mockCourseRepo, times(1)).removeStudent(validStudent, validCourseAbv);

    }

    @Test(expected = NoSuchCourseException.class)
    public void removeStudent_throwsException_whenProvidedWith_courseDoesNotExist(){
        // Arrange
        String validStudent = "username";
        String invalidCourseAbv = "TEST";
        when(mockCourseRepo.findCourseByAbbreviation(invalidCourseAbv)).thenReturn(null);

        // Act
        sut.removeStudent(validStudent, invalidCourseAbv);

    }

    @Test(expected = NotRegisteredForCourseException.class)
    public void removeStudent_throwsException_whenProvided_openCourse_withoutUserEnrolled(){
        // Arrange
        String validStudent = "username";
        String validCourseAbv = "TEST";
        Course emptyCourse = new Course("ValidCourse","TEST","This is a valid course.",
                LocalDate.parse("2020-11-11"),LocalDate.parse("2023-11-13"),13);
        when(mockCourseRepo.findCourseByAbbreviation(validCourseAbv)).thenReturn(emptyCourse);
        // Act
        try {
            sut.removeStudent(validStudent, validCourseAbv);
        }
        finally {// Assert
            verify(mockCourseRepo, times(1)).findCourseByAbbreviation(validCourseAbv);
        }
    }

    @Test(expected = CourseNotOpenException.class)
    public void removeStudent_throwsExcept_whenProvided_closedCourse(){
        // Arrange
        String validStudent = "username";
        String validCourseAbv = "TEST";
        Course closedCourse = new Course("ValidCourse","TEST","This is a valid course.",
                LocalDate.parse("1990-11-11"),LocalDate.parse("1991-11-13"),13);
        Set<String> validStudentList = new HashSet<String>(){{add(validStudent);}};
        closedCourse.setStudentUsernames(validStudentList);
        when(mockCourseRepo.findCourseByAbbreviation(validCourseAbv)).thenReturn(closedCourse);
        // Act
        try {
            sut.removeStudent(validStudent, validCourseAbv);
        } finally { // Assert
            verify(mockCourseRepo, times(1)).findCourseByAbbreviation(validCourseAbv);
            verify(mockCourseRepo, times(0)).removeStudent(validStudent, validCourseAbv);
        }

    }

}
