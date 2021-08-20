package com.revature.ncu.datasources.repositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.revature.ncu.util.exceptions.DataSourceException;
import com.revature.ncu.datasources.documents.AppUser;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


//Repository for performing CRUD operations on the Mongo user collection.
public class UserRepository implements CrudRepository<AppUser>{

    private final Logger logger = LoggerFactory.getLogger(UserRepository.class);
    private final MongoCollection<AppUser> usersCollection;

    // Get connection, access database, and create collection.
    public UserRepository(MongoClient mongoClient) {
        this.usersCollection = mongoClient.getDatabase("p1").getCollection("users", AppUser.class);
    }

    // For logging in with username/password
    public AppUser findUserByCredentials(String username, String encryptedPassword) {

        try {
            // Create a Query doc to search for matching credentials
            Document queryDoc = new Document("username", username).append("password", encryptedPassword);
            return usersCollection.find(queryDoc).first();
        }
            catch (Exception e) {
            logger.error("An unexpected exception occurred.", e);
            throw new DataSourceException(e);
        }

    }

    // For checking if a user exists
    public AppUser findUserByUsername(String username) {

        try {
            return usersCollection.find(new Document("username", username)).first();
        } catch (Exception e) {
            logger.error("An unexpected exception occurred.", e);
            throw new DataSourceException(e);
        }

    }

    // For checking if an email exists
    public AppUser findUserByEmail(String email) {
        try {
            return usersCollection.find(new Document("email", email)).first();
        } catch (Exception e) {
            logger.error("An unexpected exception occurred.", e);
            throw new DataSourceException(e);
        }
    }

    // For grabbing professor name when creating a new course.
    public String getProfName(String id) {

        try {

            Document queryDoc = new Document("_id", id);
            AppUser prof = usersCollection.find(queryDoc).first();
            return prof.getFirstName() + " " + prof.getLastName();

        } catch (Exception e) {
            logger.error("An unexpected exception occurred.", e);
            throw new DataSourceException(e);
        }

    }

    // For listing all users (admin use)
    @Override
    public List<AppUser> findAll() {

        List<AppUser> users = new ArrayList<>();

        try {
            usersCollection.find().into(users);
        } catch (Exception e) {
            logger.error("An unexpected exception occurred.", e);
            throw new DataSourceException(e);
        }
        return users;
    }

    // For session use.
    @Override
    public AppUser findById(String id) {

        try {

            Document queryDoc = new Document("_id", id);
            return usersCollection.find(queryDoc).first();

        } catch (Exception e) {
            logger.error("An unexpected exception occurred.", e);
            throw new DataSourceException(e);
        }

    }

    // For creating a new user
    @Override
    public AppUser save(AppUser newUser) {
        try {
            newUser.setId(new ObjectId().toString());
            usersCollection.insertOne(newUser);
            return newUser;

        } catch (Exception e) {
            logger.error("An unexpected exception occurred.", e);
            throw new DataSourceException(e);
        }
    }

    // No use currently, must be overridden to implement Repo.
    @Override
    public boolean update(AppUser updatedResource) {
        return false;
    }

    @Override
    public boolean deleteById(String id) {
        return false;
    }
}
