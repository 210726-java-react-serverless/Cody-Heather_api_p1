package com.revature.ncu.services;

import com.revature.ncu.datasources.documents.Course;
import com.revature.ncu.util.exceptions.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

public class CourseValidatorServiceTestSuite {

    private CourseValidatorService sut;

    @Before
    public void setup(){
        sut = new CourseValidatorService();
    }

    @After
    public void cleanUp(){
        sut = null;
    }

    // New Course Entry Validator Tests

    @Test
    public void newCourseEntryValidator_returnsTrue_whenGivenValidCourse(){
        // Arrange
        Course validCourse = new Course("ValidCourse","VLD101","This is a valid course."
                , LocalDate.parse("2021-11-11"),LocalDate.parse("2021-11-13"),13);

        // Act
        sut.newCourseEntryValidator(validCourse);

        //Assert
        Assert.assertTrue(sut.newCourseEntryValidator(validCourse));

    }

    @Test(expected = InvalidEntryException.class)
    public void newCourseEntryValidator_throwsException_whenGivenInvalidCourse_withBlankValues(){
        // Arrange
        Course invalidCourse = new Course("","",""
                , null, null,0);

        // Act
        sut.newCourseEntryValidator(invalidCourse);
    }

    @Test(expected = InvalidCourseNameException.class)
    public void newCourseEntryValidator_throwsException_whenGivenInvalidCourseName_tooLong(){
        // Arrange
        Course invalidCourse = new Course("maaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaath","VLD101","This is an invalid course."
                , LocalDate.parse("2021-11-11"),LocalDate.parse("2021-11-13"),13);
        // Act
        sut.newCourseEntryValidator(invalidCourse);
    }

    @Test(expected = InvalidCourseNameException.class)
    public void newCourseEntryValidator_throwsException_whenGivenInvalidCourseName_tooShort(){
        // Arrange
        Course invalidCourse = new Course("math","VLD101","This is a invalid course."
                , LocalDate.parse("2021-11-11"),LocalDate.parse("2021-11-13"),13);

        // Act
        sut.newCourseEntryValidator(invalidCourse);
    }

    @Test(expected = InvalidCourseAbbreviationException.class)
    public void newCourseEntryValidator_throwsException_whenGivenInvalidCourseAbbreviation(){
        // Arrange
        Course invalidCourse = new Course("Mathematics for nerds","VLD101010101","This is a invalid course."
                , LocalDate.parse("2021-11-11"),LocalDate.parse("2021-11-13"),13);
        // Act
        sut.newCourseEntryValidator(invalidCourse);
    }

    @Test(expected = InvalidCourseDescriptionException.class)
    public void newCourseEntryValidator_throwsException_whenGivenInvalidCourseDetail_tooShort(){
        // Arrange
        Course invalidCourse = new Course("Mathematics","VLD101","Invalid."
                , LocalDate.parse("2021-11-11"),LocalDate.parse("2021-11-13"),13);

        // Act
        sut.newCourseEntryValidator(invalidCourse);
    }

    @Test(expected = InvalidCourseDescriptionException.class)
    public void newCourseEntryValidator_throwsException_whenGivenInvalidCourseDetail_tooLong(){
        // Arrange
        Course invalidCourse = new Course("Mathematics","VLD101","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum"
                , LocalDate.parse("2021-11-11"),LocalDate.parse("2021-11-13"),13);

        // Act
        sut.newCourseEntryValidator(invalidCourse);
    }

    @Test(expected = InvalidCourseCapacityException.class)
    public void newCourseEntryValidator_throwsException_whenGivenInvalidCourseCap_tooLow(){
        // Arrange
        Course invalidCourse = new Course("Mathematics","VLD101","This is an invalid course."
                , LocalDate.parse("2021-11-11"),LocalDate.parse("2021-11-13"),1);

        // Act
        sut.newCourseEntryValidator(invalidCourse);
    }

    @Test(expected = InvalidCourseCapacityException.class)
    public void newCourseEntryValidator_throwsException_whenGivenInvalidCourseCap_tooHigh(){
        // Arrange
        Course invalidCourse = new Course("Mathematics","VLD101","This is an invalid course."
                , LocalDate.parse("2021-11-11"),LocalDate.parse("2021-11-13"),101);

        // Act
        sut.newCourseEntryValidator(invalidCourse);
    }

    @Test(expected = InvalidCourseDateException.class)
    public void newCourseEntryValidator_throwsException_whenGivenInvalidCourseOpenDate_afterCloseDate(){
        // Arrange
        Course invalidCourse = new Course("Mathematics","VLD101","why man why?"
                , LocalDate.parse("2021-11-13"),LocalDate.parse("2021-11-11"),13);

        // Act
        sut.newCourseEntryValidator(invalidCourse);
    }

