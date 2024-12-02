package com.accounts.SpringBootGraphQL.controller;

import com.accounts.SpringBootGraphQL.domain.BankAccount;
import com.accounts.SpringBootGraphQL.domain.Client;
import com.accounts.SpringBootGraphQL.service.BankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.BatchMapping;
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


/*    // Get clients with an N + 1 problem
    @SchemaMapping(typeName = "BankAccount", field = "client")
    Client getClient(BankAccount account) {
        log.info("Getting client for " + account.id());
        return bankService.GetClientByAccountId(account.id());
    }*/

    /**
     * Get clients without N + 1 problem
     **/
    @BatchMapping(field = "client")
    Map<BankAccount, Client> getClients(List<BankAccount> accounts) {
        log.info("Getting client for Accounts : " + accounts.size());

        Map<BankAccount, Client> clientMap = bankService.getClients(accounts);

        return clientMap;
    }
}
