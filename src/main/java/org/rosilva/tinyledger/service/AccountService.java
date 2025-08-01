package org.rosilva.tinyledger.service;

import org.rosilva.tinyledger.domain.Account;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountService {

    private final Map<UUID, Account> accounts = new ConcurrentHashMap<>();

    public Account createAccount() {
        Account newAccount = new Account();
        accounts.put(newAccount.getId(), newAccount);

        return newAccount;
    }

    public Optional<Account> getAccount(String accountId) {
        return Optional.ofNullable(accounts.get(UUID.fromString(accountId)));
    }

    public List<Account> getAccounts() {
        return new ArrayList<>(accounts.values());
    }
}
