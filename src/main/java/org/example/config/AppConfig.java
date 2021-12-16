package org.example.config;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class AppConfig extends WebMvcConfigurationSupport {

	@Bean
	public XmlMapper xmlMapper() {
		final var xmlMapper = new XmlMapper();
		xmlMapper.registerModule(new JavaTimeModule());
		return xmlMapper;
	}
}
