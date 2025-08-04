package org.rosilva.tinyledger.api;

import org.rosilva.tinyledger.api.model.Account;
import org.rosilva.tinyledger.api.model.AccountReference;
import org.rosilva.tinyledger.api.model.MoneyMovement;
import org.rosilva.tinyledger.mappers.AccountMapper;
import org.rosilva.tinyledger.mappers.MoneyMovementMapper;
import org.rosilva.tinyledger.mappers.MovementTypeMapper;
import org.rosilva.tinyledger.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        Optional<org.rosilva.tinyledger.domain.Account> account = accountService.getAccount(UUID.fromString(accountId));
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

    @Override
    public ResponseEntity<MoneyMovement> createMovement(String accountId, Integer amount, String type) {
        return accountService.getAccount(UUID.fromString(accountId))
                .map(account -> accountService.createMovement(account, amount, MovementTypeMapper.INSTANCE.mapToDomain(type))
                        .map(moneyMovement -> ResponseEntity.ok(MoneyMovementMapper.INSTANCE.mapToApi(moneyMovement)))
                        .orElseGet(() -> ResponseEntity.badRequest().build()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<MoneyMovement>> listMovements(String accountId) {
        return accountService.getAccount(UUID.fromString(accountId))
                .map(
                        account -> ResponseEntity.ok(
                                account.getMoneyMovements().stream()
                                        .map(MoneyMovementMapper.INSTANCE::mapToApi).toList()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
