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

    public static class CurrentUserIsNotTheEventOrganizerException extends IllegalStateException {
        public CurrentUserIsNotTheEventOrganizerException() {
            super("Current suer does not match the suer who created the event.");
        }
    }

    public static class CurrentUserIsNotTheOneWhoBookedTheEventException extends IllegalStateException {
        public CurrentUserIsNotTheOneWhoBookedTheEventException() {
            super("Current user does not match the user who booked the event.");
        }
    }

    public static class EventMustBeRefundedAndCancelledBeforeDeletingException extends IllegalStateException {
        public EventMustBeRefundedAndCancelledBeforeDeletingException() {
            super("Event must be fully refunded and cancelled before deleting it.");
        }
    }
}
