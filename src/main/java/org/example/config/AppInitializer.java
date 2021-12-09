package org.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

@Configuration
public class AppInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		var webApplicationContext = new AnnotationConfigWebApplicationContext();
		webApplicationContext.register(AppConfig.class);

		ServletRegistration.Dynamic servletRegistration =
						servletContext.addServlet("mvc", new DispatcherServlet(webApplicationContext));
		servletRegistration.setLoadOnStartup(1);
		servletRegistration.addMapping("/");

	}
}
