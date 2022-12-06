package org.example.config;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableSpringDataWebSupport
public class AppConfig implements WebMvcConfigurer {

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new PageableHandlerMethodArgumentResolver());
	}

	@Bean
	@Primary
	public JsonMapper jsonMapper() {
		final var jsonMapper = new JsonMapper();
		jsonMapper.registerModule(new JavaTimeModule());
		return jsonMapper;
	}

	@Bean("xmlMapper")
	public XmlMapper xmlMapper() {
		final var xmlMapper = new XmlMapper();
		xmlMapper.registerModule(new JavaTimeModule());
		return xmlMapper;
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
