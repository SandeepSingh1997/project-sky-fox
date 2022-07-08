package com.booking.bookings;

import com.booking.bookings.repository.Booking;
import com.booking.bookings.repository.BookingRepository;
import com.booking.movieAudience.repository.MovieAudience;
import com.booking.movieAudience.repository.MovieAudienceRepository;
import com.booking.exceptions.NoSeatAvailableException;
import com.booking.shows.respository.Show;
import com.booking.shows.respository.ShowRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;

import static com.booking.shows.respository.Constants.TOTAL_NO_OF_SEATS;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final MovieAudienceRepository movieAudienceRepository;
    private final ShowRepository showRepository;

    public BookingService(BookingRepository bookingRepository, MovieAudienceRepository movieAudienceRepository, ShowRepository showRepository) {
        this.bookingRepository = bookingRepository;
        this.movieAudienceRepository = movieAudienceRepository;
        this.showRepository = showRepository;
    }

    public Booking book(MovieAudience movieAudience, Long showId, Date bookingDate, int noOfSeats) throws NoSeatAvailableException {
        final var show = showRepository.findById(showId)
                .orElseThrow(() -> new EmptyResultDataAccessException("Show not found", 1));

        if (availableSeats(show) < noOfSeats) {
            throw new NoSeatAvailableException("No seats available");
        }
        movieAudienceRepository.save(movieAudience);
        BigDecimal amountPaid = show.costFor(noOfSeats);
        return bookingRepository.save(new Booking(bookingDate, show, movieAudience, noOfSeats, amountPaid));
    }

    private long availableSeats(Show show) {
        Integer bookedSeats = bookingRepository.bookedSeatsByShow(show.getId());
        if (noSeatsBooked(bookedSeats))
            return TOTAL_NO_OF_SEATS;

        return TOTAL_NO_OF_SEATS - bookedSeats;
    }

    private boolean noSeatsBooked(Integer bookedSeats) {
        return bookedSeats == null;
    }
}
