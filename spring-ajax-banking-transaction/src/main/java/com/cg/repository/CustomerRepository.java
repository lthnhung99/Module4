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
    List<Customer> findAllByFullNameLike(String fullName);
    List<Customer> findAllByFullNameLikeOrEmailLikeOrPhoneLike(String fullName, String email, String phone);

    List<Customer> findAllByIdNot(Long id);
    List<Customer> findAllByDeletedIs(Boolean boo);
    Boolean existsByEmail(String email);
    Boolean existsByEmailAndIdNot(String email, Long id);
    @Modifying
    @Query("UPDATE Customer as c set c.deleted = 1  where c.id = :id")
    void softDeleteById(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Customer as c set c.balance = c.balance + :amount where c.id = :id")
    void incrementBalance(@Param("id") Long id, @Param("amount") BigDecimal amount);
    @Modifying
    @Query("UPDATE Customer as c set c.balance = c.balance - :amount where c.id = :id")
    void decrementBalance(@Param("id") Long id, @Param("amount") BigDecimal amount);
}
