package com.example.rrs.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.example.rrs.model.User;
import com.example.rrs.web.SearchForm;

@Repository
public interface UserRepository extends MongoRepository<User, String>, QueryDslPredicateExecutor<User> {

	@Override
	List<com.example.rrs.model.User> findAll();

	List<User> findByEmail(String email);

}
