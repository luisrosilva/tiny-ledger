package org.rosilva.tinyledger.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.rosilva.tinyledger.api.model.Account;
import org.rosilva.tinyledger.api.model.AccountReference;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountReference mapToApiAccountReference(org.rosilva.tinyledger.domain.Account account);

    Account mapToApiAccount(org.rosilva.tinyledger.domain.Account account);

    @Mapping(target = "moneyMovements", ignore = true)
    org.rosilva.tinyledger.domain.Account mapToDomain(Account account);

    @Mapping(target = "moneyMovements", ignore = true)
    org.rosilva.tinyledger.domain.Account mapToDomain(AccountReference accountReference);

}
