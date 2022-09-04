package com.jana.bms.service.mapper;

import com.jana.bms.domain.Actor;
import com.jana.bms.service.dto.ActorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Actor} and its DTO {@link ActorDTO}.
 */
@Mapper(componentModel = "spring")
public interface ActorMapper extends EntityMapper<ActorDTO, Actor> {}
