package com.jana.bms.service.mapper;

import com.jana.bms.domain.Movie;
import com.jana.bms.domain.Screen;
import com.jana.bms.domain.Seat;
import com.jana.bms.domain.Show;
import com.jana.bms.service.dto.MovieDTO;
import com.jana.bms.service.dto.ScreenDTO;
import com.jana.bms.service.dto.SeatDTO;
import com.jana.bms.service.dto.ShowDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Show} and its DTO {@link ShowDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShowMapper extends EntityMapper<ShowDTO, Show> {
    @Mapping(target = "movie", source = "movie", qualifiedByName = "movieMovieId")
    @Mapping(target = "screen", source = "screen", qualifiedByName = "screenScreenId")
    @Mapping(target = "seats", source = "seats", qualifiedByName = "seatSeatIdSet")
    ShowDTO toDto(Show s);

    @Mapping(target = "removeSeat", ignore = true)
    Show toEntity(ShowDTO showDTO);

    @Named("movieMovieId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "movieId", source = "movieId")
    MovieDTO toDtoMovieMovieId(Movie movie);

    @Named("screenScreenId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "screenId", source = "screenId")
    ScreenDTO toDtoScreenScreenId(Screen screen);

    @Named("seatSeatId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "seatId", source = "seatId")
    SeatDTO toDtoSeatSeatId(Seat seat);

    @Named("seatSeatIdSet")
    default Set<SeatDTO> toDtoSeatSeatIdSet(Set<Seat> seat) {
        return seat.stream().map(this::toDtoSeatSeatId).collect(Collectors.toSet());
    }
}
