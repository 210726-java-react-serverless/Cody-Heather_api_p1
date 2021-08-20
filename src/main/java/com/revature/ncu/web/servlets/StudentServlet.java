package com.revature.ncu.web.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


// Dashboard for student options.
// TODO connect to StudentCourse servlet and logout servlet
public class StudentServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(StudentServlet.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Student Dashboard</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>This will be the student dashboard!</h1>");
        out.println("</body>");
        out.println("</html>");
    }
}
