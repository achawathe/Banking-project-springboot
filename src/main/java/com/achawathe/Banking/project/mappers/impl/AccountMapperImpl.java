package com.achawathe.Banking.project.mappers.impl;


import com.achawathe.Banking.project.domain.dto.AccountDto;
import com.achawathe.Banking.project.domain.entities.AccountEntity;
import com.achawathe.Banking.project.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountMapperImpl implements Mapper<AccountEntity, AccountDto> {

    private ModelMapper modelMapper;

    public AccountMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public AccountDto mapTo(AccountEntity accountEntity) {
        return modelMapper.map(accountEntity, AccountDto.class);
    }

    public AccountEntity mapFrom(AccountDto accountDto) {
        return modelMapper.map(accountDto, AccountEntity.class);
    }
}
