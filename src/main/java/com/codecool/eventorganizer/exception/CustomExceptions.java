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

    public static class IdCanNotExistWhenCreatingEntityException extends IllegalArgumentException {
        public IdCanNotExistWhenCreatingEntityException() {
            super("When creating entity id should not exist.");
        }
    }

    public static class CurrentUserIsNotMatching extends IllegalStateException {
        public CurrentUserIsNotMatching(String message) {
            super(message);
        }
    }

    public static class EventMustBeRefundedAndCancelledBeforeDeletingException extends IllegalStateException {
        public EventMustBeRefundedAndCancelledBeforeDeletingException() {
            super("Event must be fully refunded and cancelled before deleting it.");
        }
    }

    public static class CanNotCancelAnAlreadyCancelledEventException extends IllegalStateException {
        public CanNotCancelAnAlreadyCancelledEventException() {
            super("Can not cancel an event, that has already been cancelled.");
        }
    }
}
