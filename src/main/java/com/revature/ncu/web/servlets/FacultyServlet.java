package com.revature.ncu.web.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class FacultyServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(CourseServlet.class);

    public FacultyServlet facultyServlet = new FacultyServlet(){
    };

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println(req.getAttribute("filtered"));
        PrintWriter respWriter = resp.getWriter();
        resp.setContentType("application/json");

        try {
            //  HttpSession session = req.getSession().invalidate();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
