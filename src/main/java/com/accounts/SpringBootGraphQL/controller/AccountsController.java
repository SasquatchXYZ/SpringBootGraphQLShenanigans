package com.accounts.SpringBootGraphQL.controller;

import com.accounts.SpringBootGraphQL.entity.BankAccount;
import com.accounts.SpringBootGraphQL.domain.Client;
import com.accounts.SpringBootGraphQL.exceptions.ClientNotFoundException;
import com.accounts.SpringBootGraphQL.service.BankService;
import graphql.GraphQLError;
import graphql.schema.DataFetchingEnvironment;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.graphql.execution.ErrorType;
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
        log.info("Getting Account ");
        return bankService.accountById(accountId);
    }

    /**
     * Get clients without N + 1 problem
     **/
    @BatchMapping(field = "client", typeName = "BankAccountType")
    Map<BankAccount, Client> clients(List<BankAccount> bankAccounts) {
        log.info("Getting client for Accounts : " + bankAccounts.size());
        return bankService.getBankAccountClientMap(bankAccounts);
    }

    @MutationMapping
    Boolean addAccount(@Argument("account") BankAccount account) {
        log.info("Adding account : " + account);
        bankService.save(account);
        return true;
    }

    @MutationMapping
    BankAccount editAccount(@Argument("account") BankAccount account) {
        log.info("Editing account : " + account);
        return bankService.modify(account);
    }

    @MutationMapping
    Boolean deleteAccount(@Argument("id") Integer accountId) {
        log.info("Deleting account : " + accountId);
        return bankService.delete(accountId);
    }

    @GraphQlExceptionHandler
    public GraphQLError handle(@NonNull ClientNotFoundException ex, @NonNull DataFetchingEnvironment environment) {
        return GraphQLError
                .newError()
                .errorType(ErrorType.BAD_REQUEST)
                .message("Unable to locate the specified client. " +
                        "Please verify the client details and attempt your request again. :" +
                        ex.getMessage())
                .path(environment.getExecutionStepInfo().getPath())
                .location(environment.getField().getSourceLocation())
                .build();
    }
}
