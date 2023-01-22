package com.example.springsecurityintro;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;

public class MySessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

    private SessionRegistry sessionRegistry;
    private MySessionObj mySessionObj;

    public MySessionInformationExpiredStrategy(SessionRegistry sessionRegistry,MySessionObj mySessionObj) {
        this.sessionRegistry = sessionRegistry;
        this.mySessionObj = mySessionObj;
    }

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        sessionRegistry.removeSessionInformation(event.getSessionInformation().getSessionId());

        LoginUserDetails userDetails = (LoginUserDetails)event.getSessionInformation().getPrincipal();
        mySessionObj.getList().remove(userDetails);

        DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
        redirectStrategy.sendRedirect(event.getRequest(), event.getResponse(), "/login");
    }
}
