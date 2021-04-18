package com.coding.assignment.configuration.security;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtTokenAuthorizationOncePerRequestFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SecurityService securityService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.http.request.header}")
    private String tokenHeader;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        logger.debug("Authentication Request For '{}'", request.getRequestURL());

        final String requestTokenHeader = request.getHeader(this.tokenHeader);
        final String remoteUser = request.getRemoteUser();
        final String requestURI = request.getRequestURI();
        final String remoteAddress = request.getRemoteAddr();

        String username = null;
        String jwtToken = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                logger.error("JWT_TOKEN_UNABLE_TO_GET_USERNAME", e);
            } catch (ExpiredJwtException e) {
                logger.warn("JWT_TOKEN_EXPIRED", e);
            }
        } else {
            logger.warn("JWT_TOKEN_DOES_NOT_START_WITH_BEARER_STRING, remoteUser: "+remoteUser+", requestURI: "+requestURI+", remoteAdress: "+remoteAddress);
        }

        logger.debug("JWT_TOKEN_USERNAME_VALUE '{}'", username);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            User user = securityService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(jwtToken, user)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(user, null, null);
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

        }

        // As this authentication is in HTTP header, after success we need to continue the request normally
        // and return the response as if the resource was not secured at all
        chain.doFilter(request, response);
    }
}
