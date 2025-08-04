package org.rosilva.tinyledger.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MovementTypeMapper {

    MovementTypeMapper INSTANCE = Mappers.getMapper(MovementTypeMapper.class);

    String mapToApiMovementType(org.rosilva.tinyledger.domain.MovementType movementType);

    org.rosilva.tinyledger.domain.MovementType mapToDomain(String movementType);

}
