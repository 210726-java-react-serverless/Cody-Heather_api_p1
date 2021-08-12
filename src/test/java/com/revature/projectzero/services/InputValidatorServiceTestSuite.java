package com.revature.projectzero.services;

import com.revature.projectzero.documents.AppUser;
import com.revature.projectzero.documents.Course;
import com.revature.projectzero.services.InputValidatorService;
import com.revature.projectzero.util.exceptions.*;

import org.junit.*;


public class InputValidatorServiceTestSuite {

    private InputValidatorService sut;

    @Before
    public void setup(){
        sut = new InputValidatorService();
    }

    @After
    public void cleanUp(){
        sut = null;
    }


    // User Entry Validator Tests

    @Test
    public void userEntryValidator_returnsTrue_whenGivenValidUser(){
        // Arrange
        AppUser validUser = new AppUser("valid","valid","valid@email.com","valid5","valid-password",false);
        // Act
        sut.newUserEntryValidator(validUser);
        // Assert
        Assert.assertTrue(sut.newUserEntryValidator(validUser));

    }


    @Test(expected = InvalidEntryException.class)
    public void userEntryValidator_throwsException_whenGivenBlankValues(){
        // Arrange
        AppUser invalidUser = new AppUser("","","","","",false);

        // Act
        sut.newUserEntryValidator(invalidUser);

    }

    @Test(expected = InvalidUsernameException.class)
    public void userEntryValidator_throwsException_whenGivenInvalidUsernameUnderFourChars(){
        // Arrange
        AppUser invalidUser = new AppUser("valid","valid","valid@email.com","inv","valid-password",false);

        //Act
        sut.newUserEntryValidator(invalidUser);
    }

    @Test(expected = InvalidUsernameException.class)
    public void userEntryValidator_throwsException_whenGivenInvalidUsernameWithSymbols(){
        // Arrange
        AppUser invalidUser = new AppUser("valid","valid","valid@email.com","invalid!","valid-password",false);

        //Act
        sut.newUserEntryValidator(invalidUser);
    }

    @Test(expected = InvalidEmailException.class)
    public void userEntryValidator_throwsException_whenGivenInvalidEmail(){
        // Arrange
        AppUser invalidUser = new AppUser("valid","valid","invalid-email.com","valid","valid-password",false);
        // Act
        sut.newUserEntryValidator(invalidUser);
    }

    @Test(expected = InvalidPasswordException.class)
    public void userEntryValidator_throwsException_whenGivenInvalidPassword(){
        // Arrange
        AppUser invalidUser = new AppUser("valid","valid","valid@email.com","valid","invalid",false);
        // Act
        sut.newUserEntryValidator(invalidUser);
    }


    // Course Entry Validator Tests

    @Test
    public void newCourseEntryValidator_returnsTrue_whenGivenValidCourse(){
        // Arrange
        Course validCourse = new Course("ValidCourse","VLD101","This is a valid course.",true);

        // Act
        sut.newCourseEntryValidator(validCourse);

        //Assert
        Assert.assertTrue(sut.newCourseEntryValidator(validCourse));

    }

    @Test(expected = InvalidEntryException.class)
    public void newCourseEntryValidator_throwsException_whenGivenInvalidCourse_withBlankValues(){
        // Arrange
        Course invalidCourse = new Course("","","",true);

        // Act
        sut.newCourseEntryValidator(invalidCourse);
    }

    @Test(expected = InvalidCourseNameException.class)
    public void newCourseEntryValidator_throwsException_whenGivenInvalidCourseName_tooLong(){
        // Arrange
        Course invalidCourse = new Course("maaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaath",
                "MTH","This course name is too long.",true);

        // Act
        sut.newCourseEntryValidator(invalidCourse);
    }

    @Test(expected = InvalidCourseNameException.class)
    public void newCourseEntryValidator_throwsException_whenGivenInvalidCourseName_tooShort(){
        // Arrange
        Course invalidCourse = new Course("math","MTH101"
                ,"This course name is too short.",true);

        // Act
        sut.newCourseEntryValidator(invalidCourse);
    }

    @Test(expected = InvalidCourseAbbreviationException.class)
    public void newCourseEntryValidator_throwsException_whenGivenInvalidCourseAbbreviation(){
        // Arrange
        Course invalidCourse = new Course("Mathematics","Invalid"
                ,"This course abbreviation is too long.",true);

        // Act
        sut.newCourseEntryValidator(invalidCourse);
    }

    @Test(expected = InvalidCourseDescriptionException.class)
    public void newCourseEntryValidator_throwsException_whenGivenInvalidCourseDetail_tooShort(){
        // Arrange
        Course invalidCourse = new Course("Mathematics","MTH101"
                ,"Invalid",true);

        // Act
        sut.newCourseEntryValidator(invalidCourse);
    }

