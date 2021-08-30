package com.revature.ncu.web.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ncu.datasources.documents.Course;
import com.revature.ncu.services.CourseService;
import com.revature.ncu.util.exceptions.InvalidEntryException;
import com.revature.ncu.util.exceptions.InvalidRequestException;
import com.revature.ncu.util.exceptions.ResourcePersistenceException;
import com.revature.ncu.web.dtos.ErrorResponse;
import com.revature.ncu.web.dtos.Principal;
import com.revature.ncu.web.dtos.SuccessResponse;
import com.revature.ncu.web.dtos.UserCourseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


/**
 * Student course management operations
 * - View open/registered courses
 * - Register
 * - Withdraw
 * */
public class StudentCourseServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(StudentCourseServlet.class);

    private final CourseService courseService;

    private final ObjectMapper mapper;

    public StudentCourseServlet(CourseService courseService, ObjectMapper mapper){
        this.mapper = mapper;
        this.courseService = courseService;
    }


    // For retrieving all open courses or
    // Retrieving registered courses.
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter respWriter = resp.getWriter();
        resp.setContentType("application/json");

        // Get the principal information from the request, if it exists.
        Principal requestingUser = (Principal) req.getAttribute("principal");

        if (requestingUser == null) {
            String msg = "No session found, please login.";
            logger.info(msg);
            resp.setStatus(401);
            ErrorResponse errResp = new ErrorResponse(401, msg);
            respWriter.write(mapper.writeValueAsString(errResp));
            return;
        }else if(requestingUser.isFaculty())
        {
            String msg = "Hey, you're faculty, go to your dashboard.";
            logger.info(msg);
            resp.setStatus(401);
            ErrorResponse errResp = new ErrorResponse(401, msg);
            respWriter.write(mapper.writeValueAsString(errResp));
            return;
        }

        // Parameter for determining if the request is pulling a user's courses
        //                                                  or all open courses
        String act = req.getParameter("action");

        try{
            // Display open courses
            if(act.equals("view")) {
                List<Course> catalog = courseService.getCourses();
                String payload = mapper.writeValueAsString(catalog);
                respWriter.write(payload);      // Returning all open courses
                resp.setStatus(200);            // 200: Success
            }
            // Display user schedule
            if(act.equals("schedule")) {
                List<UserCourseDTO> schedule = courseService.getCoursesByUsername(requestingUser.getUsername());
                String payload = mapper.writeValueAsString(schedule);
                respWriter.write(payload);      // Returning Student schedule
                resp.setStatus(200);            //200: Success
            }
        }catch (InvalidRequestException | InvalidEntryException ie) {
            ie.printStackTrace();
            resp.setStatus(400);
            ErrorResponse errResp = new ErrorResponse(400, ie.getMessage());
            respWriter.write(mapper.writeValueAsString(errResp));
        } catch (ResourcePersistenceException rpe) {
            resp.setStatus(409);
            ErrorResponse errResp = new ErrorResponse(409, rpe.getMessage());
            respWriter.write(mapper.writeValueAsString(errResp));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }

    // For registering for a course
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter respWriter = resp.getWriter();
        resp.setContentType("application/json");

        // Get the principal information from the request, if it exists.
        Principal requestingUser = (Principal) req.getAttribute("principal");

        if (requestingUser == null) {
            String msg = "No session found, please login.";
            logger.info(msg);
            resp.setStatus(401);
            ErrorResponse errResp = new ErrorResponse(401, msg);
            respWriter.write(mapper.writeValueAsString(errResp));
            return;
        }else if(requestingUser.isFaculty())
        {
            String msg = "Hey, you're faculty, go to your dashboard.";
            logger.info(msg);
            resp.setStatus(401);
            ErrorResponse errResp = new ErrorResponse(401, msg);
            respWriter.write(mapper.writeValueAsString(errResp));
            return;
        }

        try{
            Course course = mapper.readValue(req.getInputStream(), Course.class);
            courseService.joinCourse(course.getCourseAbbreviation(), requestingUser.getUsername());
            SuccessResponse susResp = new SuccessResponse(201, "Successfully joined course!");
            respWriter.write(mapper.writeValueAsString(susResp));

        }catch (InvalidRequestException | InvalidEntryException ie) {
            ie.printStackTrace();
            resp.setStatus(400); // client's fault
            ErrorResponse errResp = new ErrorResponse(400, ie.getMessage());
            respWriter.write(mapper.writeValueAsString(errResp));
        } catch (ResourcePersistenceException rpe) {
            resp.setStatus(409);
            ErrorResponse errResp = new ErrorResponse(409, rpe.getMessage());
            respWriter.write(mapper.writeValueAsString(errResp));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }


    }

    // For withdrawing from a course
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter respWriter = resp.getWriter();
        resp.setContentType("application/json");

        // Get the principal information from the request, if it exists.
        Principal requestingUser = (Principal) req.getAttribute("principal");

        if (requestingUser == null) {
            String msg = "No session found, please login.";
            logger.info(msg);
            resp.setStatus(401);
            ErrorResponse errResp = new ErrorResponse(401, msg);
            respWriter.write(mapper.writeValueAsString(errResp));
            return;
        }else if(requestingUser.isFaculty())
        {
            String msg = "Hey, you're faculty, go to your dashboard.";
            logger.info(msg);
            resp.setStatus(401);
            ErrorResponse errResp = new ErrorResponse(401, msg);
            respWriter.write(mapper.writeValueAsString(errResp));
            return;
        }

        try{
            // Get abbreviation from frontend and map it to remove
            Course remove = mapper.readValue(req.getInputStream(), Course.class);
            String removeAbv = remove.getCourseAbbreviation();

            // Remove student from course.
            courseService.removeStudent(requestingUser.getUsername(), removeAbv);

            // Inform user of successful action
            SuccessResponse susResp = new SuccessResponse(204, "Successfully withdrew from course!");
            respWriter.write(mapper.writeValueAsString(susResp));

        }catch (InvalidRequestException | InvalidEntryException ie) {
            ie.printStackTrace();
            resp.setStatus(400);
            ErrorResponse errResp = new ErrorResponse(400, ie.getMessage());
            respWriter.write(mapper.writeValueAsString(errResp));
        } catch (ResourcePersistenceException rpe) {
            resp.setStatus(409);
            ErrorResponse errResp = new ErrorResponse(409, rpe.getMessage());
            respWriter.write(mapper.writeValueAsString(errResp));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }

}
