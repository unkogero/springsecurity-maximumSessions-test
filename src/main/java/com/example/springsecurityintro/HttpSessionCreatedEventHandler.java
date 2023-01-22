package com.example.springsecurityintro;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.web.session.HttpSessionCreatedEvent;
import org.springframework.stereotype.Component;

@Component
public class HttpSessionCreatedEventHandler implements ApplicationListener<HttpSessionCreatedEvent> {
    Logger logger = LoggerFactory.getLogger(HttpSessionCreatedEventHandler.class);

    @Override
    public void onApplicationEvent(HttpSessionCreatedEvent event) {
        logger.info("# HttpSessionCreatedEvent:" + event);
    }
}
