package com.accounts.SpringBootGraphQL.service;

import com.accounts.SpringBootGraphQL.entity.BankAccount;
import com.accounts.SpringBootGraphQL.domain.Client;
import com.accounts.SpringBootGraphQL.exceptions.AccountNotFoundException;
import com.accounts.SpringBootGraphQL.exceptions.ClientNotFoundException;
import com.accounts.SpringBootGraphQL.repo.BankAccountRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BankService {

    private static final List<Client> clients = List.of(
            new Client(100L, "John", "T.", "Doe"),
            new Client(101L, "Emma", "B.", "Smith"),
            new Client(102L, "James", "R.", "Brown"),
            new Client(103L, "Olivia", "S.", "Johnson"),
            new Client(104L, "William", "K.", "Jones")
    );

    @Autowired
    private BankAccountRepo repo;

    public void save(BankAccount account) {
        if (Objects.isNull(account.getId()))
            throw new AccountNotFoundException("Invalid Account Id : " + account.getId());

        if (isValidClient(account)) {
            repo.save(account);
        } else {
            throw new ClientNotFoundException("Client Not Found " + account.getClientId());
        }
    }

    public BankAccount modify(BankAccount account) {
        if (Objects.isNull(account.getId()))
            throw new AccountNotFoundException("Invalid Account Id : " + account.getId());

        if (isValidClient(account)) {
            return repo.save(account);
        } else {
            throw new ClientNotFoundException("Client Not Found " + account.getClientId());
        }
    }

    // Method to get all bank accounts
    public List<BankAccount> getAccounts(String accountStatus) {
        return repo.findByStatus(accountStatus);
    }

    public BankAccount accountById(Integer accountId) {
        if (repo.findById(accountId).isPresent()) {
            return repo.findById(accountId).get();
        }
        throw new AccountNotFoundException("Account Not Found " + accountId);
    }

    public Boolean delete(Integer accountId) {
        if (repo.findById(accountId).isPresent()) {
            repo.delete(repo.findById(accountId).get());
            return true;
        }
        return false;
    }

    private List<Client> getClients() {
        return clients;
    }

    public Map<BankAccount, Client> getBankAccountClientMap(List<BankAccount> bankAccounts) {
        // Collect all client Ids from the list of bank accounts
        Set<Long> clientIds = bankAccounts.stream()
                .map(BankAccount::getClientId)
                .collect(Collectors.toSet());

        // Fetch clients for all collected Ids
        List<Client> clients = getClients().stream()
                .filter(client -> clientIds.contains(client.id()))
                .toList();

        // Map each bank account to its corresponding client
        return clients.stream()
                .collect(Collectors.toMap(
                        client -> bankAccounts.stream()
                                .filter(bankAccount -> bankAccount.getClientId().equals(client.id()))
                                .findFirst()
                                .orElse(null),
                        client -> client
                ));
    }

    private boolean isValidClient(BankAccount account) {
        return clients.stream()
                .anyMatch(client -> client.id().equals(account.getClientId()));
    }
}
