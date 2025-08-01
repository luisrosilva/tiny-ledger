package org.rosilva.tinyledger.api;

import org.rosilva.tinyledger.api.model.Account;
import org.rosilva.tinyledger.api.model.AccountReference;
import org.rosilva.tinyledger.mappers.AccountMapper;
import org.rosilva.tinyledger.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Optional;

@Service
public class AccountApiImpl implements AccountApi {

    private final NativeWebRequest nativeWebRequest;
    private final AccountService accountService;

    public AccountApiImpl(NativeWebRequest nativeWebRequest, AccountService accountService) {
        this.nativeWebRequest = nativeWebRequest;
        this.accountService = accountService;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(nativeWebRequest);
    }

    @Override
    public ResponseEntity<AccountReference> createAccount() {
        org.rosilva.tinyledger.domain.Account createdAccount = accountService.createAccount();

        return ResponseEntity.ok(AccountMapper.INSTANCE.mapToApiAccountReference(createdAccount));
    }

    @Override
    public ResponseEntity<Account> getAccount(String accountId) {
        Optional<org.rosilva.tinyledger.domain.Account> account = accountService.getAccount(accountId);
        return account
                .map(value -> ResponseEntity.ok(AccountMapper.INSTANCE.mapToApiAccount(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<Account>> listAccounts() {
        List<org.rosilva.tinyledger.domain.Account> accountList = accountService.getAccounts();
        return ResponseEntity.ok(accountList.stream()
                .map(AccountMapper.INSTANCE::mapToApiAccount)
                .toList());
    }
}
