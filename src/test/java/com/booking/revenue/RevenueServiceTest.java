package com.booking.revenue;

import com.booking.bookings.repository.BookingRepository;
import com.booking.shows.respository.Show;
import com.booking.shows.respository.ShowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RevenueServiceTest {
    private ShowRepository showRepository;
    private BookingRepository bookingRepository;
    private RevenueService revenueService;
    private ArrayList<Show> testShows;

    @BeforeEach
    public void setUp() {
        showRepository = mock(ShowRepository.class);
        bookingRepository = mock(BookingRepository.class);
        revenueService = new RevenueService(showRepository, bookingRepository);
        testShows = new ArrayList<>() {
            {
                add(mock(Show.class));
                add(mock(Show.class));
            }
        };
    }

    @Test
    public void should_get_revenue_for_a_given_date() {
        final var testDate = Date.valueOf("2020-01-01");
        when(showRepository.findByDate(testDate)).thenReturn(testShows);
        when(bookingRepository.bookingAmountByShows(testShows)).thenReturn(BigDecimal.valueOf(500));

        final var revenue = revenueService.revenueByDate(testDate);

        assertThat(revenue, is(equalTo(BigDecimal.valueOf(500))));
    }

    @Test
    public void should_get_zero_revenue_for_date_with_no_shows() {
        final var testDate = Date.valueOf("2020-01-01");
        final var emptyShows = new ArrayList<Show>();
        when(showRepository.findByDate(testDate)).thenReturn(emptyShows);

        final var revenue = revenueService.revenueByDate(testDate);

        assertThat(revenue, is(equalTo(BigDecimal.valueOf(0))));
    }

    @Test
    public void should_get_zero_revenue_for_date_with_no_bookings() {
        final var testDate = Date.valueOf("2020-01-01");
        when(showRepository.findByDate(testDate)).thenReturn(testShows);
        when(bookingRepository.bookingAmountByShows(testShows)).thenReturn(null);

        final var revenue = revenueService.revenueByDate(testDate);

        assertThat(revenue, is(equalTo(BigDecimal.valueOf(0))));
    }
}