    @Test(expected = InvalidCourseDateException.class)
    public void newCourseEntryValidator_throwsException_whenGivenInvalidCourseCloseDate_beforeCurrentDate(){
        // Arrange
        Course invalidCourse = new Course("Mathematics","VLD101","why man why?"
                , LocalDate.parse("2021-04-13"),LocalDate.parse("2021-08-27"),13);

        // Act
        sut.newCourseEntryValidator(invalidCourse);
    }

    //isOpen Tests
    @Test
    public void isOpen_returnsTrue_whenGivenOpenCourse(){
        // Arrange
        Course validCourse = new Course("Mathematics","VLD101","This is an valid course."
                , LocalDate.parse("2021-08-11"),LocalDate.parse("2024-11-13"),12);
        boolean open;
        // Act
        open = sut.isOpen(validCourse);

        // Assert
        Assert.assertTrue(open);

    }

    @Test
    public void isOpen_returnsTrue_whenGivenCourse_openToday(){
        // Arrange
        Course validCourse = new Course("Mathematics","VLD101","This is an valid course."
                , LocalDate.now(),LocalDate.parse("2024-11-13"),12);
        boolean open;
        // Act
        open = sut.isOpen(validCourse);

        // Assert
        Assert.assertTrue(open);

    }

    @Test
    public void isOpen_returnsFalse_whenGivenClosedCourse(){
        // Arrange
        Course validCourse = new Course("Mathematics","VLD101","This is an valid course."
                , LocalDate.parse("2021-08-11"),LocalDate.parse("2021-08-13"),12);
        boolean open;
        // Act
        open = sut.isOpen(validCourse);

        // Assert
        Assert.assertFalse(open);

    }

    @Test
    public void courseUpdateValidator_returnsTrue_whenGivenValidUpdate(){
        //Arrange
        Course validOriginal = new Course("ValidCourse","VLD101","This is a valid course."
                , LocalDate.parse("2021-11-11"),LocalDate.parse("2021-11-13"),13);

        Course validUpdatingCourse = new Course("ValidUpdateCourse","VLD201","This is a valid changed course detail."
                , LocalDate.parse("2021-10-11"),LocalDate.parse("2021-10-13"),42);
        boolean valid;
        //Act
        valid = sut.courseUpdateValidator(validOriginal, validUpdatingCourse);

        //Assert
        Assert.assertTrue(valid);
    }

    @Test(expected = InvalidEntryException.class)
    public void courseUpdateValidator_throwsException_whenFieldsAreBlank(){
        //Arrange
        Course validOriginal = new Course("ValidCourse","VLD101","This is a valid course."
                , LocalDate.parse("2021-11-11"),LocalDate.parse("2021-11-13"),13);

        Course invalidUpdatingCourse = new Course("","",""
                , null,null,0);

        //Act
        sut.courseUpdateValidator(validOriginal, invalidUpdatingCourse);
    }

    @Test(expected = InvalidCourseNameException.class)
    public void courseUpdateValidator_throwsException_whenCourseNameIsTooLong(){
        //Arrange
        Course validOriginal = new Course("ValidCourse","VLD101","This is a valid course."
                , LocalDate.parse("2021-11-11"),LocalDate.parse("2021-11-13"),13);

        Course invalidUpdatingCourse = new Course("ValidUpdateCourseValidUpdateCourseValidUpdateCourseValidUpdateCourseValidUpdateCourse","VLD201","This is a valid changed course detail."
                , LocalDate.parse("2021-10-11"),LocalDate.parse("2021-10-13"),42);

        //Act
        sut.courseUpdateValidator(validOriginal, invalidUpdatingCourse);
    }

    @Test(expected = InvalidCourseNameException.class)
    public void courseUpdateValidator_throwsException_whenCourseNameIsTooshort(){
        //Arrange
        Course validOriginal = new Course("ValidCourse","VLD101","This is a valid course."
                , LocalDate.parse("2021-11-11"),LocalDate.parse("2021-11-13"),13);

        Course invalidUpdatingCourse = new Course("Val","VLD201","This is a valid changed course detail."
                , LocalDate.parse("2021-10-11"),LocalDate.parse("2021-10-13"),42);

        //Act
        sut.courseUpdateValidator(validOriginal, invalidUpdatingCourse);
    }



    @Test(expected = InvalidCourseAbbreviationException.class)
    public void courseUpdateValidator_throwsException_ifCourseAbbeviationIsTooLong(){
        //Arrange
        Course validOriginal = new Course("ValidCourse","VLD101","This is a valid course."
                , LocalDate.parse("2021-11-11"),LocalDate.parse("2021-11-13"),13);

        Course invalidUpdatingCourse = new Course("ValidCourse","VLD20142","This is a valid changed course detail."
                , LocalDate.parse("2021-10-11"),LocalDate.parse("2021-10-13"),42);

        //Act
        sut.courseUpdateValidator(validOriginal, invalidUpdatingCourse);
    }

