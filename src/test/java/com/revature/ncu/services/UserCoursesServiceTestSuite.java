package com.revature.ncu.services;

import com.revature.ncu.datasources.documents.AppUser;
import com.revature.ncu.datasources.documents.Course;
import com.revature.ncu.datasources.repositories.UserCoursesRepository;
import com.revature.ncu.util.exceptions.AlreadyRegisteredForCourseException;
import com.revature.ncu.util.exceptions.NoCoursesJoinedException;
import com.revature.ncu.util.exceptions.NotRegisteredForCourseException;
import com.revature.ncu.web.dtos.Principal;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;


public class UserCoursesServiceTestSuite {

    // Instantiate system under test
    private UserCoursesService sut;

    // Mock dependencies required for the system under test
    private AppUser mockUser;
    private CourseValidatorService courseValidatorService;
    private UserCoursesRepository mockUserCourseRepo;

    @Before
    public void setup(){

        mockUserCourseRepo = mock(UserCoursesRepository.class);
        courseValidatorService = mock(CourseValidatorService.class);
        mockUser = mock(AppUser.class);

    }

    @After
    public void cleanUp(){
        mockUserCourseRepo = null;

        sut = null;
    }


    // joinCourse tests


//    @Test
//    public void joinCourse_executesSuccessfully_whenGivenValidCourse(){
//        // Arrange
//        Principal principal = new Principal("test");
//        Set<String> stuId = new HashSet<>(); // check if the student's ID is already enrolled
//        Course validCourse = new Course("numbersandstuff","ValidCourse","VALID","This is a valid course!",
//                "Professor Valid",LocalDate.parse("2021-8-11"),LocalDate.parse("9999-8-11"),stuId,12,3);
//        when(courseValidatorService.isOpen(validCourse)).thenReturn(true);
//
//        // Act
//        sut.joinCourse(validCourse,principal);
//        // Assert
//
//        verify(mockUserCourseRepo,times(1)).findRegisteredCoursesByUsername(mockUser.getUsername());
//
//    }


//    @Test
//    public void joinCourse_executesSuccessfully_whenGivenValidCourseName_ifNoCoursesHaveBeenJoined(){
//        // Arrange
//        String validCourse = "validCourseToJoin";
//        List<String> validCourseList = new ArrayList<>();
//
//        when(mockUserCourseRepo.findRegisteredCoursesByUsername(mockUser.getUsername())).thenReturn(validCourseList);
//        // Act
//        sut.joinCourse(validCourse);
//        // Assert
//
//        verify(mockUserCourseRepo,times(1)).findRegisteredCoursesByUsername(mockUser.getUsername());
//
//    }
//
//    @Test(expected = AlreadyRegisteredForCourseException.class)
//    public void joinCourse_throwsException_whenGivenDuplicateCourseName(){
//        // Arrange
//        String duplicateCourse = "Course Already Joined!";
//        List<String> originalCourseList = new ArrayList<>();
//        originalCourseList.add(duplicateCourse);
//
//        when(mockUserCourseRepo.findRegisteredCoursesByUsername(mockUser.getUsername())).thenReturn(originalCourseList);
//        // Act
//        try{
//            sut.joinCourse(duplicateCourse);
//        } finally { // Assert
//
//            verify(mockUserCourseRepo,times(1)).findRegisteredCoursesByUsername(mockUser.getUsername());
//        }
//
//    }

    // getCourse tests

    @Test
    public void getCourses_returnsCourseList_whenListIsPopulated(){
        // Arrange
        List<String> validCourseList = new ArrayList<String>(){{add("Course1"); add("Course2");}};
        List<String> expectedCourseList = new ArrayList<String>(){{add("Course1"); add("Course2");}};

        when(mockUserCourseRepo.findRegisteredCoursesByUsername(mockUser.getUsername())).thenReturn(validCourseList);

        // Act
        List<String> actualResult = sut.getCourses();

        // Assert
        Assert.assertEquals(expectedCourseList,actualResult);

        verify(mockUserCourseRepo, times(2)).findRegisteredCoursesByUsername(mockUser.getUsername());

    }

    @Test(expected = NoCoursesJoinedException.class)
    public void getCourses_throwsException_whenUser_hasNotRegistered_forAnyCourses(){
        // Arrange
        List<String> emptyCourseList = new ArrayList<>();

        when(mockUserCourseRepo.findRegisteredCoursesByUsername(mockUser.getUsername())).thenReturn(emptyCourseList);
        // Act
        try{
            sut.getCourses();
        } finally { // Assert

            verify(mockUserCourseRepo, times(1)).findRegisteredCoursesByUsername(mockUser.getUsername());
        }
    }

    // leaveCourse tests
    @Test
    public void leaveCourse_executesSuccessfully_whenUser_hasRegistered_forCourse(){
        // Arrange
        String validCourseToLeave = "Course1";
        List<String> validCourseList = new ArrayList<String>(){{add("Course1"); add("Course2");}};

        when(mockUserCourseRepo.findRegisteredCoursesByUsername(mockUser.getUsername())).thenReturn(validCourseList);

        // Act
        sut.leaveCourse(validCourseToLeave);
        // Assert

        verify(mockUserCourseRepo,times(1)).findRegisteredCoursesByUsername(mockUser.getUsername());
        verify(mockUserCourseRepo,times(1))
                .removeCourseFromUserList(validCourseToLeave,mockUser.getUsername());

    }

    @Test(expected = NoCoursesJoinedException.class)
    public void leaveCourse_throwsException_whenUser_hasNotRegistered_forAnyCourse(){
        // Arrange
        List<String> emptyCourseList = new ArrayList<>();

        when(mockUserCourseRepo.findRegisteredCoursesByUsername(mockUser.getUsername())).thenReturn(emptyCourseList);
        // Act
        try{
            sut.getCourses();
        } finally { // Assert

            verify(mockUserCourseRepo, times(1)).findRegisteredCoursesByUsername(mockUser.getUsername());
        }
    }

    // verifyCourseEntry tests

    @Test
    public void verifyCourseEntry_returnsCourseName_whenUserAttempts_toWithdrawFromValidCourse(){
        // Arrange
        String validCourseToLeave = "Course1";
        String expectedCourseToLeave = "Course1";
        List<String> validCourseList = new ArrayList<String>(){{add("Course1"); add("Course2");}};

        // Act
        String actualResult = sut.verifyCourseEntry(validCourseList,validCourseToLeave);

        // Assert
        Assert.assertEquals(actualResult,expectedCourseToLeave);
    }

    @Test(expected = NotRegisteredForCourseException.class)
    public void verifyCourseEntry_throwsException_whenUserAttempts_toWithdrawFromInvalidCourse(){
        // Arrange
        String invalidCourseToLeave = "invalid-course";
        List<String> validCourseList = new ArrayList<String>(){{add("Course1"); add("Course2");}};

        // Act
        String exception = sut.verifyCourseEntry(validCourseList,invalidCourseToLeave);

    }

    //

}





