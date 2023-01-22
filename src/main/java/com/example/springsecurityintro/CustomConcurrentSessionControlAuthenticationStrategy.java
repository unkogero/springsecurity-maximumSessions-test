package com.example.springsecurityintro;

import java.security.Principal;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomConcurrentSessionControlAuthenticationStrategy extends ConcurrentSessionControlAuthenticationStrategy{
    private SessionRegistry sessionRegistry;

    public CustomConcurrentSessionControlAuthenticationStrategy(SessionRegistry sessionRegistry) {
        super(sessionRegistry);
        this.sessionRegistry = sessionRegistry;
        setExceptionIfMaximumExceeded(true);
        setMaximumSessions(1);
    }
    
    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {

        List<SessionInformation> sessionInfoList = this.sessionRegistry.getAllSessions(authentication.getPrincipal(), true);
        this.sessionRegistry.refreshLastRequest(httpServletRequest.getSession().getId());
        for( int i=0; i<sessionInfoList.size(); i++) {
            SessionInformation info = sessionInfoList.get(i);
            if(info.isExpired()){
                this.sessionRegistry.removeSessionInformation(info.getSessionId());
            }
        }

        LoginUserDetails userDetails = (LoginUserDetails)authentication.getPrincipal();
        List<Object> principals = this.sessionRegistry.getAllPrincipals();
        for( Object p : principals ){
            if (p instanceof LoginUserDetails) {
                LoginUserDetails u = (LoginUserDetails)p;
                if(userDetails.getUsername().equals(u.getUsername())){
                    if( u.isCredentialsNonExpired() ){
                        throw new SessionAuthenticationException("おーばー");
                    } else {
                        //this.sessionRegistry.removeSessionInformation(userDetails.();
                    }
                }
            }
        }

        // allowableSessionsExceeded(sessions, allowdNum, this.sessionRegistry);
    }
}

