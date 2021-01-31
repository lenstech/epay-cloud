package com.lens.epay.mapper;

import com.lens.epay.common.Converter;
import com.lens.epay.model.dto.sale.OrderDto;
import com.lens.epay.model.entity.Order;
import com.lens.epay.model.resource.OrderResource;
import com.lens.epay.service.AddressService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {MinimalUserMapper.class, AddressMapper.class, AddressService.class, BasketObjectMapper.class, CreditCardTransactionMapper.class})
public interface OrderMapper extends Converter<OrderDto, Order, OrderResource> {

    @Override
    @Mapping(source = "deliveryAddressId", target = "deliveryAddress", qualifiedByName = "fromIdToEntity")
    @Mapping(source = "invoiceAddressId", target = "invoiceAddress", qualifiedByName = "fromIdToEntity")
    @Mapping(source = "basketObjectDtoList", target = "basketObjects")
    Order toEntity(OrderDto orderDto);

    @Override
    @Mapping(source = "deliveryAddressId", target = "deliveryAddress", qualifiedByName = "fromIdToEntity")
    @Mapping(source = "invoiceAddressId", target = "invoiceAddress", qualifiedByName = "fromIdToEntity")
    @Mapping(source = "basketObjectDtoList", target = "basketObjects")
    void toEntityForUpdate(OrderDto orderDto, @MappingTarget Order order);

}
