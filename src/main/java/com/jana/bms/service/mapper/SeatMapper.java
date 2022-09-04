package com.jana.bms.service.mapper;

import com.jana.bms.domain.Screen;
import com.jana.bms.domain.Seat;
import com.jana.bms.service.dto.ScreenDTO;
import com.jana.bms.service.dto.SeatDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Seat} and its DTO {@link SeatDTO}.
 */
@Mapper(componentModel = "spring")
public interface SeatMapper extends EntityMapper<SeatDTO, Seat> {
    @Mapping(target = "screen", source = "screen", qualifiedByName = "screenScreenId")
    SeatDTO toDto(Seat s);

    @Named("screenScreenId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "screenId", source = "screenId")
    ScreenDTO toDtoScreenScreenId(Screen screen);
}