    @Test(expected = InvalidCourseDescriptionException.class)
    public void newCourseEntryValidator_throwsException_whenGivenInvalidCourseDetail_tooLong(){
        // Arrange
        Course invalidCourse = new Course("Mathematics","MTH101","Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum",
                true);

        // Act
        sut.newCourseEntryValidator(invalidCourse);
    }

    // newCourseNameValidator tests
    @Test
    public void newCourseNameValidator_returnsTrue_whenGivenValidName(){
        // Arrange
        String validName = "ValidName";
        Course original = new Course("ValidCourse","VLD101","This is a valid course.",true);

        // Act
        sut.newCourseNameValidator(original,validName);

        //Assert
        Assert.assertTrue(sut.newCourseNameValidator(original,validName));
    }

    @Test(expected = InvalidEntryException.class)
    public void newCourseNameValidator_throwsException_whenGivenEmptyName(){
        // Arrange
        String invalidName = "";
        Course original = new Course("ValidCourse","VLD101","This is a valid course.",true);

        // Act
        sut.newCourseNameValidator(original,invalidName);

    }

    @Test(expected = InvalidCourseNameException.class)
    public void newCourseNameValidator_throwsException_whenGivenName_thatIsTooLong(){
        // Arrange
        String invalidName = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        Course original = new Course("ValidCourse","VLD101","This is a valid course.",true);

        // Act
        sut.newCourseNameValidator(original,invalidName);

    }

    @Test(expected = InvalidCourseNameException.class)
    public void newCourseNameValidator_throwsException_whenGivenName_thatIsTooShort(){
        // Arrange
        String invalidName = "AAA";
        Course original = new Course("ValidCourse","VLD101","This is a valid course.",true);

        // Act
        sut.newCourseNameValidator(original,invalidName);

    }

    //newCourseAbvValidator
    @Test
    public void newCourseAbvValidator_returnsTrue_whenGivenAbbreviation(){
        // Arrange
        String validAbv = "VALID";
        Course original = new Course("ValidCourse","VLD101","This is a valid course.",true);

        // Act
        sut.newCourseAbvValidator(original,validAbv);

        //Assert
        Assert.assertTrue(sut.newCourseAbvValidator(original,validAbv));
    }

    @Test(expected = InvalidEntryException.class)
    public void newCourseAbvValidator_throwsException_whenGivenBlankAbbreviation(){
        // Arrange
        String invalidAbv = "";
        Course original = new Course("ValidCourse","VLD101","This is a valid course.",true);

        // Act
        sut.newCourseAbvValidator(original,invalidAbv);

    }

    @Test(expected = InvalidCourseAbbreviationException.class)
    public void newCourseAbvValidator_throwsException_whenGivenAbbreviation_thatIsTooLong(){
        // Arrange
        String invalidAbv = "AAAAAAAA";
        Course original = new Course("ValidCourse","VLD101","This is a valid course.",true);

        // Act
        sut.newCourseAbvValidator(original,invalidAbv);

    }

    @Test(expected = InvalidCourseAbbreviationException.class)
    public void newCourseAbvValidator_throwsException_whenGivenAbbreviation_thatIsLongerThanCourseName(){
        // Arrange
        String invalidAbv = "AAAAAA";
        Course original = new Course("Valid","VLD101","This is a valid course.",true);

        // Act
        sut.newCourseAbvValidator(original,invalidAbv);

    }

    // newCourseDetailsValidator

    @Test
    public void newCourseDetailsValidator_returnsTrue_whenGivenValidDescription(){
        // Arrange
        String validDesc = "This description is also valid.";

        // Act
        sut.newCourseDetailsValidator(validDesc);

        //Assert
        Assert.assertTrue(sut.newCourseDetailsValidator(validDesc));
    }

    @Test(expected = InvalidEntryException.class)
    public void newCourseDetailsValidator_throwsException_whenGivenBlankDescription(){
        // Arrange
        String invalidDesc = "";

        // Act
        sut.newCourseDetailsValidator(invalidDesc);

    }

    @Test(expected = InvalidCourseDescriptionException.class)
    public void newCourseDetailsValidator_throwsException_whenGivenDescriptionTooShort(){
        // Arrange
        String invalidDesc = "Aw heck";

        // Act
        sut.newCourseDetailsValidator(invalidDesc);

    }

    @Test(expected = InvalidCourseDescriptionException.class)
    public void newCourseDetailsValidator_throwsException_whenGivenDescriptionTooLong(){
        // Arrange
        String invalidDesc = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";

        // Act
        sut.newCourseDetailsValidator(invalidDesc);

    }



}




