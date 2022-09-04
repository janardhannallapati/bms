package com.jana.bms.service.mapper;

import com.jana.bms.domain.Booking;
import com.jana.bms.domain.Payment;
import com.jana.bms.service.dto.BookingDTO;
import com.jana.bms.service.dto.PaymentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "booking", source = "booking", qualifiedByName = "bookingBookingId")
    PaymentDTO toDto(Payment s);

    @Named("bookingBookingId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "bookingId", source = "bookingId")
    BookingDTO toDtoBookingBookingId(Booking booking);
}
