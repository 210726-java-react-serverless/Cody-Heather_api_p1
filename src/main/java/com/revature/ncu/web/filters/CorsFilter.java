package com.revature.ncu.web.filters;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter ("/*")
public class CorsFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        req.setAttribute("filtered", true);
        resp.setHeader("Access-Control-Allow-Origin", "http://chp1ui.s3-website.us-east-2.amazonaws.com/*");
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        chain.doFilter(req, resp);
    }

}