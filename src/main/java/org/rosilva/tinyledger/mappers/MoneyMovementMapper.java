package org.rosilva.tinyledger.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.rosilva.tinyledger.api.model.MoneyMovement;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(uses = { MovementTypeMapper.class })
public interface MoneyMovementMapper {

    MoneyMovementMapper INSTANCE = Mappers.getMapper(MoneyMovementMapper.class);

    @Mapping(target = "type", source = "movementType")
    MoneyMovement mapToApi(org.rosilva.tinyledger.domain.MoneyMovement moneyMovement);

    @Mapping(target = "movementType", source = "type")
    org.rosilva.tinyledger.domain.MoneyMovement mapToDomain(MoneyMovement moneyMovement);

    default OffsetDateTime mapInstantToOffsetDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return instant.atOffset(ZoneOffset.UTC);
    }

    default Instant mapOffsetDateTimeToInstant(OffsetDateTime offsetDateTime) {
        if  (offsetDateTime == null) {
            return null;
        }
        return offsetDateTime.toInstant();
    }
}
