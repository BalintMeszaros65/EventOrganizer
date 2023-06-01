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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BookedEvent")
public class BookedEventTests {
    @Mock
    Genre genre;
    @Mock
    Event event;
    @InjectMocks
    BookedEvent bookedEvent;

    @Test
    @DisplayName("getGenre")
    void shouldReturnGenre() {
        Mockito.when(event.getGenre()).thenReturn(genre);
        assertEquals(genre, bookedEvent.getGenre());
    }

    @Nested
    @DisplayName("canBeRefunded when")
    class CanBeRefunded {
        @Nested
        @DisplayName("event can be refunded and")
        class EventCanBeRefunded {
            @BeforeEach
            void setup() {
                Mockito.when(event.canBeRefunded()).thenReturn(true);
            }

            @Test
            @DisplayName("bookedEvent is refunded")
            void shouldReturnFalse() {
                bookedEvent.setRefunded(true);
                assertFalse(bookedEvent.canBeRefunded());
            }

            @Test
            @DisplayName("bookedEvent is not refunded")
            void shouldReturnTrue() {
                bookedEvent.setRefunded(false);
                assertTrue(bookedEvent.canBeRefunded());
            }
        }

        @Nested
        @DisplayName("event can not be refunded and")
        class EventCanNotBeRefunded {
            @BeforeEach
            void setup() {
                Mockito.when(event.canBeRefunded()).thenReturn(false);
            }

            @Test
            @DisplayName("bookedEvent is refunded")
            void shouldReturnFalse1() {
                bookedEvent.setRefunded(true);
                assertFalse(bookedEvent.canBeRefunded());
            }

            @Test
            @DisplayName("bookedEvent is not refunded")
            void shouldReturnFalse2() {
                bookedEvent.setRefunded(false);
                assertFalse(bookedEvent.canBeRefunded());
            }
        }
    }

    @Nested
    @DisplayName("refund when")
    class Refund {
        @Test
        @DisplayName("bookedEvent can be refunded")
        // TODO rewrite after payment
        void shouldSetIsRefundedToTrue() {
            Mockito.when(bookedEvent.canBeRefunded()).thenReturn(true);
            bookedEvent.refund();
            assertTrue(bookedEvent.isRefunded());
        }

        @Test
        @DisplayName("bookedEvent can not be refunded")
        void shouldThrowIllegalEventStateException() {
            Mockito.when(bookedEvent.canBeRefunded()).thenReturn(false);
            Exception exception = assertThrows(CustomExceptions.IllegalEventStateException.class, () -> bookedEvent.refund());
            assertEquals("Booked event can not be refunded.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("refundByEventOrganizer when")
    class RefundByEventOrganizer {
        @Test
        @DisplayName("bookedEvent is refunded")
        // TODO rewrite after payment
        void shouldNotDoAnything() {
            bookedEvent.setRefunded(true);
            bookedEvent.refundByEventOrganizer();
            assertTrue(bookedEvent.isRefunded());
        }

        @Test
        @DisplayName("bookedEvent is not refunded")
        // TODO rewrite after payment
        void shouldSetIsRefundedToTrue() {
            bookedEvent.setRefunded(false);
            bookedEvent.refundByEventOrganizer();
            assertTrue(bookedEvent.isRefunded());
        }
    }
}
