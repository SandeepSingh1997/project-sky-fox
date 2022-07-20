package com.booking.bookings;

import com.booking.bookings.repository.Booking;
import com.booking.bookings.repository.BookingRepository;
import com.booking.bookings.view.BookingConfirmationResponse;
import com.booking.customer.CustomerService;
import com.booking.exceptions.CustomerNotFoundException;
import com.booking.exceptions.NoSeatAvailableException;
import com.booking.movieAudience.repository.MovieAudience;
import com.booking.movieAudience.repository.MovieAudienceRepository;
import com.booking.shows.respository.Show;
import com.booking.shows.respository.ShowRepository;
import com.booking.users.repository.User;
import com.booking.users.repository.UserRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;

import static com.booking.shows.respository.Constants.TOTAL_NO_OF_SEATS;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final MovieAudienceRepository movieAudienceRepository;
    private final ShowRepository showRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository, MovieAudienceRepository movieAudienceRepository, ShowRepository showRepository, UserRepository userRepository, CustomerService customerService) {
        this.bookingRepository = bookingRepository;
        this.movieAudienceRepository = movieAudienceRepository;
        this.showRepository = showRepository;
        this.userRepository = userRepository;
        this.customerService = customerService;
    }

    private final CustomerService customerService;


    public User findUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public BookingConfirmationResponse book(String username, MovieAudience movieAudience, Long showId, Date bookingDate, int noOfSeats) throws NoSeatAvailableException, CustomerNotFoundException {
        final var show = showRepository.findById(showId)
                .orElseThrow(() -> new EmptyResultDataAccessException("Show not found", 1));

        if (availableSeats(show) < noOfSeats) {
            throw new NoSeatAvailableException("No seats available");
        }
        movieAudienceRepository.save(movieAudience);
        BigDecimal amountPaid = show.costFor(noOfSeats);
        Booking booking = bookingRepository.save(new Booking(bookingDate, show, movieAudience, noOfSeats, amountPaid));
        User user = findUserByUsername(username);


        if (user.getRole().getId() == 1) {
            return new BookingConfirmationResponse(
                    booking.getId(),
                    booking.getMovieAudience().getName(),
                    booking.getShow().getDate(),
                    booking.getShow().getSlot().getStartTime(),
                    booking.getAmountPaid(),
                    booking.getNoOfSeats(),
                    null
            );
        } else {
            String email = customerService.getCustomerByUserId(user.getId()).getEmail();
            return new BookingConfirmationResponse(
                    booking.getId(),
                    booking.getMovieAudience().getName(),
                    booking.getShow().getDate(),
                    booking.getShow().getSlot().getStartTime(),
                    booking.getAmountPaid(),
                    booking.getNoOfSeats(),
                    email
            );
        }

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
