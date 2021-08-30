package com.revature.ncu.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Student servlet, unused -- would be for changing student profile info, etc.
 */
public class StudentServlet extends HttpServlet {


    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Student Profile</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>This could be the student profile!</h1>");
        out.println("</body>");
        out.println("</html>");
    }
}
