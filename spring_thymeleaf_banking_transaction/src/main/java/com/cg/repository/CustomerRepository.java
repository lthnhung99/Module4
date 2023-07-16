package com.cg.repository;

import com.cg.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAllByIdNot(Long id);
    List<Customer> findAllByDeletedIsFalse();

    List<Customer> findAllByIdNotAndDeletedIsFalse(Long id);
    Boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE Customer As c Set c.balance = c.balance - :transactionAmount WHERE c.id = :customerId")
    void decrementBalance(@Param("customerId") Long customerId, @Param("transactionAmount") BigDecimal transactionAmount);
}
