package com.learning.kafka.domain.mapper.impl;

import com.learning.kafka.domain.dto.CustomerVisitEventDto;
import com.learning.kafka.domain.entities.CustomerVisitEventEntity;
import com.learning.kafka.domain.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CustomerEventVisitMapper implements Mapper<CustomerVisitEventEntity, CustomerVisitEventDto> {

    private ModelMapper modelMapper;

    public CustomerEventVisitMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public CustomerVisitEventDto mapTo(CustomerVisitEventEntity customerVisitEventEntity) {
        return modelMapper.map(customerVisitEventEntity, CustomerVisitEventDto.class);
    }

    @Override
    public CustomerVisitEventEntity mapFrom(CustomerVisitEventDto customerVisitEventDto) {
        return modelMapper.map(customerVisitEventDto, CustomerVisitEventEntity.class);
    }
}
