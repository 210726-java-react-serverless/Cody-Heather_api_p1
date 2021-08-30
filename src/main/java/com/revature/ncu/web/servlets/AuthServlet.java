package com.revature.ncu.web.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.ncu.services.UserService;
import com.revature.ncu.util.exceptions.AuthenticationException;
import com.revature.ncu.web.dtos.Credentials;
import com.revature.ncu.web.dtos.ErrorResponse;
import com.revature.ncu.web.dtos.Principal;
import com.revature.ncu.web.util.security.TokenGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Authorization Servlet
 * - Logs user in
 * - Provides a JWT token for session management
 */
public class AuthServlet extends HttpServlet {

    private final UserService userService;
    private final ObjectMapper mapper;
    private final TokenGenerator tokenGenerator;

    public AuthServlet(UserService userService, ObjectMapper mapper, TokenGenerator tokenGenerator) {
        this.userService = userService;
        this.mapper =mapper;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter respWriter = resp.getWriter();
        resp.setContentType("application/json");

        try {
            // Retrieves credentials from user
            Credentials creds = mapper.readValue(req.getInputStream(), Credentials.class);

            // Creates principal for session
            Principal principal = userService.login(creds.getUsername(), creds.getPassword());

            // Prints out username and ID
            String payload = mapper.writeValueAsString(principal);
            respWriter.write(payload);

            // Creates and sets session of currently authorized user
            String token = tokenGenerator.createToken(principal);
            resp.setHeader(tokenGenerator.getJwtConfig().getHeader(), token);

        } catch (AuthenticationException ae) { //woops wrong creds
            resp.setStatus(401); // user's fault
            ErrorResponse errResp = new ErrorResponse(401, ae.getMessage());
            respWriter.write(mapper.writeValueAsString(errResp));
        }  catch (Exception e) { //woops we messed up
            e.printStackTrace();
            resp.setStatus(500); // server's fault
            ErrorResponse errResp = new ErrorResponse(500, "The server experienced an issue, please try again later.");
            respWriter.write(mapper.writeValueAsString(errResp));
        }

    }
}
