package com.jana.bms.service.mapper;

import com.jana.bms.domain.Address;
import com.jana.bms.domain.City;
import com.jana.bms.service.dto.AddressDTO;
import com.jana.bms.service.dto.CityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressDTO}.
 */
@Mapper(componentModel = "spring")
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {
    @Mapping(target = "city", source = "city", qualifiedByName = "cityCityId")
    AddressDTO toDto(Address s);

    @Named("cityCityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "cityId", source = "cityId")
    CityDTO toDtoCityCityId(City city);
}
