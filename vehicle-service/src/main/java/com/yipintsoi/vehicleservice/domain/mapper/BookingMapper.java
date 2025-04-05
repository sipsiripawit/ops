package com.yipintsoi.vehicleservice.domain.mapper;

import com.yipintsoi.vehicleservice.domain.dto.BookingDTO;
import com.yipintsoi.vehicleservice.domain.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(source = "vehicle.id", target = "vehicleId")
    BookingDTO bookingToBookingDTO(Booking booking);

    @Mapping(source = "vehicleId", target = "vehicle.id")
    Booking bookingDTOToBooking(BookingDTO bookingDTO);
}
