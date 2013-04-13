package com.example.rrs.repository;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.example.rrs.model.Email;

@Repository
public interface EmailRepository extends PagingAndSortingRepository<Email, BigInteger> {

    @Override
	List<com.example.rrs.model.Email> findAll();
}
