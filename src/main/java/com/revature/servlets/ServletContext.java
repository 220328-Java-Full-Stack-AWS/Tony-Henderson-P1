package com.revature.servlets;

import com.revature.repositories.ConnectionManager;
import com.revature.utils.JsonWebToken;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServletContext implements ServletContextListener {

    public ServletContext() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        /* This method is called when the servlet context is initialized(when the Web application is deployed). */
        ConnectionManager.getConnection();
        JsonWebToken.load();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
