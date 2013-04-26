package com.example.rrs.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.example.rrs.model.ResourcePicture;

@Repository
public interface ResourcePictureRepository extends MongoRepository<ResourcePicture, String>,
		QueryDslPredicateExecutor<ResourcePicture> {

	@Override
	List<ResourcePicture> findAll();

}
