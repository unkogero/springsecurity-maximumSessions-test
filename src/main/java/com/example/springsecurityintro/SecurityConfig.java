package com.example.springsecurityintro;

import java.beans.BeanProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.jaas.LoginExceptionResolver;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import jakarta.servlet.ServletContextListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

    @Autowired
    MySessionObj mySessionObj;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(login -> login
                .loginProcessingUrl("/login")
                .loginPage("/login")
                //.defaultSuccessUrl("/")
                //.failureUrl("/login?error")
                .failureHandler(authenticationFailureHandler()) 
                //.successHandler(new MyAuthenticationSuccessHandler(sessionRegistry(), mySessionObj))
                .permitAll()
        ).logout(logout -> logout
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/")
                // jdbcの場合セッションイベントが来ないので、ログアウト時に自分でsessionRegistryを制御
                .addLogoutHandler(new MyLogoutHandler(sessionRegistry(), mySessionObj))
        ).authorizeHttpRequests(authz -> authz
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/").permitAll()
                .requestMatchers("/general").hasRole("GENERAL")
                .requestMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()
        ).sessionManagement(session -> session
            //.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            //.invalidSessionUrl("/login?error")
            .sessionFixation()
            .changeSessionId()
            .sessionAuthenticationStrategy(sessionstrategy())
            
            /* 以下を入れないとログインしてもsessionRegistryが増えなかった。
            あとは、expiredSessionStrategyが使えないとすると、セッション切れは自分で検知しないとだめなので確認。
            ログイン情報をセッションオブジェクトにいれれば切れの管理はできるが、セッションオブジェクトだとセッションごとの数の管理になってしまう。
            ふつうにDBみたほうがよいかも。（SpringSessionの仕様が変わるかもしれないので、自分で管理したほうがよいのか）
            */
            .maximumSessions(1)
            .maxSessionsPreventsLogin(true)
            .sessionRegistry(sessionRegistry())
            .expiredSessionStrategy(new MySessionInformationExpiredStrategy(sessionRegistry(), mySessionObj))
            

             //.expiredUrl("/error/expired")
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        return sessionRegistry;
    }

    /*
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
    */

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    protected SessionAuthenticationStrategy sessionstrategy(){
        RegisterSessionAuthenticationStrategy registerSessionAuthenticationStrategy = new RegisterSessionAuthenticationStrategy(sessionRegistry());
        ConcurrentSessionControlAuthenticationStrategy  concurrentSessionControlAuthenticationStrategy = new CustomConcurrentSessionControlAuthenticationStrategy(sessionRegistry());
        List<SessionAuthenticationStrategy> deletgateStrategies = new ArrayList<>();
        deletgateStrategies.addAll(Arrays.asList(concurrentSessionControlAuthenticationStrategy,registerSessionAuthenticationStrategy));
        return new CompositeSessionAuthenticationStrategy(deletgateStrategies);
    }

    /*
    @Bean
    public ConcurrentSessionFilter concurrentSessionFilter() {
        ConcurrentSessionFilter filter = new ConcurrentSessionFilter(sessionRegistry(),new MySessionInformationExpiredStrategy());
        return filter;
    }
     */
}
