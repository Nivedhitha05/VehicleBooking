package com.driveease.rental.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

public class BookingDTO {

    @Data
    public static class CreateRequest {
        private Long vehicleId;
        private Integer numDays;
    }

    @Data
    public static class Response {
        private Long        id;
        private String      bookingRef;
        private String      username;
        private String      vehicleName;
        private String      vehicleEmoji;
        private Integer     numDays;
        private BigDecimal  baseAmount;
        private BigDecimal  taxAmount;
        private BigDecimal  totalAmount;
        private String      status;
        private LocalDate   bookingDate;
    }
}
