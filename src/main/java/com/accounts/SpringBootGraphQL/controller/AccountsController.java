package com.accounts.SpringBootGraphQL.controller;

import com.accounts.SpringBootGraphQL.entity.BankAccount;
import com.accounts.SpringBootGraphQL.domain.Client;
import com.accounts.SpringBootGraphQL.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class AccountsController {
    @Autowired
    BankService bankService;

    @QueryMapping
    List<BankAccount> accounts() {
        log.info("Getting accounts ");
        return bankService.getAccounts();
    }


    @QueryMapping
    BankAccount accountById(@Argument("accountId") Integer accountId) {
        log.info("Getting Account by Id : " + accountId);
        return bankService.accountById(accountId);
    }

    /**
     * Get clients without N + 1 problem
     **/
    @BatchMapping(field = "client", typeName = "BankAccountType")
    Map<BankAccount, Client> clients(List<BankAccount> accounts) {
        log.info("Getting client for Accounts : " + accounts.size());
        return bankService.getBankAccountClientMap(accounts);
    }

    @MutationMapping
    Boolean addAccount(@Argument("account") BankAccount account) {
        log.info("Adding account : " + account);
        bankService.save(account);
        return true;
    }

    @MutationMapping
    BankAccount editAccount(@Argument("account") BankAccount account) {
        log.info("Updating account : " + account);
        return bankService.modify(account);
    }

    @MutationMapping
    Boolean deleteAccount(@Argument("id") Integer accountId) {
        log.info("Deleting account : " + accountId);
        return bankService.delete(accountId);
    }
}
