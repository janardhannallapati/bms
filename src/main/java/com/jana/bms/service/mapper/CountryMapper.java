package com.jana.bms.service.mapper;

import com.jana.bms.domain.Country;
import com.jana.bms.service.dto.CountryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Country} and its DTO {@link CountryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CountryMapper extends EntityMapper<CountryDTO, Country> {}
