package com.revature.ncu.web.dtos;

import com.revature.ncu.datasources.documents.AppUser;

import java.util.Objects;

public class Principal {

    private String id;
    private String username;
    private boolean faculty;

    public Principal() {
        super();
    }

    public Principal(AppUser subject) {
        this.id = subject.getId();
        this.username = subject.getUsername();
        this.faculty = subject.isFaculty();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isFaculty() {
        return faculty;
    }

    public void setFaculty(boolean faculty) {
        this.faculty = faculty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Principal principal = (Principal) o;
        return Objects.equals(id, principal.id) && Objects.equals(username, principal.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    @Override
    public String toString() {
        return "Principal{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

}
