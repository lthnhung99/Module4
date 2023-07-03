package com.cg.repository;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DepositRepository extends JpaRepository<Deposit,Long> {
}
