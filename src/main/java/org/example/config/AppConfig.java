package org.example.config;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
public class AppConfig extends WebMvcConfigurationSupport {

  @Bean
  public SpringResourceTemplateResolver templateResolver() {
    var templateResolver = new SpringResourceTemplateResolver();
    templateResolver.setApplicationContext(getApplicationContext());
    templateResolver.setPrefix("classpath:/WEB-INF/views/");
    templateResolver.setSuffix(".html");
    templateResolver.setTemplateMode(TemplateMode.HTML);
    return templateResolver;
  }

  @Bean
  public SpringTemplateEngine templateEngine() {
    var templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(templateResolver());
    return templateEngine;
  }

  @Bean
  public ThymeleafViewResolver viewResolver() {
    var viewResolver = new ThymeleafViewResolver();
    viewResolver.setTemplateEngine(templateEngine());
    return viewResolver;
  }

	@Bean
	public XmlMapper xmlMapper() {
		final var xmlMapper = new XmlMapper();
    xmlMapper.registerModule(new JavaTimeModule());
		return xmlMapper;
	}
}
