package org.rosilva.tinyledger.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Account {
    private final UUID id;
    private Long balance;
    private final ReadWriteLock lock;
    private final List<MoneyMovement> moneyMovements = new LinkedList<>();

    public Account(Long openingBalance) {
        this.id = UUID.randomUUID();
        this.lock = new ReentrantReadWriteLock();
        this.balance = openingBalance;
    }

    public Account() {
        this(0L);
    }

    public UUID getId() {
        return id;
    }

    public Optional<MoneyMovement> deposit(Integer value) {
        if (!validateMoneyMovementValue(value)) return Optional.empty();
        try {
            this.lock.writeLock().lock();
            this.balance = this.balance + value;
            MoneyMovement moneyMovement = new MoneyMovement(value, MovementType.DEPOSIT);
            this.moneyMovements.add(moneyMovement);
            return Optional.of(moneyMovement);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public Optional<MoneyMovement> withdraw(Integer value) {
        if (!validateMoneyMovementValue(value)) return Optional.empty();
        try {
            this.lock.writeLock().lock();
            if (!validateEnoughBalance(this.balance, value)) return Optional.empty();
            this.balance = this.balance - value;
            MoneyMovement moneyMovement = new MoneyMovement(value, MovementType.WITHDRAWAL);
            this.moneyMovements.add(moneyMovement);
            return Optional.of(moneyMovement);
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public Long getBalance() {
        try {
            this.lock.readLock().lock();
            return balance;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    private boolean validateMoneyMovementValue(Integer value) {
        return value >= 0;
    }

    private boolean validateEnoughBalance(Long balance, Integer value) {
        return balance >= value;
    }

    public List<MoneyMovement> getMoneyMovements() {
        try {
            this.lock.readLock().lock();
            return List.of(this.moneyMovements.toArray(MoneyMovement[]::new));
        } finally {
            this.lock.readLock().unlock();
        }
    }
}
