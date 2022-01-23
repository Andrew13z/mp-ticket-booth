package org.example.repository.cache.impl;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
class UserCacheRepositoryTest {

	@Autowired
	private EntityManager em;

	@Autowired
	private UserRepository repository;

	@Test
	void test (){
		var statistics = em.unwrap(Session.class).getSessionFactory().getStatistics();
		statistics.setStatisticsEnabled(true);

		for (int i = 0; i < 5; i++) {
			repository.findByIdWithCache(9L);
			repository.findByIdWithCache(10L);
		}

		var queryCacheHitCount = statistics.getQueryCacheHitCount();
		var queryCacheMissCount = statistics.getQueryCacheMissCount();

		//only first call to findByIdWithCache() method will be a miss
		//after that, the result will be cached
		assertEquals(8, queryCacheHitCount);
		assertEquals(2, queryCacheMissCount);

		repository.save(new User(9L, "New name", "newemail@mail.com"));

		for (int i = 0; i < 5; i++) {
			repository.findByIdWithCache(9L).get().getName();
			repository.findByIdWithCache(10L).get().getName();
		}

		var queryCacheHitCountAfterUpdate = statistics.getQueryCacheHitCount();
		var queryCacheMissCountAfterUpdate = statistics.getQueryCacheMissCount();

		//after the update of one entity,
		// the data in cache will become invalid since CacheConcurrencyStrategy is set to READ_WRITE
		assertEquals(8, queryCacheHitCountAfterUpdate);
		assertEquals(12, queryCacheMissCountAfterUpdate);
	}

}
