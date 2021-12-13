package org.example.config;

import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class AppConfig extends WebMvcConfigurationSupport {

	@Bean
	public Unmarshaller unmarshaller(){
		var unmarshaller = new XStreamMarshaller();
		unmarshaller.getXStream().allowTypes(new Class[]{Ticket.class, User.class, Event.class});
		return unmarshaller;
	}

}
