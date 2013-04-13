package com.example.rrs.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.rrs.model.URL;

@Repository
public interface URLRepository extends PagingAndSortingRepository<URL, BigInteger> {

    @Override
	List<com.example.rrs.model.URL> findAll();
}
