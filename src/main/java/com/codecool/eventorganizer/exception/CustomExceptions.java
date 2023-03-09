package com.codecool.eventorganizer.exception;

public abstract class CustomExceptions {
    public static class EventCanNotBeRefundedException extends IllegalStateException {
        public EventCanNotBeRefundedException(String message) {
            super(message);
        }
    }

    public static class EventCanNotBeBookedException extends IllegalStateException {
        public EventCanNotBeBookedException(String message) {
            super(message);
        }
    }

    public static class EmailAlreadyUsedException extends IllegalArgumentException {
        public EmailAlreadyUsedException(String message) {
            super(message);
        }
    }

    public static class MissingAttributeException extends IllegalArgumentException {
        public MissingAttributeException(String message) {
            super(message);
        }
    }

    public static class EmailCanNotBeChangedException extends IllegalArgumentException {
        public EmailCanNotBeChangedException(String message) {
            super(message);
        }
    }

    public static class PasswordChangeIsDifferentEndpointException extends IllegalArgumentException {
        public PasswordChangeIsDifferentEndpointException(String message) {
            super(message);
        }
    }

    public static class NotEnoughTicketsLeftException extends ArithmeticException {
        public NotEnoughTicketsLeftException(String message) {
            super(message);
        }
    }

    public static class TicketCountCanNotExceedVenueCapacityException extends IllegalArgumentException {
        public TicketCountCanNotExceedVenueCapacityException(String message) {
            super(message);
        }
    }
}
