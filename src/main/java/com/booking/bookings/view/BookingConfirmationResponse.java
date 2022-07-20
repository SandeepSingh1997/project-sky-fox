package com.booking.bookings.view;

import com.booking.utilities.serializers.date.DateSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

@ApiModel("Booking confirmation Response")
public class BookingConfirmationResponse {
    @JsonProperty
    @ApiModelProperty(name = "id", value = "booking  id", required = true, position = 1)
    private Long id;

    @JsonProperty
    @ApiModelProperty(name = "customerName", value = "Customer name", required = true, position = 2)
    private String customerName;

    @JsonProperty
    @JsonSerialize(using = DateSerializer.class)
    @ApiModelProperty(name = "showDate", value = "booked show date", required = true, position = 3)
    private Date showDate;

    @JsonProperty
    @ApiModelProperty(name = "startTime", value = "show start time", required = true, position = 4)
    private Time startTime;

    @JsonProperty
    @ApiModelProperty(name = "amountPaid", value = "Amount paid for booking", required = true, position = 5)
    private BigDecimal amountPaid;

    @JsonProperty
    @ApiModelProperty(name = "noOfSeats", value = "No of seats booked", required = true, position = 6)
    private int noOfSeats;


    @JsonProperty
    @ApiModelProperty(name = "email id", value = "email id", required = false, position = 7)
    private String email;


    public BookingConfirmationResponse(Long id, String customerName, Date showDate, Time startTime, BigDecimal amountPaid, int noOfSeats, String email) {
        this.id = id;
        this.customerName = customerName;
        this.showDate = showDate;
        this.startTime = startTime;
        this.amountPaid = amountPaid;
        this.noOfSeats = noOfSeats;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingConfirmationResponse that = (BookingConfirmationResponse) o;
        return noOfSeats == that.noOfSeats &&
                Objects.equals(id, that.id) &&
                Objects.equals(customerName, that.customerName) &&
                Objects.equals(showDate, that.showDate) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(amountPaid, that.amountPaid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerName, showDate, startTime, amountPaid, noOfSeats);
    }

    @Override
    public String toString() {
        return "BookingConfirmation{" +
                ", customerName='" + customerName + '\'' +
                ", showDate=" + showDate +
                ", startTime=" + startTime +
                ", amountPaid=" + amountPaid +
                ", noOfSeats=" + noOfSeats +
                ",email=" + email +
                '}';
    }
}
