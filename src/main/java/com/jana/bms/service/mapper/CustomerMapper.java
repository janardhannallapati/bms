package com.jana.bms.service.mapper;

import com.jana.bms.domain.Address;
import com.jana.bms.domain.Customer;
import com.jana.bms.service.dto.AddressDTO;
import com.jana.bms.service.dto.CustomerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {
    @Mapping(target = "address", source = "address", qualifiedByName = "addressAddressId")
    CustomerDTO toDto(Customer s);

    @Named("addressAddressId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "addressId", source = "addressId")
    AddressDTO toDtoAddressAddressId(Address address);
}
