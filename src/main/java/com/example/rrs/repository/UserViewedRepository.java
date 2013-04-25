package com.example.rrs.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.example.rrs.model.User;
import com.example.rrs.model.UserViewed;

@Repository
public interface UserViewedRepository extends MongoRepository<UserViewed, String>, QueryDslPredicateExecutor<UserViewed> {

	@Override
	List<UserViewed> findAll();
	
}
