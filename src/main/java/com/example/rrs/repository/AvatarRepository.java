package com.example.rrs.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.example.rrs.model.Avatar;
import com.example.rrs.model.User;

@Repository
public interface AvatarRepository extends MongoRepository<Avatar, String>,
		QueryDslPredicateExecutor<Avatar> {

	@Override
	List<Avatar> findAll();

}
