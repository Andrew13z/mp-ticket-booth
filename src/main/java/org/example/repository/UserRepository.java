package org.example.repository;

import org.example.model.User;
import org.example.repository.cache.UserCacheRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long>, UserCacheRepository {

	/**
	 * Gets user by email.
	 *
	 * @return Optional of user or empty optional if user is not found.
	 */
	Optional<User> findByEmail(String email);

	/**
	 * Get list of users by name.
	 * In case nothing was found, empty list is returned.
	 *
	 * @param name   User id.
	 * @param pageable Pageable.
	 * @return List of users.
	 */
	Page<User> findUsersByNameContainingIgnoreCase(String name, Pageable pageable);
}
