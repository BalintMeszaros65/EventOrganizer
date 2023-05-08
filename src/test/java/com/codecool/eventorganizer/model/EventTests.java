package com.codecool.eventorganizer.model;

import com.codecool.eventorganizer.exception.CustomExceptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Event")
public class EventTests {
    @Mock
    Venue venue;
    @Mock
    Performance performance;
    @Mock
    Genre genre;
    @Mock
    AppUser organizer;

    @InjectMocks
    Event event;

    @Test
    @DisplayName("getGenre")
        // TODO ask if it is needed (also null)
    void shouldReturnGenre() {
        Mockito.when(performance.getGenre()).thenReturn(genre);
        assertEquals(genre, event.getGenre());
    }

    @Nested
    @DisplayName("cancel when")
    class Cancel {
        @Test
        @DisplayName("Event is cancelled")
        void shouldThrowIllegalEventStateException() {
            event.setCancelled(true);
            Exception exception = assertThrows(CustomExceptions.IllegalEventStateException.class, () -> event.cancel());
            assertEquals("Can not cancel an event, that has already been cancelled.", exception.getMessage());
        }

        @Test
        @DisplayName("Event is not cancelled")
        void shouldSetIsCancelledToTrue() {
            event.setCancelled(false);
            event.cancel();
            assertTrue(event.isCancelled());

        }
    }

    @Nested
    @DisplayName("initializeTicketsToBeSold when")
    class InitializeTicketsToBeSold {
        @BeforeEach
        void setUp() {
            Mockito.when(venue.getCapacity()).thenReturn(1000);
        }

        @Test
        @DisplayName("ticketsSoldThroughOurApp is higher than Venue capacity")
        void shouldThrowTicketCountException() {
            int ticketsSoldThroughOurApp = 1500;
            int ticketsAlreadySold = 0;
            event.setTicketsSoldThroughOurApp(ticketsSoldThroughOurApp);
            Exception exception = assertThrows(CustomExceptions.TicketCountException.class,
                    () -> event.initializeTicketsToBeSold(ticketsAlreadySold));
            assertTrue(exception.getMessage().contains("Can not sell more tickets than venue's max capacity"));
        }


        @Nested
        @DisplayName("ticketsSoldThroughOurApp equals Venue capacity and")
        class TicketsSoldThroughOurAppEqualsVenueCapacity {
            @Test
            @DisplayName("ticketsAlreadySold is 0")
            void availableTicketsShouldBeEqualToTicketsSoldThroughOurApp2() {
                int ticketsSoldThroughOurApp = 1000;
                int ticketsAlreadySold = 0;
                event.setTicketsSoldThroughOurApp(ticketsSoldThroughOurApp);
                event.initializeTicketsToBeSold(ticketsAlreadySold);
                assertEquals(ticketsSoldThroughOurApp, event.getAvailableTickets());
            }

            @Test
            @DisplayName("ticketsAlreadySold is lower than ticketsSoldThroughOurApp")
            void availableTicketsShouldBeEqualToTicketsSoldThroughOurAppMinusTicketsAlreadySold() {
                int ticketsSoldThroughOurApp = 1000;
                int ticketsAlreadySold = 250;
                event.setTicketsSoldThroughOurApp(ticketsSoldThroughOurApp);
                event.initializeTicketsToBeSold(ticketsAlreadySold);
                assertEquals(ticketsSoldThroughOurApp - ticketsAlreadySold, event.getAvailableTickets());
            }

            @Test
            @DisplayName("ticketsAlreadySold is higher than ticketsSoldThroughOurApp")
            void shouldThrowTicketCountException2() {
                int ticketsSoldThroughOurApp = 1000;
                int ticketsAlreadySold = 2500;
                event.setTicketsSoldThroughOurApp(ticketsSoldThroughOurApp);
                Exception exception = assertThrows(CustomExceptions.TicketCountException.class,
                        () -> event.initializeTicketsToBeSold(ticketsAlreadySold));
                assertTrue(exception.getMessage().contains("Can not sell less tickets than already sold tickets"));
            }
        }

        @Nested
        @DisplayName("ticketsSoldThroughOurApp is lower than Venue capacity and")
        class TicketsSoldThroughOurAppLowerThanVenueCapacity {
            @Test
            @DisplayName("ticketsAlreadySold is 0")
            void availableTicketsShouldBeEqualToTicketsSoldThroughOurApp() {
                int ticketsSoldThroughOurApp = 500;
                int ticketsAlreadySold = 0;
                event.setTicketsSoldThroughOurApp(ticketsSoldThroughOurApp);
                event.initializeTicketsToBeSold(ticketsAlreadySold);
                assertEquals(ticketsSoldThroughOurApp, event.getAvailableTickets());
            }

