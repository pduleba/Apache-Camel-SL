package com.pluralsight.processor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pluralsight.processor.entity.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

}
