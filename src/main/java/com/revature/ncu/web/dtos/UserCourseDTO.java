package com.revature.ncu.web.dtos;

import com.revature.ncu.datasources.documents.Course;
import java.util.Objects;

/**
 * DTO = Data Transfer object.
 * Wrapper for displaying course information to non-admin users, hides usernames of registered users.
 */
public class UserCourseDTO {

    private String id;
    private String courseName;
    private String courseAbbreviation;
    private String courseDetail;
    private String professorName;
    private String courseOpenDate;
    private String courseCloseDate;
    private int slotsTaken;
    private int courseCapacity;


    public UserCourseDTO(Course subject) {
        this.id = subject.getId();
        this.courseName = subject.getCourseName();
        this.courseAbbreviation = subject.getCourseAbbreviation();
        this.courseDetail = subject.getCourseDetail();
        this.professorName = subject.getProfessorName();
        this.courseOpenDate = subject.getCourseOpenDate().toString();
        this.courseCloseDate = subject.getCourseCloseDate().toString();
        this.slotsTaken = subject.getSlotsTaken();
        this.courseCapacity = subject.getCourseCapacity();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseAbbreviation() {
        return courseAbbreviation;
    }

    public void setCourseAbbreviation(String courseAbbreviation) {
        this.courseAbbreviation = courseAbbreviation;
    }

    public String getCourseDetail() {
        return courseDetail;
    }

    public void setCourseDetail(String courseDetail) {
        this.courseDetail = courseDetail;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String getCourseOpenDate() {
        return courseOpenDate;
    }

    public void setCourseOpenDate(String courseOpenDate) {
        this.courseOpenDate = courseOpenDate;
    }

    public String getCourseCloseDate() {
        return courseCloseDate;
    }

    public void setCourseCloseDate(String courseCloseDate) {
        this.courseCloseDate = courseCloseDate;
    }

    public int getSlotsTaken() { return slotsTaken;}

    public void setSlotsTaken(int slotsTaken) {this.slotsTaken = slotsTaken; }

    public int getCourseCapacity() { return courseCapacity; }

    public void setCourseCapacity(int courseCapacity) { this.courseCapacity = courseCapacity; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserCourseDTO userCourseDTO = (UserCourseDTO) o;
        return Objects.equals(id, userCourseDTO.id) && Objects.equals(courseName, userCourseDTO.courseName) && Objects.equals(courseAbbreviation, userCourseDTO.courseAbbreviation) && Objects.equals(courseDetail, userCourseDTO.courseDetail) && Objects.equals(professorName, userCourseDTO.professorName) && Objects.equals(courseOpenDate, userCourseDTO.courseOpenDate) && Objects.equals(courseCloseDate, userCourseDTO.courseCloseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courseName, courseAbbreviation, courseDetail, professorName, courseOpenDate, courseCloseDate);
    }

    @Override
    public String toString() {
        return "CourseDTO{" +
                "id='" + id + '\'' +
                ", courseName='" + courseName + '\'' +
                ", courseAbbreviation='" + courseAbbreviation + '\'' +
                ", courseDetail='" + courseDetail + '\'' +
                ", professorName='" + professorName + '\'' +
                ", courseOpenDate='" + courseOpenDate + '\'' +
                ", courseCloseDate='" + courseCloseDate + '\'' +
                '}';
    }
}
