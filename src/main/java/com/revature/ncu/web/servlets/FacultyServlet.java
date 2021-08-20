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
    private final Logger logger = LoggerFactory.getLogger(FacultyServlet.class);


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Faculty Dashboard</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>This will be the faculty dashboard!</h1>");
        out.println("</body>");
        out.println("</html>");
    }
}
