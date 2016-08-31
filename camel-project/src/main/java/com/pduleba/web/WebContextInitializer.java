package com.pduleba.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.pduleba.context.ApplicationConfig;

/**
 * Initializes the web application in place of a descriptor.
 * 
 * @author Michael Hoffman, Pluralsight
 * 
 */
public class WebContextInitializer implements WebApplicationInitializer {

   @Override
   public void onStartup(ServletContext servletContext) throws ServletException {
      AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
      rootContext.register(ApplicationConfig.class);
      rootContext.setServletContext(servletContext);
      ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher",
            new DispatcherServlet(rootContext));
      dispatcher.setLoadOnStartup(1);
      dispatcher.addMapping("/");
   }

}
