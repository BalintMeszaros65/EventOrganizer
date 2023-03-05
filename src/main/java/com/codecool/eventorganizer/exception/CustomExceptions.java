package com.codecool.eventorganizer.exception;

// TODO ask if plural naming is conventional or not
public class CustomExceptions {
    // TODO ask if state is good here or not
    public static class EventCanNotBeRefunded extends IllegalStateException {
        public EventCanNotBeRefunded(String message) {
            super(message);
        }
    }

    public static class EventCanNotBeBooked extends IllegalStateException {
        public EventCanNotBeBooked(String message) {
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
}
