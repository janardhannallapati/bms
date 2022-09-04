package com.jana.bms.service.mapper;

import com.jana.bms.domain.Booking;
import com.jana.bms.domain.Seat;
import com.jana.bms.domain.Show;
import com.jana.bms.domain.ShowSeat;
import com.jana.bms.service.dto.BookingDTO;
import com.jana.bms.service.dto.SeatDTO;
import com.jana.bms.service.dto.ShowDTO;
import com.jana.bms.service.dto.ShowSeatDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ShowSeat} and its DTO {@link ShowSeatDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShowSeatMapper extends EntityMapper<ShowSeatDTO, ShowSeat> {
    @Mapping(target = "seat", source = "seat", qualifiedByName = "seatSeatId")
    @Mapping(target = "show", source = "show", qualifiedByName = "showShowId")
    @Mapping(target = "booking", source = "booking", qualifiedByName = "bookingBookingId")
    ShowSeatDTO toDto(ShowSeat s);

    @Named("seatSeatId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "seatId", source = "seatId")
    SeatDTO toDtoSeatSeatId(Seat seat);

    @Named("showShowId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "showId", source = "showId")
    ShowDTO toDtoShowShowId(Show show);

    @Named("bookingBookingId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "bookingId", source = "bookingId")
    BookingDTO toDtoBookingBookingId(Booking booking);
}
