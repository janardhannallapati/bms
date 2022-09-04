package com.jana.bms.service.mapper;

import com.jana.bms.domain.Genre;
import com.jana.bms.service.dto.GenreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Genre} and its DTO {@link GenreDTO}.
 */
@Mapper(componentModel = "spring")
public interface GenreMapper extends EntityMapper<GenreDTO, Genre> {}