    @Test(expected = InvalidCourseDescriptionException.class)
    public void courseUpdateValidator_throwsException_ifCourseDescriptionTooShort(){
        //Arrange
        Course validOriginal = new Course("ValidCourse","VLD101","This is a valid course."
                , LocalDate.parse("2021-11-11"),LocalDate.parse("2021-11-13"),13);

        Course invalidUpdatingCourse = new Course("ValidCourse","VLD201","This"
                , LocalDate.parse("2021-10-11"),LocalDate.parse("2021-10-13"),42);

        //Act
        sut.courseUpdateValidator(validOriginal, invalidUpdatingCourse);
    }

    @Test(expected = InvalidCourseDescriptionException.class)
    public void courseUpdateValidator_throwsException_whenNewDetail_isTooLong(){
        //Arrange
        Course original = new Course("ValidCourse","VLD101","This is a valid course."
                , LocalDate.parse("2021-11-11"),LocalDate.parse("2022-11-13"),13);

        Course invalidUpdatingCourse = new Course("ValidUpdateCourse","VLD201","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum"
                , LocalDate.parse("2021-11-11"),LocalDate.parse("2022-11-13"),13);
        //

        sut.courseUpdateValidator(original, invalidUpdatingCourse);
    }

    @Test(expected = InvalidCourseCapacityException.class)
    public void courseUpdateValidator_throwsException_whenNewCap_isBelowEnrolledStudents(){
        //Arrange
        Course original = new Course("ValidCourse","VLD101","This is a valid course."
                , LocalDate.parse("2021-11-11"),LocalDate.parse("2022-11-13"),13);

        original.setSlotsTaken(13);
        Course invalidUpdatingCourse = new Course("ValidUpdateCourse","VLD201","This is a valid changed course detail."
                , LocalDate.parse("2021-11-11"),LocalDate.parse("2022-11-13"),12);
        //

        sut.courseUpdateValidator(original, invalidUpdatingCourse);
    }

    @Test(expected = InvalidCourseCapacityException.class)
    public void courseUpdateValidator_throwsException_whenNewCap_isTooLow(){
        //Arrange
        Course original = new Course("ValidCourse","VLD101","This is a valid course."
                , LocalDate.parse("2021-11-11"),LocalDate.parse("2022-11-13"),13);

        Course invalidUpdatingCourse = new Course("ValidUpdateCourse","VLD201","This is a valid changed course detail."
                , LocalDate.parse("2021-11-11"),LocalDate.parse("2022-11-13"),8);
        //

        sut.courseUpdateValidator(original, invalidUpdatingCourse);
    }

    @Test(expected = InvalidCourseCapacityException.class)
    public void courseUpdateValidator_throwsException_whenNewCap_isTooHigh(){
        //Arrange
        Course original = new Course("ValidCourse","VLD101","This is a valid course."
                , LocalDate.parse("2021-11-11"),LocalDate.parse("2022-11-13"),13);

        Course invalidUpdatingCourse = new Course("ValidUpdateCourse","VLD201","This is a valid changed course detail."
                , LocalDate.parse("2021-11-11"),LocalDate.parse("2022-11-13"),101);
        //

        sut.courseUpdateValidator(original, invalidUpdatingCourse);
    }

    @Test(expected = InvalidCourseDateException.class)
    public void courseUpdateValidator_throwsException_whenNewOpenDate_isAfterCloseDate(){
        //Arrange
        Course original = new Course("ValidCourse","VLD101","This is a valid course."
                , LocalDate.parse("2021-11-11"),LocalDate.parse("2022-11-13"),13);

        Course invalidUpdatingCourse = new Course("ValidUpdateCourse","VLD201","This is a valid changed course detail."
                , LocalDate.parse("2022-08-11"),LocalDate.parse("2021-10-13"),42);
        //

        sut.courseUpdateValidator(original, invalidUpdatingCourse);
    }

    @Test(expected = InvalidCourseDateException.class)
    public void courseUpdateValidator_throwsException_whenNewCloseDate_isBeforeToday(){
        //Arrange
        Course original = new Course("ValidCourse","VLD101","This is a valid course."
                , LocalDate.parse("2021-11-11"),LocalDate.parse("2022-11-13"),13);

        Course invalidUpdatingCourse = new Course("ValidUpdateCourse","VLD201","This is a valid changed course detail."
                , LocalDate.parse("2021-05-11"),LocalDate.parse("2021-07-13"),42);
        //

        sut.courseUpdateValidator(original, invalidUpdatingCourse);
    }
}
