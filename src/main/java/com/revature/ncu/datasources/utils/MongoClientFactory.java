package com.revature.ncu.datasources.utils;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import com.revature.ncu.util.exceptions.DataSourceException;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 *  An eager Singleton Factory pattern for supplying DAO (data access object)
 *  classes with a connection to the Mongo Database
 *
 *  Limits code duplication by building the components needed for the MongoClient to communicate with the database.
 *
 */

public class MongoClientFactory {

    private final MongoClient mongoClient;

    // Eager singleton, instantiated as soon as the class is loaded.
    private static final MongoClientFactory mongoClientFactory = new MongoClientFactory();

    private final Logger logger = LoggerFactory.getLogger(MongoClientFactory.class);

    private MongoClientFactory() {

        Properties appProperties = new Properties();

        try{
            // Retrieving the application.properties file
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            appProperties.load(loader.getResourceAsStream("application.properties"));

            // Retrieving information from the application.properties file
            String ipAddress = appProperties.getProperty("ipAddress");
            int port = Integer.parseInt(appProperties.getProperty("port"));
            String dbName = appProperties.getProperty("dbName");
            String username = appProperties.getProperty("username");
            char[] password = appProperties.getProperty("password").toCharArray();

            // A serializable list containing only the ServerAddress to be sent to the MDB client
            List<ServerAddress> hosts = Collections.singletonList(new ServerAddress(ipAddress, port));

            // Using MongoDB's native SCRAM-SHA-1 support to encrypt sensitive information.
            MongoCredential credentials = MongoCredential.createScramSha1Credential(username, dbName, password);

            // Getting Codec registry and POJO Codec provider so that mongo knows what to do with POJOs
            CodecRegistry defaultCodecRegistry = getDefaultCodecRegistry();
            PojoCodecProvider pojoCodecProvider= PojoCodecProvider.builder().automatic(true).build();
            CodecRegistry pojoCodecRegistry = fromRegistries(defaultCodecRegistry, fromProviders(pojoCodecProvider));

            // Building the MongoDB client settings file.
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyToClusterSettings(builder -> builder.hosts(hosts))
                    .credential(credentials)
                    .codecRegistry(pojoCodecRegistry)
                    .build();


            this.mongoClient = MongoClients.create(settings);

        }catch (FileNotFoundException fnfe) {
            logger.error("Unable to load database properties file.");
            throw new DataSourceException(fnfe);
        } catch(Exception e){
            e.printStackTrace();
            logger.error("An unexpected exception occurred.");
            throw new DataSourceException(e);
        }

    }

    public void cleanUp(){
        mongoClient.close();
    }

    public static MongoClientFactory getInstance(){
        return mongoClientFactory;
    }

    public MongoClient getConnection(){
        return mongoClient;
    }
}
