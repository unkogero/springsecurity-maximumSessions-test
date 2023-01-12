package com.example.springsecurityintro;

import java.io.IOException;

import javax.naming.AuthenticationException;

import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationFailureHandler
implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            org.springframework.security.core.AuthenticationException exception) throws IOException, ServletException {
                String forwardUrl = "/";

                RequestDispatcher dispatch = request.getRequestDispatcher(forwardUrl);
                dispatch.forward(request, response);
        
    }
}