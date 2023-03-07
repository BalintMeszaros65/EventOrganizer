package com.codecool.eventorganizer.exception;

// TODO ask if plural naming is conventional or not
public class CustomExceptions {
    // TODO ask if state is good here or not
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
}
