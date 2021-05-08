package com.huy8895.restservice.dao;


import com.huy8895.restservice.entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerDAO extends CrudRepository<Customer, Long> {

}
