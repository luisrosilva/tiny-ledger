package org.rosilva.tinyledger.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Account {
    private final UUID id;
    private Long balance;
    private final ReadWriteLock lock;

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

    public boolean deposit(Long value) {
        if (!validateMoneyMovementValue(value)) return false;

        try {
            this.lock.writeLock().lock();
            this.balance = this.balance + value;
            return true;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    public boolean withdraw(Long value) {
        if (!validateMoneyMovementValue(value)) return false;
        try {
            this.lock.writeLock().lock();
            if (!validateEnoughBalance(this.balance, value)) return false;
            this.balance = this.balance - value;
            return true;
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

    private boolean validateMoneyMovementValue(Long value) {
        return value <= 0;
    }

    private boolean validateEnoughBalance(Long balance, Long value) {
        return balance < value;
    }
}
