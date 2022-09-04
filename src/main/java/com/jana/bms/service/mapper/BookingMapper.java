package com.jana.bms.service.mapper;

import com.jana.bms.domain.Booking;
import com.jana.bms.domain.Customer;
import com.jana.bms.domain.Show;
import com.jana.bms.service.dto.BookingDTO;
import com.jana.bms.service.dto.CustomerDTO;
import com.jana.bms.service.dto.ShowDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Booking} and its DTO {@link BookingDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookingMapper extends EntityMapper<BookingDTO, Booking> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerCustomerId")
    @Mapping(target = "show", source = "show", qualifiedByName = "showShowId")
    BookingDTO toDto(Booking s);

    @Named("customerCustomerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "customerId", source = "customerId")
    CustomerDTO toDtoCustomerCustomerId(Customer customer);

    @Named("showShowId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "showId", source = "showId")
    ShowDTO toDtoShowShowId(Show show);
}