            @Test
            @DisplayName("ticketsAlreadySold is lower than ticketsSoldThroughOurApp")
            void availableTicketsShouldBeEqualToTicketsSoldThroughOurAppMinusTicketsAlreadySold() {
                int ticketsSoldThroughOurApp = 900;
                int ticketsAlreadySold = 250;
                event.setTicketsSoldThroughOurApp(ticketsSoldThroughOurApp);
                event.initializeTicketsToBeSold(ticketsAlreadySold);
                assertEquals(ticketsSoldThroughOurApp - ticketsAlreadySold, event.getAvailableTickets());
            }

            @Test
            @DisplayName("ticketsAlreadySold is higher than ticketsSoldThroughOurApp")
            void shouldThrowTicketCountException2() {
                int ticketsSoldThroughOurApp = 100;
                int ticketsAlreadySold = 250;
                event.setTicketsSoldThroughOurApp(ticketsSoldThroughOurApp);
                Exception exception = assertThrows(CustomExceptions.TicketCountException.class,
                        () -> event.initializeTicketsToBeSold(ticketsAlreadySold));
                assertTrue(exception.getMessage().contains("Can not sell less tickets than already sold tickets"));
            }
        }
    }

    @Nested
    @DisplayName("currentPriceOfTickets when")
    class CurrentPriceOfTickets {
        @Test
        @DisplayName("ticketCount is negative")
        void shouldThrowIllegalArgumentException() {
            Exception exception = assertThrows(IllegalArgumentException.class,
                    () -> event.currentPriceOfTickets(-1));
            assertEquals("Can not calculate price for 0 or negative number of tickets.", exception.getMessage());
        }

        @Test
        @DisplayName("ticketCount is 0")
        void shouldThrowIllegalArgumentException2() {
            Exception exception = assertThrows(IllegalArgumentException.class,
                    () -> event.currentPriceOfTickets(0));
            assertEquals("Can not calculate price for 0 or negative number of tickets.", exception.getMessage());
        }

        @Test
        @DisplayName("ticketCount is positive")
        void shouldReturnCorrectAmount() {
            // test data
            Mockito.when(venue.getCapacity()).thenReturn(1000);
            int ticketCount = 500;
            event.setBasePrice(BigDecimal.valueOf(1000));
            event.setTicketsSoldThroughOurApp(1000);
            event.initializeTicketsToBeSold(0);
            // data coming from setup and test data
            int availableTickets = event.getAvailableTickets();
            int ticketsSoldThroughOurApp = event.getTicketsSoldThroughOurApp();
            BigDecimal basePrice = event.getBasePrice();
            // logic
            // TODO ask if there is a better way, also if you need to check edge case
            BigDecimal expected = BigDecimal.ZERO;
            for (int i = 0; i < ticketCount; i++) {
                if (availableTickets - i > ticketsSoldThroughOurApp * 0.9) {
                    // super early bird price
                    expected = expected.add(basePrice.multiply(BigDecimal.valueOf(0.8)));
                } else if (availableTickets - i > ticketsSoldThroughOurApp * 0.8) {
                    // early bird price
                    expected = expected.add(basePrice.multiply(BigDecimal.valueOf(0.9)));
                } else {
                    // regular price
                    expected = expected.add(basePrice);
                }
            }
            assertEquals(expected, event.currentPriceOfTickets(ticketCount));
        }
    }

    @Nested
    @DisplayName("bookTickets when")
    class BookTickets {
        @Test
        @DisplayName("ticketCount is negative")
        void shouldThrowIllegalArgumentException() {
            Exception exception = assertThrows(IllegalArgumentException.class,
                    () -> event.bookTickets(-1));
            assertEquals("Can not buy 0 or negative number of tickets.", exception.getMessage());
        }

        @Test
        @DisplayName("ticketCount is 0")
        void shouldThrowIllegalArgumentException2() {
            Exception exception = assertThrows(IllegalArgumentException.class,
                    () -> event.bookTickets(0));
            assertEquals("Can not buy 0 or negative number of tickets.", exception.getMessage());
        }

        @Nested
        @DisplayName("ticketCount is positive and")
        class TicketCountIsPositive {
            int ticketCount = 5;

            @Test
            @DisplayName("Event is cancelled")
            void shouldThrowIllegalEventStateException() {
                event.setAvailableTickets(500);
                event.setCancelled(true);
                event.setEventStartingDateAndTime(ZonedDateTime.now().plusDays(7));
                event.setDaysBeforeBookingIsClosed(2);
                Exception exception = assertThrows(CustomExceptions.IllegalEventStateException.class,
                        () -> event.bookTickets(ticketCount));
                assertEquals("Cancelled event can not be booked.", exception.getMessage());
            }

