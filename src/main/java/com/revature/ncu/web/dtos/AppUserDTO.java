package com.revature.ncu.web.dtos;

import com.revature.ncu.datasources.documents.AppUser;

import java.util.Objects;

/**
 * DTO Wrapper for User information to be sent to the front end.
 * Excludes password.
 */
public class AppUserDTO {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private boolean isFaculty; //boolean to determine if the user is faculty or student

    public AppUserDTO(AppUser subject){
        this.id = subject.getId();
        this.firstName = subject.getFirstName();
        this.lastName = subject.getLastName();
        this.email = subject.getEmail();
        this.username = subject.getUsername();
        this.isFaculty = subject.isFaculty();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isFaculty() { return isFaculty; }

    public void setFaculty(boolean faculty) { isFaculty = faculty; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUserDTO that = (AppUserDTO) o;
        return isFaculty == that.isFaculty && Objects.equals(id, that.id) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email) && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, username, isFaculty);
    }

    @Override
    public String toString() {
        return "AppUserDTO{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", isFaculty=" + isFaculty +
                '}';
    }
}
