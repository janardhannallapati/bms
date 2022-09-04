package com.jana.bms.service.mapper;

import com.jana.bms.domain.City;
import com.jana.bms.domain.Country;
import com.jana.bms.service.dto.CityDTO;
import com.jana.bms.service.dto.CountryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link City} and its DTO {@link CityDTO}.
 */
@Mapper(componentModel = "spring")
public interface CityMapper extends EntityMapper<CityDTO, City> {
    @Mapping(target = "country", source = "country", qualifiedByName = "countryCountryId")
    CityDTO toDto(City s);

    @Named("countryCountryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "countryId", source = "countryId")
    CountryDTO toDtoCountryCountryId(Country country);
}
