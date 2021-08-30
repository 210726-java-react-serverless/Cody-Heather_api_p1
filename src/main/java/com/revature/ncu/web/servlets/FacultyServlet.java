package com.revature.ncu.web.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Faculty servlet, unused -- would be for changing faculty profile info, etc.
 */
public class FacultyServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Faculty Profile</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>This could be the faculty profile!</h1>");
        out.println("</body>");
        out.println("</html>");
    }
}
