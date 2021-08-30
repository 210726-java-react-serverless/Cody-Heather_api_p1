package com.revature.ncu.web.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ncu.datasources.documents.Course;
import com.revature.ncu.services.CourseService;
import com.revature.ncu.services.UserService;
import com.revature.ncu.util.exceptions.InvalidEntryException;
import com.revature.ncu.util.exceptions.InvalidRequestException;
import com.revature.ncu.util.exceptions.ResourcePersistenceException;
import com.revature.ncu.web.dtos.ErrorResponse;
import com.revature.ncu.web.dtos.Principal;
import com.revature.ncu.web.dtos.SuccessResponse;
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
 * Faculty servlet, unused -- would be for changing student profile info.
 */

public class CourseServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(CourseServlet.class);
    private final UserService userService;
    private final CourseService courseService;
    private final ObjectMapper mapper;

    public CourseServlet(UserService userService, CourseService courseService, ObjectMapper mapper){
        this.userService = userService;
        this.courseService = courseService;
        this.mapper = mapper;
    }

    // For viewing all courses.
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
        }else if(!requestingUser.isFaculty())
        {
            String msg = "You're not supposed to be here. Action has been logged.";
            logger.info(msg);
            resp.setStatus(401);
            ErrorResponse errResp = new ErrorResponse(401, msg);
            respWriter.write(mapper.writeValueAsString(errResp));
            return;
        }

        try{
            List<Course> catalog = courseService.getAllCourses();
            String payload = mapper.writeValueAsString(catalog);
            respWriter.write(payload);
            resp.setStatus(200);

        }catch (InvalidRequestException | InvalidEntryException ie) {
            ie.printStackTrace();
            resp.setStatus(400); // client's fault
            ErrorResponse errResp = new ErrorResponse(400, ie.getMessage());
            respWriter.write(mapper.writeValueAsString(errResp));
        }catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }

    // For adding a course
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
        }else if(!requestingUser.isFaculty())
        {
            String msg = "You're not supposed to be here. Action has been logged.";
            logger.info(msg);
            resp.setStatus(401);
            ErrorResponse errResp = new ErrorResponse(401, msg);
            respWriter.write(mapper.writeValueAsString(errResp));
            return;
        }

        try{
                Course newCourse = mapper.readValue(req.getInputStream(), Course.class);
                String profName = userService.getProfNameById(requestingUser.getId());
                newCourse.setProfessorName(profName);// get professor name
                courseService.add(newCourse);

                String payload = mapper.writeValueAsString(newCourse);  //maps the principal value to a string
                respWriter.write(payload);      //returning the username and ID to the web as a string value
                resp.setStatus(201);            //201: Created

        }catch (InvalidRequestException | InvalidEntryException ie) {
            ie.printStackTrace();
            resp.setStatus(400); // client's fault
            ErrorResponse errResp = new ErrorResponse(400, ie.getMessage());
            respWriter.write(mapper.writeValueAsString(errResp));
        } catch (ResourcePersistenceException rpe) {
            resp.setStatus(409);   //409 conflict: user/email already exists
            ErrorResponse errResp = new ErrorResponse(409, rpe.getMessage());
            respWriter.write(mapper.writeValueAsString(errResp));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);    // server made an oopsie woopsie
        }


    }

    // For removing a course.
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
        }else if(!requestingUser.isFaculty())
        {
            String msg = "You're not supposed to be here. Action has been logged.";
            logger.info(msg);
            resp.setStatus(401);
            ErrorResponse errResp = new ErrorResponse(401, msg);
            respWriter.write(mapper.writeValueAsString(errResp));
            return;
        }

        try{
            Course remove = mapper.readValue(req.getInputStream(), Course.class);
            courseService.removeCourse(remove);
            SuccessResponse susResp = new SuccessResponse(204, "Successfully removed course!");
            respWriter.write(mapper.writeValueAsString(susResp));

        }catch (InvalidRequestException | InvalidEntryException ie) {
            ie.printStackTrace();
            resp.setStatus(400); // client's fault
            ErrorResponse errResp = new ErrorResponse(400, ie.getMessage());
            respWriter.write(mapper.writeValueAsString(errResp));
        } catch (ResourcePersistenceException rpe) {
            resp.setStatus(409);   //409 conflict: user/email already exists
            ErrorResponse errResp = new ErrorResponse(409, rpe.getMessage());
            respWriter.write(mapper.writeValueAsString(errResp));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);    // server made an oopsie woopsie
        }
    }

    // For updating a course.
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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
        }else if(!requestingUser.isFaculty())
        {
            String msg = "You're not supposed to be here. Action has been logged.";
            logger.info(msg);
            resp.setStatus(401);
            ErrorResponse errResp = new ErrorResponse(401, msg);
            respWriter.write(mapper.writeValueAsString(errResp));
            return;
        }
            String editParam = req.getParameter("edit");
        try{
            if(editParam == null){
                String response = "Invalid course provided";
                respWriter.write(response);
                ErrorResponse errResp = new ErrorResponse(400, response);
                respWriter.write(mapper.writeValueAsString(errResp));
                return;
            }
            Course original = courseService.findCourseByAbbreviation(editParam);
            Course editCourse = mapper.readValue(req.getInputStream(), Course.class);
            String profName = userService.getProfNameById(requestingUser.getId());
            editCourse.setProfessorName(profName);// get professor name
            courseService.updateCourse(original, editCourse);

            SuccessResponse susResp = new SuccessResponse(201, "Course updated!");
            respWriter.write(mapper.writeValueAsString(susResp));

        }catch (InvalidRequestException | InvalidEntryException ie) {
            ie.printStackTrace();
            resp.setStatus(400); // client's fault
            ErrorResponse errResp = new ErrorResponse(400, ie.getMessage());
            respWriter.write(mapper.writeValueAsString(errResp));
        } catch (ResourcePersistenceException rpe) {
            resp.setStatus(409);   //409 conflict: user/email already exists
            ErrorResponse errResp = new ErrorResponse(409, rpe.getMessage());
            respWriter.write(mapper.writeValueAsString(errResp));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);    // server made an oopsie woopsie
        }


    }
}
