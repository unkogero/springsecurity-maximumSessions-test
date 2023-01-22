package com.example.springsecurityintro;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MyLogoutHandler implements LogoutHandler{

    private SessionRegistry sessionRegistry;
    private MySessionObj mySessionObj;

    public MyLogoutHandler(SessionRegistry sessionRegistry,MySessionObj mySessionObj) {
        this.sessionRegistry = sessionRegistry;
        this.mySessionObj = mySessionObj;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        sessionRegistry.removeSessionInformation(request.getSession().getId());

        LoginUserDetails userDetails = (LoginUserDetails)authentication.getPrincipal();
        mySessionObj.getList().remove(userDetails);
    }
}
