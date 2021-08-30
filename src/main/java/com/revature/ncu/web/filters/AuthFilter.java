package com.revature.ncu.web.filters;

import com.revature.ncu.web.dtos.Principal;
import com.revature.ncu.web.util.security.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * AuthFilter for setting JWT token header.
 **/
public class AuthFilter extends HttpFilter {

    private final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    private final JwtConfig jwtConfig;

    public AuthFilter(JwtConfig jwtConfig) {
        logger.info("AuthFilter instantiated!");
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        parseToken(req);
        chain.doFilter(req, resp);
    }

    public void parseToken(HttpServletRequest req) {

        try {
            String header = req.getHeader(jwtConfig.getHeader());

            if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
                logger.warn("Request originates from an unauthenticated source.");
                return;
            }

            String token = header.replaceAll(jwtConfig.getPrefix(), "");

            Claims jwtClaims = Jwts.parser()
                    .setSigningKey(jwtConfig.getSigningKey())
                    .parseClaimsJws(token)
                    .getBody();

            req.setAttribute("principal", new Principal(jwtClaims));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}