            @Test
            @DisplayName("booking has ended")
            void shouldThrowIllegalEventStateException2() {
                event.setAvailableTickets(500);
                event.setCancelled(false);
                event.setEventStartingDateAndTime(ZonedDateTime.now().plusDays(2));
                event.setDaysBeforeBookingIsClosed(3);
                Exception exception = assertThrows(CustomExceptions.IllegalEventStateException.class,
                        () -> event.bookTickets(ticketCount));
                assertTrue(exception.getMessage().contains("Booking for event ended at"));
            }

            @Test
            @DisplayName("not enough tickets left")
            void shouldThrowIllegalEventStateException3() {
                event.setAvailableTickets(4);
                event.setCancelled(false);
                event.setEventStartingDateAndTime(ZonedDateTime.now().plusDays(7));
                event.setDaysBeforeBookingIsClosed(3);
                Exception exception = assertThrows(CustomExceptions.IllegalEventStateException.class,
                        () -> event.bookTickets(ticketCount));
                assertTrue(exception.getMessage().contains("Not enough tickets left"));
            }

            @Test
            @DisplayName("tickets can be booked")
            void shouldDecreaseAvailableTicketsByTicketCount() {
                event.setAvailableTickets(15);
                event.setCancelled(false);
                event.setEventStartingDateAndTime(ZonedDateTime.now().plusDays(7));
                event.setDaysBeforeBookingIsClosed(3);
                event.bookTickets(ticketCount);
                assertEquals(10, event.getAvailableTickets());
            }
        }
    }

    @Nested
    @DisplayName("refundTickets when")
    class RefundTickets {
        @Test
        @DisplayName("ticketCount is negative")
        void shouldThrowIllegalArgumentException() {
            Exception exception = assertThrows(IllegalArgumentException.class,
                    () -> event.refundTickets(-1));
            assertEquals("Can not refund 0 or negative number of tickets.", exception.getMessage());
        }

        @Test
        @DisplayName("ticketCount is 0")
        void shouldThrowIllegalArgumentException2() {
            Exception exception = assertThrows(IllegalArgumentException.class,
                    () -> event.refundTickets(0));
            assertEquals("Can not refund 0 or negative number of tickets.", exception.getMessage());
        }

        @Nested
        @DisplayName("ticketCount is positive and")
        class TicketCountIsPositive {
            int ticketCount = 5;

            @Test
            @DisplayName("Event is cancelled")
            void shouldThrowIllegalEventStateException() {
                event.setCancelled(true);
                event.setEventStartingDateAndTime(ZonedDateTime.now().plusDays(7));
                event.setDaysBeforeBookingIsClosed(2);
                Exception exception = assertThrows(CustomExceptions.IllegalEventStateException.class,
                        () -> event.refundTickets(ticketCount));
                assertEquals("Cancelled event can not be refunded.", exception.getMessage());
            }

            @Test
            @DisplayName("refunding has ended")
            void shouldThrowIllegalEventStateException2() {
                Mockito.when(venue.isThereRefund()).thenReturn(true);
                event.setCancelled(false);
                event.setEventStartingDateAndTime(ZonedDateTime.now().plusDays(2));
                event.setDaysBeforeBookingIsClosed(3);
                Exception exception = assertThrows(CustomExceptions.IllegalEventStateException.class,
                        () -> event.refundTickets(ticketCount));
                assertTrue(exception.getMessage().contains("Refunding for event ended at"));
            }

            @Test
            @DisplayName("there is no refunding by Venue")
            void shouldThrowIllegalEventStateException3() {
                Mockito.when(venue.isThereRefund()).thenReturn(false);
                event.setCancelled(false);
                event.setEventStartingDateAndTime(ZonedDateTime.now().plusDays(7));
                event.setDaysBeforeBookingIsClosed(2);
                Exception exception = assertThrows(CustomExceptions.IllegalEventStateException.class,
                        () -> event.refundTickets(ticketCount));
                assertEquals("Refunding is not allowed by the venue.", exception.getMessage());
            }

            @Test
            @DisplayName("tickets can be refunded")
            void shouldIncreaseAvailableTicketsByTicketCount() {
                event.setAvailableTickets(20);
                Mockito.when(venue.isThereRefund()).thenReturn(true);
                event.setCancelled(false);
                event.setEventStartingDateAndTime(ZonedDateTime.now().plusDays(7));
                event.setDaysBeforeBookingIsClosed(2);
                event.refundTickets(ticketCount);
                assertEquals(25, event.getAvailableTickets());
            }
        }
    }
}
