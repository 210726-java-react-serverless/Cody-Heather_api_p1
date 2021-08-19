package com.revature.ncu.datasources.repositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.revature.ncu.datasources.documents.Course;
import com.revature.ncu.util.exceptions.DataSourceException;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.List;

// Repository for performing CRUD operations on the Mongo courses collection

public class CourseRepository implements CrudRepository<Course> {

    private final Logger logger = LoggerFactory.getLogger(CourseRepository.class);
    private final MongoCollection<Course> coursesCollection;

    // Get connection, access database, and create collection.
    public CourseRepository(MongoClient mongoClient){
        this.coursesCollection = mongoClient.getDatabase("p1").getCollection("courses", Course.class);
    }

    public Course findCourseByName(String courseName) {
        try {
            return coursesCollection.find(new Document("courseName",courseName)).first();
        } catch (Exception e) {
            logger.error("An unexpected exception occurred.", e);
            throw new DataSourceException(e);
        }
    }

    public Course findCourseByAbbreviation(String courseAbv) {
        try {
            return coursesCollection.find(new Document("courseAbbreviation", courseAbv)).first();

        } catch (Exception e) {
            logger.error("An unexpected exception occurred.", e);
            throw new DataSourceException(e);
        }
    }

    // TODO pull a list of courses where openDate is before currentDate and closeDate is after currentDate
    public List<Course> retrieveOpenCourses() {
            return null;
    }


    // TODO method for updating all values at once
    public Course updateCourse(Course originalCourse,Course updatedCourse){

        return null;
    }

    public void updatingCourseName(Course original,String newName){
        try {
            // Append $set to "courseName" : newName
            Document updateDoc = new Document("courseName", newName);
            Document appendDoc = new Document("$set",updateDoc);
            // Search for "courseName" : original courseName
            Document searchDoc = new Document("courseName",original.getCourseName());
            // Update course name
            coursesCollection.updateOne(searchDoc,appendDoc);
        } catch (Exception e) {
            logger.error("An unexpected exception occurred.", e);
            throw new DataSourceException(e);
        }

    }

    public void updatingCourseAbv(Course original, String newAbv){
        try {
            Document updateDoc = new Document("courseAbbreviation", newAbv);
            Document appendDoc = new Document("$set",updateDoc);
            Document searchDoc = new Document("courseAbbreviation", original.getCourseAbbreviation());

            coursesCollection.updateOne(searchDoc,appendDoc);

        } catch (Exception e) {
            logger.error("An unexpected exception occurred.", e);
            throw new DataSourceException(e);
        }

    }

    public void updatingCourseDesc(Course original, String newDesc){
        try {
            Document updateDoc = new Document("courseDetail", newDesc);
            Document appendDoc = new Document("$set",updateDoc);
            Document searchDoc = new Document("courseAbbreviation",original.getCourseAbbreviation());

            coursesCollection.updateOne(searchDoc,appendDoc);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("An unexpected exception occurred.", e);
            throw new DataSourceException(e);
        }

    }


    // Remove a course from the database
    public void removeCourseByAbbreviation(Course course){
        try {
            Document queryDoc = new Document("courseAbbreviation", course.getCourseAbbreviation());
            coursesCollection.findOneAndDelete(queryDoc);
        }catch (Exception e){
            logger.error("An unexpected exception occurred.", e);
            throw new DataSourceException(e);
        }
    }

    // When a student joins a course
    public void addStudentID(Course course,String studentId){

        try {
            // Search by course ID
            Document searchDoc = new Document("_id",course.getId());

            // Create $push command to push ID into array on database
            Document updateDoc = new Document("studentIds", studentId);
            Document appendDoc = new Document("$push",updateDoc);
            coursesCollection.updateOne(searchDoc,appendDoc);

            Document slotsDoc = new Document("slotsTaken", 1);
            Document incDoc = new Document("$inc",slotsDoc);
            coursesCollection.updateOne(searchDoc,incDoc);

        }catch (Exception e){
            logger.error("An unexpected exception occurred.", e);
            throw new DataSourceException(e);
        }


    }

    // For listing all courses
    @Override
    public List<Course> findAll(){

        List<Course> courses = new ArrayList<>();

        try{
            coursesCollection.find().into(courses);
        }catch (Exception e) {
            logger.error("An unexpected exception occurred.", e);
            throw new DataSourceException(e);
        }
        return courses;

    }

    @Override
    public Course findById(String id) {
        try {
            Document queryDoc = new Document("_id", id);
            return coursesCollection.find(queryDoc).first();
        }catch (Exception e) {
            logger.error("An unexpected exception occurred.", e);
            throw new DataSourceException(e);
        }
    }

    // Save the course to the database
    @Override
    public Course save(Course newCourse) {
        try {
            newCourse.setId(new ObjectId().toString());
            coursesCollection.insertOne(newCourse);
            return newCourse;

        } catch (Exception e) {
            logger.error("An unexpected exception occurred.", e);
            throw new DataSourceException(e);
        }
    }

    @Override
    public boolean update(Course updatedResource) {
        return false;
    }

    @Override
    public boolean deleteById(String id) {
        return false;
    }

}
