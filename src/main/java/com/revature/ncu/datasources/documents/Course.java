package com.revature.ncu.datasources.documents;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


// Course POJO
public class Course {

    private String id;
    private String courseName;
    private String courseAbbreviation;
    private String courseDetail;
    private String professorName;
    //TODO logic for these
    private LocalDate courseOpenDate;
    private LocalDate courseCloseDate;
    private Set<String> studentIds;
    private int slotsTaken;
    private int courseCapacity;

    public Course(){ super(); }

    public Course(String cn, String cAbv){
        this.courseName = cn;
        this.courseAbbreviation = cAbv;
    }

    // Keeping this for the time being since removing it breaks hundreds of lines
    public Course(String cn, String cAbv, String detail) {
        this.courseName = cn;
        this.courseAbbreviation = cAbv;
        this.courseDetail = detail;
    }

    // For creating a new course.
    public Course(String cn, String cAbv, String detail,
                  LocalDate openDate, LocalDate closeDate, int cap){
        this.courseName = cn;
        this.courseAbbreviation = cAbv;
        this.courseDetail = detail;
        this.courseOpenDate = openDate;
        this.courseCloseDate = closeDate;
        this.courseCapacity = cap;
        this.slotsTaken = 0;
        this.studentIds = new HashSet<String>(cap);
    }

    // For pulling a course from the database
    public Course(String id, String cn, String cAbv, String detail, String prof,
                  LocalDate openDate, LocalDate closeDate,Set<String> stuId, int cap, int slots){
        this.id = id;
        this.courseName = cn;
        this.courseAbbreviation = cAbv;
        this.courseDetail = detail;
        this.professorName = prof;
        this.courseOpenDate = openDate;
        this.courseCloseDate = closeDate;
        this.studentIds = stuId;
        this.courseCapacity = cap;
        this.slotsTaken = slots;
    }

    // Setters and getters
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDetail() {
        return courseDetail;
    }

    public void setCourseDetail(String courseDetail) {
        this.courseDetail = courseDetail;
    }

    public void setId(String id) { this.id = id; }

    public String getId() {
        return id;
    }

    public String getCourseAbbreviation() { return courseAbbreviation; }

    public void setCourseAbbreviation(String courseAbbreviation) { this.courseAbbreviation = courseAbbreviation; }

    public String getProfessorName() { return professorName; }

    public void setProfessorName(String professorName) { this.professorName = professorName;}

    public LocalDate getCourseOpenDate() { return courseOpenDate; }

    public void setCourseOpenDate(LocalDate courseOpenDate) { this.courseOpenDate = courseOpenDate; }

    public LocalDate getCourseCloseDate() { return courseCloseDate; }

    public void setCourseCloseDate(LocalDate courseCloseDate) { this.courseCloseDate = courseCloseDate; }

    public int getSlotsTaken() { return slotsTaken; }

    public void setSlotsTaken(int slotsTaken) { this.slotsTaken = slotsTaken; }

    public int getCourseCapacity() { return courseCapacity; }

    public void setCourseCapacity(int courseCapacity) { this.courseCapacity = courseCapacity; }

    public Set<String> getStudentIds() { return studentIds; }

    public void setStudentIds(Set<String> studentIds) { this.studentIds = studentIds; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id) && Objects.equals(courseName, course.courseName) && Objects.equals(courseAbbreviation, course.courseAbbreviation) && Objects.equals(courseDetail, course.courseDetail) && Objects.equals(professorName, course.professorName) && Objects.equals(courseOpenDate, course.courseOpenDate) && Objects.equals(courseCloseDate, course.courseCloseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courseName, courseAbbreviation, courseDetail, professorName, courseOpenDate, courseCloseDate);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", courseName='" + courseName + '\'' +
                ", courseAbbreviation='" + courseAbbreviation + '\'' +
                ", courseDetail='" + courseDetail + '\'' +
                ", professorName='" + professorName + '\'' +
                ", courseOpenDate=" + courseOpenDate +
                ", courseCloseDate=" + courseCloseDate +
                '}';
    }
}
