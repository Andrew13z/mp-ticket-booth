package org.example.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

	@Bean
	@Primary
	public JsonMapper jsonMapper() {
		var jsonMapper = new JsonMapper();
		jsonMapper.registerModule(new JavaTimeModule());
		jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		return jsonMapper;
	}
}
