package com.jana.bms.service.mapper;

import com.jana.bms.domain.Theatre;
import com.jana.bms.service.dto.TheatreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Theatre} and its DTO {@link TheatreDTO}.
 */
@Mapper(componentModel = "spring")
public interface TheatreMapper extends EntityMapper<TheatreDTO, Theatre> {}
