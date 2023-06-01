package com.codecool.eventorganizer.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
@DisplayName("Customer")
public class CustomerTests {
    @Mock
    BookedEvent bookedEvent1;
    @Mock
    BookedEvent bookedEvent2;
    @Mock
    BookedEvent bookedEvent3;
    Customer customer = new Customer();

    @Nested
    @DisplayName("storeBookedEvent when")
    class StoreBookedEvent {
        @Test
        @DisplayName("bookedEvent is null")
        void shouldThrowIllegalArgumentException() {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> customer.storeBookedEvent(null));
            assertEquals("BookedEvent can not be null.", exception.getMessage());
        }

        @Nested
        @DisplayName("bookedEvent is not null and ")
        class BookedEventIsNotNull {
            @Test
            @DisplayName("bookedEvents is null")
            void shouldCreateHashSetAndAddBookedEventToIt() {
                customer.storeBookedEvent(bookedEvent1);
                Set<BookedEvent> expected = Set.of(bookedEvent1);
                assertEquals(expected, customer.getBookedEvents());
            }

            @Test
            @DisplayName("bookedEvents is not null")
            void shouldAddBookedEvent() {
                customer.storeBookedEvent(bookedEvent1);
                customer.storeBookedEvent(bookedEvent2);
                Set<BookedEvent> expected = Set.of(bookedEvent1, bookedEvent2);
                assertEquals(expected, customer.getBookedEvents());
            }
        }
    }

    @Nested
    @DisplayName("calculateAveragePricePaidForOneTicket when")
    class CalculateAveragePricePaidForOneTicket {
        @Test
        @DisplayName("bookedEvents is null")
        void shouldReturnBigDecimalZero() {
            assertEquals(BigDecimal.ZERO, customer.calculateAveragePricePaidForOneTicket());
        }

        @Nested
        @DisplayName("bookedEvents is not null and")
        class BookedEventIsNotNull {
            @Test
            @DisplayName("it is empty")
            void shouldReturnBigDecimalZero1() {
                customer.setBookedEvents(new HashSet<>());
                assertEquals(BigDecimal.ZERO, customer.calculateAveragePricePaidForOneTicket());
            }

            @Test
            @DisplayName("it only contains nulls")
            void shouldReturnBigDecimalZero2() {
                customer.setBookedEvents(new HashSet<>());
                customer.bookedEvents.add(null);
                customer.bookedEvents.add(null);
                assertEquals(BigDecimal.ZERO, customer.calculateAveragePricePaidForOneTicket());
            }

            @Test
            @DisplayName("it contains valid BookedEvents")
            void shouldCalculateAveragePriceForOneTicket() {
                // given
                customer.storeBookedEvent(bookedEvent1);
                customer.storeBookedEvent(bookedEvent2);
                customer.storeBookedEvent(bookedEvent3);
                int ticketCount1 = 10;
                int ticketCount2 = 4;
                int ticketCount3 = 1;
                Mockito.when(bookedEvent1.getTicketCount()).thenReturn(ticketCount1);
                Mockito.when(bookedEvent2.getTicketCount()).thenReturn(ticketCount2);
                Mockito.when(bookedEvent3.getTicketCount()).thenReturn(ticketCount3);
                BigDecimal amountPayed1 = BigDecimal.valueOf(70000);
                BigDecimal amountPayed2 = BigDecimal.valueOf(50000.65);
                BigDecimal amountPayed3 = BigDecimal.valueOf(12000);
                Mockito.when(bookedEvent1.getAmountPayed()).thenReturn(amountPayed1);
                Mockito.when(bookedEvent2.getAmountPayed()).thenReturn(amountPayed2);
                Mockito.when(bookedEvent3.getAmountPayed()).thenReturn(amountPayed3);

                // then
                Set<BookedEvent> bookedEvents = customer.bookedEvents;
                int numberOfTickets = bookedEvents.stream()
                        .filter(Objects::nonNull)
                        .map(BookedEvent::getTicketCount)
                        .reduce(0, Integer::sum);
                BigDecimal amountPaid = bookedEvents.stream()
                        .filter(Objects::nonNull)
                        .map(BookedEvent::getAmountPayed)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal expected = amountPaid.divide(BigDecimal.valueOf(numberOfTickets), RoundingMode.HALF_UP);
                assertEquals(expected, customer.calculateAveragePricePaidForOneTicket());
            }
        }
    }

    // TODO createWeightedRecommendationMapFromBookedEvents
    // TODO getRecommendationByType
}
