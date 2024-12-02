package com.accounts.SpringBootGraphQL.repo;

import com.accounts.SpringBootGraphQL.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepo extends JpaRepository<BankAccount, Integer> {
}
