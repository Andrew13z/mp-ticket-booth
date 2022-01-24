package org.example.dto;

import org.example.validation.group.OnCreate;
import org.example.validation.group.OnTicketCreate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * User DTO
 * @author Andrii Krokhta
 */
public class UserDto {

	@Null(groups = OnCreate.class)
	@NotNull(groups = OnTicketCreate.class)
	private Long id;

	@NotBlank(groups = OnCreate.class)
	private String name;

	@Email
	@NotBlank(groups = OnCreate.class)
	private String email;

	public UserDto() {
	}

	public UserDto(Long id, String name, String email) {
		this.id = id;
		this.name = name;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
