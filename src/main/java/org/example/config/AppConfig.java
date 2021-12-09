package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
@ComponentScan("org.example")
@PropertySource("classpath:application.properties")
public class AppConfig extends WebMvcConfigurationSupport {

	@Bean
	public SpringResourceTemplateResolver templateResolver(){
		var templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setApplicationContext(getApplicationContext());
		templateResolver.setPrefix("classpath:/WEB-INF/views/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		return templateResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine(){
		var templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		return templateEngine;
	}

	@Bean
	public ThymeleafViewResolver viewResolver(){
		var viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		return viewResolver;
	}

}
