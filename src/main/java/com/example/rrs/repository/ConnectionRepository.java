package com.example.rrs.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.example.rrs.model.Connection;

@Repository
public interface ConnectionRepository extends MongoRepository<Connection, String>,
		QueryDslPredicateExecutor<Connection> {

	@Override
	List<Connection> findAll();

}
