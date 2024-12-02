package com.accounts.SpringBootGraphQL.service;

import com.accounts.SpringBootGraphQL.domain.BankAccount;
import com.accounts.SpringBootGraphQL.domain.Client;
import com.accounts.SpringBootGraphQL.domain.Currency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BankService {

    // Immutable list for bank accounts and clients
    private static final List<BankAccount> bankAccounts = List.of(
            new BankAccount("A100", "C100", Currency.USD, 106.00f, "A"),
            new BankAccount("A101", "C200", Currency.CAD, 250.00f, "A"),
            new BankAccount("A102", "C300", Currency.CAD, 333.00f, "I"),
            new BankAccount("A103", "C400", Currency.EUR, 4000.00f, "A"),
            new BankAccount("A104", "C500", Currency.EUR, 4000.00f, "A")
    );
    private static final List<Client> clients = List.of(
            new Client("C100", "A100", "Elena", "Maria", "Gonzalez"),
            new Client("C200", "A101", "James", "Robert", "Smith"),
            new Client("C300", "A102", "Aarav", "Kumar", "Patel"),
            new Client("C400", "A103", "Linh", "Thi", "Nguyen"),
            new Client("C500", "A104", "Olivia", "Grace", "Johnson")
    );

    // Method to get all bank accounts
    public List<BankAccount> getAccounts() {
        return bankAccounts;
    }

    public BankAccount accountById(Integer accountId) {
        return bankAccounts.stream()
                .filter(account -> account.id().equals(accountId))
                .findFirst()
                .orElse(null);
    }

    public Map<BankAccount, Client> getClients(List<BankAccount> accounts) {
        return accounts.stream()
                .collect(Collectors.toMap(
                        account -> account, // Key Mapper
                        account -> clients.stream()
                                .filter(client -> client.id().equals(account.clientId()))
                                .findFirst()
                                .orElse(null) // Value Mapper
                ));
    }
}
