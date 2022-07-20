package com.booking.bookings.repository;

import com.booking.bookings.view.BookingConfirmationResponse;
import com.booking.movieAudience.repository.MovieAudience;
import com.booking.shows.respository.Show;
import com.booking.utilities.serializers.date.DateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    @ApiModelProperty(name = "id", value = "The booking id", example = "0", position = 1)
    private Long id;

    @Column(nullable = false)
    @JsonProperty
    @JsonSerialize(using = DateSerializer.class)
    @NotNull(message = "Date must be provided")
    @ApiModelProperty(name = "date", value = "Date of booking (yyyy-MM-dd)", dataType = "java.lang.String", required = true, example = "2020-01-01", position = 2)
    private Date date;

    @OneToOne
    @JoinColumn(name = "show_id")
    @JsonIgnore
    @NotNull(message = "Show must be provided")
    private Show show;

    @OneToOne
    @JoinColumn(name = "movie_audience_id")
    @JsonIgnore
    @NotNull(message = "Customer must be provided")
    private MovieAudience movieAudience;

    @Column(name = "no_of_seats", nullable = false)
    @JsonProperty
    @DecimalMin(value = "0", inclusive = false, message = "Seats should be greater than {value}")
    @NotNull(message = "No of seats must be provided")
    @ApiModelProperty(name = "no of seats", value = "Number of seats/tickets for show", required = true, example = "3", position = 3)
    private Integer noOfSeats;

    @Column(name = "amount_paid", nullable = false)
    @JsonProperty
    @DecimalMin(value = "0", inclusive = false, message = "Amount paid should be greater than {value}")
    @NotNull(message = "Amount paid must be provided")
    @ApiModelProperty(name = "amount paid", value = "Amount paid for seats", required = true, example = "300.44", position = 4)
    private BigDecimal amountPaid;

    public Booking(Date date, Show show, MovieAudience movieAudience, Integer noOfSeats, BigDecimal amountPaid) {
        this.date = date;
        this.show = show;
        this.movieAudience = movieAudience;
        this.noOfSeats = noOfSeats;
        this.amountPaid = amountPaid;
    }

    public Booking() {
    }


    public Long getId() {
        return id;
    }

    public MovieAudience getMovieAudience() {
        return movieAudience;
    }

    public Show getShow() {
        return show;
    }

    public Date getDate() {
        return date;
    }

    public Integer getNoOfSeats() {
        return noOfSeats;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id) &&
                Objects.equals(date, booking.date) &&
                Objects.equals(show, booking.show) &&
                Objects.equals(movieAudience, booking.movieAudience) &&
                Objects.equals(noOfSeats, booking.noOfSeats) &&
                Objects.equals(amountPaid, booking.amountPaid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, show, movieAudience, noOfSeats, amountPaid);
    }
}
