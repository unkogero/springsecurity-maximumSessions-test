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

        LoginUserDetails userDetails = (LoginUserDetails)authentication.getPrincipal();

        this.sessionRegistry.refreshLastRequest(httpServletRequest.getSession().getId());

        List<Object> plist = this.sessionRegistry.getAllPrincipals();
        for( Object p : plist ){
            if (p instanceof LoginUserDetails) {
                List<SessionInformation> list = this.sessionRegistry.getAllSessions(p, true);
                for( int i=0; i<list.size(); i++) {
                    SessionInformation info = list.get(i);
                    this.sessionRegistry.refreshLastRequest(info.getSessionId());
                    // ここしょうがないので自分で時間判定すればよいかも★
                    if(info.isExpired()){
                        this.sessionRegistry.removeSessionInformation(info.getSessionId());
                    }
                }
            }
        }

        List<Object> plist2 = this.sessionRegistry.getAllPrincipals();
        for( Object p : plist2 ){
            if (p instanceof LoginUserDetails) {
                List<SessionInformation> list = this.sessionRegistry.getAllSessions(p, true);
                for( int i=0; i<list.size(); i++) {
                    SessionInformation info = list.get(i);
                    if(info.isExpired()){
                        this.sessionRegistry.removeSessionInformation(info.getSessionId());
                    }
                }
            }
        }

        //LoginUserDetails userDetails = (LoginUserDetails)authentication.getPrincipal();
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

