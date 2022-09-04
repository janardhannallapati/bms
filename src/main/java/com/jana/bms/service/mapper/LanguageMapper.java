package com.jana.bms.service.mapper;

import com.jana.bms.domain.Language;
import com.jana.bms.service.dto.LanguageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Language} and its DTO {@link LanguageDTO}.
 */
@Mapper(componentModel = "spring")
public interface LanguageMapper extends EntityMapper<LanguageDTO, Language> {}
