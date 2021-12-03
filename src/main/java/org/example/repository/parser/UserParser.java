package org.example.repository.parser;

import org.example.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserParser extends AbstractParser<User> {

	@Override
	public User parse(String content) {
		var columns = content.split(",");
		return new User(Long.parseLong(columns[0]), columns[1], columns[2]);
	}

	@Override
	public long getId(User model) {
		return model.getId();
	}

	@Override
	public Class<User> getType() {
		return User.class;
	}


}
