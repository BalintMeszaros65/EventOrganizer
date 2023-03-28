package com.codecool.eventorganizer.exception;

public abstract class CustomExceptions {
    public static class IllegalEventStateException extends IllegalStateException {
        public IllegalEventStateException(String message) {
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

    public static class PasswordCanNotBeChangedException extends IllegalArgumentException {
        public PasswordCanNotBeChangedException(String message) {
            super(message);
        }
    }

    public static class TicketCountException extends ArithmeticException {
        public TicketCountException(String message) {
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
}
