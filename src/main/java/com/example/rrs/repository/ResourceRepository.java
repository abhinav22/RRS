package com.example.rrs.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.example.rrs.model.Resource;

@Repository
public interface ResourceRepository extends MongoRepository<Resource, String>,
		QueryDslPredicateExecutor<Resource> {


}
