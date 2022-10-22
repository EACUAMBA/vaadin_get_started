package com.example.application.security;

import com.example.application.views.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Collections;

//Enable Spring Security.
@EnableWebSecurity
@Configuration
public class SecurityConfig
        //Extend the VaadinWebSecurity class to configure Spring Security for Vaadin.
        extends VaadinWebSecurity {

    private static class SimpleInMemoryUserDetailsManager extends InMemoryUserDetailsManager{
        public SimpleInMemoryUserDetailsManager(){
            createUser(new User(
                    "user",
                    "{noop}userpass",
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
            ));

            createUser(new User(
                    "admin",
                    "{noop}userpass",
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))
            ));
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Allow public access to the image directory.
        http.authorizeRequests()
                .antMatchers("/images/**")
                .permitAll();

        super.configure(http);

        //Allow access to LoginView.
        setLoginView(http, LoginView.class);
    }

    //Configure an in-memory user for testing (see note below).
    @Bean
    public InMemoryUserDetailsManager userDetailsManager(){
        return new SimpleInMemoryUserDetailsManager();
    }
}
