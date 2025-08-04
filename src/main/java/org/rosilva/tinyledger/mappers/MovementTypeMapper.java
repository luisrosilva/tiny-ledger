package org.rosilva.tinyledger.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.rosilva.tinyledger.api.model.MoneyMovement;

@Mapper
public interface MovementTypeMapper {

    MovementTypeMapper INSTANCE = Mappers.getMapper(MovementTypeMapper.class);

    MoneyMovement.TypeEnum mapToApiMovementType(org.rosilva.tinyledger.domain.MovementType movementType);

    org.rosilva.tinyledger.domain.MovementType mapToDomain(String movementType);

}
