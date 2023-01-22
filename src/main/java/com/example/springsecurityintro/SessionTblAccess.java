package com.example.springsecurityintro;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.session.Session;
import org.springframework.session.jdbc.JdbcIndexedSessionRepository;
import org.springframework.session.jdbc.JdbcOperationsSessionRepository;

import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import ch.qos.logback.core.model.Model;

@Component
@EnableJdbcHttpSession
public class SessionTblAccess {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    PlatformTransactionManager transactionManager;

    private JdbcOperationsSessionRepository repository;
/* 
    public String getSessions(String username) {
        repository = new JdbcOperationsSessionRepository(jdbcTemplate, transactionManager);
        Map<String, JdbcSession> sessions = repository.findByPrincipalName(username);   

        return "flpage";
    }
    */
}