package com.revature.ncu.web.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ncu.services.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CourseServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(CourseServlet.class);
    private final CourseService courseService;
    private final ObjectMapper mapper;

    public CourseServlet(CourseService courseService, ObjectMapper mapper){
        this.courseService = courseService;
        this.mapper = mapper;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println(req.getAttribute("filtered"));
        PrintWriter respWriter = resp.getWriter();
        resp.setContentType("application/json");
    }
}
