package com.accounts.SpringBootGraphQL.controller;

import com.accounts.SpringBootGraphQL.domain.BankAccount;
import com.accounts.SpringBootGraphQL.domain.Client;
import com.accounts.SpringBootGraphQL.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

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

    @SchemaMapping(typeName = "BankAccount", field = "client")
    Client getClient(BankAccount account) {
        log.info("Getting client for " + account.id());
        return bankService.GetClientByAccountId(account.id());
    }
}
