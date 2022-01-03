package org.example.repository;

import org.example.model.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {

	@Query("select a from Account a where user.id = :userId")
	Optional<Account> findByUserId(@Param("userId") Long userId);
}
