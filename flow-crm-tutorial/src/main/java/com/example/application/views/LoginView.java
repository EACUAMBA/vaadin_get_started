package com.example.application.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

//Map the view to the "login" path. LoginView should take up the whole browser window, so don’t use MainLayout as the parent.
@Route(value = "login")
@PageTitle("Login | Vaadin CRM")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    //Instantiate a LoginForm component to capture username and password.
    private final LoginForm loginForm = new LoginForm();

    public LoginView(){
        addClassName("login-view");
        //Make LoginView full size and center its content both horizontally and vertically, by calling setAlignItems(`Alignment.CENTER)` and setJustifyContentMode(`JustifyContentMode.CENTER)`.
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        //Set the LoginForm action to "login" to post the login form to Spring Security.
        loginForm.setAction("login");

        add(new H1("Vaadin CRM"), loginForm);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        //Read query parameters and show an error if a login attempt fails.
        if(event.getLocation()
        .getQueryParameters()
        .getParameters()
        .containsKey("error")){
            loginForm.setError(true);
        }
    }
}
