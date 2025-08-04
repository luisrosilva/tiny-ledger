package org.rosilva.tinyledger.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class MoneyMovement {
    private final UUID id;
    private final Integer amount;
    private final MovementType movementType;
    private final Instant createdAt;
    private Instant effectedAt;

    public MoneyMovement(Integer amount, MovementType movementType) {
        this.id = UUID.randomUUID();
        this.amount = amount;
        this.movementType = movementType;
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public Integer getAmount() {
        return amount;
    }

    public MovementType getMovementType() {
        return movementType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getEffectedAt() {
        return effectedAt;
    }

    public void setEffectedAt(Instant effectedAt) {
        this.effectedAt = effectedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoneyMovement that = (MoneyMovement) o;
        return id.equals(that.id) && amount.equals(that.amount) && movementType == that.movementType && createdAt.equals(that.createdAt) && Objects.equals(effectedAt, that.effectedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, movementType, createdAt, effectedAt);
    }
}
