package com.achawathe.Banking.project.mappers.impl;

import com.achawathe.Banking.project.domain.dto.TransactionDto;
import com.achawathe.Banking.project.domain.entities.TransactionEntity;
import com.achawathe.Banking.project.domain.entities.UserEntity;
import com.achawathe.Banking.project.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionMapperImpl implements Mapper<TransactionEntity, TransactionDto> {

    private final ModelMapper modelMapper;
    public TransactionMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public TransactionDto mapTo(TransactionEntity transactionEntity) {
        return modelMapper.map(transactionEntity, TransactionDto.class);
    }

    @Override
    public TransactionEntity mapFrom(TransactionDto transactionDto) {
        return modelMapper.map(transactionDto, TransactionEntity.class);
    }
}
