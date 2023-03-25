package org.example.dto;

import org.example.validation.group.OnCreate;
import org.example.validation.group.OnTicketCreate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * User DTO
 * @author Andrii Krokhta
 */
public class UserDto {

	@Null(groups = OnCreate.class)
	@NotNull(groups = OnTicketCreate.class)
	private String id;

	@NotBlank(groups = OnCreate.class)
	private String name;

	@Email
	@NotBlank(groups = OnCreate.class)
	private String email;

	private BigDecimal balance;

	public UserDto() {
	}

	public UserDto(String id, String name, String email) {
		this.id = id;
		this.name = name;
		this.email = email;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof UserDto)) return false;
		UserDto userDto = (UserDto) o;
		return Objects.equals(getId(), userDto.getId()) &&
				Objects.equals(getName(), userDto.getName()) &&
				Objects.equals(getEmail(), userDto.getEmail());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getName(), getEmail());
	}
}
