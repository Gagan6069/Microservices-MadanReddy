package com.gsb.accounts.repository;

import com.gsb.accounts.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepsitory extends JpaRepository<Customer, Long> {
}
