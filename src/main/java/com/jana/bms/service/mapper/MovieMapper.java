package com.jana.bms.service.mapper;

import com.jana.bms.domain.Actor;
import com.jana.bms.domain.Genre;
import com.jana.bms.domain.Language;
import com.jana.bms.domain.Movie;
import com.jana.bms.service.dto.ActorDTO;
import com.jana.bms.service.dto.GenreDTO;
import com.jana.bms.service.dto.LanguageDTO;
import com.jana.bms.service.dto.MovieDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Movie} and its DTO {@link MovieDTO}.
 */
@Mapper(componentModel = "spring")
public interface MovieMapper extends EntityMapper<MovieDTO, Movie> {
    @Mapping(target = "language", source = "language", qualifiedByName = "languageLanguageId")
    @Mapping(target = "actors", source = "actors", qualifiedByName = "actorActorIdSet")
    @Mapping(target = "genres", source = "genres", qualifiedByName = "genreGenreIdSet")
    MovieDTO toDto(Movie s);

    @Mapping(target = "removeActor", ignore = true)
    @Mapping(target = "removeGenre", ignore = true)
    Movie toEntity(MovieDTO movieDTO);

    @Named("languageLanguageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "languageId", source = "languageId")
    LanguageDTO toDtoLanguageLanguageId(Language language);

    @Named("actorActorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "actorId", source = "actorId")
    ActorDTO toDtoActorActorId(Actor actor);

    @Named("actorActorIdSet")
    default Set<ActorDTO> toDtoActorActorIdSet(Set<Actor> actor) {
        return actor.stream().map(this::toDtoActorActorId).collect(Collectors.toSet());
    }

    @Named("genreGenreId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "genreId", source = "genreId")
    GenreDTO toDtoGenreGenreId(Genre genre);

    @Named("genreGenreIdSet")
    default Set<GenreDTO> toDtoGenreGenreIdSet(Set<Genre> genre) {
        return genre.stream().map(this::toDtoGenreGenreId).collect(Collectors.toSet());
    }
}
