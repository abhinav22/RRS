package com.example.rrs.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.example.rrs.model.Picture;

@Repository
public interface PictureRepository extends MongoRepository<Picture, String>, QueryDslPredicateExecutor<Picture> {

	@Override
	List<Picture> findAll();

}
