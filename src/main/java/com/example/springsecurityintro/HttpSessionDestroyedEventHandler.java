package com.example.springsecurityintro;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;

@Component
public class HttpSessionDestroyedEventHandler implements ApplicationListener<HttpSessionDestroyedEvent> {
    Logger logger = LoggerFactory.getLogger(HttpSessionDestroyedEventHandler.class);

    @Override
    public void onApplicationEvent(HttpSessionDestroyedEvent event) {
        logger.info("# HttpSessionDestroyedEvent:" + event);
    }
}