package com.example.springsecurityintro;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private SessionRegistry sessionRegistry;
    private MySessionObj mySessionObj;

    public MyAuthenticationSuccessHandler(SessionRegistry sessionRegistry,MySessionObj mySessionObj) {
        this.sessionRegistry = sessionRegistry;
        this.mySessionObj = mySessionObj;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // List<Object> principals = mySessionObj.getList();
        List<Object> principals = sessionRegistry.getAllPrincipals();
        LoginUserDetails userDetails = (LoginUserDetails)authentication.getPrincipal();

        for( Object p : principals ){
            if (p instanceof LoginUserDetails) {
                LoginUserDetails u = (LoginUserDetails)p;
                if(userDetails.getUsername().equals(u.getUsername())){
                    throw new SessionAuthenticationException("おーばー");
                }
            }
        }

        response.sendRedirect("/");
        principals.add(userDetails); // 先で例外が起こったら削除する必要ある
    }
}
