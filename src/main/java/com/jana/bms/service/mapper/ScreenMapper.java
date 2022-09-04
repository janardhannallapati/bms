package com.jana.bms.service.mapper;

import com.jana.bms.domain.Screen;
import com.jana.bms.domain.Theatre;
import com.jana.bms.service.dto.ScreenDTO;
import com.jana.bms.service.dto.TheatreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Screen} and its DTO {@link ScreenDTO}.
 */
@Mapper(componentModel = "spring")
public interface ScreenMapper extends EntityMapper<ScreenDTO, Screen> {
    @Mapping(target = "theatre", source = "theatre", qualifiedByName = "theatreTheatreId")
    ScreenDTO toDto(Screen s);

    @Named("theatreTheatreId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "theatreId", source = "theatreId")
    TheatreDTO toDtoTheatreTheatreId(Theatre theatre);
}
